package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.ValType;

/**
 * Static methods that emit Cranelift IR for each opcode category.
 * No control flow logic — these are pure opcode handlers that pop
 * operands from the value stack, call bridge exports, and push results.
 */
final class NativeEmitters {

    private NativeEmitters() {}

    // --- Constants ---

    static void emitI32Const(EmitContext ctx, AnnotatedInstruction ins) {
        ctx.valueStack.push(ctx.bridge.exports().emitIconst32((int) ins.operands()[0]));
    }

    static void emitI64Const(EmitContext ctx, AnnotatedInstruction ins) {
        long val = ins.operands()[0];
        ctx.valueStack.push(ctx.bridge.exports().emitIconst64((int) val, (int) (val >>> 32)));
    }

    static void emitF32Const(EmitContext ctx, AnnotatedInstruction ins) {
        ctx.valueStack.push(ctx.bridge.exports().emitF32const((int) ins.operands()[0]));
    }

    static void emitF64Const(EmitContext ctx, AnnotatedInstruction ins) {
        long bits = ins.operands()[0];
        ctx.valueStack.push(ctx.bridge.exports().emitF64const((int) bits, (int) (bits >>> 32)));
    }

    // --- i32 Arithmetic ---

    static void emitI32BinaryOp(EmitContext ctx, int op) {
        int b = ctx.valueStack.pop();
        int a = ctx.valueStack.pop();
        int result =
                switch (op) {
                    case 0 -> ctx.bridge.exports().emitIadd(a, b);
                    case 1 -> ctx.bridge.exports().emitIsub(a, b);
                    case 2 -> ctx.bridge.exports().emitImul(a, b);
                    case 3 -> ctx.bridge.exports().emitBand(a, b);
                    case 4 -> ctx.bridge.exports().emitBor(a, b);
                    case 5 -> ctx.bridge.exports().emitBxor(a, b);
                    case 6 -> ctx.bridge.exports().emitIshl(a, b);
                    case 7 -> ctx.bridge.exports().emitSshr(a, b);
                    case 8 -> ctx.bridge.exports().emitUshr(a, b);
                    case 9 -> ctx.bridge.exports().emitRotl(a, b);
                    case 10 -> ctx.bridge.exports().emitRotr(a, b);
                    default -> throw new IllegalArgumentException("Unknown i32 binary op: " + op);
                };
        ctx.valueStack.push(result);
    }

    static void emitI32UnaryOp(EmitContext ctx, int op) {
        int val = ctx.valueStack.pop();
        int result =
                switch (op) {
                    case 0 -> ctx.bridge.exports().emitClz(val);
                    case 1 -> ctx.bridge.exports().emitCtz(val);
                    case 2 -> ctx.bridge.exports().emitPopcnt(val);
                    case 3 -> ctx.bridge.exports().emitEqz(val);
                    default -> throw new IllegalArgumentException("Unknown i32 unary op: " + op);
                };
        ctx.valueStack.push(result);
    }

    // --- i64 Arithmetic ---

    static void emitI64UnaryOp(EmitContext ctx, int op) {
        int val = ctx.valueStack.pop();
        int result =
                switch (op) {
                    case 0 -> ctx.bridge.exports().emitClz(val);
                    case 1 -> ctx.bridge.exports().emitCtz(val);
                    case 2 -> ctx.bridge.exports().emitPopcnt(val);
                    case 3 -> ctx.bridge.exports().emitEqzI64(val);
                    default -> throw new IllegalArgumentException("Unknown i64 unary op: " + op);
                };
        ctx.valueStack.push(result);
    }

    // --- Comparisons (i32 and i64 share emitIcmp) ---

    static void emitIcmp(EmitContext ctx, int cmpCode) {
        int b = ctx.valueStack.pop();
        int a = ctx.valueStack.pop();
        ctx.valueStack.push(ctx.bridge.exports().emitIcmp(cmpCode, a, b));
    }

    // --- Float Arithmetic ---

    static void emitFloatBinaryOp(EmitContext ctx, int op) {
        int b = ctx.valueStack.pop();
        int a = ctx.valueStack.pop();
        int result =
                switch (op) {
                    case 0 -> ctx.bridge.exports().emitFadd(a, b);
                    case 1 -> ctx.bridge.exports().emitFsub(a, b);
                    case 2 -> ctx.bridge.exports().emitFmul(a, b);
                    case 3 -> ctx.bridge.exports().emitFdiv(a, b);
                    case 4 -> ctx.bridge.exports().emitFmin(a, b);
                    case 5 -> ctx.bridge.exports().emitFmax(a, b);
                    case 6 -> ctx.bridge.exports().emitFcopysign(a, b);
                    default -> throw new IllegalArgumentException("Unknown float binary op: " + op);
                };
        ctx.valueStack.push(result);
    }

