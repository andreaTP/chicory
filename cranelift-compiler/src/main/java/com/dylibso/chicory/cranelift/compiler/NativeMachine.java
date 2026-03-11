package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.cranelift.CraneliftBridge;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;

/**
 * Machine implementation that compiles Wasm functions to native x86_64
 * via Cranelift and executes them through Panama FFM.
 *
 * <p>Calling convention for all compiled functions:
 * <pre>
 *   param 0: memBase  (i64/ADDRESS) — pointer to linear memory
 *   param 1: ctxPtr   (i64/ADDRESS) — pointer to call context struct
 *   param 2+: Wasm function parameters
 *   return: Wasm return value
 * </pre>
 *
 * <p>See {@link CtxBuffer} for the full layout definition.
 */
final class NativeMachine implements Machine {

    private static final int CTX_SIZE = CtxBuffer.CTX_SIZE;
    private static final Arena ARENA = Arena.ofShared();

    private final Instance instance;
    private final MethodHandle[] downcalls;
    private final MemorySegment codeRegion;
    private final MemorySegment ctxBuffer;
    private final MemorySegment funcTable;
    private final MemorySegment globalsBuffer;
    private final int numImports;
    private final int globalCount;
    private boolean globalsInitialized;
    // Pending exception from upcall stubs (cannot throw through native frames)
    private volatile Throwable pendingException;

    NativeMachine(Instance instance) {
        this.instance = instance;
        var module = instance.module();
        this.numImports =
                (int)
                        module.importSection().stream()
                                .filter(
                                        i ->
                                                i.importType()
                                                        == com.dylibso.chicory.wasm.types
                                                                .ExternalType.FUNCTION)
                                .count();
        int totalFuncs = numImports + module.codeSection().functionBodyCount();
        this.downcalls = new MethodHandle[totalFuncs];

        // Allocate call context buffer
        ctxBuffer = ARENA.allocate(CTX_SIZE, 8);

        // Allocate function pointer table (one i64 per function)
        funcTable = ARENA.allocate((long) totalFuncs * 8, 8);

        // Globals buffer: one i64 per global
        this.globalCount =
                (int)
                                module.importSection().stream()
                                        .filter(
                                                i ->
                                                        i.importType()
                                                                == com.dylibso.chicory.wasm.types
                                                                        .ExternalType.GLOBAL)
                                        .count()
                        + (module.globalSection() != null
                                ? module.globalSection().globalCount()
                                : 0);
        this.globalsBuffer =
                globalCount > 0 ? ARENA.allocate((long) globalCount * 8, 8) : MemorySegment.NULL;

        // Create CALL_INDIRECT trampoline upcall stub
        MemorySegment trampolineStub = createTrampolineStub();

        // Create memory.grow upcall stub
        MemorySegment memGrowStub = createMemGrowStub();

        // Write pointers to ctxBuffer
        ctxBuffer.set(ValueLayout.JAVA_LONG, CtxBuffer.FUNC_TABLE_PTR, funcTable.address());
        ctxBuffer.set(ValueLayout.JAVA_LONG, CtxBuffer.TRAMPOLINE_PTR, trampolineStub.address());
        ctxBuffer.set(ValueLayout.JAVA_LONG, CtxBuffer.GLOBALS_PTR, globalsBuffer.address());
        ctxBuffer.set(ValueLayout.JAVA_LONG, CtxBuffer.MEM_GROW_PTR, memGrowStub.address());

        // Compile all module-defined functions
        var bridge = new CraneliftBridge();
        bridge.init("x86_64-unknown-linux-gnu");

        var compiler = new NativeCompiler(bridge, module);
        byte[][] compiledCode = compiler.compileAll();

        // mmap all compiled code into a single executable region
        long totalSize = 0;
        for (byte[] code : compiledCode) {
            if (code != null) {
                totalSize += align(code.length, 16);
            }
        }
        totalSize = Math.max(totalSize, 4096);
        totalSize = align(totalSize, 4096);

        try {
            codeRegion = PanamaExecutor.mmapCode(totalSize);
            long offset = 0;
            for (int i = 0; i < compiledCode.length; i++) {
                if (compiledCode[i] != null) {
                    int funcId = numImports + i;
                    MemorySegment.copy(
                            MemorySegment.ofArray(compiledCode[i]),
                            0,
                            codeRegion,
                            offset,
                            compiledCode[i].length);

                    var funcType =
                            (FunctionType)
                                    module.typeSection()
                                            .getType(module.functionSection().getFunctionType(i));

                    MemorySegment codePtr = codeRegion.asSlice(offset);
                    downcalls[funcId] = createDowncall(codePtr, funcType);

                    // Store native code address in function pointer table
                    funcTable.set(ValueLayout.JAVA_LONG, (long) funcId * 8, codePtr.address());

                    offset += align(compiledCode[i].length, 16);
                }
            }
            PanamaExecutor.mprotectExec(codeRegion, totalSize);

            // Create import upcall stubs and store in function pointer table
            for (int funcId = 0; funcId < numImports; funcId++) {
                var importFunc = instance.imports().function(funcId);
                var funcType = importFunc.functionType();
                MemorySegment stub = createImportStub(funcId, funcType);
                funcTable.set(ValueLayout.JAVA_LONG, (long) funcId * 8, stub.address());
                downcalls[funcId] = null; // imports dispatch through call() directly
            }

            // For uncompiled functions, create per-signature safety stubs
            for (int i = 0; i < compiledCode.length; i++) {
                if (compiledCode[i] == null) {
                    int funcId = numImports + i;
                    var funcType =
                            (FunctionType)
                                    module.typeSection()
                                            .getType(module.functionSection().getFunctionType(i));
                    try {
                        MemorySegment stub = createImportStub(funcId, funcType);
                        funcTable.set(ValueLayout.JAVA_LONG, (long) funcId * 8, stub.address());
                    } catch (Exception e) {
                        System.err.println(
                                "WARNING: Safety stub failed for func "
                                        + funcId
                                        + ": "
                                        + e.getMessage());
                        // Leave funcTable entry as 0 — will throw from call() if invoked
                    }
                }
            }
        } catch (Throwable e) {
            throw new ChicoryException("Failed to set up native code", e);
        }
    }

