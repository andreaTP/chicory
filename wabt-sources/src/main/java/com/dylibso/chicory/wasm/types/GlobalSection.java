/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Global;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GlobalSection
extends Section {
    private final List<Global> globals;

    private GlobalSection(List<Global> globals) {
        super(6L);
        this.globals = List.copyOf(globals);
    }

    public Global[] globals() {
        return this.globals.toArray(new Global[0]);
    }

    public int globalCount() {
        return this.globals.size();
    }

    public Global getGlobal(int idx) {
        return this.globals.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof GlobalSection)) {
            return false;
        }
        GlobalSection that = (GlobalSection)o;
        return Objects.equals(this.globals, that.globals);
    }

    public int hashCode() {
        return Objects.hashCode(this.globals);
    }

    public static final class Builder {
        private final List<Global> globals = new ArrayList<Global>();

        private Builder() {
        }

        public Builder addGlobal(Global global) {
            Objects.requireNonNull(global, "global");
            this.globals.add(global);
            return this;
        }

        public GlobalSection build() {
            return new GlobalSection(this.globals);
        }
    }
}

