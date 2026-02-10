/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ChicoryInterruptedException;
import com.dylibso.chicory.runtime.CtrlFrame;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.MStack;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.runtime.OpcodeImpl;
import com.dylibso.chicory.runtime.StackFrame;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.TagInstance;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.runtime.WasmException;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.CatchOpCode;
import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class InterpreterMachine
implements Machine {
    private final MStack stack;
    protected final Deque<StackFrame> callStack;
    private final Instance instance;

    public InterpreterMachine(Instance instance) {
        this.instance = instance;
        this.stack = new MStack();
        this.callStack = new ArrayDeque<StackFrame>();
    }

    protected void evalDefault(MStack stack, Instance instance, Deque<StackFrame> callStack, Instruction instruction, Operands operands) throws ChicoryException {
        throw new RuntimeException("Machine doesn't recognize Instruction " + String.valueOf(instruction));
    }

    @Override
    public long[] call(int funcId, long[] args) throws ChicoryException {
        return this.call(this.stack, this.instance, this.callStack, funcId, args, null, true);
    }

    protected long[] call(MStack stack, Instance instance, Deque<StackFrame> callStack, int funcId, long[] args, FunctionType callType, boolean popResults) throws ChicoryException {
        StackFrame stackFrame;
        FunctionBody func;
        InterpreterMachine.checkInterruption();
        int typeId = instance.functionType(funcId);
        FunctionType type = instance.type(typeId);
        if (callType != null) {
            InterpreterMachine.verifyIndirectCall(type, callType);
        }
        if ((func = instance.function(funcId)) != null) {
            stackFrame = new StackFrame(instance, funcId, args, type.params(), func.localTypes(), func.instructions());
            stackFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
            callStack.push(stackFrame);
            try {
                this.eval(stack, instance, callStack);
            }
            catch (StackOverflowError e) {
                throw new ChicoryException("call stack exhausted", e);
            }
        }
        stackFrame = new StackFrame(instance, funcId, args);
        stackFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
        callStack.push(stackFrame);
        ImportFunction imprt = instance.imports().function(funcId);
        try {
            long[] results = imprt.handle().apply(instance, args);
            if (results != null) {
                for (long result : results) {
                    stack.push(result);
                }
            }
        }
        catch (WasmException e) {
            InterpreterMachine.THROW_REF(instance, instance.registerException(e), stack, stackFrame, callStack);
        }
        if (!callStack.isEmpty()) {
            callStack.pop();
        }
        if (!popResults) {
            return null;
        }
        if (type.returns().isEmpty()) {
            return null;
        }
        if (stack.size() == 0) {
            return null;
        }
        int totalResults = ValType.sizeOf(type.returns());
        long[] results = new long[totalResults];
        for (int i = totalResults - 1; i >= 0; --i) {
            results[i] = stack.pop();
        }
        return results;
    }

    protected Instance instance() {
        return this.instance;
    }

    protected MStack stack() {
        return this.stack;
    }

    protected void eval(MStack stack, Instance instance, Deque<StackFrame> callStack) throws ChicoryException {
        StackFrame frame = callStack.peek();
        boolean shouldReturn = false;
        block261: while (!frame.terminated()) {
            if (shouldReturn) {
                return;
            }
            AnnotatedInstruction instruction = frame.loadCurrentInstruction();
            OpCode opcode = instruction.opcode();
            Operands operands = instruction::operand;
            instance.onExecution(instruction, stack);
            switch (opcode) {
                case UNREACHABLE: {
                    throw new TrapException("Trapped on unreachable instruction");
                }
                case NOP: {
                    continue block261;
                }
                case LOOP: 
                case BLOCK: {
                    InterpreterMachine.BLOCK(frame, stack, instance, instruction);
                    continue block261;
                }
                case TRY_TABLE: {
                    InterpreterMachine.TRY_TABLE(frame, stack, instance, instruction, frame.currentPc());
                    continue block261;
                }
                case IF: {
                    InterpreterMachine.IF(frame, stack, instance, instruction);
                    continue block261;
                }
                case ELSE: {
                    frame.jumpTo(instruction.labelTrue());
                    continue block261;
                }
                case BR: {
                    InterpreterMachine.BR(frame, stack, instruction);
                    continue block261;
                }
                case BR_IF: {
                    InterpreterMachine.BR_IF(frame, stack, instruction);
                    continue block261;
                }
                case BR_TABLE: {
                    InterpreterMachine.BR_TABLE(frame, stack, instruction);
                    continue block261;
                }
                case BR_ON_NULL: {
                    InterpreterMachine.BR_ON_NULL(frame, stack, instruction);
                    continue block261;
                }
                case BR_ON_NON_NULL: {
                    InterpreterMachine.BR_ON_NON_NULL(frame, stack, instruction);
                    continue block261;
                }
                case END: {
                    CtrlFrame ctrlFrame = frame.popCtrl();
                    StackFrame.doControlTransfer(ctrlFrame, stack);
                    if (!frame.isLastBlock()) continue block261;
                    break block261;
                }
                case RETURN: {
                    CtrlFrame ctrlFrame = frame.popCtrlTillCall();
                    StackFrame.doControlTransfer(ctrlFrame, stack);
                    callStack.clear();
                    shouldReturn = true;
                    continue block261;
                }
                case RETURN_CALL: {
                    frame = InterpreterMachine.RETURN_CALL(stack, instance, callStack, operands, frame);
                    continue block261;
                }
                case RETURN_CALL_INDIRECT: {
                    frame = InterpreterMachine.RETURN_CALL_INDIRECT(stack, instance, callStack, operands, frame);
                    continue block261;
                }
                case RETURN_CALL_REF: {
                    frame = InterpreterMachine.RETURN_CALL_REF(stack, instance, callStack, frame);
                    continue block261;
                }
                case THROW: {
                    int tagNumber = (int)operands.get(0);
                    TagInstance tag = instance.tag(tagNumber);
                    FunctionType type = instance.type(tag.tagType().typeIdx());
                    long[] args = InterpreterMachine.extractArgsForParams(stack, type.params());
                    WasmException exception = new WasmException(instance, tagNumber, args);
                    int exceptionIdx = instance.registerException(exception);
                    frame = InterpreterMachine.THROW_REF(instance, exceptionIdx, stack, frame, callStack);
                    continue block261;
                }
                case THROW_REF: {
                    int exceptionIdx = (int)stack.pop();
                    frame = InterpreterMachine.THROW_REF(instance, exceptionIdx, stack, frame, callStack);
                    continue block261;
                }
                case CALL_INDIRECT: {
                    this.CALL_INDIRECT(stack, instance, callStack, operands);
                    continue block261;
                }
                case DROP: {
                    InterpreterMachine.DROP(stack, operands);
                    continue block261;
                }
                case SELECT: {
                    InterpreterMachine.SELECT(stack, operands);
                    continue block261;
                }
                case SELECT_T: {
                    InterpreterMachine.SELECT_T(stack, operands);
                    continue block261;
                }
                case LOCAL_GET: {
                    InterpreterMachine.LOCAL_GET(stack, operands, frame);
                    continue block261;
                }
                case LOCAL_SET: {
                    InterpreterMachine.LOCAL_SET(stack, operands, frame);
                    continue block261;
                }
                case LOCAL_TEE: {
                    InterpreterMachine.LOCAL_TEE(stack, operands, frame);
                    continue block261;
                }
                case GLOBAL_GET: {
                    InterpreterMachine.GLOBAL_GET(stack, instance, operands);
                    continue block261;
                }
                case GLOBAL_SET: {
                    InterpreterMachine.GLOBAL_SET(stack, instance, operands);
                    continue block261;
                }
                case TABLE_GET: {
                    InterpreterMachine.TABLE_GET(stack, instance, operands);
                    continue block261;
                }
                case TABLE_SET: {
                    InterpreterMachine.TABLE_SET(stack, instance, operands);
                    continue block261;
                }
                case I32_LOAD: {
                    InterpreterMachine.I32_LOAD(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD: {
                    InterpreterMachine.I64_LOAD(stack, instance, operands);
                    continue block261;
                }
                case F32_LOAD: {
                    InterpreterMachine.F32_LOAD(stack, instance, operands);
                    continue block261;
                }
                case F64_LOAD: {
                    InterpreterMachine.F64_LOAD(stack, instance, operands);
                    continue block261;
                }
                case I32_LOAD8_S: {
                    InterpreterMachine.I32_LOAD8_S(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD8_S: {
                    InterpreterMachine.I64_LOAD8_S(stack, instance, operands);
                    continue block261;
                }
                case I32_LOAD8_U: {
                    InterpreterMachine.I32_LOAD8_U(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD8_U: {
                    InterpreterMachine.I64_LOAD8_U(stack, instance, operands);
                    continue block261;
                }
                case I32_LOAD16_S: {
                    InterpreterMachine.I32_LOAD16_S(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD16_S: {
                    InterpreterMachine.I64_LOAD16_S(stack, instance, operands);
                    continue block261;
                }
                case I32_LOAD16_U: {
                    InterpreterMachine.I32_LOAD16_U(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD16_U: {
                    InterpreterMachine.I64_LOAD16_U(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD32_S: {
                    InterpreterMachine.I64_LOAD32_S(stack, instance, operands);
                    continue block261;
                }
                case I64_LOAD32_U: {
                    InterpreterMachine.I64_LOAD32_U(stack, instance, operands);
                    continue block261;
                }
                case I32_STORE: {
                    InterpreterMachine.I32_STORE(stack, instance, operands);
                    continue block261;
                }
                case I32_STORE16: 
                case I64_STORE16: {
                    InterpreterMachine.I64_STORE16(stack, instance, operands);
                    continue block261;
                }
                case I64_STORE: {
                    InterpreterMachine.I64_STORE(stack, instance, operands);
                    continue block261;
                }
                case F32_STORE: {
                    InterpreterMachine.F32_STORE(stack, instance, operands);
                    continue block261;
                }
                case F64_STORE: {
                    InterpreterMachine.F64_STORE(stack, instance, operands);
                    continue block261;
                }
                case MEMORY_GROW: {
                    InterpreterMachine.MEMORY_GROW(stack, instance);
                    continue block261;
                }
                case MEMORY_FILL: {
                    InterpreterMachine.MEMORY_FILL(stack, instance);
                    continue block261;
                }
                case I32_STORE8: 
                case I64_STORE8: {
                    InterpreterMachine.I64_STORE8(stack, instance, operands);
                    continue block261;
                }
                case I64_STORE32: {
                    InterpreterMachine.I64_STORE32(stack, instance, operands);
                    continue block261;
                }
                case MEMORY_SIZE: {
                    InterpreterMachine.MEMORY_SIZE(stack, instance);
                    continue block261;
                }
                case I32_CONST: {
                    stack.push(operands.get(0));
                    continue block261;
                }
                case I64_CONST: {
                    stack.push(operands.get(0));
                    continue block261;
                }
                case F32_CONST: {
                    stack.push(operands.get(0));
                    continue block261;
                }
                case F64_CONST: {
                    stack.push(operands.get(0));
                    continue block261;
                }
                case I32_EQ: {
                    InterpreterMachine.I32_EQ(stack);
                    continue block261;
                }
                case I64_EQ: {
                    InterpreterMachine.I64_EQ(stack);
                    continue block261;
                }
                case I32_NE: {
                    InterpreterMachine.I32_NE(stack);
                    continue block261;
                }
                case I64_NE: {
                    InterpreterMachine.I64_NE(stack);
                    continue block261;
                }
                case I32_EQZ: {
                    InterpreterMachine.I32_EQZ(stack);
                    continue block261;
                }
                case I64_EQZ: {
                    InterpreterMachine.I64_EQZ(stack);
                    continue block261;
                }
                case I32_LT_S: {
                    InterpreterMachine.I32_LT_S(stack);
                    continue block261;
                }
                case I32_LT_U: {
                    InterpreterMachine.I32_LT_U(stack);
                    continue block261;
                }
                case I64_LT_S: {
                    InterpreterMachine.I64_LT_S(stack);
                    continue block261;
                }
                case I64_LT_U: {
                    InterpreterMachine.I64_LT_U(stack);
                    continue block261;
                }
                case I32_GT_S: {
                    InterpreterMachine.I32_GT_S(stack);
                    continue block261;
                }
                case I32_GT_U: {
                    InterpreterMachine.I32_GT_U(stack);
                    continue block261;
                }
                case I64_GT_S: {
                    InterpreterMachine.I64_GT_S(stack);
                    continue block261;
                }
                case I64_GT_U: {
                    InterpreterMachine.I64_GT_U(stack);
                    continue block261;
                }
                case I32_GE_S: {
                    InterpreterMachine.I32_GE_S(stack);
                    continue block261;
                }
                case I32_GE_U: {
                    InterpreterMachine.I32_GE_U(stack);
                    continue block261;
                }
                case I64_GE_U: {
                    InterpreterMachine.I64_GE_U(stack);
                    continue block261;
                }
                case I64_GE_S: {
                    InterpreterMachine.I64_GE_S(stack);
                    continue block261;
                }
                case I32_LE_S: {
                    InterpreterMachine.I32_LE_S(stack);
                    continue block261;
                }
                case I32_LE_U: {
                    InterpreterMachine.I32_LE_U(stack);
                    continue block261;
                }
                case I64_LE_S: {
                    InterpreterMachine.I64_LE_S(stack);
                    continue block261;
                }
                case I64_LE_U: {
                    InterpreterMachine.I64_LE_U(stack);
                    continue block261;
                }
                case F32_EQ: {
                    InterpreterMachine.F32_EQ(stack);
                    continue block261;
                }
                case F64_EQ: {
                    InterpreterMachine.F64_EQ(stack);
                    continue block261;
                }
                case I32_CLZ: {
                    InterpreterMachine.I32_CLZ(stack);
                    continue block261;
                }
                case I32_CTZ: {
                    InterpreterMachine.I32_CTZ(stack);
                    continue block261;
                }
                case I32_POPCNT: {
                    InterpreterMachine.I32_POPCNT(stack);
                    continue block261;
                }
                case I32_ADD: {
                    InterpreterMachine.I32_ADD(stack);
                    continue block261;
                }
                case I64_ADD: {
                    InterpreterMachine.I64_ADD(stack);
                    continue block261;
                }
                case I32_SUB: {
                    InterpreterMachine.I32_SUB(stack);
                    continue block261;
                }
                case I64_SUB: {
                    InterpreterMachine.I64_SUB(stack);
                    continue block261;
                }
                case I32_MUL: {
                    InterpreterMachine.I32_MUL(stack);
                    continue block261;
                }
                case I64_MUL: {
                    InterpreterMachine.I64_MUL(stack);
                    continue block261;
                }
                case I32_DIV_S: {
                    InterpreterMachine.I32_DIV_S(stack);
                    continue block261;
                }
                case I32_DIV_U: {
                    InterpreterMachine.I32_DIV_U(stack);
                    continue block261;
                }
                case I64_DIV_S: {
                    InterpreterMachine.I64_DIV_S(stack);
                    continue block261;
                }
                case I64_DIV_U: {
                    InterpreterMachine.I64_DIV_U(stack);
                    continue block261;
                }
                case I32_REM_S: {
                    InterpreterMachine.I32_REM_S(stack);
                    continue block261;
                }
                case I32_REM_U: {
                    InterpreterMachine.I32_REM_U(stack);
                    continue block261;
                }
                case I64_AND: {
                    InterpreterMachine.I64_AND(stack);
                    continue block261;
                }
                case I64_OR: {
                    InterpreterMachine.I64_OR(stack);
                    continue block261;
                }
                case I64_XOR: {
                    InterpreterMachine.I64_XOR(stack);
                    continue block261;
                }
                case I64_SHL: {
                    InterpreterMachine.I64_SHL(stack);
                    continue block261;
                }
                case I64_SHR_S: {
                    InterpreterMachine.I64_SHR_S(stack);
                    continue block261;
                }
                case I64_SHR_U: {
                    InterpreterMachine.I64_SHR_U(stack);
                    continue block261;
                }
                case I64_REM_S: {
                    InterpreterMachine.I64_REM_S(stack);
                    continue block261;
                }
                case I64_REM_U: {
                    InterpreterMachine.I64_REM_U(stack);
                    continue block261;
                }
                case I64_ROTL: {
                    InterpreterMachine.I64_ROTL(stack);
                    continue block261;
                }
                case I64_ROTR: {
                    InterpreterMachine.I64_ROTR(stack);
                    continue block261;
                }
                case I64_CLZ: {
                    InterpreterMachine.I64_CLZ(stack);
                    continue block261;
                }
                case I64_CTZ: {
                    InterpreterMachine.I64_CTZ(stack);
                    continue block261;
                }
                case I64_POPCNT: {
                    InterpreterMachine.I64_POPCNT(stack);
                    continue block261;
                }
                case F32_NEG: {
                    InterpreterMachine.F32_NEG(stack);
                    continue block261;
                }
                case F64_NEG: {
                    InterpreterMachine.F64_NEG(stack);
                    continue block261;
                }
                case CALL: {
                    this.CALL(operands);
                    continue block261;
                }
                case CALL_REF: {
                    this.CALL_REF();
                    continue block261;
                }
                case I32_AND: {
                    InterpreterMachine.I32_AND(stack);
                    continue block261;
                }
                case I32_OR: {
                    InterpreterMachine.I32_OR(stack);
                    continue block261;
                }
                case I32_XOR: {
                    InterpreterMachine.I32_XOR(stack);
                    continue block261;
                }
                case I32_SHL: {
                    InterpreterMachine.I32_SHL(stack);
                    continue block261;
                }
                case I32_SHR_S: {
                    InterpreterMachine.I32_SHR_S(stack);
                    continue block261;
                }
                case I32_SHR_U: {
                    InterpreterMachine.I32_SHR_U(stack);
                    continue block261;
                }
                case I32_ROTL: {
                    InterpreterMachine.I32_ROTL(stack);
                    continue block261;
                }
                case I32_ROTR: {
                    InterpreterMachine.I32_ROTR(stack);
                    continue block261;
                }
                case F32_ADD: {
                    InterpreterMachine.F32_ADD(stack);
                    continue block261;
                }
                case F64_ADD: {
                    InterpreterMachine.F64_ADD(stack);
                    continue block261;
                }
                case F32_SUB: {
                    InterpreterMachine.F32_SUB(stack);
                    continue block261;
                }
                case F64_SUB: {
                    InterpreterMachine.F64_SUB(stack);
                    continue block261;
                }
                case F32_MUL: {
                    InterpreterMachine.F32_MUL(stack);
                    continue block261;
                }
                case F64_MUL: {
                    InterpreterMachine.F64_MUL(stack);
                    continue block261;
                }
                case F32_DIV: {
                    InterpreterMachine.F32_DIV(stack);
                    continue block261;
                }
                case F64_DIV: {
                    InterpreterMachine.F64_DIV(stack);
                    continue block261;
                }
                case F32_MIN: {
                    InterpreterMachine.F32_MIN(stack);
                    continue block261;
                }
                case F64_MIN: {
                    InterpreterMachine.F64_MIN(stack);
                    continue block261;
                }
                case F32_MAX: {
                    InterpreterMachine.F32_MAX(stack);
                    continue block261;
                }
                case F64_MAX: {
                    InterpreterMachine.F64_MAX(stack);
                    continue block261;
                }
                case F32_SQRT: {
                    InterpreterMachine.F32_SQRT(stack);
                    continue block261;
                }
                case F64_SQRT: {
                    InterpreterMachine.F64_SQRT(stack);
                    continue block261;
                }
                case F32_FLOOR: {
                    InterpreterMachine.F32_FLOOR(stack);
                    continue block261;
                }
                case F64_FLOOR: {
                    InterpreterMachine.F64_FLOOR(stack);
                    continue block261;
                }
                case F32_CEIL: {
                    InterpreterMachine.F32_CEIL(stack);
                    continue block261;
                }
                case F64_CEIL: {
                    InterpreterMachine.F64_CEIL(stack);
                    continue block261;
                }
                case F32_TRUNC: {
                    InterpreterMachine.F32_TRUNC(stack);
                    continue block261;
                }
                case F64_TRUNC: {
                    InterpreterMachine.F64_TRUNC(stack);
                    continue block261;
                }
                case F32_NEAREST: {
                    InterpreterMachine.F32_NEAREST(stack);
                    continue block261;
                }
                case F64_NEAREST: {
                    InterpreterMachine.F64_NEAREST(stack);
                    continue block261;
                }
                case I32_EXTEND_8_S: {
                    InterpreterMachine.I32_EXTEND_8_S(stack);
                    continue block261;
                }
                case I32_EXTEND_16_S: {
                    InterpreterMachine.I32_EXTEND_16_S(stack);
                    continue block261;
                }
                case I64_EXTEND_8_S: {
                    InterpreterMachine.I64_EXTEND_8_S(stack);
                    continue block261;
                }
                case I64_EXTEND_16_S: {
                    InterpreterMachine.I64_EXTEND_16_S(stack);
                    continue block261;
                }
                case I64_EXTEND_32_S: {
                    InterpreterMachine.I64_EXTEND_32_S(stack);
                    continue block261;
                }
                case F64_CONVERT_I64_U: {
                    InterpreterMachine.F64_CONVERT_I64_U(stack);
                    continue block261;
                }
                case F64_CONVERT_I32_U: {
                    InterpreterMachine.F64_CONVERT_I32_U(stack);
                    continue block261;
                }
                case F64_CONVERT_I32_S: {
                    InterpreterMachine.F64_CONVERT_I32_S(stack);
                    continue block261;
                }
                case F64_PROMOTE_F32: {
                    InterpreterMachine.F64_PROMOTE_F32(stack);
                    continue block261;
                }
                case F64_REINTERPRET_I64: {
                    InterpreterMachine.F64_REINTERPRET_I64(stack);
                    continue block261;
                }
                case I64_TRUNC_F64_S: {
                    InterpreterMachine.I64_TRUNC_F64_S(stack);
                    continue block261;
                }
                case I32_WRAP_I64: {
                    InterpreterMachine.I32_WRAP_I64(stack);
                    continue block261;
                }
                case I64_EXTEND_I32_S: {
                    InterpreterMachine.I64_EXTEND_I32_S(stack);
                    continue block261;
                }
                case I64_EXTEND_I32_U: {
                    InterpreterMachine.I64_EXTEND_I32_U(stack);
                    continue block261;
                }
                case I32_REINTERPRET_F32: {
                    InterpreterMachine.I32_REINTERPRET_F32(stack);
                    continue block261;
                }
                case I64_REINTERPRET_F64: {
                    InterpreterMachine.I64_REINTERPRET_F64(stack);
                    continue block261;
                }
                case F32_REINTERPRET_I32: {
                    InterpreterMachine.F32_REINTERPRET_I32(stack);
                    continue block261;
                }
                case F32_COPYSIGN: {
                    InterpreterMachine.F32_COPYSIGN(stack);
                    continue block261;
                }
                case F32_ABS: {
                    InterpreterMachine.F32_ABS(stack);
                    continue block261;
                }
                case F64_COPYSIGN: {
                    InterpreterMachine.F64_COPYSIGN(stack);
                    continue block261;
                }
                case F64_ABS: {
                    InterpreterMachine.F64_ABS(stack);
                    continue block261;
                }
                case F32_NE: {
                    InterpreterMachine.F32_NE(stack);
                    continue block261;
                }
                case F64_NE: {
                    InterpreterMachine.F64_NE(stack);
                    continue block261;
                }
                case F32_LT: {
                    InterpreterMachine.F32_LT(stack);
                    continue block261;
                }
                case F64_LT: {
                    InterpreterMachine.F64_LT(stack);
                    continue block261;
                }
                case F32_LE: {
                    InterpreterMachine.F32_LE(stack);
                    continue block261;
                }
                case F64_LE: {
                    InterpreterMachine.F64_LE(stack);
                    continue block261;
                }
                case F32_GE: {
                    InterpreterMachine.F32_GE(stack);
                    continue block261;
                }
                case F64_GE: {
                    InterpreterMachine.F64_GE(stack);
                    continue block261;
                }
                case F32_GT: {
                    InterpreterMachine.F32_GT(stack);
                    continue block261;
                }
                case F64_GT: {
                    InterpreterMachine.F64_GT(stack);
                    continue block261;
                }
                case F32_DEMOTE_F64: {
                    InterpreterMachine.F32_DEMOTE_F64(stack);
                    continue block261;
                }
                case F32_CONVERT_I32_S: {
                    InterpreterMachine.F32_CONVERT_I32_S(stack);
                    continue block261;
                }
                case I32_TRUNC_F32_S: {
                    InterpreterMachine.I32_TRUNC_F32_S(stack);
                    continue block261;
                }
                case I32_TRUNC_SAT_F32_S: {
                    InterpreterMachine.I32_TRUNC_SAT_F32_S(stack);
                    continue block261;
                }
                case I32_TRUNC_SAT_F32_U: {
                    InterpreterMachine.I32_TRUNC_SAT_F32_U(stack);
                    continue block261;
                }
                case I32_TRUNC_SAT_F64_S: {
                    InterpreterMachine.I32_TRUNC_SAT_F64_S(stack);
                    continue block261;
                }
                case I32_TRUNC_SAT_F64_U: {
                    InterpreterMachine.I32_TRUNC_SAT_F64_U(stack);
                    continue block261;
                }
                case F32_CONVERT_I32_U: {
                    InterpreterMachine.F32_CONVERT_I32_U(stack);
                    continue block261;
                }
                case I32_TRUNC_F32_U: {
                    InterpreterMachine.I32_TRUNC_F32_U(stack);
                    continue block261;
                }
                case F32_CONVERT_I64_S: {
                    InterpreterMachine.F32_CONVERT_I64_S(stack);
                    continue block261;
                }
                case F32_CONVERT_I64_U: {
                    InterpreterMachine.F32_CONVERT_I64_U(stack);
                    continue block261;
                }
                case F64_CONVERT_I64_S: {
                    InterpreterMachine.F64_CONVERT_I64_S(stack);
                    continue block261;
                }
                case I64_TRUNC_F32_U: {
                    InterpreterMachine.I64_TRUNC_F32_U(stack);
                    continue block261;
                }
                case I64_TRUNC_F64_U: {
                    InterpreterMachine.I64_TRUNC_F64_U(stack);
                    continue block261;
                }
                case I64_TRUNC_SAT_F32_S: {
                    InterpreterMachine.I64_TRUNC_SAT_F32_S(stack);
                    continue block261;
                }
                case I64_TRUNC_SAT_F32_U: {
                    InterpreterMachine.I64_TRUNC_SAT_F32_U(stack);
                    continue block261;
                }
                case I64_TRUNC_SAT_F64_S: {
                    InterpreterMachine.I64_TRUNC_SAT_F64_S(stack);
                    continue block261;
                }
                case I64_TRUNC_SAT_F64_U: {
                    InterpreterMachine.I64_TRUNC_SAT_F64_U(stack);
                    continue block261;
                }
                case I32_TRUNC_F64_S: {
                    InterpreterMachine.I32_TRUNC_F64_S(stack);
                    continue block261;
                }
                case I32_TRUNC_F64_U: {
                    InterpreterMachine.I32_TRUNC_F64_U(stack);
                    continue block261;
                }
                case I64_TRUNC_F32_S: {
                    InterpreterMachine.I64_TRUNC_F32_S(stack);
                    continue block261;
                }
                case MEMORY_INIT: {
                    InterpreterMachine.MEMORY_INIT(stack, instance, operands);
                    continue block261;
                }
                case TABLE_INIT: {
                    InterpreterMachine.TABLE_INIT(stack, instance, operands);
                    continue block261;
                }
                case DATA_DROP: {
                    InterpreterMachine.DATA_DROP(instance, operands);
                    continue block261;
                }
                case MEMORY_COPY: {
                    InterpreterMachine.MEMORY_COPY(stack, instance);
                    continue block261;
                }
                case TABLE_COPY: {
                    InterpreterMachine.TABLE_COPY(stack, instance, operands);
                    continue block261;
                }
                case TABLE_FILL: {
                    InterpreterMachine.TABLE_FILL(stack, instance, operands);
                    continue block261;
                }
                case TABLE_SIZE: {
                    InterpreterMachine.TABLE_SIZE(stack, instance, operands);
                    continue block261;
                }
                case TABLE_GROW: {
                    InterpreterMachine.TABLE_GROW(stack, instance, operands);
                    continue block261;
                }
                case REF_FUNC: {
                    stack.push(operands.get(0));
                    continue block261;
                }
                case REF_NULL: {
                    InterpreterMachine.REF_NULL(stack);
                    continue block261;
                }
                case REF_IS_NULL: {
                    InterpreterMachine.REF_IS_NULL(stack);
                    continue block261;
                }
                case REF_AS_NON_NULL: {
                    InterpreterMachine.REF_AS_NON_NULL(stack);
                    continue block261;
                }
                case ELEM_DROP: {
                    InterpreterMachine.ELEM_DROP(instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_LOAD: {
                    InterpreterMachine.I32_ATOMIC_LOAD(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_LOAD: {
                    InterpreterMachine.I64_ATOMIC_LOAD(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_LOAD8_U: {
                    InterpreterMachine.I64_ATOMIC_LOAD8_U(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_LOAD8_U: {
                    InterpreterMachine.I32_ATOMIC_LOAD8_U(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_LOAD16_U: {
                    InterpreterMachine.I32_ATOMIC_LOAD16_U(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_LOAD16_U: {
                    InterpreterMachine.I64_ATOMIC_LOAD16_U(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_LOAD32_U: {
                    InterpreterMachine.I64_ATOMIC_LOAD32_U(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_STORE: {
                    InterpreterMachine.I32_ATOMIC_STORE(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_STORE: {
                    InterpreterMachine.I64_ATOMIC_STORE(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_STORE8: 
                case I64_ATOMIC_STORE8: {
                    InterpreterMachine.I64_ATOMIC_STORE8(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_STORE16: 
                case I64_ATOMIC_STORE16: {
                    InterpreterMachine.I64_ATOMIC_STORE16(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_STORE32: {
                    InterpreterMachine.I64_ATOMIC_STORE32(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_RMW_ADD: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.ADD);
                    continue block261;
                }
                case I32_ATOMIC_RMW_SUB: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.SUB);
                    continue block261;
                }
                case I32_ATOMIC_RMW_AND: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.AND);
                    continue block261;
                }
                case I32_ATOMIC_RMW_OR: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.OR);
                    continue block261;
                }
                case I32_ATOMIC_RMW_XOR: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.XOR);
                    continue block261;
                }
                case I32_ATOMIC_RMW_XCHG: {
                    InterpreterMachine.I32_ATOMIC_RMW(stack, instance, operands, AtomicOp.XCHG);
                    continue block261;
                }
                case I32_ATOMIC_RMW_CMPXCHG: {
                    InterpreterMachine.I32_ATOMIC_RMW_CMPXCHG(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_RMW_ADD: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.ADD);
                    continue block261;
                }
                case I64_ATOMIC_RMW_SUB: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.SUB);
                    continue block261;
                }
                case I64_ATOMIC_RMW_AND: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.AND);
                    continue block261;
                }
                case I64_ATOMIC_RMW_OR: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.OR);
                    continue block261;
                }
                case I64_ATOMIC_RMW_XOR: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.XOR);
                    continue block261;
                }
                case I64_ATOMIC_RMW_XCHG: {
                    InterpreterMachine.I64_ATOMIC_RMW(stack, instance, operands, AtomicOp.XCHG);
                    continue block261;
                }
                case I64_ATOMIC_RMW_CMPXCHG: {
                    InterpreterMachine.I64_ATOMIC_RMW_CMPXCHG(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_ADD_U: 
                case I64_ATOMIC_RMW8_ADD_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.ADD);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_SUB_U: 
                case I64_ATOMIC_RMW8_SUB_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.SUB);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_AND_U: 
                case I64_ATOMIC_RMW8_AND_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.AND);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_OR_U: 
                case I64_ATOMIC_RMW8_OR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.OR);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_XOR_U: 
                case I64_ATOMIC_RMW8_XOR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.XOR);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_XCHG_U: 
                case I64_ATOMIC_RMW8_XCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_U(stack, instance, operands, AtomicOp.XCHG);
                    continue block261;
                }
                case I32_ATOMIC_RMW8_CMPXCHG_U: 
                case I64_ATOMIC_RMW8_CMPXCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW8_CMPXCHG_U(stack, instance, operands);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_ADD_U: 
                case I64_ATOMIC_RMW16_ADD_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.ADD);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_SUB_U: 
                case I64_ATOMIC_RMW16_SUB_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.SUB);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_AND_U: 
                case I64_ATOMIC_RMW16_AND_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.AND);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_OR_U: 
                case I64_ATOMIC_RMW16_OR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.OR);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_XOR_U: 
                case I64_ATOMIC_RMW16_XOR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.XOR);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_XCHG_U: 
                case I64_ATOMIC_RMW16_XCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_U(stack, instance, operands, AtomicOp.XCHG);
                    continue block261;
                }
                case I32_ATOMIC_RMW16_CMPXCHG_U: 
                case I64_ATOMIC_RMW16_CMPXCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW16_CMPXCHG_U(stack, instance, operands);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_ADD_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.ADD);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_SUB_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.SUB);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_AND_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.AND);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_OR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.OR);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_XOR_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.XOR);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_XCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_U(stack, instance, operands, AtomicOp.XCHG);
                    continue block261;
                }
                case I64_ATOMIC_RMW32_CMPXCHG_U: {
                    InterpreterMachine.I64_ATOMIC_RMW32_CMPXCHG_U(stack, instance, operands);
                    continue block261;
                }
                case MEM_ATOMIC_WAIT32: {
                    InterpreterMachine.MEM_ATOMIC_WAIT32(stack, instance, operands);
                    continue block261;
                }
                case MEM_ATOMIC_WAIT64: {
                    InterpreterMachine.MEM_ATOMIC_WAIT64(stack, instance, operands);
                    continue block261;
                }
                case MEM_ATOMIC_NOTIFY: {
                    InterpreterMachine.MEM_ATOMIC_NOTIFY(stack, instance, operands);
                    continue block261;
                }
                case ATOMIC_FENCE: {
                    InterpreterMachine.ATOMIC_FENCE(instance);
                    continue block261;
                }
                default: {
                    this.evalDefault(stack, instance, callStack, instruction, operands);
                    continue block261;
                }
            }
        }
    }

    private static void I32_GE_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_GE_U(a, b));
    }

    private static void I64_GT_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_GT_U(a, b));
    }

    private static void I32_GE_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_GE_S(a, b));
    }

    private static void I64_GE_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_GE_U(a, b));
    }

    private static void I64_GE_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_GE_S(a, b));
    }

    private static void I32_LE_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_LE_S(a, b));
    }

    private static void I32_LE_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_LE_U(a, b));
    }

    private static void I64_LE_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_LE_S(a, b));
    }

    private static void I64_LE_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_LE_U(a, b));
    }

    private static void F32_EQ(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_EQ(a, b));
    }

    private static void F64_EQ(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_EQ(a, b));
    }

    private static void I32_CLZ(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(OpcodeImpl.I32_CLZ(tos));
    }

    private static void I32_CTZ(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(OpcodeImpl.I32_CTZ(tos));
    }

    private static void I32_POPCNT(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(OpcodeImpl.I32_POPCNT(tos));
    }

    private static void I32_ADD(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(a + b);
    }

    private static void I64_ADD(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a + b);
    }

    private static void I32_SUB(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(b - a);
    }

    private static void I64_SUB(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(b - a);
    }

    private static void I32_MUL(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a * b);
    }

    private static void I64_MUL(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a * b);
    }

    private static void I32_DIV_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_DIV_S(a, b));
    }

    private static void I32_DIV_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_DIV_U(a, b));
    }

    private static void I64_EXTEND_8_S(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_EXTEND_8_S(tos));
    }

    private static void I64_EXTEND_16_S(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_EXTEND_16_S(tos));
    }

    private static void I64_EXTEND_32_S(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_EXTEND_32_S(tos));
    }

    private static void F64_CONVERT_I64_U(MStack stack) {
        long tos = stack.pop();
        stack.push(Value.doubleToLong(OpcodeImpl.F64_CONVERT_I64_U(tos)));
    }

    private static void F64_CONVERT_I32_U(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(Value.doubleToLong(OpcodeImpl.F64_CONVERT_I32_U(tos)));
    }

    private static void F64_CONVERT_I32_S(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(Value.doubleToLong(OpcodeImpl.F64_CONVERT_I32_S(tos)));
    }

    private static void I32_EXTEND_8_S(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(OpcodeImpl.I32_EXTEND_8_S(tos));
    }

    private static void F64_NEAREST(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_NEAREST(val)));
    }

    private static void F32_NEAREST(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_NEAREST(val)));
    }

    private static void F64_TRUNC(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_TRUNC(val)));
    }

    private static void F64_CEIL(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_CEIL(val)));
    }

    private static void F32_CEIL(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_CEIL(val)));
    }

    private static void F64_FLOOR(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_FLOOR(val)));
    }

    private static void F32_FLOOR(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_FLOOR(val)));
    }

    private static void F64_SQRT(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_SQRT(val)));
    }

    private static void F32_SQRT(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_SQRT(val)));
    }

    private static void F64_MAX(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_MAX(a, b)));
    }

    private static void F32_MAX(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_MAX(a, b)));
    }

    private static void F64_MIN(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_MIN(a, b)));
    }

    private static void F32_MIN(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_MIN(a, b)));
    }

    private static void F64_DIV(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(b / a));
    }

    private static void F32_DIV(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(b / a));
    }

    private static void F64_MUL(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(b * a));
    }

    private static void F32_MUL(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(b * a));
    }

    private static void F64_SUB(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(b - a));
    }

    private static void F32_SUB(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(b - a));
    }

    private static void F64_ADD(MStack stack) {
        double a = Value.longToDouble(stack.pop());
        double b = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(a + b));
    }

    private static void F32_ADD(MStack stack) {
        float a = Value.longToFloat(stack.pop());
        float b = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(a + b));
    }

    private static void I32_ROTR(MStack stack) {
        int c = (int)stack.pop();
        int v = (int)stack.pop();
        stack.push(OpcodeImpl.I32_ROTR(v, c));
    }

    private static void I32_ROTL(MStack stack) {
        int c = (int)stack.pop();
        int v = (int)stack.pop();
        stack.push(OpcodeImpl.I32_ROTL(v, c));
    }

    private static void I32_SHR_U(MStack stack) {
        int c = (int)stack.pop();
        int v = (int)stack.pop();
        stack.push(v >>> c);
    }

    private static void I32_SHR_S(MStack stack) {
        int c = (int)stack.pop();
        int v = (int)stack.pop();
        stack.push(v >> c);
    }

    private static void I32_SHL(MStack stack) {
        int c = (int)stack.pop();
        int v = (int)stack.pop();
        stack.push(v << c);
    }

    private static void I32_XOR(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(a ^ b);
    }

    private static void I32_OR(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(a | b);
    }

    private static void I32_AND(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(a & b);
    }

    private static void I64_POPCNT(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_POPCNT(tos));
    }

    private static void I64_CTZ(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_CTZ(tos));
    }

    private static void I64_CLZ(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_CLZ(tos));
    }

    private static void I64_ROTR(MStack stack) {
        long c = stack.pop();
        long v = stack.pop();
        stack.push(OpcodeImpl.I64_ROTR(v, c));
    }

    private static void I64_ROTL(MStack stack) {
        long c = stack.pop();
        long v = stack.pop();
        stack.push(OpcodeImpl.I64_ROTL(v, c));
    }

    private static void I64_REM_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_REM_U(a, b));
    }

    private static void I64_REM_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_REM_S(a, b));
    }

    private static void I64_SHR_U(MStack stack) {
        long c = stack.pop();
        long v = stack.pop();
        stack.push(v >>> (int)c);
    }

    private static void I64_SHR_S(MStack stack) {
        long c = stack.pop();
        long v = stack.pop();
        stack.push(v >> (int)c);
    }

    private static void I64_SHL(MStack stack) {
        long c = stack.pop();
        long v = stack.pop();
        stack.push(v << (int)c);
    }

    private static void I64_XOR(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a ^ b);
    }

    private static void I64_OR(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a | b);
    }

    private static void I64_AND(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(a & b);
    }

    private static void I32_REM_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_REM_U(a, b));
    }

    private static void I32_REM_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_REM_S(a, b));
    }

    private static void I64_DIV_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_DIV_U(a, b));
    }

    private static void I64_DIV_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_DIV_S(a, b));
    }

    private static void I64_GT_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_GT_S(a, b));
    }

    private static void I32_GT_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_GT_U(a, b));
    }

    private static void I32_GT_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_GT_S(a, b));
    }

    private static void I64_LT_U(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_LT_U(a, b));
    }

    private static void I64_LT_S(MStack stack) {
        long b = stack.pop();
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_LT_S(a, b));
    }

    private static void I32_LT_U(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_LT_U(a, b));
    }

    private static void I32_LT_S(MStack stack) {
        int b = (int)stack.pop();
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_LT_S(a, b));
    }

    private static void I64_EQZ(MStack stack) {
        long a = stack.pop();
        stack.push(OpcodeImpl.I64_EQZ(a));
    }

    private static void I32_EQZ(MStack stack) {
        int a = (int)stack.pop();
        stack.push(OpcodeImpl.I32_EQZ(a));
    }

    private static void I64_NE(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(OpcodeImpl.I64_NE(a, b));
    }

    private static void I32_NE(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(OpcodeImpl.I32_NE(a, b));
    }

    private static void I64_EQ(MStack stack) {
        long a = stack.pop();
        long b = stack.pop();
        stack.push(OpcodeImpl.I64_EQ(a, b));
    }

    private static void I32_EQ(MStack stack) {
        int a = (int)stack.pop();
        int b = (int)stack.pop();
        stack.push(OpcodeImpl.I32_EQ(a, b));
    }

    private static void MEMORY_SIZE(MStack stack, Instance instance) {
        int sz = instance.memory().pages();
        stack.push(sz);
    }

    private static void I64_STORE32(MStack stack, Instance instance, Operands operands) {
        long value = stack.pop();
        int ptr = (int)(operands.get(1) + (long)((int)stack.pop()));
        instance.memory().writeI32(ptr, (int)value);
    }

    private static void I64_STORE8(MStack stack, Instance instance, Operands operands) {
        byte value = (byte)stack.pop();
        int ptr = (int)(operands.get(1) + (long)((int)stack.pop()));
        instance.memory().writeByte(ptr, value);
    }

    private static void F64_PROMOTE_F32(MStack stack) {
        long tos = stack.pop();
        stack.push(Double.doubleToRawLongBits(Float.intBitsToFloat((int)tos)));
    }

    private static void F64_REINTERPRET_I64(MStack stack) {
        long tos = stack.pop();
        stack.push(Value.doubleToLong(OpcodeImpl.F64_REINTERPRET_I64(tos)));
    }

    private static void I32_WRAP_I64(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(tos);
    }

    private static void I64_EXTEND_I32_S(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(tos);
    }

    private static void I64_EXTEND_I32_U(MStack stack) {
        long tos = stack.pop();
        stack.push(OpcodeImpl.I64_EXTEND_I32_U((int)tos));
    }

    private static void I32_REINTERPRET_F32(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I32_REINTERPRET_F32(tos));
    }

    private static void I64_REINTERPRET_F64(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I64_REINTERPRET_F64(tos));
    }

    private static void F32_REINTERPRET_I32(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(Value.floatToLong(OpcodeImpl.F32_REINTERPRET_I32(tos)));
    }

    private static void F32_DEMOTE_F64(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.floatToLong((float)val));
    }

    private static void F32_CONVERT_I32_S(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(Value.floatToLong(OpcodeImpl.F32_CONVERT_I32_S(tos)));
    }

    private static void I32_EXTEND_16_S(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(OpcodeImpl.I32_EXTEND_16_S(tos));
    }

    private static void I64_TRUNC_F64_S(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_F64_S(tos));
    }

    private static void F32_COPYSIGN(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_COPYSIGN(a, b)));
    }

    private static void F32_ABS(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_ABS(val)));
    }

    private static void F64_ABS(MStack stack) {
        double val = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_ABS(val)));
    }

    private static void F32_NE(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_NE(a, b));
    }

    private static void F64_NE(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_NE(a, b));
    }

    private static void F32_LT(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_LT(a, b));
    }

    private static void F64_LT(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_LT(a, b));
    }

    private static void F32_LE(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_LE(a, b));
    }

    private static void F64_LE(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_LE(a, b));
    }

    private static void F32_GE(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_GE(a, b));
    }

    private static void F64_GE(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_GE(a, b));
    }

    private static void F32_GT(MStack stack) {
        float b = Value.longToFloat(stack.pop());
        float a = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.F32_GT(a, b));
    }

    private static void F64_GT(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.F64_GT(a, b));
    }

    private static void F32_CONVERT_I32_U(MStack stack) {
        int tos = (int)stack.pop();
        stack.push(Value.floatToLong(OpcodeImpl.F32_CONVERT_I32_U(tos)));
    }

    private static void F32_CONVERT_I64_S(MStack stack) {
        long tos = stack.pop();
        stack.push(Value.floatToLong(OpcodeImpl.F32_CONVERT_I64_S(tos)));
    }

    private static void REF_NULL(MStack stack) {
        stack.push(-1L);
    }

    private static void ELEM_DROP(Instance instance, Operands operands) {
        int x = (int)operands.get(0);
        instance.setElement(x, null);
    }

    private static void REF_IS_NULL(MStack stack) {
        long val = stack.pop();
        stack.push(val == -1L ? 1L : 0L);
    }

    private static void REF_AS_NON_NULL(MStack stack) {
        long val = stack.pop();
        if (val == -1L) {
            throw new TrapException("Trapped on ref_as_non_null on null reference");
        }
        stack.push(val);
    }

    private static void DATA_DROP(Instance instance, Operands operands) {
        int segment = (int)operands.get(0);
        instance.memory().drop(segment);
    }

    private static void F64_CONVERT_I64_S(MStack stack) {
        long tos = stack.pop();
        stack.push(Value.doubleToLong(OpcodeImpl.F64_CONVERT_I64_S(tos)));
    }

    private static void TABLE_GROW(MStack stack, Instance instance, Operands operands) {
        int tableidx = (int)operands.get(0);
        TableInstance table = instance.table(tableidx);
        int size = (int)stack.pop();
        int val = (int)stack.pop();
        int res = table.grow(size, val, instance);
        stack.push(res);
    }

    private static void TABLE_SIZE(MStack stack, Instance instance, Operands operands) {
        int tableidx = (int)operands.get(0);
        TableInstance table = instance.table(tableidx);
        stack.push(table.size());
    }

    private static void TABLE_FILL(MStack stack, Instance instance, Operands operands) {
        int tableidx = (int)operands.get(0);
        int size = (int)stack.pop();
        int val = (int)stack.pop();
        int offset = (int)stack.pop();
        OpcodeImpl.TABLE_FILL(instance, tableidx, size, val, offset);
    }

    private static void TABLE_COPY(MStack stack, Instance instance, Operands operands) {
        int tableidxSrc = (int)operands.get(1);
        int tableidxDst = (int)operands.get(0);
        int size = (int)stack.pop();
        int s = (int)stack.pop();
        int d = (int)stack.pop();
        OpcodeImpl.TABLE_COPY(instance, tableidxSrc, tableidxDst, size, s, d);
    }

    private static void MEMORY_COPY(MStack stack, Instance instance) {
        int size = (int)stack.pop();
        int offset = (int)stack.pop();
        int destination = (int)stack.pop();
        instance.memory().copy(destination, offset, size);
    }

    private static void TABLE_INIT(MStack stack, Instance instance, Operands operands) {
        int tableidx = (int)operands.get(1);
        int elementidx = (int)operands.get(0);
        int size = (int)stack.pop();
        int elemidx = (int)stack.pop();
        int offset = (int)stack.pop();
        OpcodeImpl.TABLE_INIT(instance, tableidx, elementidx, size, elemidx, offset);
    }

    private static void MEMORY_INIT(MStack stack, Instance instance, Operands operands) {
        int segmentId = (int)operands.get(0);
        int size = (int)stack.pop();
        int offset = (int)stack.pop();
        int destination = (int)stack.pop();
        instance.memory().initPassiveSegment(segmentId, destination, offset, size);
    }

    private static void I64_TRUNC_F32_S(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_F32_S(tos));
    }

    private static void I32_TRUNC_F64_U(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_F64_U(tos));
    }

    private static void I32_TRUNC_F64_S(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_F64_S(tos));
    }

    private static void I64_TRUNC_SAT_F64_U(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_SAT_F64_U(tos));
    }

    private static void I64_TRUNC_SAT_F64_S(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_SAT_F64_S(tos));
    }

    private static void I64_TRUNC_SAT_F32_U(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_SAT_F32_U(tos));
    }

    private static void I64_TRUNC_SAT_F32_S(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_SAT_F32_S(tos));
    }

    private static void I64_TRUNC_F64_U(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_F64_U(tos));
    }

    private static void I64_TRUNC_F32_U(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I64_TRUNC_F32_U(tos));
    }

    private static void F32_CONVERT_I64_U(MStack stack) {
        long tos = stack.pop();
        stack.push(Value.floatToLong(OpcodeImpl.F32_CONVERT_I64_U(tos)));
    }

    private static void I32_TRUNC_F32_U(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_F32_U(tos));
    }

    private static void I32_TRUNC_SAT_F64_U(MStack stack) {
        double tos = Double.longBitsToDouble(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_SAT_F64_U(tos));
    }

    private static void I32_TRUNC_SAT_F64_S(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_SAT_F64_S(tos));
    }

    private static void I32_TRUNC_SAT_F32_U(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_SAT_F32_U(tos));
    }

    private static void I32_TRUNC_SAT_F32_S(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_SAT_F32_S(tos));
    }

    private static void I32_TRUNC_F32_S(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(OpcodeImpl.I32_TRUNC_F32_S(tos));
    }

    private static void F64_COPYSIGN(MStack stack) {
        double b = Value.longToDouble(stack.pop());
        double a = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(OpcodeImpl.F64_COPYSIGN(a, b)));
    }

    private static void F32_TRUNC(MStack stack) {
        float val = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(OpcodeImpl.F32_TRUNC(val)));
    }

    protected void CALL(Operands operands) {
        int funcId = (int)operands.get(0);
        int typeId = this.instance.functionType(funcId);
        FunctionType type = this.instance.type(typeId);
        long[] args = InterpreterMachine.extractArgsForParams(this.stack, type.params());
        this.call(this.stack, this.instance, this.callStack, funcId, args, type, false);
    }

    private void CALL_REF() {
        int funcId = (int)this.stack.pop();
        if (funcId == -1) {
            throw new TrapException("Trapped on call_ref on null function reference");
        }
        int typeId = this.instance.functionType(funcId);
        FunctionType type = this.instance.type(typeId);
        long[] args = InterpreterMachine.extractArgsForParams(this.stack, type.params());
        this.call(this.stack, this.instance, this.callStack, funcId, args, type, false);
    }

    private static void F64_NEG(MStack stack) {
        double tos = Value.longToDouble(stack.pop());
        stack.push(Value.doubleToLong(-tos));
    }

    private static void F32_NEG(MStack stack) {
        float tos = Value.longToFloat(stack.pop());
        stack.push(Value.floatToLong(-tos));
    }

    private static void MEMORY_FILL(MStack stack, Instance instance) {
        int size = (int)stack.pop();
        byte val = (byte)stack.pop();
        int offset = (int)stack.pop();
        int end = size + offset;
        instance.memory().fill(val, offset, end);
    }

    private static void MEMORY_GROW(MStack stack, Instance instance) {
        int size = (int)stack.pop();
        int nPages = instance.memory().grow(size);
        stack.push(nPages);
    }

    protected static int readMemPtr(MStack stack, Operands operands) {
        int address = (int)stack.pop();
        if (operands.get(1) < 0L || operands.get(1) >= Integer.MAX_VALUE || address < 0) {
            throw new WasmRuntimeException("out of bounds memory access");
        }
        return (int)(operands.get(1) + (long)address);
    }

    private static void F64_STORE(MStack stack, Instance instance, Operands operands) {
        double value = Value.longToDouble(stack.pop());
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().writeF64(ptr, value);
    }

    private static void F32_STORE(MStack stack, Instance instance, Operands operands) {
        float value = Value.longToFloat(stack.pop());
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().writeF32(ptr, value);
    }

    private static void I64_STORE(MStack stack, Instance instance, Operands operands) {
        long value = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().writeLong(ptr, value);
    }

    private static void I64_STORE16(MStack stack, Instance instance, Operands operands) {
        short value = (short)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().writeShort(ptr, value);
    }

    private static void I32_STORE(MStack stack, Instance instance, Operands operands) {
        int value = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().writeI32(ptr, value);
    }

    private static void I64_LOAD32_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readU32(ptr);
        stack.push(val);
    }

    private static void I64_LOAD32_S(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI32(ptr);
        stack.push(val);
    }

    private static void I64_LOAD16_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readU16(ptr);
        stack.push(val);
    }

    private static void I32_LOAD16_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readU16(ptr);
        stack.push(val);
    }

    private static void I64_LOAD16_S(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI16(ptr);
        stack.push(val);
    }

    private static void I32_LOAD16_S(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI16(ptr);
        stack.push(val);
    }

    private static void I64_LOAD8_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readU8(ptr);
        stack.push(val);
    }

    private static void I32_LOAD8_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readU8(ptr);
        stack.push(val);
    }

    private static void I64_LOAD8_S(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI8(ptr);
        stack.push(val);
    }

    private static void I32_LOAD8_S(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI8(ptr);
        stack.push(val);
    }

    private static void F64_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readF64(ptr);
        stack.push(val);
    }

    private static void F32_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readF32(ptr);
        stack.push(val);
    }

    private static void I64_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI64(ptr);
        stack.push(val);
    }

    private static void I32_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        long val = instance.memory().readI32(ptr);
        stack.push(val);
    }

    private static void TABLE_SET(MStack stack, Instance instance, Operands operands) {
        int idx = (int)operands.get(0);
        TableInstance table = instance.table(idx);
        int value = (int)stack.pop();
        int i = (int)stack.pop();
        table.setRef(i, value, instance);
    }

    private static void TABLE_GET(MStack stack, Instance instance, Operands operands) {
        int idx = (int)operands.get(0);
        int i = (int)stack.pop();
        stack.push(OpcodeImpl.TABLE_GET(instance, idx, i));
    }

    private static void GLOBAL_SET(MStack stack, Instance instance, Operands operands) {
        int id = (int)operands.get(0);
        if (!instance.global(id).getType().equals(ValType.V128)) {
            long val = stack.pop();
            instance.global(id).setValue(val);
        } else {
            long high = stack.pop();
            long low = stack.pop();
            instance.global(id).setValueLow(low);
            instance.global(id).setValueHigh(high);
        }
    }

    private static void GLOBAL_GET(MStack stack, Instance instance, Operands operands) {
        int idx = (int)operands.get(0);
        stack.push(instance.global(idx).getValueLow());
        if (instance.global(idx).getType().equals(ValType.V128)) {
            stack.push(instance.global(idx).getValueHigh());
        }
    }

    private static void DROP(MStack stack, Operands operands) {
        if (operands.get(0) == 123L) {
            stack.pop();
        }
        stack.pop();
    }

    private static void SELECT(MStack stack, Operands operands) {
        int pred = (int)stack.pop();
        if (operands.get(0) == 123L) {
            long b1 = stack.pop();
            long b2 = stack.pop();
            long a1 = stack.pop();
            long a2 = stack.pop();
            if (pred == 0) {
                stack.push(b2);
                stack.push(b1);
            } else {
                stack.push(a2);
                stack.push(a1);
            }
        } else {
            long b = stack.pop();
            long a = stack.pop();
            if (pred == 0) {
                stack.push(b);
            } else {
                stack.push(a);
            }
        }
    }

    private static void SELECT_T(MStack stack, Operands operands) {
        int pred = (int)stack.pop();
        long typeId = operands.get(0);
        if (typeId == ValType.V128.id()) {
            long b1 = stack.pop();
            long b2 = stack.pop();
            long a1 = stack.pop();
            long a2 = stack.pop();
            if (pred == 0) {
                stack.push(b2);
                stack.push(b1);
            } else {
                stack.push(a2);
                stack.push(a1);
            }
        } else {
            long b = stack.pop();
            long a = stack.pop();
            if (pred == 0) {
                stack.push(b);
            } else {
                stack.push(a);
            }
        }
    }

    private static void LOCAL_GET(MStack stack, Operands operands, StackFrame currentStackFrame) {
        int idx = (int)operands.get(0);
        int i = currentStackFrame.localIndexOf(idx);
        if (currentStackFrame.localType(idx).equals(ValType.V128)) {
            stack.push(currentStackFrame.local(i));
            stack.push(currentStackFrame.local(i + 1));
        } else {
            stack.push(currentStackFrame.local(i));
        }
    }

    private static void LOCAL_SET(MStack stack, Operands operands, StackFrame currentStackFrame) {
        int idx = (int)operands.get(0);
        int i = currentStackFrame.localIndexOf(idx);
        if (currentStackFrame.localType(idx).equals(ValType.V128)) {
            currentStackFrame.setLocal(i, stack.pop());
            currentStackFrame.setLocal(i + 1, stack.pop());
        } else {
            currentStackFrame.setLocal(i, stack.pop());
        }
    }

    private static void LOCAL_TEE(MStack stack, Operands operands, StackFrame currentStackFrame) {
        int idx = (int)operands.get(0);
        int i = currentStackFrame.localIndexOf(idx);
        if (currentStackFrame.localType(idx).equals(ValType.V128)) {
            long tmp = stack.pop();
            currentStackFrame.setLocal(i, tmp);
            currentStackFrame.setLocal(i + 1, stack.peek());
            stack.push(tmp);
        } else {
            currentStackFrame.setLocal(i, stack.peek());
        }
    }

    private static void I32_ATOMIC_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int val = instance.memory().atomicReadInt(ptr);
        stack.push(val);
    }

    private static void I64_ATOMIC_LOAD(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        long val = instance.memory().atomicReadLong(ptr);
        stack.push(val);
    }

    private static void I64_ATOMIC_LOAD8_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        byte val = instance.memory().atomicReadByte(ptr);
        stack.push(Byte.toUnsignedLong(val));
    }

    private static void I32_ATOMIC_LOAD8_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        byte val = instance.memory().atomicReadByte(ptr);
        stack.push(Byte.toUnsignedLong(val));
    }

    private static void I32_ATOMIC_LOAD16_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        short val = instance.memory().atomicReadShort(ptr);
        stack.push(Short.toUnsignedLong(val));
    }

    private static void I64_ATOMIC_LOAD16_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        short val = instance.memory().atomicReadShort(ptr);
        stack.push(Short.toUnsignedLong(val));
    }

    private static void I64_ATOMIC_LOAD32_U(MStack stack, Instance instance, Operands operands) {
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int val = instance.memory().atomicReadInt(ptr);
        stack.push(Integer.toUnsignedLong(val));
    }

    private static void I32_ATOMIC_STORE(MStack stack, Instance instance, Operands operands) {
        int value = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        instance.memory().atomicWriteInt(ptr, value);
    }

    private static void I64_ATOMIC_STORE8(MStack stack, Instance instance, Operands operands) {
        byte value = (byte)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        instance.memory().atomicWriteByte(ptr, value);
    }

    private static void I64_ATOMIC_STORE16(MStack stack, Instance instance, Operands operands) {
        short value = (short)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        instance.memory().atomicWriteShort(ptr, value);
    }

    private static void I64_ATOMIC_STORE32(MStack stack, Instance instance, Operands operands) {
        long value = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        instance.memory().atomicWriteInt(ptr, (int)value);
    }

    private static void I64_ATOMIC_STORE(MStack stack, Instance instance, Operands operands) {
        long value = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        instance.memory().atomicWriteLong(ptr, value);
    }

    private static void I32_ATOMIC_RMW(MStack stack, Instance instance, Operands operands, AtomicOp op) {
        int oldVal;
        int operand = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        switch (op) {
            case ADD: {
                oldVal = instance.memory().atomicAddInt(ptr, operand);
                break;
            }
            case SUB: {
                oldVal = instance.memory().atomicAddInt(ptr, -operand);
                break;
            }
            case AND: {
                oldVal = instance.memory().atomicAndInt(ptr, operand);
                break;
            }
            case OR: {
                oldVal = instance.memory().atomicOrInt(ptr, operand);
                break;
            }
            case XOR: {
                oldVal = instance.memory().atomicXorInt(ptr, operand);
                break;
            }
            case XCHG: {
                oldVal = instance.memory().atomicXchgInt(ptr, operand);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected atomic op: " + String.valueOf((Object)op));
            }
        }
        stack.push(oldVal);
    }

    private static void I32_ATOMIC_RMW_CMPXCHG(MStack stack, Instance instance, Operands operands) {
        int replacement = (int)stack.pop();
        int expected = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int oldVal = instance.memory().atomicCmpxchgInt(ptr, expected, replacement);
        stack.push(oldVal);
    }

    private static void I64_ATOMIC_RMW(MStack stack, Instance instance, Operands operands, AtomicOp op) {
        long oldVal;
        long operand = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        switch (op) {
            case ADD: {
                oldVal = instance.memory().atomicAddLong(ptr, operand);
                break;
            }
            case SUB: {
                oldVal = instance.memory().atomicAddLong(ptr, -operand);
                break;
            }
            case AND: {
                oldVal = instance.memory().atomicAndLong(ptr, operand);
                break;
            }
            case OR: {
                oldVal = instance.memory().atomicOrLong(ptr, operand);
                break;
            }
            case XOR: {
                oldVal = instance.memory().atomicXorLong(ptr, operand);
                break;
            }
            case XCHG: {
                oldVal = instance.memory().atomicXchgLong(ptr, operand);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected atomic op: " + String.valueOf((Object)op));
            }
        }
        stack.push(oldVal);
    }

    private static void I64_ATOMIC_RMW_CMPXCHG(MStack stack, Instance instance, Operands operands) {
        long replacement = stack.pop();
        long expected = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        long oldVal = instance.memory().atomicCmpxchgLong(ptr, expected, replacement);
        stack.push(oldVal);
    }

    private static void I64_ATOMIC_RMW8_U(MStack stack, Instance instance, Operands operands, AtomicOp op) {
        byte oldVal;
        byte operand = (byte)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        switch (op) {
            case ADD: {
                oldVal = instance.memory().atomicAddByte(ptr, operand);
                break;
            }
            case SUB: {
                oldVal = instance.memory().atomicAddByte(ptr, -operand);
                break;
            }
            case AND: {
                oldVal = instance.memory().atomicAndByte(ptr, operand);
                break;
            }
            case OR: {
                oldVal = instance.memory().atomicOrByte(ptr, operand);
                break;
            }
            case XOR: {
                oldVal = instance.memory().atomicXorByte(ptr, operand);
                break;
            }
            case XCHG: {
                oldVal = instance.memory().atomicXchgByte(ptr, operand);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected atomic op: " + String.valueOf((Object)op));
            }
        }
        stack.push(Byte.toUnsignedLong(oldVal));
    }

    private static void I64_ATOMIC_RMW8_CMPXCHG_U(MStack stack, Instance instance, Operands operands) {
        byte replacement = (byte)stack.pop();
        byte expected = (byte)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        byte oldVal = instance.memory().atomicCmpxchgByte(ptr, expected, replacement);
        stack.push(Byte.toUnsignedLong(oldVal));
    }

    private static void I64_ATOMIC_RMW16_U(MStack stack, Instance instance, Operands operands, AtomicOp op) {
        short oldVal;
        short operand = (short)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        switch (op) {
            case ADD: {
                oldVal = instance.memory().atomicAddShort(ptr, operand);
                break;
            }
            case SUB: {
                oldVal = instance.memory().atomicAddShort(ptr, -operand);
                break;
            }
            case AND: {
                oldVal = instance.memory().atomicAndShort(ptr, operand);
                break;
            }
            case OR: {
                oldVal = instance.memory().atomicOrShort(ptr, operand);
                break;
            }
            case XOR: {
                oldVal = instance.memory().atomicXorShort(ptr, operand);
                break;
            }
            case XCHG: {
                oldVal = instance.memory().atomicXchgShort(ptr, operand);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected atomic op: " + String.valueOf((Object)op));
            }
        }
        stack.push(Short.toUnsignedLong(oldVal));
    }

    private static void I64_ATOMIC_RMW16_CMPXCHG_U(MStack stack, Instance instance, Operands operands) {
        short replacement = (short)stack.pop();
        short expected = (short)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        short oldVal = instance.memory().atomicCmpxchgShort(ptr, expected, replacement);
        stack.push(Short.toUnsignedLong(oldVal));
    }

    private static void I64_ATOMIC_RMW32_U(MStack stack, Instance instance, Operands operands, AtomicOp op) {
        int oldVal;
        int operand = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        switch (op) {
            case ADD: {
                oldVal = instance.memory().atomicAddInt(ptr, operand);
                break;
            }
            case SUB: {
                oldVal = instance.memory().atomicAddInt(ptr, -operand);
                break;
            }
            case AND: {
                oldVal = instance.memory().atomicAndInt(ptr, operand);
                break;
            }
            case OR: {
                oldVal = instance.memory().atomicOrInt(ptr, operand);
                break;
            }
            case XOR: {
                oldVal = instance.memory().atomicXorInt(ptr, operand);
                break;
            }
            case XCHG: {
                oldVal = instance.memory().atomicXchgInt(ptr, operand);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected atomic op: " + String.valueOf((Object)op));
            }
        }
        stack.push(Integer.toUnsignedLong(oldVal));
    }

    private static void I64_ATOMIC_RMW32_CMPXCHG_U(MStack stack, Instance instance, Operands operands) {
        int replacement = (int)stack.pop();
        int expected = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int oldVal = instance.memory().atomicCmpxchgInt(ptr, expected, replacement);
        stack.push(Integer.toUnsignedLong(oldVal));
    }

    private static void MEM_ATOMIC_WAIT32(MStack stack, Instance instance, Operands operands) {
        long timeout = stack.pop();
        int expected = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int result = instance.memory().atomicWait(ptr, expected, timeout);
        stack.push(result);
    }

    private static void MEM_ATOMIC_WAIT64(MStack stack, Instance instance, Operands operands) {
        long timeout = stack.pop();
        long expected = stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        int result = instance.memory().atomicWait(ptr, expected, timeout);
        stack.push(result);
    }

    private static void MEM_ATOMIC_NOTIFY(MStack stack, Instance instance, Operands operands) {
        int maxThreads = (int)stack.pop();
        int ptr = InterpreterMachine.readMemPtr(stack, operands);
        int result = instance.memory().atomicNotify(ptr, maxThreads);
        stack.push(result);
    }

    private static void ATOMIC_FENCE(Instance instance) {
        instance.memory().atomicFence();
    }

    private static StackFrame RETURN_CALL(MStack stack, Instance instance, Deque<StackFrame> callStack, Operands operands, StackFrame currentStackFrame) {
        int funcId = (int)operands.get(0);
        int typeId = instance.functionType(funcId);
        FunctionType type = instance.type(typeId);
        FunctionBody func = instance.function(funcId);
        long[] args = InterpreterMachine.extractArgsForParams(stack, type.params());
        if (currentStackFrame.funcId() == funcId) {
            CtrlFrame ctrlFrame = currentStackFrame.popCtrlTillCall();
            StackFrame.doControlTransfer(ctrlFrame, stack);
            currentStackFrame.reset(args);
            currentStackFrame.pushCtrl(ctrlFrame);
            return currentStackFrame;
        }
        boolean fromCallStack = !callStack.isEmpty();
        CtrlFrame ctrlFrame = fromCallStack ? callStack.pop().popCtrlTillCall() : currentStackFrame.popCtrlTillCall();
        StackFrame.doControlTransfer(ctrlFrame, stack);
        if (func != null) {
            StackFrame newFrame = new StackFrame(instance, funcId, args, type.params(), func.localTypes(), func.instructions());
            newFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
            if (fromCallStack) {
                callStack.push(newFrame);
            }
            return newFrame;
        }
        StackFrame newFrame = new StackFrame(instance, funcId, args);
        newFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
        callStack.push(newFrame);
        ImportFunction imprt = instance.imports().function(funcId);
        try {
            long[] results = imprt.handle().apply(instance, args);
            if (results != null) {
                for (long result : results) {
                    stack.push(result);
                }
            }
        }
        catch (WasmException e) {
            InterpreterMachine.THROW_REF(instance, instance.registerException(e), stack, newFrame, callStack);
        }
        if (fromCallStack) {
            callStack.push(newFrame);
        }
        return newFrame;
    }

    private static StackFrame RETURN_CALL_INDIRECT(MStack stack, Instance instance, Deque<StackFrame> callStack, Operands operands, StackFrame currentStackFrame) {
        boolean fromCallStack;
        int tableIdx = (int)operands.get(1);
        TableInstance table = instance.table(tableIdx);
        int typeId = (int)operands.get(0);
        int funcTableIdx = (int)stack.pop();
        int funcId = table.requiredRef(funcTableIdx);
        Instance refInstance = Objects.requireNonNullElse(table.instance(funcTableIdx), instance);
        FunctionType type = refInstance.type(typeId);
        FunctionType callType = refInstance.type(refInstance.functionType(funcId));
        InterpreterMachine.verifyIndirectCall(callType, type);
        Class<?> refMachine = refInstance.getMachine().getClass();
        if (!refInstance.equals(instance) && !refMachine.equals(instance.getMachine().getClass())) {
            throw new ChicoryException("Indirect tail-call to a different Machine implementation is not supported: " + refMachine.getName());
        }
        long[] args = InterpreterMachine.extractArgsForParams(stack, type.params());
        if (currentStackFrame.funcId() == funcId) {
            CtrlFrame ctrlFrame = currentStackFrame.popCtrlTillCall();
            StackFrame.doControlTransfer(ctrlFrame, stack);
            currentStackFrame.reset(args);
            currentStackFrame.pushCtrl(ctrlFrame);
            return currentStackFrame;
        }
        FunctionBody func = instance.function(funcId);
        boolean bl = fromCallStack = !callStack.isEmpty();
        if (func != null) {
            CtrlFrame ctrlFrame = fromCallStack ? callStack.pop().popCtrlTillCall() : currentStackFrame.popCtrlTillCall();
            StackFrame.doControlTransfer(ctrlFrame, stack);
            StackFrame newFrame = new StackFrame(instance, funcId, args, type.params(), func.localTypes(), func.instructions());
            newFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
            if (fromCallStack) {
                callStack.push(newFrame);
            }
            return newFrame;
        }
        StackFrame newFrame = new StackFrame(instance, funcId, args);
        newFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
        callStack.push(newFrame);
        ImportFunction imprt = instance.imports().function(funcId);
        try {
            long[] results = imprt.handle().apply(instance, args);
            if (results != null) {
                for (long result : results) {
                    stack.push(result);
                }
            }
        }
        catch (WasmException e) {
            InterpreterMachine.THROW_REF(instance, instance.registerException(e), stack, newFrame, callStack);
        }
        if (fromCallStack) {
            callStack.push(newFrame);
        }
        return newFrame;
    }

    private static StackFrame RETURN_CALL_REF(MStack stack, Instance instance, Deque<StackFrame> callStack, StackFrame currentStackFrame) {
        int funcId = (int)stack.pop();
        if (funcId == -1) {
            throw new TrapException("Trapped on call_ref on null function reference");
        }
        int typeId = instance.functionType(funcId);
        FunctionType type = instance.type(typeId);
        FunctionBody func = instance.function(funcId);
        long[] args = InterpreterMachine.extractArgsForParams(stack, type.params());
        if (currentStackFrame.funcId() == funcId) {
            CtrlFrame ctrlFrame = currentStackFrame.popCtrlTillCall();
            StackFrame.doControlTransfer(ctrlFrame, stack);
            currentStackFrame.reset(args);
            currentStackFrame.pushCtrl(ctrlFrame);
            return currentStackFrame;
        }
        StackFrame ctrlFrame = callStack.pop();
        StackFrame.doControlTransfer(ctrlFrame.popCtrlTillCall(), stack);
        StackFrame newFrame = new StackFrame(instance, funcId, args, type.params(), func.localTypes(), func.instructions());
        newFrame.pushCtrl(OpCode.CALL, 0, ValType.sizeOf(type.returns()), stack.size());
        callStack.push(newFrame);
        return newFrame;
    }

    private void CALL_INDIRECT(MStack stack, Instance instance, Deque<StackFrame> callStack, Operands operands) {
        int tableIdx = (int)operands.get(1);
        TableInstance table = instance.table(tableIdx);
        int typeId = (int)operands.get(0);
        int funcTableIdx = (int)stack.pop();
        int funcId = table.requiredRef(funcTableIdx);
        Instance refInstance = Objects.requireNonNullElse(table.instance(funcTableIdx), instance);
        FunctionType type = refInstance.type(typeId);
        long[] args = InterpreterMachine.extractArgsForParams(stack, type.params());
        if (this.useCurrentInstanceInterpreter(instance, refInstance, funcId)) {
            this.call(stack, instance, callStack, funcId, args, type, false);
        } else {
            InterpreterMachine.checkInterruption();
            FunctionType callType = refInstance.type(refInstance.functionType(funcId));
            InterpreterMachine.verifyIndirectCall(callType, type);
            long[] results = refInstance.getMachine().call(funcId, args);
            if (results != null) {
                for (long result : results) {
                    stack.push(result);
                }
            }
        }
    }

    protected boolean useCurrentInstanceInterpreter(Instance instance, Instance refInstance, int funcId) {
        return refInstance.equals(instance);
    }

    private static int numberOfParams(Instance instance, AnnotatedInstruction scope) {
        int typeId = (int)scope.operand(0);
        if (typeId == 64) {
            return 0;
        }
        if (ValType.isValid(typeId)) {
            return 0;
        }
        return ValType.sizeOf(instance.type(typeId).params());
    }

    private static int numberOfValuesToReturn(Instance instance, AnnotatedInstruction scope) {
        if (scope.opcode() == OpCode.END) {
            return 0;
        }
        int typeId = (int)scope.operand(0);
        if (typeId == 64) {
            return 0;
        }
        if (ValType.isValid(typeId)) {
            if ((long)typeId == ValType.V128.id()) {
                return 2;
            }
            return 1;
        }
        return ValType.sizeOf(instance.type(typeId).returns());
    }

    protected static StackFrame THROW_REF(Instance instance, int exceptionIdx, MStack stack, StackFrame frame, Deque<StackFrame> callStack) {
        WasmException exception = instance.exn(exceptionIdx);
        boolean found = false;
        while (!found) {
            while (frame.ctrlStackSize() > 0) {
                CtrlFrame ctrlFrame = frame.popCtrl();
                if (ctrlFrame.opCode != OpCode.TRY_TABLE) continue;
                frame.jumpTo(ctrlFrame.pc);
                AnnotatedInstruction tryInst = frame.loadCurrentInstruction();
                List<CatchOpCode.Catch> catches = tryInst.catches();
                for (int i = 0; i < catches.size() && !found; ++i) {
                    CatchOpCode.Catch currentCatch = catches.get(i);
                    boolean compatibleImport = false;
                    if (currentCatch.opcode() == CatchOpCode.CATCH || currentCatch.opcode() == CatchOpCode.CATCH_REF) {
                        TagInstance currentCatchTag = instance.tag(currentCatch.tag());
                        TagInstance exceptionTag = exception.instance().tag(exception.tagIdx());
                        if (currentCatch.tag() < instance.imports().tagCount() && currentCatchTag.type().paramsMatch(exceptionTag.type()) && currentCatchTag.type().returnsMatch(exceptionTag.type())) {
                            compatibleImport = true;
                        } else if (exceptionTag != currentCatchTag) continue;
                    }
                    switch (currentCatch.opcode()) {
                        case CATCH: {
                            if (currentCatch.tag() != exception.tagIdx() && !compatibleImport) break;
                            found = true;
                            for (long arg : exception.args()) {
                                stack.push(arg);
                            }
                            break;
                        }
                        case CATCH_REF: {
                            if (currentCatch.tag() != exception.tagIdx() && !compatibleImport) break;
                            found = true;
                            for (long arg : exception.args()) {
                                stack.push(arg);
                            }
                            stack.push(exceptionIdx);
                            break;
                        }
                        case CATCH_ALL: {
                            found = true;
                            break;
                        }
                        case CATCH_ALL_REF: {
                            found = true;
                            stack.push(exceptionIdx);
                        }
                    }
                    if (!found) continue;
                    InterpreterMachine.ctrlJump(frame, stack, currentCatch.label());
                    frame.jumpTo(currentCatch.resolvedLabel());
                    return frame;
                }
            }
            if (found) continue;
            if (callStack.isEmpty()) {
                throw exception;
            }
            frame = callStack.pop();
        }
        throw new RuntimeException("unreacheable");
    }

    private static void BLOCK(StackFrame frame, MStack stack, Instance instance, AnnotatedInstruction instruction) {
        int paramsSize = InterpreterMachine.numberOfParams(instance, instruction);
        int returnsSize = InterpreterMachine.numberOfValuesToReturn(instance, instruction);
        frame.pushCtrl(instruction.opcode(), paramsSize, returnsSize, stack.size() - paramsSize);
    }

    private static void TRY_TABLE(StackFrame frame, MStack stack, Instance instance, AnnotatedInstruction instruction, int pc) {
        int paramsSize = InterpreterMachine.numberOfParams(instance, instruction);
        int returnsSize = InterpreterMachine.numberOfValuesToReturn(instance, instruction);
        frame.pushCtrl(instruction.opcode(), paramsSize, returnsSize, stack.size() - paramsSize, pc);
    }

    private static void IF(StackFrame frame, MStack stack, Instance instance, AnnotatedInstruction instruction) {
        long predValue = stack.pop();
        int paramsSize = InterpreterMachine.numberOfParams(instance, instruction);
        int returnsSize = InterpreterMachine.numberOfValuesToReturn(instance, instruction);
        frame.pushCtrl(instruction.opcode(), paramsSize, returnsSize, stack.size() - paramsSize);
        frame.jumpTo(predValue == 0L ? instruction.labelFalse() : instruction.labelTrue());
    }

    private static void ctrlJump(StackFrame frame, MStack stack, int n) {
        CtrlFrame ctrlFrame = frame.popCtrl(n);
        frame.pushCtrl(ctrlFrame);
        if (ctrlFrame.opCode == OpCode.LOOP) {
            StackFrame.doControlTransfer(ctrlFrame, stack);
        }
    }

    private static void BR(StackFrame frame, MStack stack, AnnotatedInstruction instruction) {
        InterpreterMachine.checkInterruption();
        InterpreterMachine.ctrlJump(frame, stack, (int)instruction.operand(0));
        frame.jumpTo(instruction.labelTrue());
    }

    private static void BR_TABLE(StackFrame frame, MStack stack, AnnotatedInstruction instruction) {
        int pred = (int)stack.pop();
        int defaultIdx = instruction.operandCount() - 1;
        if (pred < 0 || pred >= defaultIdx) {
            InterpreterMachine.ctrlJump(frame, stack, (int)instruction.operand(defaultIdx));
            frame.jumpTo(instruction.labelTable().get(defaultIdx));
        } else {
            InterpreterMachine.ctrlJump(frame, stack, (int)instruction.operand(pred));
            frame.jumpTo(instruction.labelTable().get(pred));
        }
    }

    private static void BR_IF(StackFrame frame, MStack stack, AnnotatedInstruction instruction) {
        int pred = (int)stack.pop();
        if (pred == 0) {
            frame.jumpTo(instruction.labelFalse());
        } else {
            InterpreterMachine.ctrlJump(frame, stack, (int)instruction.operand(0));
            frame.jumpTo(instruction.labelTrue());
        }
    }

    private static void BR_ON_NULL(StackFrame frame, MStack stack, AnnotatedInstruction instruction) {
        int ref = (int)stack.pop();
        if (ref == -1) {
            InterpreterMachine.BR(frame, stack, instruction);
        } else {
            stack.push(ref);
        }
    }

    private static void BR_ON_NON_NULL(StackFrame frame, MStack stack, AnnotatedInstruction instruction) {
        int ref = (int)stack.pop();
        if (ref != -1) {
            stack.push(ref);
            InterpreterMachine.BR(frame, stack, instruction);
        }
    }

    protected static long[] extractArgsForParams(MStack stack, List<ValType> params) {
        if (params == null) {
            return Value.EMPTY_VALUES;
        }
        long[] args = new long[ValType.sizeOf(params)];
        for (int i = 0; i < args.length; ++i) {
            args[args.length - i - 1] = stack.pop();
        }
        return args;
    }

    private static boolean functionTypeMatch(FunctionType actual, FunctionType expected) {
        int i;
        if (actual.params().size() != expected.params().size() || actual.returns().size() != expected.returns().size()) {
            return false;
        }
        for (i = 0; i < actual.params().size(); ++i) {
            ValType expectedParam;
            ValType actualParam = actual.params().get(i);
            if (ValType.matches(actualParam, expectedParam = expected.params().get(i))) continue;
            return false;
        }
        for (i = 0; i < actual.returns().size(); ++i) {
            ValType actualReturn = actual.returns().get(i);
            ValType expectedReturn = expected.returns().get(i);
            if (ValType.matches(expectedReturn, actualReturn)) continue;
            return false;
        }
        return true;
    }

    protected static void verifyIndirectCall(FunctionType actual, FunctionType expected) throws ChicoryException {
        if (!InterpreterMachine.functionTypeMatch(actual, expected)) {
            throw new ChicoryException("indirect call type mismatch");
        }
    }

    private static void checkInterruption() {
        if (Thread.currentThread().isInterrupted()) {
            throw new ChicoryInterruptedException("Thread interrupted");
        }
    }

    @FunctionalInterface
    protected static interface Operands {
        public long get(int var1);
    }

    private static enum AtomicOp {
        ADD,
        SUB,
        AND,
        OR,
        XOR,
        XCHG;

    }
}

