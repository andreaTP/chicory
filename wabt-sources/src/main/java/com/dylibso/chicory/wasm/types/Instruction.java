/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.OpCode;
import java.util.Arrays;
import java.util.Objects;

public class Instruction {
    public static final long[] EMPTY_OPERANDS = new long[0];
    private final int address;
    private final OpCode opcode;
    private final long[] operands;

    public Instruction(int address, OpCode opcode, long[] operands) {
        this.address = address;
        this.opcode = opcode;
        this.operands = operands.length == 0 ? EMPTY_OPERANDS : (long[])operands.clone();
    }

    public int address() {
        return this.address;
    }

    public OpCode opcode() {
        return this.opcode;
    }

    public long[] operands() {
        return (long[])this.operands.clone();
    }

    public int operandCount() {
        return this.operands.length;
    }

    public long operand(int index) {
        return this.operands[index];
    }

    public void setOperand(int index, long value) {
        this.operands[index] = value;
    }

    public String toString() {
        String result = String.format("0x%08X", this.address) + ": ";
        if (this.operands.length > 0) {
            return result + String.valueOf((Object)this.opcode) + " " + Arrays.toString(this.operands);
        }
        return result + this.opcode.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Instruction)) {
            return false;
        }
        Instruction that = (Instruction)o;
        return this.address == that.address && this.opcode == that.opcode && Objects.deepEquals(this.operands, that.operands);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.address, this.opcode, Arrays.hashCode(this.operands)});
    }
}

