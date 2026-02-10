/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Section;
import com.dylibso.chicory.wasm.types.TypeSection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FunctionSection
extends Section {
    private final List<Integer> typeIndices;

    private FunctionSection(List<Integer> typeIndices) {
        super(3L);
        this.typeIndices = List.copyOf(typeIndices);
    }

    public int getFunctionType(int idx) {
        return this.typeIndices.get(idx);
    }

    public FunctionType getFunctionType(int idx, TypeSection typeSection) {
        return typeSection.getType(this.getFunctionType(idx));
    }

    public int functionCount() {
        return this.typeIndices.size();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof FunctionSection)) {
            return false;
        }
        FunctionSection that = (FunctionSection)o;
        return Objects.equals(this.typeIndices, that.typeIndices);
    }

    public int hashCode() {
        return Objects.hashCode(this.typeIndices);
    }

    public static final class Builder {
        private final List<Integer> typeIndices = new ArrayList<Integer>();

        private Builder() {
        }

        public Builder addFunctionType(int typeIndex) {
            this.typeIndices.add(typeIndex);
            return this;
        }

        public FunctionSection build() {
            return new FunctionSection(this.typeIndices);
        }
    }
}

