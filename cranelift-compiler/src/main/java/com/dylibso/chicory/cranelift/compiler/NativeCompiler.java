package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.cranelift.CraneliftBridge;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.FunctionImport;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Walks Wasm function bodies and emits Cranelift IR via the bridge.
 * Maintains an explicit value stack mapping Wasm stack semantics to
 * Cranelift's SSA value IDs, and a control stack for structured control flow.
 */
final class NativeCompiler {

    private final CraneliftBridge bridge;
    private final WasmModule module;
    private final int numImports;

    NativeCompiler(CraneliftBridge bridge, WasmModule module) {
        this.bridge = bridge;
        this.module = module;
        this.numImports =
                (int)
                        module.importSection().stream()
                                .filter(i -> i.importType() == ExternalType.FUNCTION)
                                .count();
    }

    // --- Control frame ---

    private static final class ControlFrame {
        enum Kind {
            BLOCK,
            LOOP,
            IF,
            FUNCTION
        }

        final Kind kind;
        final int mergeBlock; // after-END block (forward target)
        final int loopBlock; // LOOP only: loop header (backward target), -1 otherwise
        int elseBlock; // IF only: else block, -1 otherwise
        final int[] mergeParamIds; // value IDs of merge block's params (one per return)
        final FunctionType blockType; // full block type (params + returns)
        final int stackHeight; // value stack height at block entry
        boolean unreachable; // after br/return, code is dead
        boolean hasElse; // IF: have we seen ELSE?

        ControlFrame(
                Kind kind,
                int mergeBlock,
                int loopBlock,
                int elseBlock,
                int[] mergeParamIds,
                FunctionType blockType,
                int stackHeight) {
            this.kind = kind;
            this.mergeBlock = mergeBlock;
            this.loopBlock = loopBlock;
            this.elseBlock = elseBlock;
            this.mergeParamIds = mergeParamIds;
            this.blockType = blockType;
            this.stackHeight = stackHeight;
        }

        int branchTarget() {
            return kind == Kind.LOOP ? loopBlock : mergeBlock;
        }

        int branchArgCount() {
            if (kind == Kind.LOOP) {
                return blockType.params().size();
            }
            return blockType.returns().size();
        }
    }

    // --- Compilation ---

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

        // Our calling convention: memBase (i64), ctxPtr (i64), then Wasm params
        bridge.exports().addParamType(CraneliftBridge.TYPE_I64); // memBase
        bridge.exports().addParamType(CraneliftBridge.TYPE_I64); // ctxPtr

        // Then Wasm function params
        for (ValType param : funcType.params()) {
            bridge.exports().addParamType(valTypeToBridgeType(param));
        }

        // Return types
        for (ValType ret : funcType.returns()) {
            bridge.exports().addReturnType(valTypeToBridgeType(ret));
        }

        bridge.exports().buildFunction();

        // Create entry block — do NOT seal (deferred sealing)
        int entry = bridge.exports().createBlock();
        bridge.exports().appendBlockParamsForFuncParams(entry);
        bridge.exports().switchToBlock(entry);

        // Get params as value IDs
        int memBaseParam = bridge.exports().funcParam(entry, 0);
        int ctxPtr = bridge.exports().funcParam(entry, 1);
        int[] paramVals = new int[funcType.params().size()];
        for (int i = 0; i < paramVals.length; i++) {
            paramVals[i] = bridge.exports().funcParam(entry, i + 2);
        }

        // memBase as a variable (can be re-defined after memory.grow)
        int memBaseVar = bridge.exports().declareVar(CraneliftBridge.TYPE_I64);
        bridge.exports().defVar(memBaseVar, memBaseParam);

