/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;

public final class StartSection
extends Section {
    private final long startIndex;

    private StartSection(long startIndex) {
        super(8L);
        this.startIndex = startIndex;
    }

    public long startIndex() {
        return this.startIndex;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof StartSection)) {
            return false;
        }
        StartSection that = (StartSection)o;
        return this.startIndex == that.startIndex;
    }

    public int hashCode() {
        return Long.hashCode(this.startIndex);
    }

    public static final class Builder {
        private long startIndex;

        private Builder() {
        }

        public Builder setStartIndex(long startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public StartSection build() {
            return new StartSection(this.startIndex);
        }
    }
}