    private static long align(long value, long alignment) {
        return (value + alignment - 1) & ~(alignment - 1);
    }

    // --- Downcall creation (Java → native) ---

    private MethodHandle createDowncall(MemorySegment codePtr, FunctionType funcType) {
        var layouts = new ArrayList<ValueLayout>();
        layouts.add(ValueLayout.ADDRESS); // memBase
        layouts.add(ValueLayout.ADDRESS); // ctxPtr

        for (ValType param : funcType.params()) {
            layouts.add(valTypeToLayout(param));
        }

        ValueLayout returnLayout = null;
        if (!funcType.returns().isEmpty()) {
            returnLayout = valTypeToLayout(funcType.returns().get(0));
        }

        FunctionDescriptor desc;
        if (returnLayout != null) {
            desc = FunctionDescriptor.of(returnLayout, layouts.toArray(new ValueLayout[0]));
        } else {
            desc = FunctionDescriptor.ofVoid(layouts.toArray(new ValueLayout[0]));
        }

        return Linker.nativeLinker().downcallHandle(codePtr, desc);
    }

    // --- Import upcall stubs (native → Java for host imports) ---

    private MemorySegment createImportStub(int funcId, FunctionType funcType) {
        try {
            // Build the native descriptor matching compiled function convention:
            // (ADDRESS memBase, ADDRESS ctxPtr, typed_params...) -> typed_return
            var layouts = new ArrayList<ValueLayout>();
            layouts.add(ValueLayout.ADDRESS); // memBase
            layouts.add(ValueLayout.ADDRESS); // ctxPtr
            for (ValType param : funcType.params()) {
                layouts.add(valTypeToLayout(param));
            }

            ValueLayout returnLayout = null;
            if (!funcType.returns().isEmpty()) {
                returnLayout = valTypeToLayout(funcType.returns().get(0));
            }

            FunctionDescriptor desc;
            if (returnLayout != null) {
                desc = FunctionDescriptor.of(returnLayout, layouts.toArray(new ValueLayout[0]));
            } else {
                desc = FunctionDescriptor.ofVoid(layouts.toArray(new ValueLayout[0]));
            }

            // Build Java param types for the MethodHandle
            var targetParamTypes = new ArrayList<Class<?>>();
            targetParamTypes.add(MemorySegment.class); // memBase
            targetParamTypes.add(MemorySegment.class); // ctxPtr
            for (ValType param : funcType.params()) {
                targetParamTypes.add(valTypeToJavaClass(param));
            }

            // importDispatchDirect reads args from ctxBuffer (written by CALL handler)
            MethodHandle directHandler =
                    MethodHandles.lookup()
                            .bind(
                                    this,
                                    "importDispatchDirect",
                                    MethodType.methodType(long.class, int.class));
            directHandler = MethodHandles.insertArguments(directHandler, 0, funcId);
            // Now: () -> long

            // Drop all native params (the stub ignores them, reads from ctxBuffer)
            MethodHandle dropper =
                    MethodHandles.dropArguments(
                            directHandler, 0, targetParamTypes.toArray(new Class[0]));

            // Cast return type to match native descriptor
            if (returnLayout == null) {
                // Void function: discard the long return value
                var voidType =
                        MethodType.methodType(void.class, targetParamTypes.toArray(new Class[0]));
                dropper = dropper.asType(voidType);
            } else if (!funcType.returns().isEmpty()) {
                var retType = funcType.returns().get(0);
                if (retType.equals(ValType.I32)) {
                    dropper =
                            MethodHandles.explicitCastArguments(
                                    dropper,
                                    MethodType.methodType(
                                            int.class, targetParamTypes.toArray(new Class[0])));
                }
            }

            return Linker.nativeLinker().upcallStub(dropper, desc, ARENA);
        } catch (Exception e) {
            throw new ChicoryException("Failed to create import stub for func " + funcId, e);
        }
    }

