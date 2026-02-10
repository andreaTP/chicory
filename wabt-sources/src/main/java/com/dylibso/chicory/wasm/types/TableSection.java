/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;
import com.dylibso.chicory.wasm.types.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TableSection
extends Section {
    private final List<Table> tables;

    private TableSection(List<Table> tables) {
        super(4L);
        this.tables = List.copyOf(tables);
    }

    public int tableCount() {
        return this.tables.size();
    }

    public Table getTable(int idx) {
        return this.tables.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof TableSection)) {
            return false;
        }
        TableSection that = (TableSection)o;
        return Objects.equals(this.tables, that.tables);
    }

    public int hashCode() {
        return Objects.hashCode(this.tables);
    }

    public static final class Builder {
        private final List<Table> tables = new ArrayList<Table>();

        private Builder() {
        }

        public Builder addTable(Table table) {
            Objects.requireNonNull(table, "table");
            this.tables.add(table);
            return this;
        }

        public TableSection build() {
            return new TableSection(this.tables);
        }
    }
}