        // Cache for SigRef IDs per unique function type (for call_indirect)
        Map<String, Integer> sigRefCache = new HashMap<>();

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
            int zero = emitZero(localType);
            bridge.exports().defVar(localVars[numParams + i], zero);
        }

        // Walk instructions with control stack
        Deque<Integer> valueStack = new ArrayDeque<>();
        Deque<ControlFrame> controlStack = new ArrayDeque<>();

        // Push implicit function-level frame
        controlStack.push(
                new ControlFrame(ControlFrame.Kind.FUNCTION, -1, -1, -1, new int[0], funcType, 0));

        for (AnnotatedInstruction ins : body.instructions()) {
            emitInstruction(
                    ins,
                    valueStack,
                    controlStack,
                    localVars,
                    memBaseVar,
                    ctxPtr,
                    sigRefCache,
                    funcType);
        }

        // Seal all blocks at the end (deferred sealing)
        bridge.exports().sealAllBlocks();

        return bridge.compile();
    }

    private int emitZero(ValType type) {
        if (type.equals(ValType.I32)) return bridge.exports().emitIconst32(0);
        if (type.equals(ValType.I64)) return bridge.exports().emitIconst64(0, 0);
        if (type.equals(ValType.F32)) return bridge.exports().emitF32const(0);
        if (type.equals(ValType.F64)) return bridge.exports().emitF64const(0, 0);
        throw new UnsupportedOperationException("Unsupported local type: " + type);
    }

    // --- Block type decoding ---

    private FunctionType decodeBlockType(AnnotatedInstruction ins) {
        long typeId = ins.operands()[0];
        if (typeId == 0x40) {
            return FunctionType.empty();
        }
        if (ValType.isValid(typeId)) {
            return FunctionType.returning(ValType.builder().fromId(typeId).build());
        }
        // Type index — look up in type section
        return (FunctionType) module.typeSection().getType((int) typeId);
    }

    // --- Control stack helpers ---

    private static ControlFrame getControlFrame(Deque<ControlFrame> controlStack, int depth) {
        Iterator<ControlFrame> it = controlStack.iterator();
        for (int i = 0; i < depth; i++) {
            it.next();
        }
        return it.next();
    }

    private int[] appendBlockParams(int blockId, java.util.List<ValType> types) {
        int[] paramIds = new int[types.size()];
        for (int i = 0; i < types.size(); i++) {
            paramIds[i] =
                    bridge.exports().appendBlockParam(blockId, valTypeToBridgeType(types.get(i)));
        }
        return paramIds;
    }

    private void emitJumpToBlock(int blockId, int argCount, Deque<Integer> valueStack) {
        if (argCount == 0) {
            bridge.exports().emitJump(blockId);
        } else if (argCount == 1) {
            bridge.exports().emitJumpWithArg(blockId, valueStack.pop());
        } else {
            int[] args = new int[argCount];
            for (int i = argCount - 1; i >= 0; i--) {
                args[i] = valueStack.pop();
            }
            for (int i = 0; i < argCount; i++) {
                bridge.exports().pushCallArg(args[i]);
            }
            bridge.exports().emitJumpWithArgs(blockId);
        }
    }

    private static void trimValueStack(Deque<Integer> valueStack, int targetHeight) {
        while (valueStack.size() > targetHeight) {
            valueStack.pop();
        }
    }

    // --- Instruction emission ---

    private void emitInstruction(
            AnnotatedInstruction ins,
            Deque<Integer> valueStack,
            Deque<ControlFrame> controlStack,
            int[] localVars,
            int memBaseVar,
            int ctxPtr,
            Map<String, Integer> sigRefCache,
            FunctionType funcType) {

        // Skip dead code after unconditional transfers
        if (!controlStack.isEmpty() && controlStack.peek().unreachable) {
            switch (ins.opcode()) {
                case END:
                case ELSE:
                    // These reset unreachable — process normally below
                    break;
                case BLOCK:
                case LOOP:
                case IF:
                    // Push dummy frame to keep control stack balanced
                    controlStack.push(
                            new ControlFrame(
                                    ins.opcode() == OpCode.IF
                                            ? ControlFrame.Kind.IF
                                            : ins.opcode() == OpCode.LOOP
                                                    ? ControlFrame.Kind.LOOP
                                                    : ControlFrame.Kind.BLOCK,
                                    -1,
                                    -1,
                                    -1,
                                    new int[0],
                                    FunctionType.empty(),
                                    valueStack.size()));
                    controlStack.peek().unreachable = true;
                    return;
                default:
                    return; // skip all other instructions
            }
        }

        switch (ins.opcode()) {
            // --- Constants ---
            case I32_CONST:
                valueStack.push(bridge.exports().emitIconst32((int) ins.operands()[0]));
                break;

            case I64_CONST:
                {
                    long val = ins.operands()[0];
                    valueStack.push(bridge.exports().emitIconst64((int) val, (int) (val >>> 32)));
                    break;
                }

            case F32_CONST:
                valueStack.push(bridge.exports().emitF32const((int) ins.operands()[0]));
                break;

            case F64_CONST:
                {
                    long bits = ins.operands()[0];
                    valueStack.push(bridge.exports().emitF64const((int) bits, (int) (bits >>> 32)));
                    break;
                }

            // --- Arithmetic ---
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

            case I32_DIV_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSdiv(a, bb));
                    break;
                }
            case I32_DIV_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUdiv(a, bb));
                    break;
                }
            case I32_REM_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSrem(a, bb));
                    break;
                }
            case I32_REM_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUrem(a, bb));
                    break;
                }
            case I32_AND:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBand(a, bb));
                    break;
                }
            case I32_OR:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBor(a, bb));
                    break;
                }
            case I32_XOR:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBxor(a, bb));
                    break;
                }
            case I32_SHL:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIshl(a, bb));
                    break;
                }
            case I32_SHR_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSshr(a, bb));
                    break;
                }
            case I32_SHR_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUshr(a, bb));
                    break;
                }
            case I32_ROTL:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitRotl(a, bb));
                    break;
                }
            case I32_ROTR:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitRotr(a, bb));
                    break;
                }
            case I32_CLZ:
                valueStack.push(bridge.exports().emitClz(valueStack.pop()));
                break;
            case I32_CTZ:
                valueStack.push(bridge.exports().emitCtz(valueStack.pop()));
                break;
            case I32_POPCNT:
                valueStack.push(bridge.exports().emitPopcnt(valueStack.pop()));
                break;

            // --- Comparisons ---
            case I32_EQZ:
                valueStack.push(bridge.exports().emitEqz(valueStack.pop()));
                break;
            case I32_EQ:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(0, a, bb));
                    break;
                }
            case I32_NE:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(1, a, bb));
                    break;
                }
            case I32_LT_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(2, a, bb));
                    break;
                }
            case I32_LT_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(3, a, bb));
                    break;
                }
            case I32_GT_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(4, a, bb));
                    break;
                }
            case I32_GT_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(5, a, bb));
                    break;
                }
            case I32_LE_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(6, a, bb));
                    break;
                }
            case I32_LE_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(7, a, bb));
                    break;
                }
            case I32_GE_S:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(8, a, bb));
                    break;
                }
            case I32_GE_U:
                {
                    int bb = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(9, a, bb));
                    break;
                }

            // --- Extensions ---
            case I32_EXTEND_8_S:
                valueStack.push(bridge.exports().emitSextend832(valueStack.pop()));
                break;
            case I32_EXTEND_16_S:
                valueStack.push(bridge.exports().emitSextend1632(valueStack.pop()));
                break;

            // --- Memory ---
            case I32_STORE:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStoreI32(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case I32_LOAD:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoadI32(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_STORE:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStoreI64(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case I64_LOAD:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoadI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case F32_STORE:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStoreF32(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case F32_LOAD:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoadF32(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case F64_STORE:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStoreF64(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case F64_LOAD:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoadF64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            // --- Sub-word memory loads (i32) ---
            case I32_LOAD8_U:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad8u(bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I32_LOAD8_S:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad8s(bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I32_LOAD16_U:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad16u(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I32_LOAD16_S:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad16s(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            // --- Sub-word memory stores (i32) ---
            case I32_STORE8:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStore8(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case I32_STORE16:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStore16(bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            // --- Sub-word memory loads (i64) ---
            case I64_LOAD8_U:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad8uI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_LOAD8_S:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad8sI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_LOAD16_U:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad16uI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_LOAD16_S:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad16sI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_LOAD32_U:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad32uI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            case I64_LOAD32_S:
                {
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    valueStack.push(
                            bridge.exports()
                                    .emitLoad32sI64(
                                            bridge.exports().useVar(memBaseVar), addr, offset));
                    break;
                }

            // --- Sub-word memory stores (i64) ---
            case I64_STORE8:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStore8I64(
                                    bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case I64_STORE16:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStore16I64(
                                    bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            case I64_STORE32:
                {
                    int value = valueStack.pop();
                    int addr = valueStack.pop();
                    int offset = (int) ins.operands()[1];
                    bridge.exports()
                            .emitStore32I64(
                                    bridge.exports().useVar(memBaseVar), addr, value, offset);
                    break;
                }

            // --- Locals ---
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

            // --- Select ---
            case SELECT:
            case SELECT_T:
                {
                    int cond = valueStack.pop();
                    int val2 = valueStack.pop();
                    int val1 = valueStack.pop();
                    valueStack.push(bridge.exports().emitSelect(cond, val1, val2));
                    break;
                }

            // --- Globals ---
            case GLOBAL_GET:
                {
                    int globalIdx = (int) ins.operands()[0];
                    // Load globalsPtr from ctxBuffer[200]
                    int zero = bridge.exports().emitIconst32(0);
                    int globalsPtr = bridge.exports().emitLoadI64(ctxPtr, zero, 200);
                    // Load value as i64 from globals buffer
                    int offsetVal = bridge.exports().emitIconst32(globalIdx * 8);
                    int rawVal = bridge.exports().emitLoadI64(globalsPtr, offsetVal, 0);
                    // Narrow to the actual global type
                    ValType globalType = resolveGlobalType(globalIdx);
                    valueStack.push(narrowFromI64ForType(rawVal, globalType));
                    break;
                }

            case GLOBAL_SET:
                {
                    int globalIdx = (int) ins.operands()[0];
                    int value = valueStack.pop();
                    // Widen to i64 for storage
                    ValType globalType = resolveGlobalType(globalIdx);
                    int widened = widenToI64ForType(value, globalType);
                    // Load globalsPtr from ctxBuffer[200]
                    int zero = bridge.exports().emitIconst32(0);
                    int globalsPtr = bridge.exports().emitLoadI64(ctxPtr, zero, 200);
                    // Store to globals buffer
                    int offsetVal = bridge.exports().emitIconst32(globalIdx * 8);
                    bridge.exports().emitStoreI64(globalsPtr, offsetVal, widened, 0);
                    break;
                }

            // --- Memory operations ---
            case MEMORY_SIZE:
                {
                    // Load current page count from ctxBuffer[216]
                    int zero = bridge.exports().emitIconst32(0);
                    int pages = bridge.exports().emitLoadI32(ctxPtr, zero, 216);
                    valueStack.push(pages);
                    break;
                }

            case MEMORY_GROW:
                {
                    int delta = valueStack.pop();
                    int zero = bridge.exports().emitIconst32(0);
                    // Write grow delta to ctxBuffer[32] as argCount (repurposed)
                    bridge.exports().emitStoreI32(ctxPtr, zero, delta, 32);
                    // Load memGrowStub ptr from ctxBuffer[208]
                    int memGrowPtr = bridge.exports().emitLoadI64(ctxPtr, zero, 208);
                    // Call memGrowStub(ctxPtr) -> i64 (old page count or -1)
                    int growSig = getOrCreateTrampolineSigRef(sigRefCache);
                    bridge.exports().pushCallArg(ctxPtr);
                    int rawResult = bridge.exports().emitCallIndirect(growSig, memGrowPtr);
                    // Result is i32 (old page count or -1)
                    int result = bridge.exports().emitIreduceI32(rawResult);
                    valueStack.push(result);
                    // Reload memBase from ctxBuffer[224] (may have changed)
                    int newMemBase = bridge.exports().emitLoadI64(ctxPtr, zero, 224);
                    bridge.exports().defVar(memBaseVar, newMemBase);
                    break;
                }

            // --- Unreachable ---
            case UNREACHABLE:
                bridge.exports().emitTrap();
                controlStack.peek().unreachable = true;
                break;

            // --- i64 Arithmetic ---
            case I64_ADD:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIadd(a, b));
                    break;
                }

            case I64_SUB:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIsub(a, b));
                    break;
                }

            case I64_MUL:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitImul(a, b));
                    break;
                }

            case I64_DIV_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSdiv(a, b));
                    break;
                }

            case I64_DIV_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUdiv(a, b));
                    break;
                }

            case I64_REM_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSrem(a, b));
                    break;
                }

            case I64_REM_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUrem(a, b));
                    break;
                }

            case I64_AND:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBand(a, b));
                    break;
                }

            case I64_OR:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBor(a, b));
                    break;
                }

            case I64_XOR:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitBxor(a, b));
                    break;
                }

            case I64_SHL:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIshl(a, b));
                    break;
                }

            case I64_SHR_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitSshr(a, b));
                    break;
                }

            case I64_SHR_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitUshr(a, b));
                    break;
                }

            case I64_ROTL:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitRotl(a, b));
                    break;
                }

            case I64_ROTR:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitRotr(a, b));
                    break;
                }

            case I64_CLZ:
                valueStack.push(bridge.exports().emitClz(valueStack.pop()));
                break;

            case I64_CTZ:
                valueStack.push(bridge.exports().emitCtz(valueStack.pop()));
                break;

            case I64_POPCNT:
                valueStack.push(bridge.exports().emitPopcnt(valueStack.pop()));
                break;

            // --- i64 Comparisons ---
            case I64_EQZ:
                valueStack.push(bridge.exports().emitEqzI64(valueStack.pop()));
                break;

            case I64_EQ:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(0, a, b));
                    break;
                }

            case I64_NE:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(1, a, b));
                    break;
                }

            case I64_LT_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(2, a, b));
                    break;
                }

            case I64_LT_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(3, a, b));
                    break;
                }

            case I64_GT_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(4, a, b));
                    break;
                }

            case I64_GT_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(5, a, b));
                    break;
                }

            case I64_LE_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(6, a, b));
                    break;
                }

            case I64_LE_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(7, a, b));
                    break;
                }

            case I64_GE_S:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(8, a, b));
                    break;
                }

            case I64_GE_U:
                {
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(bridge.exports().emitIcmp(9, a, b));
                    break;
                }

            // --- i64 Extensions ---
            case I64_EXTEND_I32_S:
                valueStack.push(bridge.exports().emitSextendI64(valueStack.pop()));
                break;

            case I64_EXTEND_I32_U:
                valueStack.push(bridge.exports().emitUextendI64(valueStack.pop()));
                break;

            case I64_EXTEND_8_S:
                valueStack.push(bridge.exports().emitSextend864(valueStack.pop()));
                break;

            case I64_EXTEND_16_S:
                valueStack.push(bridge.exports().emitSextend1664(valueStack.pop()));
                break;

            case I64_EXTEND_32_S:
                valueStack.push(bridge.exports().emitSextend3264(valueStack.pop()));
                break;

            // --- i32 wrap i64 ---
            case I32_WRAP_I64:
                valueStack.push(bridge.exports().emitI32WrapI64(valueStack.pop()));
                break;

            // --- Misc ---
            case NOP:
                break;

            case DROP:
                valueStack.pop();
                break;

            // --- Control flow ---
            case BLOCK:
                {
                    FunctionType bt = decodeBlockType(ins);
                    int mergeBlock = bridge.exports().createBlock();
                    int[] mergeParamIds = appendBlockParams(mergeBlock, bt.returns());
                    // Pop block params from stack (block inputs)
                    int savedHeight = valueStack.size() - bt.params().size();
                    controlStack.push(
                            new ControlFrame(
                                    ControlFrame.Kind.BLOCK,
                                    mergeBlock,
                                    -1,
                                    -1,
                                    mergeParamIds,
                                    bt,
                                    savedHeight));
                    break;
                }

            case LOOP:
                {
                    FunctionType bt = decodeBlockType(ins);
                    int loopHeader = bridge.exports().createBlock();
                    int mergeBlock = bridge.exports().createBlock();
                    int[] mergeParamIds = appendBlockParams(mergeBlock, bt.returns());
                    // Loop header gets params (for backward branches)
                    int[] loopParamIds = appendBlockParams(loopHeader, bt.params());
                    // Pop block params from stack and pass to loop header
                    int savedHeight = valueStack.size() - bt.params().size();
                    emitJumpToBlock(loopHeader, bt.params().size(), valueStack);
                    bridge.exports().switchToBlock(loopHeader);
                    // Push loop header params onto value stack
                    for (int pid : loopParamIds) {
                        valueStack.push(pid);
                    }
                    controlStack.push(
                            new ControlFrame(
                                    ControlFrame.Kind.LOOP,
                                    mergeBlock,
                                    loopHeader,
                                    -1,
                                    mergeParamIds,
                                    bt,
                                    savedHeight));
                    break;
                }

            case IF:
                {
                    FunctionType bt = decodeBlockType(ins);
                    int condition = valueStack.pop();
                    int thenBlock = bridge.exports().createBlock();
                    int elseBlock = bridge.exports().createBlock();
                    int mergeBlock = bridge.exports().createBlock();
                    int[] mergeParamIds = appendBlockParams(mergeBlock, bt.returns());
                    int savedHeight = valueStack.size() - bt.params().size();
                    bridge.exports().emitBrif(condition, thenBlock, elseBlock);
                    bridge.exports().switchToBlock(thenBlock);
                    controlStack.push(
                            new ControlFrame(
                                    ControlFrame.Kind.IF,
                                    mergeBlock,
                                    -1,
                                    elseBlock,
                                    mergeParamIds,
                                    bt,
                                    savedHeight));
                    break;
                }

            case ELSE:
                {
                    ControlFrame frame = controlStack.peek();
                    if (frame.mergeBlock < 0) {
                        frame.hasElse = true;
                        break;
                    }
                    if (!frame.unreachable) {
                        emitJumpToBlock(
                                frame.mergeBlock, frame.blockType.returns().size(), valueStack);
                    }
                    trimValueStack(valueStack, frame.stackHeight);
                    bridge.exports().switchToBlock(frame.elseBlock);
                    // Re-push block params for else branch (they're still on the
                    // outer stack at stackHeight)
                    frame.hasElse = true;
                    frame.unreachable = false;
                    break;
                }

            case END:
                {
                    ControlFrame frame = controlStack.pop();
                    boolean isDummy =
                            frame.mergeBlock < 0 && frame.kind != ControlFrame.Kind.FUNCTION;

                    switch (frame.kind) {
                        case FUNCTION:
                            if (!frame.unreachable) {
                                int retCount = frame.blockType.returns().size();
                                if (retCount > 0 && !valueStack.isEmpty()) {
                                    // TODO: multi-return functions
                                    bridge.exports().emitReturn(valueStack.pop());
                                } else {
                                    bridge.exports().emitReturnVoid();
                                }
                            }
                            break;

                        case BLOCK:
                        case LOOP:
                            if (isDummy) break;
                            if (!frame.unreachable) {
                                emitJumpToBlock(
                                        frame.mergeBlock,
                                        frame.blockType.returns().size(),
                                        valueStack);
                            }
                            bridge.exports().switchToBlock(frame.mergeBlock);
                            trimValueStack(valueStack, frame.stackHeight);
                            for (int pid : frame.mergeParamIds) {
                                valueStack.push(pid);
                            }
                            break;

                        case IF:
                            if (isDummy) break;
                            if (!frame.hasElse) {
                                if (!frame.unreachable) {
                                    emitJumpToBlock(
                                            frame.mergeBlock,
                                            frame.blockType.returns().size(),
                                            valueStack);
                                }
                                bridge.exports().switchToBlock(frame.elseBlock);
                                bridge.exports().emitJump(frame.mergeBlock);
                            } else {
                                if (!frame.unreachable) {
                                    emitJumpToBlock(
                                            frame.mergeBlock,
                                            frame.blockType.returns().size(),
                                            valueStack);
                                }
                            }
                            bridge.exports().switchToBlock(frame.mergeBlock);
                            trimValueStack(valueStack, frame.stackHeight);
                            for (int pid : frame.mergeParamIds) {
                                valueStack.push(pid);
                            }
                            break;
                    }
                    if (!controlStack.isEmpty() && !isDummy) {
                        controlStack.peek().unreachable = false;
                    }
                    break;
                }

            case BR:
                {
                    int depth = (int) ins.operands()[0];
                    ControlFrame target = getControlFrame(controlStack, depth);
                    if (target.kind == ControlFrame.Kind.FUNCTION) {
                        int retCount = target.blockType.returns().size();
                        if (retCount > 0) {
                            bridge.exports().emitReturn(valueStack.pop());
                        } else {
                            bridge.exports().emitReturnVoid();
                        }
                    } else {
                        int brTarget = target.branchTarget();
                        int argCount = target.branchArgCount();
                        emitJumpToBlock(brTarget, argCount, valueStack);
                    }
                    controlStack.peek().unreachable = true;
                    break;
                }

            case BR_IF:
                {
                    int depth = (int) ins.operands()[0];
                    int condition = valueStack.pop();
                    ControlFrame target = getControlFrame(controlStack, depth);
                    int brTarget = target.branchTarget();
                    int fallthroughBlock = bridge.exports().createBlock();
                    int argCount = target.branchArgCount();

                    if (argCount > 0) {
                        // Pop args, push to accumulator, emit brif with jump args
                        int[] args = new int[argCount];
                        for (int i = argCount - 1; i >= 0; i--) {
                            args[i] = valueStack.pop();
                        }
                        for (int i = 0; i < argCount; i++) {
                            bridge.exports().pushCallArg(args[i]);
                        }
                        bridge.exports()
                                .emitBrifWithJumpArgs(condition, brTarget, fallthroughBlock);
                        // Push args back for fallthrough
                        for (int i = 0; i < argCount; i++) {
                            valueStack.push(args[i]);
                        }
                    } else {
                        bridge.exports().emitBrif(condition, brTarget, fallthroughBlock);
                    }
                    bridge.exports().switchToBlock(fallthroughBlock);
                    break;
                }

            case RETURN:
                {
                    ControlFrame funcFrame = null;
                    for (ControlFrame f : controlStack) {
                        funcFrame = f;
                    }
                    if (funcFrame != null && !funcFrame.blockType.returns().isEmpty()) {
                        bridge.exports().emitReturn(valueStack.pop());
                    } else {
                        bridge.exports().emitReturnVoid();
                    }
                    controlStack.peek().unreachable = true;
                    break;
                }

            // --- Function calls ---
            case CALL:
                {
                    int targetFuncId = (int) ins.operands()[0];
                    FunctionType targetType = resolveCallTargetType(targetFuncId);

                    // Get or create SigRef for the target's calling convention
                    int sigRef = getOrCreateSigRef(targetType, sigRefCache);

                    // Pop Wasm args from value stack (reverse order)
                    int argCount = targetType.params().size();
                    int[] argVals = new int[argCount];
                    for (int i = argCount - 1; i >= 0; i--) {
                        argVals[i] = valueStack.pop();
                    }

                    // Write args to ctxBuffer for imports (they read from buffer)
                    int zero = bridge.exports().emitIconst32(0);
                    bridge.exports()
                            .emitStoreI32(
                                    ctxPtr, zero, bridge.exports().emitIconst32(argCount), 32);
                    for (int i = 0; i < argCount; i++) {
                        int widened = widenToI64(argVals[i], targetType.params().get(i));
                        bridge.exports().emitStoreI64(ctxPtr, zero, widened, 40 + 8 * i);
                    }

                    // Load function pointer from funcTable[funcId]
                    int funcTablePtr = bridge.exports().emitLoadI64(ctxPtr, zero, 0);
                    int funcIdOffset =
                            bridge.exports().emitIconst32(targetFuncId * 8); // byte offset
                    int funcPtr = bridge.exports().emitLoadI64(funcTablePtr, funcIdOffset, 0);

                    // Push call args: memBase, ctxPtr, then wasm args
                    bridge.exports().pushCallArg(bridge.exports().useVar(memBaseVar));
                    bridge.exports().pushCallArg(ctxPtr);
                    for (int i = 0; i < argCount; i++) {
                        bridge.exports().pushCallArg(argVals[i]);
                    }

                    // Emit call_indirect
                    int rawResult = bridge.exports().emitCallIndirect(sigRef, funcPtr);

                    // Push result if function returns a value
                    if (!targetType.returns().isEmpty()) {
                        valueStack.push(rawResult);
                    }
                    break;
                }

            case CALL_INDIRECT:
                {
                    int typeId = (int) ins.operands()[0];
                    int tableIdx = (int) ins.operands()[1];
                    FunctionType targetType = (FunctionType) module.typeSection().getType(typeId);

                    // Pop table element index from Wasm stack
                    int tableElemIdx = valueStack.pop();

                    // Pop Wasm args
                    int argCount = targetType.params().size();
                    int[] argVals = new int[argCount];
                    for (int i = argCount - 1; i >= 0; i--) {
                        argVals[i] = valueStack.pop();
                    }

                    int zero = bridge.exports().emitIconst32(0);

                    // Write CALL_INDIRECT metadata to ctxBuffer
                    bridge.exports()
                            .emitStoreI32(ctxPtr, zero, bridge.exports().emitIconst32(typeId), 20);
                    bridge.exports()
                            .emitStoreI32(
                                    ctxPtr, zero, bridge.exports().emitIconst32(tableIdx), 24);
                    bridge.exports().emitStoreI32(ctxPtr, zero, tableElemIdx, 28);
                    bridge.exports()
                            .emitStoreI32(
                                    ctxPtr, zero, bridge.exports().emitIconst32(argCount), 32);

                    // Write args to ctxBuffer (widened to i64)
                    for (int i = 0; i < argCount; i++) {
                        int widened = widenToI64(argVals[i], targetType.params().get(i));
                        bridge.exports().emitStoreI64(ctxPtr, zero, widened, 40 + 8 * i);
                    }

                    // Load trampoline ptr from ctxBuffer[8]
                    int trampolinePtr = bridge.exports().emitLoadI64(ctxPtr, zero, 8);

                    // Create SigRef for trampoline: (i64) -> i64
                    int trampolineSig = getOrCreateTrampolineSigRef(sigRefCache);

                    // Call trampoline with ctxPtr
                    bridge.exports().pushCallArg(ctxPtr);
                    int rawResult = bridge.exports().emitCallIndirect(trampolineSig, trampolinePtr);

                    // Narrow result and push
                    if (!targetType.returns().isEmpty()) {
                        int narrowed = narrowFromI64(rawResult, targetType.returns().get(0));
                        valueStack.push(narrowed);
                    }
                    break;
                }

            default:
                throw new UnsupportedOperationException(
                        "Opcode not yet supported by native compiler: " + ins.opcode());
        }
    }

    // --- Call helpers ---

    private FunctionType resolveCallTargetType(int funcId) {
        if (funcId < numImports) {
            int idx = 0;
            for (var imp : module.importSection().stream().toList()) {
                if (imp.importType() == ExternalType.FUNCTION) {
                    if (idx == funcId) {
                        int typeIdx = ((FunctionImport) imp).typeIndex();
                        return (FunctionType) module.typeSection().getType(typeIdx);
                    }
                    idx++;
                }
            }
            throw new IllegalArgumentException("Import function not found: " + funcId);
        }
        int bodyIdx = funcId - numImports;
        int typeIdx = module.functionSection().getFunctionType(bodyIdx);
        return (FunctionType) module.typeSection().getType(typeIdx);
    }

    /**
     * Get or create a SigRef matching the native calling convention for a function type:
     * (memBase: i64, ctxPtr: i64, wasm_params...) -> wasm_return
     */
    private int getOrCreateSigRef(FunctionType funcType, Map<String, Integer> cache) {
        String key = funcType.toString();
        Integer cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        bridge.exports().beginSig();
        bridge.exports().sigAddParam(CraneliftBridge.TYPE_I64); // memBase
        bridge.exports().sigAddParam(CraneliftBridge.TYPE_I64); // ctxPtr
        for (ValType param : funcType.params()) {
            bridge.exports().sigAddParam(valTypeToBridgeType(param));
        }
        for (ValType ret : funcType.returns()) {
            bridge.exports().sigAddReturn(valTypeToBridgeType(ret));
        }
        int sigRef = bridge.exports().endSig();
        cache.put(key, sigRef);
        return sigRef;
    }

    /**
     * Get or create a SigRef for the CALL_INDIRECT trampoline: (i64) -> i64
     */
    private int getOrCreateTrampolineSigRef(Map<String, Integer> cache) {
        String key = "__trampoline__";
        Integer cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        bridge.exports().beginSig();
        bridge.exports().sigAddParam(CraneliftBridge.TYPE_I64);
        bridge.exports().sigAddReturn(CraneliftBridge.TYPE_I64);
        int sigRef = bridge.exports().endSig();
        cache.put(key, sigRef);
        return sigRef;
    }

    private int widenToI64(int valId, ValType type) {
        if (type.equals(ValType.I32)) {
            return bridge.exports().emitUextendI64(valId);
        }
        // I64 is already 64-bit
        return valId;
    }

    private int narrowFromI64(int valId, ValType type) {
        if (type.equals(ValType.I32)) {
            return bridge.exports().emitIreduceI32(valId);
        }
        return valId;
    }

    private int widenToI64ForType(int valId, ValType type) {
        if (type.equals(ValType.I32)) {
            return bridge.exports().emitUextendI64(valId);
        }
        if (type.equals(ValType.F32)) {
            int bits = bridge.exports().emitBitcastF32ToI32(valId);
            return bridge.exports().emitUextendI64(bits);
        }
        if (type.equals(ValType.F64)) {
            return bridge.exports().emitBitcastF64ToI64(valId);
        }
        return valId; // I64
    }

    private int narrowFromI64ForType(int valId, ValType type) {
        if (type.equals(ValType.I32)) {
            return bridge.exports().emitIreduceI32(valId);
        }
        if (type.equals(ValType.F32)) {
            int narrow = bridge.exports().emitIreduceI32(valId);
            return bridge.exports().emitBitcastI32ToF32(narrow);
        }
        if (type.equals(ValType.F64)) {
            return bridge.exports().emitBitcastI64ToF64(valId);
        }
        return valId; // I64
    }

    private ValType resolveGlobalType(int globalIdx) {
        // Check imported globals first
        int importGlobalIdx = 0;
        for (var imp : module.importSection().stream().toList()) {
            if (imp.importType() == ExternalType.GLOBAL) {
                if (importGlobalIdx == globalIdx) {
                    return ((com.dylibso.chicory.wasm.types.GlobalImport) imp).type();
                }
                importGlobalIdx++;
            }
        }
        // Module-defined global
        int moduleGlobalIdx = globalIdx - importGlobalIdx;
        return module.globalSection().getGlobal(moduleGlobalIdx).valueType();
    }

    private static int valTypeToBridgeType(ValType type) {
        if (type.equals(ValType.I32)) return CraneliftBridge.TYPE_I32;
        if (type.equals(ValType.I64)) return CraneliftBridge.TYPE_I64;
        if (type.equals(ValType.F32)) return CraneliftBridge.TYPE_F32;
        if (type.equals(ValType.F64)) return CraneliftBridge.TYPE_F64;
        throw new UnsupportedOperationException("Unsupported ValType for native: " + type);
    }
}
