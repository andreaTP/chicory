/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.GlobalInstance;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportGlobal;
import com.dylibso.chicory.runtime.ImportMemory;
import com.dylibso.chicory.runtime.ImportTable;
import com.dylibso.chicory.runtime.ImportTag;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.Export;
import com.dylibso.chicory.wasm.types.ExportSection;
import com.dylibso.chicory.wasm.types.FunctionType;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;

public class Store {
    final LinkedHashMap<QualifiedName, ImportFunction> functions = new LinkedHashMap();
    final LinkedHashMap<QualifiedName, ImportGlobal> globals = new LinkedHashMap();
    final LinkedHashMap<QualifiedName, ImportMemory> memories = new LinkedHashMap();
    final LinkedHashMap<QualifiedName, ImportTable> tables = new LinkedHashMap();
    final LinkedHashMap<QualifiedName, ImportTag> tags = new LinkedHashMap();

    public Store addFunction(ImportFunction ... function) {
        for (ImportFunction f : function) {
            this.functions.put(new QualifiedName(f.module(), f.name()), f);
        }
        return this;
    }

    public Store addGlobal(ImportGlobal ... global) {
        for (ImportGlobal g : global) {
            this.globals.put(new QualifiedName(g.module(), g.name()), g);
        }
        return this;
    }

    public Store addMemory(ImportMemory ... memory) {
        for (ImportMemory m : memory) {
            this.memories.put(new QualifiedName(m.module(), m.name()), m);
        }
        return this;
    }

    public Store addTable(ImportTable ... table) {
        for (ImportTable t : table) {
            this.tables.put(new QualifiedName(t.module(), t.name()), t);
        }
        return this;
    }

    public Store addTag(ImportTag ... tag) {
        for (ImportTag t : tag) {
            this.tags.put(new QualifiedName(t.module(), t.name()), t);
        }
        return this;
    }

    public Store addImportValues(ImportValues importValues) {
        return this.addGlobal(importValues.globals()).addFunction(importValues.functions()).addMemory(importValues.memories()).addTable(importValues.tables()).addTag(importValues.tags());
    }

    public ImportValues toImportValues() {
        return ImportValues.builder().withFunctions(this.functions.values()).withGlobals(this.globals.values()).withMemories(this.memories.values()).withTables(this.tables.values()).withTags(this.tags.values()).build();
    }

    public Store register(String name, Instance instance) {
        ExportSection exportSection = instance.module().exportSection();
        block7: for (int i = 0; i < exportSection.exportCount(); ++i) {
            Export export = exportSection.getExport(i);
            String exportName = export.name();
            switch (export.exportType()) {
                case FUNCTION: {
                    ExportFunction f = instance.export(exportName);
                    FunctionType ftype = instance.exportType(exportName);
                    this.addFunction(new ImportFunction(name, exportName, ftype, (inst, args) -> f.apply(args)));
                    continue block7;
                }
                case TABLE: {
                    this.addTable(new ImportTable(name, exportName, instance.table(export.index())));
                    continue block7;
                }
                case MEMORY: {
                    this.addMemory(new ImportMemory(name, exportName, instance.memory()));
                    continue block7;
                }
                case GLOBAL: {
                    GlobalInstance g = instance.global(export.index());
                    this.addGlobal(new ImportGlobal(name, exportName, g));
                    continue block7;
                }
                case TAG: {
                    this.addTag(new ImportTag(name, exportName, instance.tag(export.index())));
                }
            }
        }
        return this;
    }

    public Instance instantiate(String name, WasmModule m) {
        return this.instantiate(name, (ImportValues imports) -> Instance.builder(m).withImportValues((ImportValues)imports).build());
    }

    public Instance instantiate(String name, Function<ImportValues, Instance> instanceFactory) {
        ImportValues importValues = this.toImportValues();
        Instance instance = instanceFactory.apply(importValues);
        this.register(name, instance);
        return instance;
    }

    static class QualifiedName {
        private final String module;
        private final String name;

        public QualifiedName(String module, String name) {
            this.module = module;
            this.name = name;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof QualifiedName)) {
                return false;
            }
            QualifiedName qualifiedName = (QualifiedName)o;
            return Objects.equals(this.module, qualifiedName.module) && Objects.equals(this.name, qualifiedName.name);
        }

        public int hashCode() {
            return Objects.hash(this.module, this.name);
        }
    }
}