    static void emitFloatUnaryOp(EmitContext ctx, int op) {
        int val = ctx.valueStack.pop();
        int result =
                switch (op) {
                    case 0 -> ctx.bridge.exports().emitFabs(val);
                    case 1 -> ctx.bridge.exports().emitFneg(val);
                    case 2 -> ctx.bridge.exports().emitCeil(val);
                    case 3 -> ctx.bridge.exports().emitFloor(val);
                    case 4 -> ctx.bridge.exports().emitTruncFloat(val);
                    case 5 -> ctx.bridge.exports().emitNearest(val);
                    case 6 -> ctx.bridge.exports().emitSqrt(val);
                    default -> throw new IllegalArgumentException("Unknown float unary op: " + op);
                };
        ctx.valueStack.push(result);
    }

    // --- Float Comparisons ---

    static void emitFcmp(EmitContext ctx, int cmpCode) {
        int b = ctx.valueStack.pop();
        int a = ctx.valueStack.pop();
        ctx.valueStack.push(ctx.bridge.exports().emitFcmp(cmpCode, a, b));
    }

    // --- Safe Division (with trap pre-checks) ---

    static void emitSafeDiv(EmitContext ctx, boolean signed, boolean isRem, boolean is64) {
        int divisor = ctx.valueStack.pop();
        int dividend = ctx.valueStack.pop();

        int trapBlockZero = ctx.bridge.exports().createBlock();
        int safeBlock;

        int zero =
                is64
                        ? ctx.bridge.exports().emitIconst64(0, 0)
                        : ctx.bridge.exports().emitIconst32(0);
        int isZero = ctx.bridge.exports().emitIcmp(0, divisor, zero); // EQ

        if (signed && !isRem) {
            int checkOverflow = ctx.bridge.exports().createBlock();
            safeBlock = ctx.bridge.exports().createBlock();
            ctx.bridge.exports().emitBrif(isZero, trapBlockZero, checkOverflow);

            ctx.bridge.exports().switchToBlock(checkOverflow);
            int intMin;
            int negOne;
            if (is64) {
                intMin = ctx.bridge.exports().emitIconst64(0, 0x80000000);
                negOne = ctx.bridge.exports().emitIconst64(-1, -1);
            } else {
                intMin = ctx.bridge.exports().emitIconst32(0x80000000);
                negOne = ctx.bridge.exports().emitIconst32(-1);
            }
            int isMin = ctx.bridge.exports().emitIcmp(0, dividend, intMin);
            int isNeg1 = ctx.bridge.exports().emitIcmp(0, divisor, negOne);
            int both = ctx.bridge.exports().emitBand(isMin, isNeg1);

            int trapBlockOverflow = ctx.bridge.exports().createBlock();
            ctx.bridge.exports().emitBrif(both, trapBlockOverflow, safeBlock);

            fillTrapBlock(ctx, trapBlockOverflow, CtxBuffer.TRAP_INT_OVERFLOW);
        } else {
            safeBlock = ctx.bridge.exports().createBlock();
            ctx.bridge.exports().emitBrif(isZero, trapBlockZero, safeBlock);
        }

        fillTrapBlock(ctx, trapBlockZero, CtxBuffer.TRAP_DIV_BY_ZERO);

        ctx.bridge.exports().switchToBlock(safeBlock);
        int result;
        if (signed) {
            result =
                    isRem
                            ? ctx.bridge.exports().emitSrem(dividend, divisor)
                            : ctx.bridge.exports().emitSdiv(dividend, divisor);
        } else {
            result =
                    isRem
                            ? ctx.bridge.exports().emitUrem(dividend, divisor)
                            : ctx.bridge.exports().emitUdiv(dividend, divisor);
        }
        ctx.valueStack.push(result);
    }

    // --- Safe Float Truncation ---

