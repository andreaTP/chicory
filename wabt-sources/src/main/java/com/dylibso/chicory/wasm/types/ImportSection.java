/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ImportSection
extends Section {
    private final List<Import> imports;

    private ImportSection(List<Import> imports) {
        super(2L);
        this.imports = List.copyOf(imports);
    }

    public int importCount() {
        return this.imports.size();
    }

    public Import getImport(int idx) {
        return this.imports.get(idx);
    }

    public Stream<Import> stream() {
        return this.imports.stream();
    }

    public int count(ExternalType type) {
        return (int)this.imports.stream().filter(i -> i.importType() == type).count();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ImportSection)) {
            return false;
        }
        ImportSection that = (ImportSection)o;
        return Objects.equals(this.imports, that.imports);
    }

    public int hashCode() {
        return Objects.hashCode(this.imports);
    }

    public static final class Builder {
        private final List<Import> imports = new ArrayList<Import>();

        private Builder() {
        }

        public Builder addImport(Import import_) {
            Objects.requireNonNull(import_, "import_");
            this.imports.add(import_);
            return this;
        }

        public ImportSection build() {
            return new ImportSection(this.imports);
        }
    }
}

