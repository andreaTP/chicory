package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.cranelift.CraneliftBridge;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Walks Wasm function bodies and emits Cranelift IR via the bridge.
 * Maintains an explicit value stack mapping Wasm stack semantics to
 * Cranelift's SSA value IDs.
 */
final class NativeCompiler {

    private final CraneliftBridge bridge;
    private final WasmModule module;

    NativeCompiler(CraneliftBridge bridge, WasmModule module) {
        this.bridge = bridge;
        this.module = module;
    }

    /**
     * Compile all module-defined functions. Returns array indexed by
     * function body index (not funcId — add numImports to get funcId).
     * Null entries mean compilation was skipped/failed.
     */
    byte[][] compileAll() {
        int count = module.codeSection().functionBodyCount();
        byte[][] results = new byte[count][];

        for (int i = 0; i < count; i++) {
            try {
                results[i] = compileFunction(i);
            } catch (Exception e) {
                System.err.println("Failed to compile function " + i + ": " + e.getMessage());
                results[i] = null;
            }
        }
        return results;
    }

    private byte[] compileFunction(int bodyIndex) {
        var body = module.codeSection().getFunctionBody(bodyIndex);
        int typeIdx = module.functionSection().getFunctionType(bodyIndex);
        var funcType = (FunctionType) module.typeSection().getType(typeIdx);

        bridge.exports().createFunction();

        // Our calling convention: first param is always memBase (i64 pointer)
        bridge.exports().addParamType(CraneliftBridge.TYPE_I64);

        // Then Wasm function params
        for (ValType param : funcType.params()) {
            bridge.exports().addParamType(valTypeToBridgeType(param));
        }

        // Return types
        for (ValType ret : funcType.returns()) {
            bridge.exports().addReturnType(valTypeToBridgeType(ret));
        }

        bridge.exports().buildFunction();

        // Create entry block
        int entry = bridge.exports().createBlock();
        bridge.exports().appendBlockParamsForFuncParams(entry);
        bridge.exports().switchToBlock(entry);
        bridge.exports().sealBlock(entry);

        // Get params as value IDs
        int memBase = bridge.exports().funcParam(entry, 0);
        int[] paramVals = new int[funcType.params().size()];
        for (int i = 0; i < paramVals.length; i++) {
            paramVals[i] = bridge.exports().funcParam(entry, i + 1);
        }

        // Declare variables for all locals (params + body locals)
        int numParams = funcType.params().size();
        int numBodyLocals = body.localTypes().size();
        int totalLocals = numParams + numBodyLocals;
        int[] localVars = new int[totalLocals];

        // Declare param locals
        for (int i = 0; i < numParams; i++) {
            localVars[i] =
                    bridge.exports().declareVar(valTypeToBridgeType(funcType.params().get(i)));
            bridge.exports().defVar(localVars[i], paramVals[i]);
        }

        // Declare body locals (initialized to zero)
        for (int i = 0; i < numBodyLocals; i++) {
            ValType localType = body.localTypes().get(i);
            localVars[numParams + i] = bridge.exports().declareVar(valTypeToBridgeType(localType));
            int zero = bridge.exports().emitIconst32(0);
            bridge.exports().defVar(localVars[numParams + i], zero);
        }

        // Walk instructions
        Deque<Integer> valueStack = new ArrayDeque<>();

        for (Instruction ins : body.instructions()) {
            emitInstruction(ins, valueStack, localVars, memBase, funcType);
        }

        return bridge.compile();
    }

    private void emitInstruction(
            Instruction ins,
            Deque<Integer> valueStack,
            int[] localVars,
            int memBase,
            FunctionType funcType) {

        switch (ins.opcode()) {
            case I32_CONST:
                valueStack.push(bridge.exports().emitIconst32((int) ins.operands()[0]));
                break;

            case I32_ADD:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIadd(a, b));
                    break;
                }

            case I32_SUB:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIsub(a, b));
                    break;
                }

            case I32_MUL:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitImul(a, b));
                    break;
                }

            case LOCAL_GET:
                valueStack.push(bridge.exports().useVar(localVars[(int) ins.operands()[0]]));
                break;

            case LOCAL_SET:
                {
                    int val = valueStack.pop();
                    bridge.exports().defVar(localVars[(int) ins.operands()[0]], val);
                    break;
                }

            case LOCAL_TEE:
                {
                    int val = valueStack.peek();
                    bridge.exports().defVar(localVars[(int) ins.operands()[0]], val);
                    break;
                }

            case DROP:
                valueStack.pop();
                break;

            case END:
                // End of function — if there's a return value on the stack, return it
                if (!funcType.returns().isEmpty() && !valueStack.isEmpty()) {
                    bridge.exports().emitReturn(valueStack.pop());
                } else if (funcType.returns().isEmpty()) {
                    bridge.exports().emitReturnVoid();
                }
                break;

            default:
                throw new UnsupportedOperationException(
                        "Opcode not yet supported by native compiler: " + ins.opcode());
        }
    }

    private static int valTypeToBridgeType(ValType type) {
        if (type.equals(ValType.I32)) return CraneliftBridge.TYPE_I32;
        if (type.equals(ValType.I64)) return CraneliftBridge.TYPE_I64;
        if (type.equals(ValType.F32)) return CraneliftBridge.TYPE_F32;
        if (type.equals(ValType.F64)) return CraneliftBridge.TYPE_F64;
        throw new UnsupportedOperationException("Unsupported ValType for native: " + type);
    }
}
