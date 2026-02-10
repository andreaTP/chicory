/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;

public final class DataCountSection
extends Section {
    private final int dataCount;

    private DataCountSection(int dataCount) {
        super(12L);
        this.dataCount = dataCount;
    }

    public int dataCount() {
        return this.dataCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DataCountSection)) {
            return false;
        }
        DataCountSection that = (DataCountSection)o;
        return this.dataCount == that.dataCount;
    }

    public int hashCode() {
        return Integer.hashCode(this.dataCount);
    }

    public static class Builder {
        private int dataCount;

        public Builder withDataCount(int dataCount) {
            this.dataCount = dataCount;
            return this;
        }

        public DataCountSection build() {
            return new DataCountSection(this.dataCount);
        }
    }
}