    /**
     * Dispatches a function call. Reads args from ctxBuffer.
     * Called by import upcall stubs and uncompiled function safety stubs.
     */
    @SuppressWarnings("unused")
    private long importDispatchDirect(int funcId) {
        try {
            int argCount = ctxBuffer.get(ValueLayout.JAVA_INT, CtxBuffer.ARG_COUNT);
            long[] args = new long[argCount];
            for (int i = 0; i < argCount; i++) {
                args[i] = ctxBuffer.get(ValueLayout.JAVA_LONG, CtxBuffer.argOffset(i));
            }
            if (funcId < numImports) {
                var importFunc = instance.imports().function(funcId);
                long[] result = importFunc.handle().apply(instance, args);
                return result.length > 0 ? result[0] : 0L;
            } else {
                // Uncompiled module function — throw
                throw new ChicoryException("Function " + funcId + " not compiled");
            }
        } catch (Throwable t) {
            pendingException = t;
            return 0L;
        }
    }

    // --- CALL_INDIRECT trampoline ---

    private MemorySegment createTrampolineStub() {
        try {
            MethodHandle handler =
                    MethodHandles.lookup()
                            .bind(
                                    this,
                                    "callIndirectTrampoline",
                                    MethodType.methodType(long.class, long.class));
            var desc = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG);
            return Linker.nativeLinker().upcallStub(handler, desc, ARENA);
        } catch (Exception e) {
            throw new ChicoryException("Failed to create trampoline stub", e);
        }
    }

    @SuppressWarnings("unused")
    private long callIndirectTrampoline(long ctxAddr) {
        try {
            var ctx = MemorySegment.ofAddress(ctxAddr).reinterpret(CTX_SIZE);
            int typeId = ctx.get(ValueLayout.JAVA_INT, CtxBuffer.TYPE_ID);
            int tableIdx = ctx.get(ValueLayout.JAVA_INT, CtxBuffer.TABLE_IDX);
            int elemIdx = ctx.get(ValueLayout.JAVA_INT, CtxBuffer.ELEM_IDX);
            int argCount = ctx.get(ValueLayout.JAVA_INT, CtxBuffer.ARG_COUNT);

            int funcId = instance.table(tableIdx).requiredRef(elemIdx);

            // Type check
            int actualTypeIdx = instance.functionType(funcId);
            if (actualTypeIdx != typeId) {
                throw new ChicoryException("indirect call type mismatch");
            }

            long[] args = new long[argCount];
            for (int i = 0; i < argCount; i++) {
                args[i] = ctx.get(ValueLayout.JAVA_LONG, CtxBuffer.argOffset(i));
            }

            long[] result = this.call(funcId, args);
            return result.length > 0 ? result[0] : 0L;
        } catch (Throwable t) {
            pendingException = t;
            return 0L;
        }
    }

    // --- Memory grow upcall stub ---

    private MemorySegment createMemGrowStub() {
        try {
            MethodHandle handler =
                    MethodHandles.lookup()
                            .bind(
                                    this,
                                    "memoryGrowHandler",
                                    MethodType.methodType(long.class, long.class));
            var desc = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG);
            return Linker.nativeLinker().upcallStub(handler, desc, ARENA);
        } catch (Exception e) {
            throw new ChicoryException("Failed to create memory grow stub", e);
        }
    }

    @SuppressWarnings("unused")
    private long memoryGrowHandler(long ctxAddr) {
        try {
            var ctx = MemorySegment.ofAddress(ctxAddr).reinterpret(CTX_SIZE);
            int delta = ctx.get(ValueLayout.JAVA_INT, CtxBuffer.ARG_COUNT);
            var mem = instance.memory();
            int oldPages = mem.grow(delta);
            // Update memory base and page count in ctxBuffer
            if (oldPages != -1 && mem instanceof NativeMemory nativeMemory) {
                ctx.set(
                        ValueLayout.JAVA_LONG,
                        CtxBuffer.MEM_BASE_ADDR,
                        nativeMemory.nativeAddress().address());
                ctx.set(ValueLayout.JAVA_INT, CtxBuffer.MEMORY_PAGES, mem.pages());
            }
            return oldPages;
        } catch (Throwable t) {
            pendingException = t;
            return -1L;
        }
    }

    // --- Globals initialization ---

    /**
     * Lazily replace module-defined GlobalInstance objects with NativeGlobalInstance
     * backed by the off-heap globalsBuffer. Called once on first native call,
     * after Instance.initialize() has created the original GlobalInstance objects.
     *
     * For imported globals, we copy their current value into the buffer (read-only
     * from native code's perspective — imported mutable globals are rare).
     */
    private void initializeNativeGlobals() {
        if (globalsInitialized || globalCount == 0) return;
        globalsInitialized = true;

        int importGlobalCount =
                (int)
                        instance.module().importSection().stream()
                                .filter(
                                        i ->
                                                i.importType()
                                                        == com.dylibso.chicory.wasm.types
                                                                .ExternalType.GLOBAL)
                                .count();

        // Copy imported global values into buffer (these remain as-is in Instance)
        for (int i = 0; i < importGlobalCount; i++) {
            globalsBuffer.set(ValueLayout.JAVA_LONG, (long) i * 8, instance.global(i).getValue());
        }

        // Replace module-defined globals with NativeGlobalInstance via reflection
        try {
            var globalsField = instance.getClass().getDeclaredField("globals");
            globalsField.setAccessible(true);
            var globals = (com.dylibso.chicory.runtime.GlobalInstance[]) globalsField.get(instance);

            for (int i = 0; i < globals.length; i++) {
                var old = globals[i];
                int globalIdx = importGlobalCount + i;
                var nativeGlobal =
                        new NativeGlobalInstance(
                                globalsBuffer,
                                globalIdx,
                                old.getValue(),
                                old.getType(),
                                old.getMutabilityType());
                nativeGlobal.setInstance(instance);
                globals[i] = nativeGlobal;
            }
        } catch (ReflectiveOperationException e) {
            throw new ChicoryException("Failed to initialize native globals", e);
        }
    }

    private static ChicoryException trapException(int trapCode) {
        return switch (trapCode) {
            case CtxBuffer.TRAP_DIV_BY_ZERO -> new ChicoryException("integer divide by zero");
            case CtxBuffer.TRAP_INT_OVERFLOW -> new ChicoryException("integer overflow");
            case CtxBuffer.TRAP_UNREACHABLE -> new ChicoryException("unreachable");
            case CtxBuffer.TRAP_TRUNC_OVERFLOW ->
                    new ChicoryException("invalid conversion to integer");
            default -> new ChicoryException("trap: unknown code " + trapCode);
        };
    }

    // --- Main dispatch ---

    private ValueLayout valTypeToLayout(ValType type) {
        if (type.equals(ValType.I32)) return ValueLayout.JAVA_INT;
        if (type.equals(ValType.I64)) return ValueLayout.JAVA_LONG;
        if (type.equals(ValType.F32)) return ValueLayout.JAVA_FLOAT;
        if (type.equals(ValType.F64)) return ValueLayout.JAVA_DOUBLE;
        // Reference types (funcref, externref) are opaque i64
        int op = type.opcode();
        if (op == ValType.ID.RefNull || op == ValType.ID.Ref) return ValueLayout.JAVA_LONG;
        throw new ChicoryException("Unsupported type for native: " + type);
    }

    private Class<?> valTypeToJavaClass(ValType type) {
        if (type.equals(ValType.I32)) return int.class;
        if (type.equals(ValType.I64)) return long.class;
        if (type.equals(ValType.F32)) return float.class;
        if (type.equals(ValType.F64)) return double.class;
        int op = type.opcode();
        if (op == ValType.ID.RefNull || op == ValType.ID.Ref) return long.class;
        throw new ChicoryException("Unsupported type: " + type);
    }

    @Override
    public long[] call(int funcId, long[] args) throws ChicoryException {
        if (funcId < numImports) {
            // Host import — delegate directly
            var imprt = instance.imports().function(funcId);
            return imprt.handle().apply(instance, args);
        }

        var handle = downcalls[funcId];
        if (handle == null) {
            throw new ChicoryException("Function " + funcId + " not compiled");
        }

        try {
            var funcType = (FunctionType) instance.type(instance.functionType(funcId));

            // Lazily replace GlobalInstance with NativeGlobalInstance (once)
            initializeNativeGlobals();

            var mem = instance.memory();
            if (mem instanceof NativeMemory nativeMemory) {
                ctxBuffer.set(
                        ValueLayout.JAVA_LONG,
                        CtxBuffer.MEM_BASE_ADDR,
                        nativeMemory.nativeAddress().address());
                ctxBuffer.set(ValueLayout.JAVA_INT, CtxBuffer.MEMORY_PAGES, mem.pages());
            }

            // Build arguments: memBase + ctxPtr + wasm params
            var callArgs = new Object[2 + args.length];
            if (mem instanceof NativeMemory nativeMemory) {
                callArgs[0] = nativeMemory.nativeAddress();
            } else {
                // No native memory — pass NULL (functions not accessing memory will work)
                callArgs[0] = MemorySegment.NULL;
            }
            callArgs[1] = ctxBuffer;

            for (int i = 0; i < args.length; i++) {
                var paramType = funcType.params().get(i);
                if (paramType.equals(ValType.I32)) {
                    callArgs[i + 2] = (int) args[i];
                } else if (paramType.equals(ValType.I64)) {
                    callArgs[i + 2] = args[i];
                } else if (paramType.equals(ValType.F32)) {
                    callArgs[i + 2] = Value.longToFloat(args[i]);
                } else if (paramType.equals(ValType.F64)) {
                    callArgs[i + 2] = Value.longToDouble(args[i]);
                } else {
                    // Reference types and others: treat as i64
                    callArgs[i + 2] = args[i];
                }
            }

            Object result = handle.invokeWithArguments(callArgs);

            // Check for traps (pre-checks write trap code to ctxBuffer)
            int trapCode = ctxBuffer.get(ValueLayout.JAVA_INT, CtxBuffer.TRAP_CODE);
            if (trapCode != 0) {
                ctxBuffer.set(ValueLayout.JAVA_INT, CtxBuffer.TRAP_CODE, 0); // reset
                throw trapException(trapCode);
            }

            // Check for exceptions from upcall stubs (cannot throw through native)
            if (pendingException != null) {
                var ex = pendingException;
                pendingException = null;
                if (ex instanceof ChicoryException ce) throw ce;
                if (ex instanceof RuntimeException re) throw re;
                throw new ChicoryException("Exception in native upcall", ex);
            }

            if (funcType.returns().isEmpty()) {
                return new long[0];
            }

            var returnType = funcType.returns().get(0);
            if (returnType.equals(ValType.I32)) {
                return new long[] {((Integer) result).longValue()};
            } else if (returnType.equals(ValType.I64)) {
                return new long[] {(Long) result};
            } else if (returnType.equals(ValType.F32)) {
                return new long[] {Value.floatToLong((Float) result)};
            } else if (returnType.equals(ValType.F64)) {
                return new long[] {Value.doubleToLong((Double) result)};
            } else {
                // Reference types and others: treat as i64
                return new long[] {(Long) result};
            }
        } catch (ChicoryException e) {
            throw e;
        } catch (Throwable e) {
            throw new ChicoryException("Native call failed for func " + funcId, e);
        }
    }
}
