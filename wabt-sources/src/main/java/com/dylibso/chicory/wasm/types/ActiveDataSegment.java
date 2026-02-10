/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.Instruction;
import java.util.List;
import java.util.Objects;

public final class ActiveDataSegment
extends DataSegment {
    private final long idx;
    private final List<Instruction> offsetInstructions;

    public ActiveDataSegment(long idx, List<Instruction> offsetInstructions, byte[] data) {
        super(data);
        this.idx = idx;
        this.offsetInstructions = List.copyOf(offsetInstructions);
    }

    public long index() {
        return this.idx;
    }

    public List<Instruction> offsetInstructions() {
        return this.offsetInstructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ActiveDataSegment)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ActiveDataSegment that = (ActiveDataSegment)o;
        return this.idx == that.idx && Objects.equals(this.offsetInstructions, that.offsetInstructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.idx, this.offsetInstructions);
    }
}

