package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.cranelift.CraneliftBridge;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Machine implementation that compiles Wasm functions to native x86_64
 * via Cranelift and executes them through Panama FFM.
 */
final class NativeMachine implements Machine {

    private final Instance instance;
    private final MethodHandle[] downcalls;
    private final MemorySegment codeRegion;
    private final int numImports;

    NativeMachine(Instance instance) {
        this.instance = instance;
        var module = instance.module();
        this.numImports =
                module.importSection().stream()
                                        .filter(
                                                i ->
                                                        i.importType()
                                                                == com.dylibso.chicory.wasm.types
                                                                        .ExternalType.FUNCTION)
                                        .count()
                                > 0
                        ? (int)
                                module.importSection().stream()
                                        .filter(
                                                i ->
                                                        i.importType()
                                                                == com.dylibso.chicory.wasm.types
                                                                        .ExternalType.FUNCTION)
                                        .count()
                        : 0;
        int totalFuncs = numImports + module.codeSection().functionBodyCount();
        this.downcalls = new MethodHandle[totalFuncs];

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
                            module.typeSection()
                                    .getType(module.functionSection().getFunctionType(i));

                    downcalls[funcId] =
                            createDowncall(codeRegion.asSlice(offset), (FunctionType) funcType);
                    offset += align(compiledCode[i].length, 16);
                }
            }
            PanamaExecutor.mprotectExec(codeRegion, totalSize);
        } catch (Throwable e) {
            throw new ChicoryException("Failed to set up native code", e);
        }
    }

    private static long align(long value, long alignment) {
        return (value + alignment - 1) & ~(alignment - 1);
    }

    private MethodHandle createDowncall(MemorySegment codePtr, FunctionType funcType) {
        // Our calling convention: (memBase: i64, wasm_params...) -> wasm_return
        var layouts = new java.util.ArrayList<ValueLayout>();
        layouts.add(ValueLayout.ADDRESS); // memBase pointer

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

    private ValueLayout valTypeToLayout(ValType type) {
        if (type.equals(ValType.I32)) return ValueLayout.JAVA_INT;
        if (type.equals(ValType.I64)) return ValueLayout.JAVA_LONG;
        if (type.equals(ValType.F32)) return ValueLayout.JAVA_FLOAT;
        if (type.equals(ValType.F64)) return ValueLayout.JAVA_DOUBLE;
        throw new ChicoryException("Unsupported type for native: " + type);
    }

    @Override
    public long[] call(int funcId, long[] args) throws ChicoryException {
        if (funcId < numImports) {
            // Host import — delegate to instance
            var imprt = instance.imports().function(funcId);
            return imprt.handle().apply(instance, args);
        }

        var handle = downcalls[funcId];
        if (handle == null) {
            throw new ChicoryException("Function " + funcId + " not compiled");
        }

        try {
            var funcType = (FunctionType) instance.type(instance.functionType(funcId));
            // Build arguments: memBase + wasm params
            // For now, pass NULL as memory base (no memory access)
            var callArgs = new Object[1 + args.length];
            callArgs[0] = MemorySegment.NULL; // TODO: real memory pointer

            for (int i = 0; i < args.length; i++) {
                var paramType = funcType.params().get(i);
                if (paramType.equals(ValType.I32)) {
                    callArgs[i + 1] = (int) args[i];
                } else if (paramType.equals(ValType.I64)) {
                    callArgs[i + 1] = args[i];
                } else if (paramType.equals(ValType.F32)) {
                    callArgs[i + 1] = Value.longToFloat(args[i]);
                } else if (paramType.equals(ValType.F64)) {
                    callArgs[i + 1] = Value.longToDouble(args[i]);
                } else {
                    throw new ChicoryException("Unsupported param type: " + paramType);
                }
            }

            Object result = handle.invokeWithArguments(callArgs);

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
