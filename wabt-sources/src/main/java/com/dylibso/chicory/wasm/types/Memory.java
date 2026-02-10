/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.MemoryLimits;
import java.util.Objects;

public final class Memory {
    private final MemoryLimits limits;

    public Memory(MemoryLimits limits) {
        this.limits = Objects.requireNonNull(limits, "memoryLimits");
    }

    public MemoryLimits limits() {
        return this.limits;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Memory)) {
            return false;
        }
        Memory memory = (Memory)o;
        return Objects.equals(this.limits, memory.limits);
    }

    public int hashCode() {
        return Objects.hashCode(this.limits);
    }
}

