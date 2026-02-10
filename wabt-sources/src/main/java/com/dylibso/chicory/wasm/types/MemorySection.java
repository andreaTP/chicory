/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Memory;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MemorySection
extends Section {
    private final List<Memory> memories;

    private MemorySection(List<Memory> memories) {
        super(5L);
        this.memories = List.copyOf(memories);
    }

    public int memoryCount() {
        return this.memories.size();
    }

    public Memory getMemory(int idx) {
        return this.memories.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof MemorySection)) {
            return false;
        }
        MemorySection that = (MemorySection)o;
        return Objects.equals(this.memories, that.memories);
    }

    public int hashCode() {
        return Objects.hashCode(this.memories);
    }

    public static final class Builder {
        private final List<Memory> memories = new ArrayList<Memory>();

        private Builder() {
        }

        public Builder addMemory(Memory memory) {
            Objects.requireNonNull(memory, "memory");
            this.memories.add(memory);
            return this;
        }

        public MemorySection build() {
            return new MemorySection(this.memories);
        }
    }
}

