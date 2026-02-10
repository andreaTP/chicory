/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.Validator;
import com.dylibso.chicory.wasm.types.CodeSection;
import com.dylibso.chicory.wasm.types.CustomSection;
import com.dylibso.chicory.wasm.types.DataCountSection;
import com.dylibso.chicory.wasm.types.DataSection;
import com.dylibso.chicory.wasm.types.ElementSection;
import com.dylibso.chicory.wasm.types.ExportSection;
import com.dylibso.chicory.wasm.types.FunctionSection;
import com.dylibso.chicory.wasm.types.GlobalSection;
import com.dylibso.chicory.wasm.types.ImportSection;
import com.dylibso.chicory.wasm.types.MemorySection;
import com.dylibso.chicory.wasm.types.NameCustomSection;
import com.dylibso.chicory.wasm.types.StartSection;
import com.dylibso.chicory.wasm.types.TableSection;
import com.dylibso.chicory.wasm.types.TagSection;
import com.dylibso.chicory.wasm.types.TypeSection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class WasmModule {
    private final Map<String, CustomSection> customSections;
    private final TypeSection typeSection;
    private final ImportSection importSection;
    private final FunctionSection functionSection;
    private final TableSection tableSection;
    private final Optional<MemorySection> memorySection;
    private final GlobalSection globalSection;
    private final ExportSection exportSection;
    private final Optional<StartSection> startSection;
    private final ElementSection elementSection;
    private final CodeSection codeSection;
    private final DataSection dataSection;
    private final Optional<DataCountSection> dataCountSection;
    private final Optional<TagSection> tagSection;
    private final List<Integer> ignoredSections;
    private final String digest;

    private WasmModule(TypeSection typeSection, ImportSection importSection, FunctionSection functionSection, TableSection tableSection, Optional<MemorySection> memorySection, GlobalSection globalSection, ExportSection exportSection, Optional<StartSection> startSection, ElementSection elementSection, CodeSection codeSection, DataSection dataSection, Optional<DataCountSection> dataCountSection, Optional<TagSection> tagSection, Map<String, CustomSection> customSections, List<Integer> ignoredSections, String digest) {
        this.typeSection = Objects.requireNonNull(typeSection);
        this.importSection = Objects.requireNonNull(importSection);
        this.functionSection = Objects.requireNonNull(functionSection);
        this.tableSection = Objects.requireNonNull(tableSection);
        this.memorySection = memorySection;
        this.globalSection = Objects.requireNonNull(globalSection);
        this.exportSection = Objects.requireNonNull(exportSection);
        this.startSection = startSection;
        this.elementSection = Objects.requireNonNull(elementSection);
        this.codeSection = Objects.requireNonNull(codeSection);
        this.dataSection = Objects.requireNonNull(dataSection);
        this.dataCountSection = dataCountSection;
        this.tagSection = tagSection;
        this.customSections = Map.copyOf(customSections);
        this.ignoredSections = List.copyOf(ignoredSections);
        this.digest = digest;
    }

    public TypeSection typeSection() {
        return this.typeSection;
    }

    public FunctionSection functionSection() {
        return this.functionSection;
    }

    public ExportSection exportSection() {
        return this.exportSection;
    }

    public Optional<StartSection> startSection() {
        return this.startSection;
    }

    public ImportSection importSection() {
        return this.importSection;
    }

    public CodeSection codeSection() {
        return this.codeSection;
    }

    public DataSection dataSection() {
        return this.dataSection;
    }

    public Optional<DataCountSection> dataCountSection() {
        return this.dataCountSection;
    }

    public Optional<MemorySection> memorySection() {
        return this.memorySection;
    }

    public GlobalSection globalSection() {
        return this.globalSection;
    }

    public TableSection tableSection() {
        return this.tableSection;
    }

    public String digest() {
        return this.digest;
    }

    public List<CustomSection> customSections() {
        return new ArrayList<CustomSection>(this.customSections.values());
    }

    public CustomSection customSection(String name) {
        return this.customSections.get(name);
    }

    public NameCustomSection nameSection() {
        return (NameCustomSection)this.customSections.get("name");
    }

    public ElementSection elementSection() {
        return this.elementSection;
    }

    public Optional<TagSection> tagSection() {
        return this.tagSection;
    }

    public List<Integer> ignoredSections() {
        return this.ignoredSections;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof WasmModule)) {
            return false;
        }
        WasmModule that = (WasmModule)o;
        return Objects.equals(this.typeSection, that.typeSection) && Objects.equals(this.importSection, that.importSection) && Objects.equals(this.functionSection, that.functionSection) && Objects.equals(this.tableSection, that.tableSection) && Objects.equals(this.memorySection, that.memorySection) && Objects.equals(this.globalSection, that.globalSection) && Objects.equals(this.exportSection, that.exportSection) && Objects.equals(this.startSection, that.startSection) && Objects.equals(this.elementSection, that.elementSection) && Objects.equals(this.codeSection, that.codeSection) && Objects.equals(this.dataSection, that.dataSection) && Objects.equals(this.dataCountSection, that.dataCountSection) && Objects.equals(this.tagSection, that.tagSection) && Objects.equals(this.ignoredSections, that.ignoredSections);
    }

    public int hashCode() {
        return Objects.hash(this.typeSection, this.importSection, this.functionSection, this.tableSection, this.memorySection, this.globalSection, this.exportSection, this.startSection, this.elementSection, this.codeSection, this.dataSection, this.dataCountSection, this.tagSection);
    }

    public static final class Builder {
        private TypeSection typeSection = TypeSection.builder().build();
        private ImportSection importSection = ImportSection.builder().build();
        private FunctionSection functionSection = FunctionSection.builder().build();
        private TableSection tableSection = TableSection.builder().build();
        private Optional<MemorySection> memorySection = Optional.empty();
        private GlobalSection globalSection = GlobalSection.builder().build();
        private ExportSection exportSection = ExportSection.builder().build();
        private Optional<StartSection> startSection = Optional.empty();
        private ElementSection elementSection = ElementSection.builder().build();
        private CodeSection codeSection = CodeSection.builder().build();
        private DataSection dataSection = DataSection.builder().build();
        private Optional<DataCountSection> dataCountSection = Optional.empty();
        private Optional<TagSection> tagSection = Optional.empty();
        private final Map<String, CustomSection> customSections = new HashMap<String, CustomSection>();
        private final List<Integer> ignoredSections = new ArrayList<Integer>();
        private boolean validate = true;
        private String digest;

        private Builder() {
        }

        public Builder setTypeSection(TypeSection ts) {
            this.typeSection = Objects.requireNonNull(ts);
            return this;
        }

        public Builder setImportSection(ImportSection is) {
            this.importSection = Objects.requireNonNull(is);
            return this;
        }

        public Builder setFunctionSection(FunctionSection fs) {
            this.functionSection = Objects.requireNonNull(fs);
            return this;
        }

        public Builder setTableSection(TableSection ts) {
            this.tableSection = Objects.requireNonNull(ts);
            return this;
        }

        public Builder setMemorySection(MemorySection ms) {
            this.memorySection = Optional.ofNullable(ms);
            return this;
        }

        public Builder setGlobalSection(GlobalSection gs) {
            this.globalSection = Objects.requireNonNull(gs);
            return this;
        }

        public Builder setExportSection(ExportSection es) {
            this.exportSection = Objects.requireNonNull(es);
            return this;
        }

        public Builder setStartSection(StartSection ss) {
            this.startSection = Optional.ofNullable(ss);
            return this;
        }

        public Builder setElementSection(ElementSection es) {
            this.elementSection = Objects.requireNonNull(es);
            return this;
        }

        public Builder setCodeSection(CodeSection cs) {
            this.codeSection = Objects.requireNonNull(cs);
            return this;
        }

        public Builder setDataSection(DataSection ds) {
            this.dataSection = Objects.requireNonNull(ds);
            return this;
        }

        public Builder setDataCountSection(DataCountSection dcs) {
            this.dataCountSection = Optional.ofNullable(dcs);
            return this;
        }

        public Builder setTagSection(TagSection ts) {
            this.tagSection = Optional.ofNullable(ts);
            return this;
        }

        public Builder addCustomSection(String name, CustomSection cs) {
            Objects.requireNonNull(name);
            Objects.requireNonNull(cs);
            this.customSections.put(name, cs);
            return this;
        }

        public Builder addIgnoredSection(int id) {
            this.ignoredSections.add(id);
            return this;
        }

        public Builder withValidation(boolean validate) {
            this.validate = validate;
            return this;
        }

        public Builder withDigest(String digest) {
            this.digest = digest;
            return this;
        }

        public WasmModule build() {
            WasmModule module = new WasmModule(this.typeSection, this.importSection, this.functionSection, this.tableSection, this.memorySection, this.globalSection, this.exportSection, this.startSection, this.elementSection, this.codeSection, this.dataSection, this.dataCountSection, this.tagSection, this.customSections, this.ignoredSections, this.digest);
            Validator validator = new Validator(module);
            validator.validateModule();
            if (this.validate) {
                validator.validateTypes();
                validator.validateFunctions();
                validator.validateGlobals();
                validator.validateElements();
                validator.validateData();
                validator.validateTags();
                validator.validateTables();
            }
            return module;
        }
    }
}

