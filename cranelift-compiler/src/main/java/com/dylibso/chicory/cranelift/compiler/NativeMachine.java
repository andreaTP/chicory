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
 * <p>ctxBuffer layout:
 * <pre>
 *   [0]:  funcTablePtr (i64) — pointer to function pointer table
 *   [8]:  trampolinePtr (i64) — upcall stub for CALL_INDIRECT fallback
 *   [16]: callKind (i32) — 0=CALL, 1=CALL_INDIRECT
 *   [20]: funcIdOrTypeId (i32)
 *   [24]: tableIdx (i32)
 *   [28]: tableElementIdx (i32)
 *   [32]: argCount (i32)
 *   [36]: padding (i32)
 *   [40+]: args as i64
 * </pre>
 */
final class NativeMachine implements Machine {

    private static final int CTX_SIZE = 256;
    private static final Arena ARENA = Arena.ofShared();

    private final Instance instance;
    private final MethodHandle[] downcalls;
    private final MemorySegment codeRegion;
    private final MemorySegment ctxBuffer;
    private final MemorySegment funcTable;
    private final int numImports;
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

        // Create CALL_INDIRECT trampoline upcall stub
        MemorySegment trampolineStub = createTrampolineStub();

        // Write pointers to ctxBuffer
        ctxBuffer.set(ValueLayout.JAVA_LONG, 0, funcTable.address());
        ctxBuffer.set(ValueLayout.JAVA_LONG, 8, trampolineStub.address());

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

            // For uncompiled functions, store trampoline address as fallback
            for (int i = 0; i < compiledCode.length; i++) {
                if (compiledCode[i] == null) {
                    int funcId = numImports + i;
                    funcTable.set(
                            ValueLayout.JAVA_LONG, (long) funcId * 8, trampolineStub.address());
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
                // Void function: drop the long return value
                dropper =
                        MethodHandles.explicitCastArguments(
                                dropper,
                                MethodType.methodType(
                                        void.class, targetParamTypes.toArray(new Class[0])));
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
     * Dispatches an import call. Reads args from ctxBuffer.
     * Called by import upcall stubs via function pointer table.
     */
    @SuppressWarnings("unused")
    private long importDispatchDirect(int funcId) {
        try {
            var importFunc = instance.imports().function(funcId);
            int argCount = ctxBuffer.get(ValueLayout.JAVA_INT, 32);
            long[] args = new long[argCount];
            for (int i = 0; i < argCount; i++) {
                args[i] = ctxBuffer.get(ValueLayout.JAVA_LONG, 40 + 8L * i);
            }
            long[] result = importFunc.handle().apply(instance, args);
            return result.length > 0 ? result[0] : 0L;
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
            int typeId = ctx.get(ValueLayout.JAVA_INT, 20);
            int tableIdx = ctx.get(ValueLayout.JAVA_INT, 24);
            int elemIdx = ctx.get(ValueLayout.JAVA_INT, 28);
            int argCount = ctx.get(ValueLayout.JAVA_INT, 32);

            int funcId = instance.table(tableIdx).requiredRef(elemIdx);

            // Type check
            int actualTypeIdx = instance.functionType(funcId);
            if (actualTypeIdx != typeId) {
                throw new ChicoryException("indirect call type mismatch");
            }

            long[] args = new long[argCount];
            for (int i = 0; i < argCount; i++) {
                args[i] = ctx.get(ValueLayout.JAVA_LONG, 40 + 8L * i);
            }

            long[] result = this.call(funcId, args);
            return result.length > 0 ? result[0] : 0L;
        } catch (Throwable t) {
            pendingException = t;
            return 0L;
        }
    }

    // --- Main dispatch ---

    private ValueLayout valTypeToLayout(ValType type) {
        if (type.equals(ValType.I32)) return ValueLayout.JAVA_INT;
        if (type.equals(ValType.I64)) return ValueLayout.JAVA_LONG;
        if (type.equals(ValType.F32)) return ValueLayout.JAVA_FLOAT;
        if (type.equals(ValType.F64)) return ValueLayout.JAVA_DOUBLE;
        throw new ChicoryException("Unsupported type for native: " + type);
    }

    private Class<?> valTypeToJavaClass(ValType type) {
        if (type.equals(ValType.I32)) return int.class;
        if (type.equals(ValType.I64)) return long.class;
        if (type.equals(ValType.F32)) return float.class;
        if (type.equals(ValType.F64)) return double.class;
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
            // Build arguments: memBase + ctxPtr + wasm params
            var callArgs = new Object[2 + args.length];
            var mem = instance.memory();
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
                    throw new ChicoryException("Unsupported param type: " + paramType);
                }
            }

            Object result = handle.invokeWithArguments(callArgs);

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
                throw new ChicoryException("Unsupported return type: " + returnType);
            }
        } catch (ChicoryException e) {
            throw e;
        } catch (Throwable e) {
            throw new ChicoryException("Native call failed for func " + funcId, e);
        }
    }
}