    static void emitSafeTrunc(EmitContext ctx, int targetType, boolean signed) {
        int fval = ctx.valueStack.pop();

        int satResult =
                signed
                        ? ctx.bridge.exports().emitFcvtToSintSat(targetType, fval)
                        : ctx.bridge.exports().emitFcvtToUintSat(targetType, fval);

        int isNan = ctx.bridge.exports().emitFcmp(1, fval, fval); // NE → true if NaN
        int trapBlock = ctx.bridge.exports().createBlock();
        int okBlock = ctx.bridge.exports().createBlock();
        ctx.bridge.exports().emitBrif(isNan, trapBlock, okBlock);

        fillTrapBlock(ctx, trapBlock, CtxBuffer.TRAP_TRUNC_OVERFLOW);

        ctx.bridge.exports().switchToBlock(okBlock);
        ctx.valueStack.push(satResult);
    }

    // --- Trap helper ---

    static void fillTrapBlock(EmitContext ctx, int trapBlock, int trapCode) {
        ctx.bridge.exports().switchToBlock(trapBlock);
        int ctxVal = ctx.bridge.exports().useVar(ctx.ctxPtrVar);
        int zero = ctx.bridge.exports().emitIconst32(0);
        int code = ctx.bridge.exports().emitIconst32(trapCode);
        ctx.bridge.exports().emitStoreI32(ctxVal, zero, code, CtxBuffer.TRAP_CODE);
        ctx.emitReturnForFuncType();
    }

    // --- Extensions ---

