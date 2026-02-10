/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ByteBufferMemory;
import com.dylibso.chicory.runtime.ConstantEvaluators;
import com.dylibso.chicory.runtime.ExecutionListener;
import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.GlobalInstance;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportGlobal;
import com.dylibso.chicory.runtime.ImportMemory;
import com.dylibso.chicory.runtime.ImportTable;
import com.dylibso.chicory.runtime.ImportTag;
import com.dylibso.chicory.runtime.ImportValue;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.InterpreterMachine;
import com.dylibso.chicory.runtime.MStack;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.TagInstance;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.runtime.WasmException;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.UninstantiableException;
import com.dylibso.chicory.wasm.UnlinkableException;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.ActiveElement;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.Export;
import com.dylibso.chicory.wasm.types.ExportSection;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.FunctionImport;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Global;
import com.dylibso.chicory.wasm.types.GlobalImport;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.MemoryImport;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.MemorySection;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableImport;
import com.dylibso.chicory.wasm.types.TagImport;
import com.dylibso.chicory.wasm.types.TagSection;
import com.dylibso.chicory.wasm.types.TagType;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Instance {
    public static final String START_FUNCTION_NAME = "_start";
    private final WasmModule module;
    private final Machine machine;
    private final FunctionBody[] functions;
    private final Memory memory;
    private final DataSegment[] dataSegments;
    private final Global[] globalInitializers;
    private final GlobalInstance[] globals;
    private final FunctionType[] types;
    private final int[] functionTypes;
    private final ImportValues imports;
    private final TableInstance[] tables;
    private final Element[] elements;
    private final TagInstance[] tags;
    private final Map<String, Export> exports;
    private final ExecutionListener listener;
    private final Exports fluentExports;
    private final Map<Integer, WasmException> exnRefs;

    Instance(WasmModule module, Global[] globalInitializers, Memory memory, DataSegment[] dataSegments, FunctionBody[] functions, FunctionType[] types, int[] functionTypes, ImportValues imports, Table[] tables, Element[] elements, TagType[] tags, Map<String, Export> exports, Function<Instance, Machine> machineFactory, boolean initialize, boolean start, ExecutionListener listener) {
        int i;
        this.module = module;
        this.globalInitializers = (Global[])globalInitializers.clone();
        this.globals = new GlobalInstance[globalInitializers.length];
        this.memory = memory;
        this.dataSegments = dataSegments;
        this.functions = (FunctionBody[])functions.clone();
        this.types = (FunctionType[])types.clone();
        this.functionTypes = (int[])functionTypes.clone();
        this.imports = imports;
        this.machine = machineFactory.apply(this);
        this.tables = new TableInstance[tables.length];
        this.elements = (Element[])elements.clone();
        this.tags = tags == null ? new TagInstance[]{} : new TagInstance[tags.length];
        for (i = 0; i < this.tags.length; ++i) {
            this.tags[i] = new TagInstance(tags[i]);
            this.tags[i].setType(types[tags[i].typeIdx()]);
        }
        this.exports = exports;
        this.listener = listener;
        this.fluentExports = new Exports(this);
        this.exnRefs = new HashMap<Integer, WasmException>();
        for (i = 0; i < tables.length; ++i) {
            int initValue = (int)ConstantEvaluators.computeConstantValue(this, tables[i].initialize())[0];
            this.tables[i] = new TableInstance(tables[i], initValue);
        }
        if (initialize) {
            this.initialize(start);
        }
    }

    public Instance initialize(boolean start) {
        Export startFunction;
        for (Element el : this.elements) {
            if (!(el instanceof ActiveElement)) continue;
            ActiveElement ae = (ActiveElement)el;
            TableInstance table = this.table(ae.tableIndex());
            int offset = (int)ConstantEvaluators.computeConstantValue(this, ae.offset())[0];
            List<List<Instruction>> initializers = ae.initializers();
            if ((long)offset > table.limits().min() || offset + initializers.size() - 1 >= table.size()) {
                throw new UninstantiableException("out of bounds table access");
            }
            for (int i = 0; i < initializers.size(); ++i) {
                List<Instruction> init = initializers.get(i);
                int index = offset + i;
                long[] value = ConstantEvaluators.computeConstantValue(this, init);
                Instance inst = ConstantEvaluators.computeConstantInstance(this, init);
                assert (ae.type().isReference());
                table.setRef(index, (int)value[0], inst);
            }
        }
        for (int i = 0; i < this.globalInitializers.length; ++i) {
            Global g = this.globalInitializers[i];
            long[] values = ConstantEvaluators.computeConstantValue(this, g.initInstructions());
            this.globals[i] = new GlobalInstance(values[0], values.length > 1 ? values[1] : 0L, g.valueType(), g.mutabilityType());
            this.globals[i].setInstance(this);
        }
        if (this.memory != null && this.imports.memories().length == 0) {
            this.memory.zero();
            this.memory.initialize(this, this.dataSegments);
        } else if (this.imports.memories().length > 0) {
            this.imports.memories()[0].memory().initialize(this, this.dataSegments);
        } else if (Arrays.stream(this.dataSegments).anyMatch(ds -> ds instanceof ActiveDataSegment)) {
            for (DataSegment ds2 : this.dataSegments) {
                if (!(ds2 instanceof ActiveDataSegment)) continue;
                ActiveDataSegment memory = (ActiveDataSegment)ds2;
                throw new InvalidException("unknown memory " + memory.index());
            }
            throw new InvalidException("unknown memory");
        }
        if (this.module.startSection().isPresent()) {
            try {
                this.machine.call((int)this.module.startSection().get().startIndex(), new long[0]);
            }
            catch (TrapException e) {
                throw new UninstantiableException(e.getMessage(), e);
            }
        }
        if ((startFunction = this.exports.get(START_FUNCTION_NAME)) != null && start) {
            this.export(START_FUNCTION_NAME).apply(new long[0]);
        }
        return this;
    }

    public FunctionType exportType(String name) {
        return this.type(this.functionType(this.exports.get(name).index()));
    }

    public Exports exports() {
        return this.fluentExports;
    }

    public ExportFunction export(String name) {
        return this.fluentExports.function(name);
    }

    public FunctionBody function(long idx) {
        if (idx < 0L || idx >= (long)(this.functions.length + this.imports.functionCount())) {
            throw new InvalidException("unknown function " + idx);
        }
        if (idx < (long)this.imports.functionCount()) {
            return null;
        }
        return this.functions[(int)idx - this.imports.functionCount()];
    }

    public int functionCount() {
        return this.imports.functionCount() + this.functions.length;
    }

    public Memory memory() {
        return this.memory;
    }

    public GlobalInstance global(int idx) {
        if (idx < this.imports.globalCount()) {
            return this.imports.global(idx).instance();
        }
        int i = idx - this.imports.globalCount();
        if (i < 0 || i >= this.globals.length) {
            throw new InvalidException("unknown global " + idx);
        }
        return this.globals[idx - this.imports.globalCount()];
    }

    public FunctionType type(int idx) {
        if (idx >= this.types.length) {
            throw new InvalidException("unknown type " + idx);
        }
        return this.types[idx];
    }

    public int functionType(int idx) {
        if (idx >= this.functionTypes.length) {
            throw new InvalidException("unknown function " + idx);
        }
        return this.functionTypes[idx];
    }

    public ImportValues imports() {
        return this.imports;
    }

    public WasmModule module() {
        return this.module;
    }

    public TableInstance table(int idx) {
        if (idx < 0 || idx >= this.tables.length + this.imports.tableCount()) {
            throw new InvalidException("unknown table " + idx);
        }
        if (idx < this.imports.tableCount()) {
            return this.imports.table(idx).table();
        }
        return this.tables[idx - this.imports.tableCount()];
    }

    public Element element(int idx) {
        if (idx < 0 || idx >= this.elements.length) {
            throw new InvalidException("unknown elem segment " + idx);
        }
        return this.elements[idx];
    }

    public int elementCount() {
        return this.elements.length;
    }

    public void setElement(int idx, Element val) {
        this.elements[idx] = val;
    }

    public TagInstance tag(int idx) {
        if (idx < this.imports.tagCount()) {
            return this.imports.tag(idx).tag();
        }
        return this.tags[idx - this.imports.tagCount()];
    }

    public int tagCount() {
        return this.tags.length;
    }

    public int registerException(WasmException ex) {
        this.exnRefs.put(ex.tagIdx(), ex);
        return ex.tagIdx();
    }

    public WasmException exn(int idx) {
        return this.exnRefs.get(idx);
    }

    public Machine getMachine() {
        return this.machine;
    }

    void onExecution(Instruction instruction, MStack stack) {
        if (this.listener != null) {
            this.listener.onExecution(instruction, stack);
        }
    }

    public static Builder builder(WasmModule module) {
        return new Builder(module);
    }

    public static final class Exports {
        private final Instance instance;

        private Exports(Instance instance) {
            this.instance = instance;
        }

        private Export getExport(ExternalType type, String name) throws InvalidException {
            Export export = this.instance.exports.get(name);
            if (export == null) {
                throw new InvalidException("Unknown export with name " + name);
            }
            if (export.exportType() != type) {
                throw new InvalidException("The export " + export.name() + " is of type " + String.valueOf((Object)export.exportType()) + " and cannot be converted to " + String.valueOf((Object)type));
            }
            return export;
        }

        public ExportFunction function(String name) {
            Export export = this.getExport(ExternalType.FUNCTION, name);
            return args -> this.instance.machine.call(export.index(), args);
        }

        public GlobalInstance global(String name) {
            Export export = this.getExport(ExternalType.GLOBAL, name);
            return this.instance.global(export.index());
        }

        public TableInstance table(String name) {
            Export export = this.getExport(ExternalType.TABLE, name);
            return this.instance.table(export.index());
        }

        public Memory memory(String name) {
            Export export = this.getExport(ExternalType.MEMORY, name);
            assert (export.index() == 0);
            return this.instance.memory();
        }
    }

    public static final class Builder {
        private final WasmModule module;
        private boolean initialize = true;
        private boolean start = true;
        private MemoryLimits memoryLimits;
        private Function<MemoryLimits, Memory> memoryFactory;
        private ExecutionListener listener;
        private ImportValues importValues;
        private Function<Instance, Machine> machineFactory;

        private Builder(WasmModule module) {
            this.module = Objects.requireNonNull(module);
        }

        public Builder withInitialize(boolean init) {
            this.initialize = init;
            return this;
        }

        public Builder withStart(boolean s) {
            this.start = s;
            return this;
        }

        public Builder withMemoryLimits(MemoryLimits limits) {
            this.memoryLimits = limits;
            return this;
        }

        public Builder withMemoryFactory(Function<MemoryLimits, Memory> memoryFactory) {
            this.memoryFactory = memoryFactory;
            return this;
        }

        public Builder withUnsafeExecutionListener(ExecutionListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder withImportValues(ImportValues importValues) {
            this.importValues = importValues;
            return this;
        }

        public Builder withMachineFactory(Function<Instance, Machine> machineFactory) {
            this.machineFactory = machineFactory;
            return this;
        }

        private boolean checkExternalFunctionSignature(FunctionImport imprt, ImportFunction f) {
            try {
                this.validateExternalFunctionSignature(imprt, f);
                return true;
            }
            catch (UnlinkableException e) {
                return false;
            }
        }

        private void validateExternalFunctionSignature(FunctionImport imprt, ImportFunction f) {
            FunctionType expectedType = this.module.typeSection().getType(imprt.typeIndex());
            if (!f.functionType().equals(expectedType)) {
                throw new UnlinkableException("incompatible import type for host function " + f.module() + "." + f.name());
            }
        }

        private boolean checkHostGlobalType(GlobalImport i, ImportGlobal g) {
            try {
                this.validateHostGlobalType(i, g);
                return true;
            }
            catch (UnlinkableException e) {
                return false;
            }
        }

        private void validateHostGlobalType(GlobalImport i, ImportGlobal g) {
            boolean typesMatch;
            if (i.mutabilityType() == MutabilityType.Var) {
                typesMatch = i.type().equals(g.instance().getType());
            } else if (i.mutabilityType() == MutabilityType.Const) {
                typesMatch = ValType.matches(g.instance().getType(), i.type());
            } else {
                throw new ChicoryException("internal error: mutability type is not var or const: " + String.valueOf((Object)i.mutabilityType()));
            }
            if (!typesMatch || i.mutabilityType() != g.instance().getMutabilityType()) {
                throw new UnlinkableException("incompatible import type");
            }
        }

        private boolean checkHostTagType(TagImport i, ImportTag t) {
            try {
                this.validateHostTagType(i, t);
                return true;
            }
            catch (UnlinkableException e) {
                return false;
            }
        }

        private void validateHostTagType(TagImport i, ImportTag t) {
            ValType got;
            ValType expected;
            int j;
            FunctionType expectedType = this.module.typeSection().getType(i.tagType().typeIdx());
            FunctionType gotType = t.tag().type();
            if (expectedType.params().size() != gotType.params().size() || expectedType.returns().size() != gotType.returns().size()) {
                throw new UnlinkableException("incompatible import type for tag " + t.module() + "." + t.name());
            }
            for (j = 0; j < expectedType.params().size(); ++j) {
                expected = expectedType.params().get(j);
                if (expected.equals(got = gotType.params().get(j))) continue;
                throw new UnlinkableException("incompatible import type for tag " + t.module() + "." + t.name());
            }
            for (j = 0; j < expectedType.returns().size(); ++j) {
                expected = expectedType.returns().get(j);
                if (expected.equals(got = gotType.returns().get(j))) continue;
                throw new UnlinkableException("incompatible import type for tag " + t.module() + "." + t.name());
            }
        }

        private boolean checkHostTableType(TableImport i, ImportTable t) {
            try {
                this.validateHostTableType(i, t);
                return true;
            }
            catch (UnlinkableException e) {
                return false;
            }
        }

        private void validateHostTableType(TableImport i, ImportTable t) {
            long minExpected = t.table().limits().min();
            long maxExpected = t.table().limits().max();
            long minCurrent = i.limits().min();
            long maxCurrent = i.limits().max();
            if (!i.entryType().equals(t.table().elementType())) {
                throw new UnlinkableException("incompatible import type");
            }
            if (minExpected < minCurrent || maxExpected > maxCurrent || t.table().limits().shared() != i.limits().shared()) {
                throw new UnlinkableException("incompatible import type, non-compatible limits, expected: " + String.valueOf(i.limits()) + ", current: " + String.valueOf(t.table().limits()) + " on table: " + t.module() + "." + t.name());
            }
        }

        private void validateHostMemoryType(MemoryImport i, ImportMemory m) {
            int importMaxPages;
            int hostMemCurrentPages = m.memory().pages();
            int hostMemMaxPages = m.memory().maximumPages();
            int importInitialPages = i.limits().initialPages();
            int n = importMaxPages = i.limits().maximumPages() == 65536 ? Short.MAX_VALUE : i.limits().maximumPages();
            if (hostMemCurrentPages < importInitialPages || hostMemMaxPages > importMaxPages || m.memory().shared() != i.limits().shared()) {
                throw new UnlinkableException("incompatible import type, non-compatible limits, import: " + String.valueOf(i.limits()) + ", host initial pages: " + m.memory().initialPages() + ", host max pages: " + m.memory().maximumPages() + ", host shared: " + m.memory().shared() + " on memory: " + m.module() + "." + m.name());
            }
        }

        private void validateNegativeImportType(String moduleName, String name, ImportValue[] external) {
            for (ImportValue fh : external) {
                if (!fh.module().equals(moduleName) || !fh.name().equals(name)) continue;
                throw new UnlinkableException("incompatible import type");
            }
        }

        private void validateNegativeImportType(String moduleName, String name, ExternalType typ, ImportValues importValues) {
            switch (typ) {
                case FUNCTION: {
                    this.validateNegativeImportType(moduleName, name, importValues.globals());
                    this.validateNegativeImportType(moduleName, name, importValues.memories());
                    this.validateNegativeImportType(moduleName, name, importValues.tables());
                    this.validateNegativeImportType(moduleName, name, importValues.tags());
                    break;
                }
                case GLOBAL: {
                    this.validateNegativeImportType(moduleName, name, importValues.functions());
                    this.validateNegativeImportType(moduleName, name, importValues.memories());
                    this.validateNegativeImportType(moduleName, name, importValues.tables());
                    this.validateNegativeImportType(moduleName, name, importValues.tags());
                    break;
                }
                case MEMORY: {
                    this.validateNegativeImportType(moduleName, name, importValues.functions());
                    this.validateNegativeImportType(moduleName, name, importValues.globals());
                    this.validateNegativeImportType(moduleName, name, importValues.tables());
                    this.validateNegativeImportType(moduleName, name, importValues.tags());
                    break;
                }
                case TABLE: {
                    this.validateNegativeImportType(moduleName, name, importValues.functions());
                    this.validateNegativeImportType(moduleName, name, importValues.globals());
                    this.validateNegativeImportType(moduleName, name, importValues.memories());
                    this.validateNegativeImportType(moduleName, name, importValues.tags());
                    break;
                }
                case TAG: {
                    this.validateNegativeImportType(moduleName, name, importValues.functions());
                    this.validateNegativeImportType(moduleName, name, importValues.globals());
                    this.validateNegativeImportType(moduleName, name, importValues.memories());
                    this.validateNegativeImportType(moduleName, name, importValues.tables());
                }
            }
        }

        private ImportValues mapHostImports(Import[] imports, ImportValues importValues, int memoryCount) {
            Function<ExternalType, Integer> count = t -> (int)Arrays.stream(imports).filter(i -> i.importType() == t).count();
            ImportFunction[] hostFuncs = new ImportFunction[count.apply(ExternalType.FUNCTION).intValue()];
            int hostFuncIdx = 0;
            ImportGlobal[] hostGlobals = new ImportGlobal[count.apply(ExternalType.GLOBAL).intValue()];
            int hostGlobalIdx = 0;
            ImportMemory[] hostMems = new ImportMemory[count.apply(ExternalType.MEMORY).intValue()];
            int hostMemIdx = 0;
            ImportTag[] hostTags = new ImportTag[count.apply(ExternalType.TAG).intValue()];
            int hostTagIdx = 0;
            if (hostMems.length + memoryCount > 1) {
                throw new InvalidException("multiple memories");
            }
            ImportTable[] hostTables = new ImportTable[count.apply(ExternalType.TABLE).intValue()];
            int hostTableIdx = 0;
            List names = Arrays.stream(imports).map(i -> i.module() + "." + i.name()).collect(Collectors.toList());
            for (int impIdx = 0; impIdx < imports.length; ++impIdx) {
                Import i2 = imports[impIdx];
                String name = i2.module() + "." + i2.name();
                Stream<String> aliases = names.stream().filter(s -> s.equals(name));
                long aliasesCount = aliases.count();
                int aliasNum = 0;
                boolean found = false;
                this.validateNegativeImportType(i2.module(), i2.name(), i2.importType(), importValues);
                Function<ImportValue, Boolean> checkName = fh -> i2.module().equals(fh.module()) && i2.name().equals(fh.name());
                switch (i2.importType()) {
                    case FUNCTION: {
                        int j;
                        int cnt = importValues.functionCount();
                        for (j = 0; j < cnt; ++j) {
                            ImportFunction f = importValues.function(j);
                            if (!checkName.apply(f).booleanValue()) continue;
                            if (aliasesCount == 1L || (long)(++aliasNum) == aliasesCount) {
                                this.validateExternalFunctionSignature((FunctionImport)i2, f);
                            } else if (!this.checkExternalFunctionSignature((FunctionImport)i2, f)) continue;
                            hostFuncs[hostFuncIdx] = f;
                            found = true;
                            break;
                        }
                        ++hostFuncIdx;
                        break;
                    }
                    case GLOBAL: {
                        int j;
                        int cnt = importValues.globalCount();
                        for (j = 0; j < cnt; ++j) {
                            ImportGlobal g = importValues.global(j);
                            if (!checkName.apply(g).booleanValue()) continue;
                            if (aliasesCount == 1L || (long)(++aliasNum) == aliasesCount) {
                                this.validateHostGlobalType((GlobalImport)i2, g);
                            } else if (!this.checkHostGlobalType((GlobalImport)i2, g)) continue;
                            hostGlobals[hostGlobalIdx] = g;
                            found = true;
                            break;
                        }
                        ++hostGlobalIdx;
                        break;
                    }
                    case MEMORY: {
                        int j;
                        int cnt = importValues.memoryCount();
                        for (j = 0; j < cnt; ++j) {
                            ImportMemory m = importValues.memory(j);
                            if (!checkName.apply(m).booleanValue()) continue;
                            this.validateHostMemoryType((MemoryImport)i2, m);
                            hostMems[hostMemIdx] = m;
                            found = true;
                            break;
                        }
                        ++hostMemIdx;
                        break;
                    }
                    case TABLE: {
                        ImportValue t2;
                        int j;
                        int cnt = importValues.tableCount();
                        for (j = 0; j < cnt; ++j) {
                            t2 = importValues.table(j);
                            if (!checkName.apply(t2).booleanValue()) continue;
                            if (aliasesCount == 1L || (long)(++aliasNum) == aliasesCount) {
                                this.validateHostTableType((TableImport)i2, (ImportTable)t2);
                            } else if (!this.checkHostTableType((TableImport)i2, (ImportTable)t2)) continue;
                            hostTables[hostTableIdx] = t2;
                            found = true;
                            break;
                        }
                        ++hostTableIdx;
                        break;
                    }
                    case TAG: {
                        ImportValue t2;
                        int j;
                        int cnt = importValues.tagCount();
                        for (j = 0; j < cnt; ++j) {
                            t2 = importValues.tag(j);
                            if (!checkName.apply(t2).booleanValue()) continue;
                            if (aliasesCount == 1L || (long)(++aliasNum) == aliasesCount) {
                                this.validateHostTagType((TagImport)i2, (ImportTag)t2);
                            } else if (!this.checkHostTagType((TagImport)i2, (ImportTag)t2)) continue;
                            hostTags[hostTagIdx] = t2;
                            found = true;
                            break;
                        }
                        ++hostTagIdx;
                    }
                }
                if (found) continue;
                throw new UnlinkableException("unknown import, could not find host function for import number: " + impIdx + " named " + name);
            }
            return ImportValues.builder().addFunction(hostFuncs).addGlobal(hostGlobals).addMemory(hostMems).addTable(hostTables).addTag(hostTags).build();
        }

        private Map<String, Export> genExports(ExportSection export) {
            HashMap<String, Export> exports = new HashMap<String, Export>();
            int cnt = export.exportCount();
            for (int i = 0; i < cnt; ++i) {
                Export e = export.getExport(i);
                if (exports.containsKey(e.name())) {
                    throw new InvalidException("duplicate export name " + e.name());
                }
                exports.put(e.name(), e);
            }
            return exports;
        }

        public Instance build() {
            Map<String, Export> exports = this.genExports(this.module.exportSection());
            Global[] globalInitializers = this.module.globalSection().globals();
            DataSegment[] dataSegments = this.module.dataSection().dataSegments();
            FunctionType[] types = this.module.typeSection().types();
            int numFuncTypes = this.module.functionSection().functionCount() + this.module.importSection().count(ExternalType.FUNCTION);
            FunctionBody[] functions = this.module.codeSection().functionBodies();
            int importId = 0;
            int[] functionTypes = new int[numFuncTypes];
            int funcIdx = 0;
            int importCount = this.module.importSection().importCount();
            Import[] imports = new Import[importCount];
            for (int i = 0; i < importCount; ++i) {
                Import imprt = this.module.importSection().getImport(i);
                if (imprt.importType() == ExternalType.FUNCTION) {
                    int type = ((FunctionImport)imprt).typeIndex();
                    if (type >= this.module.typeSection().typeCount()) {
                        throw new InvalidException("unknown type");
                    }
                    functionTypes[funcIdx] = type;
                    ++funcIdx;
                }
                imports[importId++] = imprt;
            }
            ImportValues mappedHostImports = this.mapHostImports(imports, Objects.requireNonNullElseGet(this.importValues, ImportValues::empty), this.module.memorySection().map(MemorySection::memoryCount).orElse(0));
            for (int i = 0; i < this.module.functionSection().functionCount(); ++i) {
                functionTypes[funcIdx++] = this.module.functionSection().getFunctionType(i);
            }
            int tableLength = this.module.tableSection().tableCount();
            Table[] tables = new Table[tableLength];
            for (int i = 0; i < tableLength; ++i) {
                tables[i] = this.module.tableSection().getTable(i);
            }
            Element[] elements = this.module.elementSection().elements();
            Memory memory = null;
            if (this.module.memorySection().isPresent()) {
                MemorySection memories = this.module.memorySection().get();
                if (memories.memoryCount() > 0) {
                    MemoryLimits defaultLimits = memories.getMemory(0).limits();
                    memory = Objects.requireNonNullElse(this.memoryFactory, ByteBufferMemory::new).apply(Objects.requireNonNullElse(this.memoryLimits, defaultLimits));
                }
            } else if (mappedHostImports != null && mappedHostImports.memoryCount() > 0) {
                if (mappedHostImports.memory(0) == null || mappedHostImports.memory(0).memory() == null) {
                    throw new InvalidException("unknown memory, imported memory not defined, cannot run the program");
                }
                memory = mappedHostImports.memory(0).memory();
            }
            for (Export e : exports.values()) {
                switch (e.exportType()) {
                    case FUNCTION: {
                        if (e.index() < this.module.functionSection().functionCount() + mappedHostImports.functionCount()) break;
                        throw new InvalidException("unknown function " + e.index());
                    }
                    case GLOBAL: {
                        if (e.index() < this.module.globalSection().globalCount() + mappedHostImports.globalCount()) break;
                        throw new InvalidException("unknown global " + e.index());
                    }
                    case TABLE: {
                        if (e.index() < this.module.tableSection().tableCount() + mappedHostImports.tableCount()) break;
                        throw new InvalidException("unknown table " + e.index());
                    }
                    case MEMORY: {
                        Integer memoryCount = this.module.memorySection().map(MemorySection::memoryCount).orElse(0);
                        if (e.index() < memoryCount + mappedHostImports.memoryCount()) break;
                        throw new InvalidException("unknown memory " + String.valueOf(e));
                    }
                }
            }
            if (this.machineFactory == null) {
                this.machineFactory = InterpreterMachine::new;
            }
            return new Instance(this.module, globalInitializers, memory, dataSegments, functions, types, functionTypes, mappedHostImports, tables, elements, this.module.tagSection().map(TagSection::types).orElse(null), exports, this.machineFactory, this.initialize, this.start, this.listener);
        }
    }
}

