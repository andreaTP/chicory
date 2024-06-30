package com.dylibso.chicory.runtime;

import static com.dylibso.chicory.runtime.ConstantEvaluators.computeConstantInstance;
import static com.dylibso.chicory.runtime.ConstantEvaluators.computeConstantValue;
import static com.dylibso.chicory.runtime.Module.START_FUNCTION_NAME;

import com.dylibso.chicory.runtime.exceptions.WASMMachineException;
import com.dylibso.chicory.wasm.exceptions.ChicoryException;
import com.dylibso.chicory.wasm.exceptions.InvalidException;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.ActiveElement;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Global;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Instance {
    private final Module module;
    private final Machine machine;
    private final FunctionBody[] functions;
    private final Memory memory;
    private final DataSegment[] dataSegments;
    private final Global[] globalInitializers;
    private final GlobalInstance[] globals;
    private final int importedGlobalsOffset;
    private final int importedFunctionsOffset;
    private final int importedTablesOffset;
    private final FunctionType[] types;
    private final int[] functionTypes;
    private final HostImports imports;
    private final Table[] roughTables;
    private TableInstance[] tables;
    private final Element[] elements;
    private final boolean start;
    private final boolean typeValidation;
    private final ExecutionListener listener;

    public Instance(
            Module module,
            Global[] globalInitializers,
            int importedGlobalsOffset,
            int importedFunctionsOffset,
            int importedTablesOffset,
            Memory memory,
            DataSegment[] dataSegments,
            FunctionBody[] functions,
            FunctionType[] types,
            int[] functionTypes,
            HostImports imports,
            Table[] tables,
            Element[] elements,
            boolean initialize,
            boolean start,
            boolean typeValidation) {
        this(
                module,
                globalInitializers,
                importedGlobalsOffset,
                importedFunctionsOffset,
                importedTablesOffset,
                memory,
                dataSegments,
                functions,
                types,
                functionTypes,
                imports,
                tables,
                elements,
                InterpreterMachine::new,
                initialize,
                start,
                typeValidation,
                null);
    }

    public Instance(
            Module module,
            Global[] globalInitializers,
            int importedGlobalsOffset,
            int importedFunctionsOffset,
            int importedTablesOffset,
            Memory memory,
            DataSegment[] dataSegments,
            FunctionBody[] functions,
            FunctionType[] types,
            int[] functionTypes,
            HostImports imports,
            Table[] tables,
            Element[] elements,
            Function<Instance, Machine> machineFactory,
            boolean initialize,
            boolean start,
            boolean typeValidation,
            ExecutionListener listener) {
        this.module = module;
        this.globalInitializers = globalInitializers.clone();
        this.globals = new GlobalInstance[globalInitializers.length + importedGlobalsOffset];
        this.importedGlobalsOffset = importedGlobalsOffset;
        this.importedFunctionsOffset = importedFunctionsOffset;
        this.importedTablesOffset = importedTablesOffset;
        this.memory = memory;
        this.dataSegments = dataSegments;
        this.functions = functions.clone();
        this.types = types.clone();
        this.functionTypes = functionTypes.clone();
        this.imports = imports;
        this.machine = machineFactory.apply(this);
        this.roughTables = tables.clone();
        this.elements = elements.clone();
        this.start = start;
        this.listener = listener;
        this.typeValidation = typeValidation;

        if (initialize) {
            initialize(this.start);
        }
    }

    private void verifyGlobalType(ValueType expected, ValueType actual) {
        if (actual != expected) {
            throw new InvalidException(
                    "type mismatch, expected: " + expected + ", actual: " + actual);
        }
    }

    public Instance initialize(boolean start) {
        this.tables = new TableInstance[this.roughTables.length];
        for (var i = 0; i < this.roughTables.length; i++) {
            this.tables[i] = new TableInstance(this.roughTables[i]);
        }
        for (var el : elements) {
            if (el instanceof ActiveElement) {
                var ae = (ActiveElement) el;
                var table = table(ae.tableIndex());

                Value offset = computeConstantValue(this, ae.offset());
                if (offset.type() != ValueType.I32) {
                    throw new ChicoryException("Invalid offset type in element " + offset.type());
                }
                List<List<Instruction>> initializers = ae.initializers();
                for (int i = 0; i < initializers.size(); i++) {
                    final List<Instruction> init = initializers.get(i);
                    var index = offset.asInt() + i;
                    var value = computeConstantValue(this, init);
                    var inst = computeConstantInstance(this, init);
                    if (ae.type() == ValueType.FuncRef) {
                        function(value.asFuncRef());
                        table.setRef(index, value.asFuncRef(), inst);
                    } else {
                        assert ae.type() == ValueType.ExternRef;
                        table.setRef(index, value.asExtRef(), inst);
                    }
                }
            }
        }

        for (var i = 0; i < globalInitializers.length; i++) {
            var g = globalInitializers[i];
            var initialized = false;
            for (var j = 0; j < g.initInstructions().size(); j++) {
                var instr = g.initInstructions().get(j);
                switch (instr.opcode()) {
                    case I32_CONST:
                        verifyGlobalType(g.valueType(), ValueType.I32);
                        globals[i] = new GlobalInstance((Value.i32(instr.operands()[0])));
                        break;
                    case I64_CONST:
                        verifyGlobalType(g.valueType(), ValueType.I64);
                        globals[i] = new GlobalInstance(Value.i64(instr.operands()[0]));
                        break;
                    case F32_CONST:
                        verifyGlobalType(g.valueType(), ValueType.F32);
                        globals[i] = new GlobalInstance(Value.f32(instr.operands()[0]));
                        break;
                    case F64_CONST:
                        verifyGlobalType(g.valueType(), ValueType.F64);
                        globals[i] = new GlobalInstance(Value.f64(instr.operands()[0]));
                        break;
                    case GLOBAL_GET:
                        {
                            var idx = (int) instr.operands()[0];
                            if (idx < imports.globalCount()) {
                                if (imports.global(idx).mutabilityType() != MutabilityType.Const) {
                                    throw new InvalidException(
                                            "constant expression required, initializer expression"
                                                    + " cannot reference a mutable global");
                                }
                                verifyGlobalType(g.valueType(), imports.global(idx).instance().getValue().type());
                                globals[i] = imports.global(idx).instance();
                            } else {
                                throw new InvalidException(
                                        "unknown global, initializer expression can only reference"
                                                + " an imported global");
                            }
                            break;
                        }
                    case REF_NULL:
                        globals[i] = new GlobalInstance(Value.EXTREF_NULL);
                        break;
                    case REF_FUNC:
                        globals[i] = new GlobalInstance(Value.funcRef((int) instr.operands()[0]));
                        break;
                    default:
                        throw new InvalidException("constant expression required");
                }
                if (initialized && g.mutabilityType() == MutabilityType.Const) {
                    throw new InvalidException(
                            "type mismatch, expected [] but found extra instructions");
                }
                initialized = true;
            }
            if (!initialized || globals[i] == null) {
                throw new InvalidException("type mismatch, unknown global");
            }
            globals[i].setInstance(this);
        }

        if (memory != null) {
            memory.initialize(this, dataSegments);
        } else if (imports.memories().length > 0) {
            imports.memories()[0].memory().initialize(this, dataSegments);
        } else if (Arrays.stream(dataSegments).anyMatch(ds -> ds instanceof ActiveDataSegment)) {
            throw new InvalidException("unknown memory");
        }

        // Type validation needs to remain optional until it's finished
        if (typeValidation) {
            // TODO: can be parallelized?
            for (int i = 0; i < this.functions.length; i++) {
                if (this.function(i) != null) {
                    var funcType = this.functionType(i);
                    if (funcType >= this.types.length) {
                        throw new InvalidException("unknown type " + funcType);
                    }
                    new TypeValidator().validate(this.function(i), this.types[funcType], this);
                }
            }
        }

        if (start && module.export(START_FUNCTION_NAME) != null) {
            export(START_FUNCTION_NAME).apply();
        }

        return this;
    }

    public ExportFunction export(String name) {
        var export = module.export(name);
        if (export == null) throw new ChicoryException("Unknown export with name " + name);

        switch (export.exportType()) {
            case FUNCTION:
                {
                    var funcId = export.index();
                    return (args) -> {
                        this.module.logger().debug(() -> "Args: " + Arrays.toString(args));
                        try {
                            return machine.call(funcId, args);
                        } catch (Exception e) {
                            throw new WASMMachineException(machine.getStackTrace(), e);
                        }
                    };
                }
            case GLOBAL:
                {
                    return new ExportFunction() {
                        @Override
                        public Value[] apply(Value... args) throws ChicoryException {
                            assert (args.length == 0);
                            return new Value[] {readGlobal(export.index())};
                        }
                    };
                }
            default:
                {
                    throw new ChicoryException("not implemented");
                }
        }
    }

    public FunctionBody function(int idx) {
        if (idx < importedFunctionsOffset) return null;
        if (idx >= (functions.length + importedFunctionsOffset)) {
            throw new InvalidException("unknown function " + idx);
        }
        return functions[idx - importedFunctionsOffset];
    }

    public int functionCount() {
        return importedFunctionsOffset + functions.length;
    }

    public Memory memory() {
        return memory;
    }

    public GlobalInstance global(int idx) {
        if (idx < importedGlobalsOffset) {
            return imports.global(idx).instance();
        }
        return globals[idx - importedGlobalsOffset];
    }

    public void writeGlobal(int idx, Value val) {
        if (idx < importedGlobalsOffset) {
            imports.global(idx).instance().setValue(val);
        }
        globals[idx - importedGlobalsOffset].setValue(val);
    }

    public Value readGlobal(int idx) {
        if (idx < importedGlobalsOffset) {
            return imports.global(idx).instance().getValue();
        }
        var i = idx - importedGlobalsOffset;
        if (i < 0 || i >= globals.length || globals[idx - importedGlobalsOffset] == null) {
            throw new InvalidException("unknown global " + i);
        }
        return globals[idx - importedGlobalsOffset].getValue();
    }

    public Global globalInitializer(int idx) {
        if (idx < importedGlobalsOffset) {
            return null;
        }
        if ((idx - importedGlobalsOffset) >= globalInitializers.length) {
            throw new InvalidException("unknown global " + idx);
        }
        return globalInitializers[idx - importedGlobalsOffset];
    }

    public int globalCount() {
        return globals.length;
    }

    public FunctionType type(int idx) {
        if (idx >= types.length) {
            throw new InvalidException("unknown type " + idx);
        }
        return types[idx];
    }

    public int typeCount() {
        return types.length;
    }

    public int functionType(int idx) {
        if (idx >= functionTypes.length) {
            throw new InvalidException("unknown function " + idx);
        }
        return functionTypes[idx];
    }

    public HostImports imports() {
        return imports;
    }

    public Module module() {
        return module;
    }

    public TableInstance table(int idx) {
        if (idx < 0 || idx >= (tables.length + importedTablesOffset)) {
            throw new InvalidException("unknown table " + idx);
        }
        if (idx < importedTablesOffset) {
            return imports.table(idx).table();
        }
        return tables[idx - importedTablesOffset];
    }

    public DataSegment[] dataSegments() {
        return dataSegments;
    }

    public Element element(int idx) {
        if (idx < 0 || idx >= elements.length) {
            throw new InvalidException("unknown elem segment " + idx);
        }
        return elements[idx];
    }

    public int elementCount() {
        return elements.length;
    }

    public void setElement(int idx, Element val) {
        elements[idx] = val;
    }

    public Machine getMachine() {
        return machine;
    }

    public Value[] callHostFunction(int funcId, Value[] args) {
        var imprt = imports.function(funcId);
        if (imprt == null) {
            throw new ChicoryException("Missing host import, number: " + funcId);
        }
        return imprt.handle().apply(this, args);
    }

    public void onExecution(Instruction instruction, long[] operands, MStack stack) {
        if (listener != null) {
            listener.onExecution(instruction, operands, stack);
        }
    }
}