    static void emitI32Extend8S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextend832(ctx.valueStack.pop()));
    }

    static void emitI32Extend16S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextend1632(ctx.valueStack.pop()));
    }

    static void emitI64ExtendI32S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextendI64(ctx.valueStack.pop()));
    }

    static void emitI64ExtendI32U(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitUextendI64(ctx.valueStack.pop()));
    }

    static void emitI64Extend8S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextend864(ctx.valueStack.pop()));
    }

    static void emitI64Extend16S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextend1664(ctx.valueStack.pop()));
    }

    static void emitI64Extend32S(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitSextend3264(ctx.valueStack.pop()));
    }

    static void emitI32WrapI64(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitI32WrapI64(ctx.valueStack.pop()));
    }

    // --- Memory bounds check ---

    private static final int[] LOAD_ACCESS_SIZE = {4, 8, 4, 8, 1, 1, 2, 2, 1, 1, 2, 2, 4, 4};
    private static final int[] STORE_ACCESS_SIZE = {4, 8, 4, 8, 1, 2, 1, 2, 4};

    /**
     * Emit a bounds check: if addr + offset + accessSize > memPages * 65536,
     * trap with OOB. Single compare + branch, predicted not-taken.
     */
    private static void emitBoundsCheck(EmitContext ctx, int addr, int offset, int accessSize) {
        // Compute effective end address as i64 to avoid i32 overflow
        int addr64 = ctx.bridge.exports().emitUextendI64(addr);
        int end =
                ctx.bridge
                        .exports()
                        .emitIadd(
                                addr64, ctx.bridge.exports().emitIconst64(offset + accessSize, 0));

        // Load memory size in bytes: memPages * 65536
        int zero = ctx.bridge.exports().emitIconst32(0);
        int memPages =
                ctx.bridge
                        .exports()
                        .emitLoadI32(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.MEMORY_PAGES);
        int memPages64 = ctx.bridge.exports().emitUextendI64(memPages);
        int memSize =
                ctx.bridge.exports().emitIshl(memPages64, ctx.bridge.exports().emitIconst64(16, 0));

        // if end > memSize → trap
        int oob = ctx.bridge.exports().emitIcmp(5, end, memSize); // GT unsigned
        int trapBlock = ctx.bridge.exports().createBlock();
        int okBlock = ctx.bridge.exports().createBlock();
        ctx.bridge.exports().emitBrif(oob, trapBlock, okBlock);

        fillTrapBlock(ctx, trapBlock, CtxBuffer.TRAP_OOB);

        ctx.bridge.exports().switchToBlock(okBlock);
    }

    // --- Memory loads ---

    static void emitLoad(EmitContext ctx, AnnotatedInstruction ins, int loadType) {
        int addr = ctx.valueStack.pop();
        int offset = (int) ins.operands()[1];
        emitBoundsCheck(ctx, addr, offset, LOAD_ACCESS_SIZE[loadType]);
        int memBase = ctx.bridge.exports().useVar(ctx.memBaseVar);
        int result =
                switch (loadType) {
                    case 0 -> ctx.bridge.exports().emitLoadI32(memBase, addr, offset);
                    case 1 -> ctx.bridge.exports().emitLoadI64(memBase, addr, offset);
                    case 2 -> ctx.bridge.exports().emitLoadF32(memBase, addr, offset);
                    case 3 -> ctx.bridge.exports().emitLoadF64(memBase, addr, offset);
                    case 4 -> ctx.bridge.exports().emitLoad8u(memBase, addr, offset);
                    case 5 -> ctx.bridge.exports().emitLoad8s(memBase, addr, offset);
                    case 6 -> ctx.bridge.exports().emitLoad16u(memBase, addr, offset);
                    case 7 -> ctx.bridge.exports().emitLoad16s(memBase, addr, offset);
                    case 8 -> ctx.bridge.exports().emitLoad8uI64(memBase, addr, offset);
                    case 9 -> ctx.bridge.exports().emitLoad8sI64(memBase, addr, offset);
                    case 10 -> ctx.bridge.exports().emitLoad16uI64(memBase, addr, offset);
                    case 11 -> ctx.bridge.exports().emitLoad16sI64(memBase, addr, offset);
                    case 12 -> ctx.bridge.exports().emitLoad32uI64(memBase, addr, offset);
                    case 13 -> ctx.bridge.exports().emitLoad32sI64(memBase, addr, offset);
                    default -> throw new IllegalArgumentException("Unknown load type: " + loadType);
                };
        ctx.valueStack.push(result);
    }

    // --- Memory stores ---

    static void emitStore(EmitContext ctx, AnnotatedInstruction ins, int storeType) {
        int value = ctx.valueStack.pop();
        int addr = ctx.valueStack.pop();
        int offset = (int) ins.operands()[1];
        emitBoundsCheck(ctx, addr, offset, STORE_ACCESS_SIZE[storeType]);
        int memBase = ctx.bridge.exports().useVar(ctx.memBaseVar);
        switch (storeType) {
            case 0 -> ctx.bridge.exports().emitStoreI32(memBase, addr, value, offset);
            case 1 -> ctx.bridge.exports().emitStoreI64(memBase, addr, value, offset);
            case 2 -> ctx.bridge.exports().emitStoreF32(memBase, addr, value, offset);
            case 3 -> ctx.bridge.exports().emitStoreF64(memBase, addr, value, offset);
            case 4 -> ctx.bridge.exports().emitStore8(memBase, addr, value, offset);
            case 5 -> ctx.bridge.exports().emitStore16(memBase, addr, value, offset);
            case 6 -> ctx.bridge.exports().emitStore8I64(memBase, addr, value, offset);
            case 7 -> ctx.bridge.exports().emitStore16I64(memBase, addr, value, offset);
            case 8 -> ctx.bridge.exports().emitStore32I64(memBase, addr, value, offset);
            default -> throw new IllegalArgumentException("Unknown store type: " + storeType);
        }
    }

    // --- Locals ---

    static void emitLocalGet(EmitContext ctx, AnnotatedInstruction ins) {
        ctx.valueStack.push(ctx.bridge.exports().useVar(ctx.localVars[(int) ins.operands()[0]]));
    }

    static void emitLocalSet(EmitContext ctx, AnnotatedInstruction ins) {
        int val = ctx.valueStack.pop();
        ctx.bridge.exports().defVar(ctx.localVars[(int) ins.operands()[0]], val);
    }

    static void emitLocalTee(EmitContext ctx, AnnotatedInstruction ins) {
        int val = ctx.valueStack.peek();
        ctx.bridge.exports().defVar(ctx.localVars[(int) ins.operands()[0]], val);
    }

    // --- Select ---

    static void emitSelect(EmitContext ctx) {
        int cond = ctx.valueStack.pop();
        int val2 = ctx.valueStack.pop();
        int val1 = ctx.valueStack.pop();
        ctx.valueStack.push(ctx.bridge.exports().emitSelect(cond, val1, val2));
    }

    // --- Globals ---

    static void emitGlobalGet(EmitContext ctx, AnnotatedInstruction ins) {
        int globalIdx = (int) ins.operands()[0];
        int zero = ctx.bridge.exports().emitIconst32(0);
        int globalsPtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.GLOBALS_PTR);
        int offsetVal = ctx.bridge.exports().emitIconst32(globalIdx * 8);
        int rawVal = ctx.bridge.exports().emitLoadI64(globalsPtr, offsetVal, 0);
        ValType globalType = ctx.resolveGlobalType(globalIdx);
        ctx.valueStack.push(ctx.narrowFromI64ForType(rawVal, globalType));
    }

    static void emitGlobalSet(EmitContext ctx, AnnotatedInstruction ins) {
        int globalIdx = (int) ins.operands()[0];
        int value = ctx.valueStack.pop();
        ValType globalType = ctx.resolveGlobalType(globalIdx);
        int widened = ctx.widenToI64ForType(value, globalType);
        int zero = ctx.bridge.exports().emitIconst32(0);
        int globalsPtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.GLOBALS_PTR);
        int offsetVal = ctx.bridge.exports().emitIconst32(globalIdx * 8);
        ctx.bridge.exports().emitStoreI64(globalsPtr, offsetVal, widened, 0);
    }

    // --- Memory operations ---

    static void emitMemorySize(EmitContext ctx) {
        int zero = ctx.bridge.exports().emitIconst32(0);
        int pages =
                ctx.bridge
                        .exports()
                        .emitLoadI32(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.MEMORY_PAGES);
        ctx.valueStack.push(pages);
    }

    static void emitMemoryGrow(EmitContext ctx) {
        int delta = ctx.valueStack.pop();
        int zero = ctx.bridge.exports().emitIconst32(0);
        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        delta,
                        CtxBuffer.MEM_GROW_DELTA);
        int memGrowPtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.MEM_GROW_PTR);
        int growSig = ctx.getOrCreateTrampolineSigRef();
        ctx.bridge.exports().pushCallArg(ctx.bridge.exports().useVar(ctx.ctxPtrVar));
        int rawResult = ctx.bridge.exports().emitCallIndirect(growSig, memGrowPtr);
        int result = ctx.bridge.exports().emitIreduceI32(rawResult);
        ctx.valueStack.push(result);
        int newMemBase =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.MEM_BASE_ADDR);
        ctx.bridge.exports().defVar(ctx.memBaseVar, newMemBase);
    }

    // --- Unreachable ---

    static void emitUnreachable(EmitContext ctx) {
        int ctxVal = ctx.bridge.exports().useVar(ctx.ctxPtrVar);
        int zero = ctx.bridge.exports().emitIconst32(0);
        int code = ctx.bridge.exports().emitIconst32(CtxBuffer.TRAP_UNREACHABLE);
        ctx.bridge.exports().emitStoreI32(ctxVal, zero, code, CtxBuffer.TRAP_CODE);
        ctx.emitReturnForFuncType();
    }

    // --- Conversions ---

    static void emitTruncSat(EmitContext ctx, int targetType, boolean signed) {
        int val = ctx.valueStack.pop();
        ctx.valueStack.push(
                signed
                        ? ctx.bridge.exports().emitFcvtToSintSat(targetType, val)
                        : ctx.bridge.exports().emitFcvtToUintSat(targetType, val));
    }

    static void emitConvertFloat(EmitContext ctx, int targetType, boolean signed) {
        int val = ctx.valueStack.pop();
        ctx.valueStack.push(
                signed
                        ? ctx.bridge.exports().emitFcvtFromSint(targetType, val)
                        : ctx.bridge.exports().emitFcvtFromUint(targetType, val));
    }

    static void emitFpromote(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitFpromote(ctx.valueStack.pop()));
    }

    static void emitFdemote(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitFdemote(ctx.valueStack.pop()));
    }

    static void emitBitcastI32ToF32(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitBitcastI32ToF32(ctx.valueStack.pop()));
    }

    static void emitBitcastF32ToI32(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitBitcastF32ToI32(ctx.valueStack.pop()));
    }

    static void emitBitcastI64ToF64(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitBitcastI64ToF64(ctx.valueStack.pop()));
    }

    static void emitBitcastF64ToI64(EmitContext ctx) {
        ctx.valueStack.push(ctx.bridge.exports().emitBitcastF64ToI64(ctx.valueStack.pop()));
    }

    // --- Calls ---

    static void emitCall(EmitContext ctx, AnnotatedInstruction ins) {
        int targetFuncId = (int) ins.operands()[0];
        FunctionType targetType = ctx.resolveCallTargetType(targetFuncId);

        int sigRef = ctx.getOrCreateSigRef(targetType);

        int argCount = targetType.params().size();
        int[] argVals = new int[argCount];
        for (int i = argCount - 1; i >= 0; i--) {
            argVals[i] = ctx.valueStack.pop();
        }

        int zero = ctx.bridge.exports().emitIconst32(0);
        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        ctx.bridge.exports().emitIconst32(argCount),
                        CtxBuffer.ARG_COUNT);
        int argsPtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.ARGS_PTR);
        for (int i = 0; i < argCount; i++) {
            int widened = ctx.widenToI64(argVals[i], targetType.params().get(i));
            ctx.bridge.exports().emitStoreI64(argsPtr, zero, widened, CtxBuffer.argOffset(i));
        }

        int funcTablePtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.FUNC_TABLE_PTR);
        int funcIdOffset = ctx.bridge.exports().emitIconst32(targetFuncId * 8);
        int funcPtr = ctx.bridge.exports().emitLoadI64(funcTablePtr, funcIdOffset, 0);

        ctx.bridge.exports().pushCallArg(ctx.bridge.exports().useVar(ctx.memBaseVar));
        ctx.bridge.exports().pushCallArg(ctx.bridge.exports().useVar(ctx.ctxPtrVar));
        for (int i = 0; i < argCount; i++) {
            ctx.bridge.exports().pushCallArg(argVals[i]);
        }

        // For multi-return callees, the SigRef must match the actual native
        // signature (single i64 return), not the Wasm signature.
        boolean calleeMultiReturn = targetType.returns().size() > 1;
        int actualSigRef =
                calleeMultiReturn ? ctx.getOrCreateMultiReturnSigRef(targetType) : sigRef;

        int rawResult = ctx.bridge.exports().emitCallIndirect(actualSigRef, funcPtr);

        if (calleeMultiReturn) {
            // Read return values from argsBuffer
            int zero2 = ctx.bridge.exports().emitIconst32(0);
            int argsPtr2 =
                    ctx.bridge
                            .exports()
                            .emitLoadI64(
                                    ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                    zero2,
                                    CtxBuffer.ARGS_PTR);
            for (int i = 0; i < targetType.returns().size(); i++) {
                int raw = ctx.bridge.exports().emitLoadI64(argsPtr2, zero2, CtxBuffer.argOffset(i));
                ctx.valueStack.push(ctx.narrowFromI64ForType(raw, targetType.returns().get(i)));
            }
        } else if (!targetType.returns().isEmpty()) {
            ctx.valueStack.push(rawResult);
        }
    }

    static void emitCallIndirect(EmitContext ctx, AnnotatedInstruction ins) {
        int typeId = (int) ins.operands()[0];
        int tableIdx = (int) ins.operands()[1];
        FunctionType targetType = (FunctionType) ctx.module.typeSection().getType(typeId);

        int tableElemIdx = ctx.valueStack.pop();

        int argCount = targetType.params().size();
        int[] argVals = new int[argCount];
        for (int i = argCount - 1; i >= 0; i--) {
            argVals[i] = ctx.valueStack.pop();
        }

        int zero = ctx.bridge.exports().emitIconst32(0);

        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        ctx.bridge.exports().emitIconst32(typeId),
                        CtxBuffer.TYPE_ID);
        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        ctx.bridge.exports().emitIconst32(tableIdx),
                        CtxBuffer.TABLE_IDX);
        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        tableElemIdx,
                        CtxBuffer.ELEM_IDX);
        ctx.bridge
                .exports()
                .emitStoreI32(
                        ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                        zero,
                        ctx.bridge.exports().emitIconst32(argCount),
                        CtxBuffer.ARG_COUNT);

        int argsPtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.ARGS_PTR);
        for (int i = 0; i < argCount; i++) {
            int widened = ctx.widenToI64(argVals[i], targetType.params().get(i));
            ctx.bridge.exports().emitStoreI64(argsPtr, zero, widened, CtxBuffer.argOffset(i));
        }

        int trampolinePtr =
                ctx.bridge
                        .exports()
                        .emitLoadI64(
                                ctx.bridge.exports().useVar(ctx.ctxPtrVar),
                                zero,
                                CtxBuffer.TRAMPOLINE_PTR);
        int trampolineSig = ctx.getOrCreateTrampolineSigRef();
        ctx.bridge.exports().pushCallArg(ctx.bridge.exports().useVar(ctx.ctxPtrVar));
        int rawResult = ctx.bridge.exports().emitCallIndirect(trampolineSig, trampolinePtr);

        if (!targetType.returns().isEmpty()) {
            int narrowed = ctx.narrowFromI64(rawResult, targetType.returns().get(0));
            ctx.valueStack.push(narrowed);
        }
    }
}
