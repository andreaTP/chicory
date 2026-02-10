/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportGlobal;
import com.dylibso.chicory.runtime.ImportMemory;
import com.dylibso.chicory.runtime.ImportTable;
import com.dylibso.chicory.runtime.ImportTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class ImportValues {
    private static final ImportFunction[] NO_EXTERNAL_FUNCTIONS = new ImportFunction[0];
    private static final ImportGlobal[] NO_EXTERNAL_GLOBALS = new ImportGlobal[0];
    private static final ImportMemory[] NO_EXTERNAL_MEMORIES = new ImportMemory[0];
    private static final ImportTable[] NO_EXTERNAL_TABLES = new ImportTable[0];
    private static final ImportTag[] NO_EXTERNAL_TAGS = new ImportTag[0];
    private final ImportFunction[] functions;
    private final ImportGlobal[] globals;
    private final ImportMemory[] memories;
    private final ImportTable[] tables;
    private final ImportTag[] tags;

    private ImportValues(ImportFunction[] functions, ImportGlobal[] globals, ImportMemory[] memories, ImportTable[] tables, ImportTag[] tags) {
        this.functions = (ImportFunction[])functions.clone();
        this.globals = (ImportGlobal[])globals.clone();
        this.memories = (ImportMemory[])memories.clone();
        this.tables = (ImportTable[])tables.clone();
        this.tags = (ImportTag[])tags.clone();
    }

    public ImportFunction[] functions() {
        return (ImportFunction[])this.functions.clone();
    }

    public int functionCount() {
        return this.functions.length;
    }

    public ImportFunction function(int idx) {
        return this.functions[idx];
    }

    public ImportGlobal[] globals() {
        return this.globals;
    }

    public int globalCount() {
        return this.globals.length;
    }

    public ImportGlobal global(int idx) {
        return this.globals[idx];
    }

    public ImportMemory[] memories() {
        return this.memories;
    }

    public int memoryCount() {
        return this.memories.length;
    }

    public ImportMemory memory(int idx) {
        return this.memories[idx];
    }

    public ImportTable[] tables() {
        return this.tables;
    }

    public int tableCount() {
        return this.tables.length;
    }

    public ImportTable table(int idx) {
        return this.tables[idx];
    }

    public ImportTag[] tags() {
        return this.tags;
    }

    public int tagCount() {
        return this.tags.length;
    }

    public ImportTag tag(int idx) {
        return this.tags[idx];
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ImportValues empty() {
        return new Builder().build();
    }

    public static final class Builder {
        private Collection<ImportFunction> functions;
        private Collection<ImportGlobal> globals;
        private Collection<ImportMemory> memories;
        private Collection<ImportTable> tables;
        private Collection<ImportTag> tags;

        Builder() {
        }

        public Builder withFunctions(Collection<ImportFunction> functions) {
            this.functions = functions;
            return this;
        }

        public Builder addFunction(ImportFunction ... function) {
            if (this.functions == null) {
                this.functions = new ArrayList<ImportFunction>();
            }
            Collections.addAll(this.functions, function);
            return this;
        }

        public Builder withGlobals(Collection<ImportGlobal> globals) {
            this.globals = globals;
            return this;
        }

        public Builder addGlobal(ImportGlobal ... global) {
            if (this.globals == null) {
                this.globals = new ArrayList<ImportGlobal>();
            }
            Collections.addAll(this.globals, global);
            return this;
        }

        public Builder withMemories(Collection<ImportMemory> memories) {
            this.memories = memories;
            return this;
        }

        public Builder addMemory(ImportMemory ... memory) {
            if (this.memories == null) {
                this.memories = new ArrayList<ImportMemory>();
            }
            Collections.addAll(this.memories, memory);
            return this;
        }

        public Builder withTables(Collection<ImportTable> tables) {
            this.tables = tables;
            return this;
        }

        public Builder addTable(ImportTable ... table) {
            if (this.tables == null) {
                this.tables = new ArrayList<ImportTable>();
            }
            Collections.addAll(this.tables, table);
            return this;
        }

        public Builder withTags(Collection<ImportTag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder addTag(ImportTag ... tag) {
            if (this.tags == null) {
                this.tags = new ArrayList<ImportTag>();
            }
            Collections.addAll(this.tags, tag);
            return this;
        }

        public ImportValues build() {
            ImportValues importValues = new ImportValues(this.functions == null ? NO_EXTERNAL_FUNCTIONS : this.functions.toArray(NO_EXTERNAL_FUNCTIONS), this.globals == null ? NO_EXTERNAL_GLOBALS : this.globals.toArray(NO_EXTERNAL_GLOBALS), this.memories == null ? NO_EXTERNAL_MEMORIES : this.memories.toArray(NO_EXTERNAL_MEMORIES), this.tables == null ? NO_EXTERNAL_TABLES : this.tables.toArray(NO_EXTERNAL_TABLES), this.tags == null ? NO_EXTERNAL_TAGS : this.tags.toArray(NO_EXTERNAL_TAGS));
            return importValues;
        }
    }
}

