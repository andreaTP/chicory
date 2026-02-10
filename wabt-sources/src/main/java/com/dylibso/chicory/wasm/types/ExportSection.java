/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Export;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ExportSection
extends Section {
    private final List<Export> exports;

    private ExportSection(List<Export> exports) {
        super(7L);
        this.exports = List.copyOf(exports);
    }

    public int exportCount() {
        return this.exports.size();
    }

    public Export getExport(int idx) {
        return this.exports.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ExportSection)) {
            return false;
        }
        ExportSection that = (ExportSection)o;
        return Objects.equals(this.exports, that.exports);
    }

    public int hashCode() {
        return Objects.hashCode(this.exports);
    }

    public static final class Builder {
        private final List<Export> exports = new ArrayList<Export>();

        private Builder() {
        }

        public Builder addExport(Export export) {
            Objects.requireNonNull(export, "export");
            this.exports.add(export);
            return this;
        }

        public ExportSection build() {
            return new ExportSection(this.exports);
        }
    }
}

