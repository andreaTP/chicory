/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.MalformedException;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

final class ConstantEvaluators {
    private ConstantEvaluators() {
    }

    public static long[] computeConstantValue(Instance instance, Instruction[] expr) {
        return ConstantEvaluators.computeConstantValue(instance, Arrays.asList(expr));
    }

    public static long[] computeConstantValue(Instance instance, List<Instruction> expr) {
        ArrayDeque<long[]> stack = new ArrayDeque<long[]>();
        block13: for (Instruction instruction : expr) {
            switch (instruction.opcode()) {
                case I32_ADD: {
                    int x = (int)((long[])stack.pop())[0];
                    int y = (int)((long[])stack.pop())[0];
                    stack.push(new long[]{x + y});
                    continue block13;
                }
                case I32_SUB: {
                    int x = (int)((long[])stack.pop())[0];
                    int y = (int)((long[])stack.pop())[0];
                    stack.push(new long[]{y - x});
                    continue block13;
                }
                case I32_MUL: {
                    int x = (int)((long[])stack.pop())[0];
                    int y = (int)((long[])stack.pop())[0];
                    int res = x * y;
                    stack.push(new long[]{res});
                    continue block13;
                }
                case I64_ADD: {
                    long x = ((long[])stack.pop())[0];
                    long y = ((long[])stack.pop())[0];
                    stack.push(new long[]{x + y});
                    continue block13;
                }
                case I64_SUB: {
                    long x = ((long[])stack.pop())[0];
                    long y = ((long[])stack.pop())[0];
                    stack.push(new long[]{y - x});
                    continue block13;
                }
                case I64_MUL: {
                    long x = ((long[])stack.pop())[0];
                    long y = ((long[])stack.pop())[0];
                    stack.push(new long[]{x * y});
                    continue block13;
                }
                case V128_CONST: {
                    stack.push(new long[]{instruction.operand(0), instruction.operand(1)});
                    continue block13;
                }
                case F32_CONST: 
                case F64_CONST: 
                case I32_CONST: 
                case I64_CONST: 
                case REF_FUNC: {
                    stack.push(new long[]{instruction.operand(0)});
                    continue block13;
                }
                case REF_NULL: {
                    stack.push(new long[]{-1L});
                    continue block13;
                }
                case GLOBAL_GET: {
                    int idx = (int)instruction.operand(0);
                    if (instance.global(idx).getType().equals(ValType.V128)) {
                        stack.push(new long[]{instance.global(idx).getValueLow(), instance.global(idx).getValueHigh()});
                        continue block13;
                    }
                    stack.push(new long[]{instance.global(idx).getValueLow()});
                    continue block13;
                }
                case END: {
                    continue block13;
                }
            }
            throw new MalformedException("Invalid instruction in constant value" + String.valueOf(instruction));
        }
        return (long[])stack.pop();
    }

    public static Instance computeConstantInstance(Instance instance, List<Instruction> expr) {
        for (Instruction instruction : expr) {
            if (instruction.opcode() != OpCode.GLOBAL_GET) continue;
            return instance.global((int)instruction.operand(0)).getInstance();
        }
        return instance;
    }
}

