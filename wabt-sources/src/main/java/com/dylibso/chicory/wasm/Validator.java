/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.MalformedException;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.ActiveElement;
import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.CatchOpCode;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.DeclarativeElement;
import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.FunctionImport;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Global;
import com.dylibso.chicory.wasm.types.GlobalImport;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableImport;
import com.dylibso.chicory.wasm.types.TagImport;
import com.dylibso.chicory.wasm.types.TagSection;
import com.dylibso.chicory.wasm.types.TagType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Validator {
    private final List<ValType> valTypeStack = new ArrayList<ValType>();
    private final List<CtrlFrame> ctrlFrameStack = new ArrayList<CtrlFrame>();
    private final List<Integer> initStack = new ArrayList<Integer>();
    private final List<InvalidException> errors = new ArrayList<InvalidException>();
    private final WasmModule module;
    private final List<ValType> locals;
    private final List<Boolean> localsInitialized;
    private final List<Global> globalImports;
    private final List<Integer> functionImports;
    private final List<ValType> tableImports;
    private final List<TagType> tagImports;
    private final int memoryImports;
    private final Set<Integer> declaredFunctions;
    private static int[] typesWithDefaultValue = new int[]{124, 124, 125, 126, 127, 123, 99};

    private static boolean isNum(ValType t) {
        return t.isNumeric() || t.equals(ValType.BOT);
    }

    private static boolean isRef(ValType t) {
        return t.isReference() || t.equals(ValType.BOT);
    }

    Validator(WasmModule module) {
        this.module = Objects.requireNonNull(module);
        this.locals = new ArrayList<ValType>();
        this.localsInitialized = new ArrayList<Boolean>();
        this.globalImports = module.importSection().stream().filter(GlobalImport.class::isInstance).map(GlobalImport.class::cast).map(i -> new Global(i.type(), i.mutabilityType(), List.of())).collect(Collectors.toList());
        this.functionImports = module.importSection().stream().filter(FunctionImport.class::isInstance).map(FunctionImport.class::cast).map(FunctionImport::typeIndex).collect(Collectors.toList());
        this.tableImports = module.importSection().stream().filter(TableImport.class::isInstance).map(TableImport.class::cast).map(TableImport::entryType).collect(Collectors.toList());
        this.memoryImports = module.importSection().count(ExternalType.MEMORY);
        this.declaredFunctions = module.elementSection().stream().filter(DeclarativeElement.class::isInstance).flatMap(element -> element.initializers().stream()).flatMap(this::declaredFunctions).collect(Collectors.toSet());
        this.tagImports = module.importSection().stream().filter(TagImport.class::isInstance).map(TagImport.class::cast).map(TagImport::tagType).collect(Collectors.toList());
    }

    private Stream<Integer> declaredFunctions(List<Instruction> init) {
        if (!init.isEmpty() && init.get(0).opcode() == OpCode.REF_FUNC) {
            int idx = (int)init.get(0).operand(0);
            this.getFunctionType(idx);
            if (idx >= this.functionImports.size()) {
                return Stream.of(Integer.valueOf(idx));
            }
        }
        return Stream.empty();
    }

    private void pushVal(ValType valType) {
        this.valTypeStack.add(valType);
    }

    private ValType popVal() {
        CtrlFrame frame = this.peekCtrl();
        if (this.valTypeStack.size() == frame.height && frame.unreachable) {
            return ValType.BOT;
        }
        if (this.valTypeStack.size() == frame.height) {
            this.errors.add(new InvalidException("type mismatch: instruction requires [i32] but stack has []"));
            return ValType.BOT;
        }
        return this.valTypeStack.remove(this.valTypeStack.size() - 1);
    }

    private ValType popVal(ValType expected) {
        ValType actual = this.popVal();
        if (!(ValType.matches(actual, expected) || actual.equals(ValType.BOT) || expected.equals(ValType.BOT))) {
            this.errors.add(new InvalidException("type mismatch: instruction requires [" + expected.toString().toLowerCase(Locale.ROOT) + "] but stack has [" + actual.toString().toLowerCase(Locale.ROOT) + "]"));
        }
        return actual;
    }

    private ValType popRef() {
        ValType actual = this.popVal();
        if (!Validator.isRef(actual)) {
            this.errors.add(new InvalidException("type mismatch, popRef(), expected reference type but got: " + String.valueOf(actual)));
        }
        if (actual.equals(ValType.BOT)) {
            return ValType.RefBot;
        }
        return actual;
    }

    private void pushVals(List<ValType> valTypes) {
        for (ValType t : valTypes) {
            this.pushVal(t);
        }
    }

    private List<ValType> popVals(List<ValType> valTypes) {
        ValType[] popped = new ValType[valTypes.size()];
        for (int i = valTypes.size() - 1; i >= 0; --i) {
            popped[i] = this.popVal(valTypes.get(i));
        }
        return Arrays.asList(popped);
    }

    private ValType getLocal(int idx) {
        if (idx >= this.locals.size()) {
            throw new InvalidException("unknown local " + idx);
        }
        if (!this.localsInitialized.get(idx).booleanValue()) {
            this.errors.add(new InvalidException("uninitialized local: index " + idx));
        }
        return this.getLocalType(idx);
    }

    private void setLocal(int idx) {
        if (idx >= this.locals.size()) {
            throw new InvalidException("unknown local " + idx);
        }
        if (!this.localsInitialized.get(idx).booleanValue()) {
            this.initStack.add(idx);
            this.localsInitialized.set(idx, true);
        }
    }

    private void resetLocals(int height) {
        while (this.initStack.size() > height) {
            this.localsInitialized.set(this.initStack.remove(this.initStack.size() - 1), false);
        }
    }

    private void pushCtrl(OpCode opCode, List<ValType> in, List<ValType> out) {
        CtrlFrame frame = new CtrlFrame(opCode, in, out, this.valTypeStack.size(), this.initStack.size(), false, false);
        this.pushCtrl(frame);
        this.pushVals(in);
    }

    private void pushCtrl(CtrlFrame frame) {
        this.ctrlFrameStack.add(frame);
    }

    private CtrlFrame popCtrl() {
        if (this.ctrlFrameStack.isEmpty()) {
            this.errors.add(new InvalidException("type mismatch, control frame stack empty"));
        }
        CtrlFrame frame = this.peekCtrl();
        this.popVals(frame.endTypes);
        if (this.valTypeStack.size() != frame.height) {
            this.errors.add(new InvalidException("type mismatch, mismatching stack height"));
        }
        this.resetLocals(frame.initHeight);
        this.ctrlFrameStack.remove(this.ctrlFrameStack.size() - 1);
        return frame;
    }

    private CtrlFrame peekCtrl() {
        return this.ctrlFrameStack.get(this.ctrlFrameStack.size() - 1);
    }

    private CtrlFrame getCtrl(int n) {
        return this.ctrlFrameStack.get(this.ctrlFrameStack.size() - 1 - n);
    }

    private static List<ValType> labelTypes(CtrlFrame frame) {
        return frame.opCode == OpCode.LOOP ? frame.startTypes : frame.endTypes;
    }

    private void resetAtStackLimit() {
        CtrlFrame frame = this.peekCtrl();
        while (this.valTypeStack.size() > frame.height) {
            this.valTypeStack.remove(this.valTypeStack.size() - 1);
        }
    }

    private void unreachable() {
        CtrlFrame frame = this.peekCtrl();
        this.resetAtStackLimit();
        frame.unreachable = true;
    }

    private void validateMemory(int id) {
        if (this.module.memorySection().isEmpty() && this.memoryImports == 0 || id != 0) {
            throw new InvalidException("unknown memory " + id);
        }
    }

    private void validateMemAlign(long current, long expected) {
        if (current != expected) {
            throw new InvalidException("invalid memory alignement, current: " + current + ", expected: " + expected);
        }
    }

    private void validateLane(int id, int max) {
        if (id < 0 || id >= max) {
            throw new InvalidException("invalid lane index " + id + " max is " + max);
        }
    }

    private void validateDataSegment(int idx) {
        if (idx < 0 || idx >= this.module.dataSection().dataSegmentCount()) {
            throw new InvalidException("unknown data segment " + idx);
        }
    }

    private ValType valType(long id) {
        return ValType.builder().fromId(id).build(this.module.typeSection()::getType);
    }

    private ValType valType(int opcode, int typeIdx) {
        return ValType.builder().withOpcode(opcode).withTypeIdx(typeIdx).build(this.module.typeSection()::getType);
    }

    private List<ValType> getReturns(AnnotatedInstruction op) {
        long typeId = op.operand(0);
        if (typeId == 64L) {
            return List.of();
        }
        if (ValType.isValid(typeId)) {
            return List.of(this.valType(typeId));
        }
        return this.getType((int)typeId).returns();
    }

    private List<ValType> getParams(AnnotatedInstruction op) {
        long typeId = op.operand(0);
        if (typeId == 64L) {
            return List.of();
        }
        if (ValType.isValid(typeId)) {
            return List.of();
        }
        if (typeId >= (long)this.module.typeSection().typeCount()) {
            throw new MalformedException("unexpected end");
        }
        return this.getType((int)typeId).params();
    }

    private ValType getLocalType(int idx) {
        if (idx >= this.locals.size()) {
            throw new InvalidException("unknown local " + idx);
        }
        return this.locals.get(idx);
    }

    private FunctionType getType(int idx) {
        if (idx < 0 || idx >= this.module.typeSection().typeCount()) {
            throw new InvalidException("unknown type " + idx);
        }
        return this.module.typeSection().getType(idx);
    }

    private Global getGlobal(int idx) {
        if (idx < 0 || idx >= this.globalImports.size() + this.module.globalSection().globalCount()) {
            throw new InvalidException("unknown global " + idx);
        }
        if (idx < this.globalImports.size()) {
            return this.globalImports.get(idx);
        }
        return this.module.globalSection().getGlobal(idx - this.globalImports.size());
    }

    private int getFunctionType(int idx) {
        if (idx < 0 || idx >= this.functionImports.size() + this.module.functionSection().functionCount()) {
            throw new InvalidException("unknown function " + idx);
        }
        if (idx < this.functionImports.size()) {
            return this.functionImports.get(idx);
        }
        return this.module.functionSection().getFunctionType(idx - this.functionImports.size());
    }

    private ValType getTableType(int idx) {
        if (idx < 0 || idx >= this.tableImports.size() + this.module.tableSection().tableCount()) {
            throw new InvalidException("unknown table " + idx);
        }
        if (idx < this.tableImports.size()) {
            return this.tableImports.get(idx);
        }
        return this.module.tableSection().getTable(idx - this.tableImports.size()).elementType();
    }

    private TagType getTagType(int idx) {
        Integer tagCount = this.module.tagSection().map(ts -> ts.tagCount()).orElse(0);
        if (idx < 0 || idx >= this.tagImports.size() + tagCount) {
            throw new InvalidException("unknown tag " + idx);
        }
        if (idx < this.tagImports.size()) {
            return this.tagImports.get(idx);
        }
        return this.module.tagSection().get().getTag(idx - this.tagImports.size());
    }

    private Element getElement(int idx) {
        if (idx < 0 || idx >= this.module.elementSection().elementCount()) {
            throw new InvalidException("unknown elem segment " + idx);
        }
        return this.module.elementSection().getElement(idx);
    }

    void validateModule() {
        if (this.module.functionSection().functionCount() != this.module.codeSection().functionBodyCount()) {
            throw new MalformedException("function and code section have inconsistent lengths");
        }
        if (this.module.dataCountSection().map(dcs -> dcs.dataCount() != this.module.dataSection().dataSegmentCount()).orElse(false).booleanValue()) {
            throw new MalformedException("data count and data section have inconsistent lengths");
        }
        if (this.module.startSection().isPresent()) {
            long index = this.module.startSection().get().startIndex();
            if (index < 0L || index > Integer.MAX_VALUE) {
                throw new InvalidException("unknown function " + index);
            }
            FunctionType type = this.getType(this.getFunctionType((int)index));
            if (!type.params().isEmpty() || !type.returns().isEmpty()) {
                throw new InvalidException("invalid start function, must have empty signature " + String.valueOf(type));
            }
        }
    }

    void validateData() {
        for (DataSegment ds : this.module.dataSection().dataSegments()) {
            if (!(ds instanceof ActiveDataSegment)) continue;
            ActiveDataSegment ads = (ActiveDataSegment)ds;
            if (ads.index() != 0L) {
                throw new InvalidException("unknown memory " + ads.index());
            }
            this.validateConstantExpression(ads.offsetInstructions(), ValType.I32);
        }
    }

    void validateTypes() {
        FunctionType[] types = this.module.typeSection().types();
        for (int i = 0; i < types.length; ++i) {
            FunctionType t = types[i];
            t.params().forEach(this::validateValueType);
            t.returns().forEach(this::validateValueType);
        }
    }

    void validateTags() {
        for (TagType tagType : this.module.tagSection().map(ts -> ts.types()).orElse(new TagType[0])) {
            FunctionType type = this.module.typeSection().getType(tagType.typeIdx());
            if (type.returns().size() <= 0) continue;
            throw new InvalidException("non-empty tag result type index: " + tagType.typeIdx());
        }
    }

    void validateTables() {
        for (int i = 0; i < this.module.tableSection().tableCount(); ++i) {
            Table t = this.module.tableSection().getTable(i);
            this.validateConstantExpression(t.initialize(), t.elementType());
        }
    }

    void validateElements() {
        long totalFunctions = (long)this.module.functionSection().functionCount() + this.module.importSection().stream().filter(i -> i.importType() == ExternalType.FUNCTION).count();
        for (Element el : this.module.elementSection().elements()) {
            this.validateValueType(el.type());
            if (el instanceof ActiveElement) {
                ActiveElement ae = (ActiveElement)el;
                if (!ValType.matches(ae.type(), this.getTableType(ae.tableIndex()))) {
                    throw new InvalidException("type mismatch, active element doesn't match table type");
                }
                this.validateConstantExpression(ae.offset(), ValType.I32);
                for (int i2 = 0; i2 < ae.initializers().size(); ++i2) {
                    List<Instruction> initializers = ae.initializers().get(i2);
                    if (initializers.stream().filter(x -> x.opcode() != OpCode.END).count() != 1L) {
                        throw new InvalidException("type mismatch, constant expression required");
                    }
                    for (Instruction init : initializers) {
                        long idx;
                        if (init.opcode() != OpCode.REF_FUNC || (idx = init.operands()[0]) >= 0L && idx < totalFunctions) continue;
                        throw new InvalidException("unknown function " + idx);
                    }
                    this.validateConstantExpression(ae.initializers().get(i2), this.getTableType(ae.tableIndex()));
                }
                continue;
            }
            if (!(el instanceof DeclarativeElement)) continue;
            for (List<Instruction> init : el.initializers()) {
                if (init.stream().filter(x -> x.opcode() != OpCode.END).count() == 1L) continue;
                throw new InvalidException("type mismatch, constant expression required");
            }
        }
    }

    void validateGlobals() {
        for (Global g : this.module.globalSection().globals()) {
            this.validateConstantExpression(g.initInstructions(), g.valueType());
        }
    }

    private void validateConstantExpression(List<? extends Instruction> expr, ValType expectedType) {
        this.validateValueType(expectedType);
        int allFuncCount = this.functionImports.size() + this.module.functionSection().functionCount();
        ArrayDeque<ValType> valTypeStack = new ArrayDeque<ValType>();
        block13: for (Instruction instruction : expr) {
            switch (instruction.opcode()) {
                case I32_CONST: {
                    valTypeStack.push(ValType.I32);
                    continue block13;
                }
                case I32_ADD: 
                case I32_SUB: 
                case I32_MUL: {
                    valTypeStack.pop();
                    valTypeStack.pop();
                    valTypeStack.push(ValType.I32);
                    continue block13;
                }
                case I64_CONST: {
                    valTypeStack.push(ValType.I64);
                    continue block13;
                }
                case I64_ADD: 
                case I64_SUB: 
                case I64_MUL: {
                    valTypeStack.pop();
                    valTypeStack.pop();
                    valTypeStack.push(ValType.I64);
                    continue block13;
                }
                case F32_CONST: {
                    valTypeStack.push(ValType.F32);
                    continue block13;
                }
                case F64_CONST: {
                    valTypeStack.push(ValType.F64);
                    continue block13;
                }
                case V128_CONST: {
                    valTypeStack.push(ValType.V128);
                    continue block13;
                }
                case REF_NULL: {
                    int operand = (int)instruction.operand(0);
                    valTypeStack.push(this.valType(99, operand));
                    continue block13;
                }
                case REF_FUNC: {
                    int idx = (int)instruction.operand(0);
                    valTypeStack.push(this.valType(100, this.getFunctionType(idx)));
                    if (idx >= 0 && idx <= allFuncCount) continue block13;
                    throw new InvalidException("unknown function " + idx);
                }
                case GLOBAL_GET: {
                    int idx = (int)instruction.operand(0);
                    if (idx < this.globalImports.size()) {
                        Global global = this.globalImports.get(idx);
                        if (global.mutabilityType() != MutabilityType.Const) {
                            throw new InvalidException("constant expression required, initializer expression cannot reference a mutable global");
                        }
                        valTypeStack.push(global.valueType());
                        continue block13;
                    }
                    throw new InvalidException("unknown global " + idx + ", initializer expression can only reference an imported global");
                }
                case END: {
                    continue block13;
                }
            }
            throw new InvalidException("constant expression required, but non-constant instruction encountered: " + String.valueOf(instruction));
        }
        if (valTypeStack.size() < 1) {
            throw new InvalidException("type mismatch, no constant expressions found");
        }
        if (valTypeStack.size() != 1) {
            throw new InvalidException("type mismatch, values remaining on the stack after evaluation");
        }
        ValType exprType = (ValType)valTypeStack.pop();
        if (exprType != null && !ValType.matches(exprType, expectedType)) {
            throw new InvalidException("type mismatch");
        }
    }

    void validateFunctions() {
        for (int i = 0; i < this.module.codeSection().functionBodyCount(); ++i) {
            FunctionBody body = this.module.codeSection().getFunctionBody(i);
            int idx = this.functionImports.size() + i;
            FunctionType type = this.getType(this.getFunctionType(idx));
            this.validateFunction(idx, body, type);
        }
    }

    private static boolean hasDefaultValue(ValType t) {
        for (int t2 : typesWithDefaultValue) {
            if (t.opcode() != t2) continue;
            return true;
        }
        return false;
    }

    private void validateValueType(ValType valueType) {
        int idx;
        if (valueType.isReference() && valueType.typeIdx() >= 0 && (idx = valueType.typeIdx()) >= this.module.typeSection().typeCount()) {
            throw new InvalidException("unknown type " + idx);
        }
    }

    void validateFunction(int funcIdx, FunctionBody body, FunctionType functionType) {
        this.valTypeStack.clear();
        this.locals.clear();
        this.localsInitialized.clear();
        functionType.params().forEach(t -> {
            this.validateValueType((ValType)t);
            this.locals.add((ValType)t);
            this.localsInitialized.add(true);
        });
        body.localTypes().forEach(t -> {
            this.validateValueType((ValType)t);
            this.locals.add((ValType)t);
            this.localsInitialized.add(Validator.hasDefaultValue(t));
        });
        functionType.returns().forEach(this::validateValueType);
        this.pushCtrl(null, new ArrayList<ValType>(), functionType.returns());
        block133: for (int i = 0; i < body.instructions().size(); ++i) {
            AnnotatedInstruction op = body.instructions().get(i);
            switch (op.opcode()) {
                case UNREACHABLE: {
                    this.unreachable();
                    break;
                }
                case TRY_TABLE: {
                    int idx;
                    List<ValType> t1 = this.getParams(op);
                    List<ValType> t2 = this.getReturns(op);
                    this.popVals(t1);
                    List<CatchOpCode.Catch> catches = CatchOpCode.decode(op.operands());
                    for (idx = 0; idx < catches.size(); ++idx) {
                        CatchOpCode.Catch currentCatch = catches.get(idx);
                        if (this.ctrlFrameStack.size() < currentCatch.label()) {
                            throw new InvalidException("something something");
                        }
                        this.pushCtrl(OpCode.THROW, List.of(), Validator.labelTypes(this.getCtrl(currentCatch.label())));
                        switch (currentCatch.opcode()) {
                            case CATCH: {
                                FunctionType tagType = this.module.typeSection().getType(this.getTagType(currentCatch.tag()).typeIdx());
                                this.pushVals(tagType.params());
                                break;
                            }
                            case CATCH_REF: {
                                FunctionType tagType = this.module.typeSection().getType(this.getTagType(currentCatch.tag()).typeIdx());
                                this.pushVals(tagType.params());
                                this.pushVal(ValType.ExnRef);
                                break;
                            }
                            case CATCH_ALL: {
                                break;
                            }
                            case CATCH_ALL_REF: {
                                this.pushVal(ValType.ExnRef);
                            }
                        }
                        this.popCtrl();
                    }
                    this.pushCtrl(op.opcode(), t1, t2);
                    break;
                }
                case THROW: {
                    int tagNumber = (int)op.operand(0);
                    if (this.tagImports.size() + this.module.tagSection().map(TagSection::tagCount).orElse(0) <= tagNumber) {
                        throw new InvalidException("unknown tag " + tagNumber);
                    }
                    FunctionType type = this.module.typeSection().getType(this.getTagType(tagNumber).typeIdx());
                    this.popVals(type.params());
                    assert (type.returns().size() == 0);
                    this.unreachable();
                    break;
                }
                case THROW_REF: {
                    this.popVal(ValType.ExnRef);
                    this.unreachable();
                    break;
                }
                case IF: {
                    this.popVal(ValType.I32);
                }
                case LOOP: 
                case BLOCK: {
                    List<ValType> t1 = this.getParams(op);
                    List<ValType> t2 = this.getReturns(op);
                    t2.forEach(this::validateValueType);
                    this.popVals(t1);
                    this.pushCtrl(op.opcode(), t1, t2);
                    break;
                }
                case END: {
                    CtrlFrame frame = this.popCtrl();
                    if (frame.opCode == OpCode.IF && !frame.hasElse && frame.startTypes.size() != frame.endTypes.size()) {
                        throw new InvalidException("type mismatch, unbalanced if branches");
                    }
                    this.pushVals(frame.endTypes);
                    break;
                }
                case ELSE: {
                    CtrlFrame frame = this.popCtrl();
                    if (frame.opCode != OpCode.IF) {
                        throw new InvalidException("else doesn't belong to if");
                    }
                    this.pushCtrl(op.opcode(), frame.startTypes, frame.endTypes);
                    this.peekCtrl().hasElse = true;
                    break;
                }
                case BR: {
                    int n = (int)op.operand(0);
                    this.popVals(Validator.labelTypes(this.getCtrl(n)));
                    this.unreachable();
                    break;
                }
                case BR_IF: {
                    this.popVal(ValType.I32);
                    int n = (int)op.operand(0);
                    List<ValType> labelTypes = Validator.labelTypes(this.getCtrl(n));
                    this.popVals(labelTypes);
                    this.pushVals(labelTypes);
                    break;
                }
                case BR_TABLE: {
                    int idx;
                    this.popVal(ValType.I32);
                    int m = (int)op.operand(op.operandCount() - 1);
                    if (this.ctrlFrameStack.size() - 1 - m < 0) {
                        throw new InvalidException("unknown label " + m);
                    }
                    List<ValType> defaultBranchLabelTypes = Validator.labelTypes(this.getCtrl(m));
                    int arity = defaultBranchLabelTypes.size();
                    for (idx = 0; idx < op.operandCount() - 1; ++idx) {
                        CtrlFrame ctrlFrame;
                        int n = (int)op.operand(idx);
                        try {
                            ctrlFrame = this.getCtrl(n);
                        }
                        catch (IndexOutOfBoundsException e) {
                            throw new InvalidException("unknown label", e);
                        }
                        List<ValType> labelTypes = Validator.labelTypes(ctrlFrame);
                        if (labelTypes.size() != arity) {
                            throw new InvalidException("type mismatch, mismatched arity in BR_TABLE for label " + n);
                        }
                        this.pushVals(this.popVals(labelTypes));
                    }
                    this.popVals(defaultBranchLabelTypes);
                    this.unreachable();
                    break;
                }
                case BR_ON_NULL: {
                    int n = (int)op.operand(0);
                    ValType rt = this.popRef();
                    List<ValType> labelTypes = Validator.labelTypes(this.getCtrl(n));
                    this.popVals(labelTypes);
                    this.pushVals(labelTypes);
                    this.pushVal(this.valType(100, rt.typeIdx()));
                    break;
                }
                case BR_ON_NON_NULL: {
                    int n = (int)op.operand(0);
                    ValType rt = this.popRef();
                    this.pushVal(this.valType(100, rt.typeIdx()));
                    List<ValType> labelTypes = Validator.labelTypes(this.getCtrl(n));
                    this.popVals(labelTypes);
                    this.pushVals(labelTypes);
                    this.popVal();
                    break;
                }
                case RETURN: {
                    this.VALIDATE_RETURN();
                    break;
                }
                case RETURN_CALL: {
                    this.VALIDATE_CALL((int)op.operand(0), true);
                    this.VALIDATE_RETURN();
                    break;
                }
                case RETURN_CALL_INDIRECT: {
                    this.VALIDATE_CALL_INDIRECT(op.operand(0), (int)op.operand(1), true);
                    this.VALIDATE_RETURN();
                    break;
                }
                case RETURN_CALL_REF: {
                    this.VALIDATE_CALL_REF((int)op.operand(0), true);
                    this.VALIDATE_RETURN();
                    break;
                }
            }
            switch (op.opcode()) {
                case MEMORY_COPY: {
                    this.validateMemory((int)op.operand(0));
                    this.validateMemory((int)op.operand(1));
                    break;
                }
                case MEMORY_FILL: {
                    this.validateMemory((int)op.operand(0));
                    break;
                }
                case MEMORY_INIT: {
                    this.validateMemory((int)op.operand(1));
                    this.validateDataSegment((int)op.operand(0));
                    break;
                }
                case MEMORY_SIZE: 
                case MEMORY_GROW: 
                case I32_LOAD: 
                case I32_LOAD8_U: 
                case I32_ATOMIC_LOAD8_U: 
                case I32_LOAD8_S: 
                case I32_LOAD16_U: 
                case I32_ATOMIC_LOAD16_U: 
                case I32_LOAD16_S: 
                case I64_LOAD: 
                case I64_LOAD8_S: 
                case I64_LOAD8_U: 
                case I64_ATOMIC_LOAD8_U: 
                case I64_ATOMIC_LOAD16_U: 
                case I64_LOAD16_S: 
                case I64_LOAD16_U: 
                case I64_LOAD32_S: 
                case I64_LOAD32_U: 
                case I64_ATOMIC_LOAD32_U: 
                case F32_LOAD: 
                case F64_LOAD: 
                case I32_STORE: 
                case I32_ATOMIC_STORE: 
                case I32_STORE8: 
                case I32_ATOMIC_STORE8: 
                case I32_STORE16: 
                case I32_ATOMIC_STORE16: 
                case I32_ATOMIC_RMW_ADD: 
                case I32_ATOMIC_RMW_CMPXCHG: 
                case I32_ATOMIC_RMW8_CMPXCHG_U: 
                case I32_ATOMIC_RMW16_CMPXCHG_U: 
                case I64_ATOMIC_RMW_CMPXCHG: 
                case I64_ATOMIC_RMW8_CMPXCHG_U: 
                case I64_ATOMIC_RMW16_CMPXCHG_U: 
                case I64_ATOMIC_RMW32_CMPXCHG_U: 
                case I32_ATOMIC_RMW_XCHG: 
                case I32_ATOMIC_RMW_OR: 
                case I32_ATOMIC_RMW_XOR: 
                case I32_ATOMIC_RMW_SUB: 
                case I32_ATOMIC_RMW_AND: 
                case I32_ATOMIC_RMW8_ADD_U: 
                case I32_ATOMIC_RMW8_XCHG_U: 
                case I32_ATOMIC_RMW8_OR_U: 
                case I32_ATOMIC_RMW8_XOR_U: 
                case I32_ATOMIC_RMW8_AND_U: 
                case I32_ATOMIC_RMW8_SUB_U: 
                case I32_ATOMIC_RMW16_ADD_U: 
                case I32_ATOMIC_RMW16_XCHG_U: 
                case I32_ATOMIC_RMW16_OR_U: 
                case I32_ATOMIC_RMW16_XOR_U: 
                case I32_ATOMIC_RMW16_AND_U: 
                case I32_ATOMIC_RMW16_SUB_U: 
                case I64_STORE: 
                case I64_ATOMIC_STORE: 
                case I64_STORE8: 
                case I64_ATOMIC_STORE8: 
                case I64_STORE16: 
                case I64_ATOMIC_STORE16: 
                case I64_STORE32: 
                case I64_ATOMIC_STORE32: 
                case F32_STORE: 
                case F64_STORE: 
                case I64_ATOMIC_RMW_ADD: 
                case I64_ATOMIC_RMW_XCHG: 
                case I64_ATOMIC_RMW_OR: 
                case I64_ATOMIC_RMW_XOR: 
                case I64_ATOMIC_RMW_SUB: 
                case I64_ATOMIC_RMW_AND: 
                case I64_ATOMIC_RMW8_ADD_U: 
                case I64_ATOMIC_RMW8_XCHG_U: 
                case I64_ATOMIC_RMW8_OR_U: 
                case I64_ATOMIC_RMW8_XOR_U: 
                case I64_ATOMIC_RMW8_AND_U: 
                case I64_ATOMIC_RMW8_SUB_U: 
                case I64_ATOMIC_RMW16_ADD_U: 
                case I64_ATOMIC_RMW16_XCHG_U: 
                case I64_ATOMIC_RMW16_OR_U: 
                case I64_ATOMIC_RMW16_XOR_U: 
                case I64_ATOMIC_RMW16_AND_U: 
                case I64_ATOMIC_RMW16_SUB_U: 
                case I64_ATOMIC_RMW32_ADD_U: 
                case I64_ATOMIC_RMW32_XCHG_U: 
                case I64_ATOMIC_RMW32_OR_U: 
                case I64_ATOMIC_RMW32_XOR_U: 
                case I64_ATOMIC_RMW32_AND_U: 
                case I64_ATOMIC_RMW32_SUB_U: 
                case V128_STORE: 
                case MEM_ATOMIC_NOTIFY: 
                case MEM_ATOMIC_WAIT32: 
                case MEM_ATOMIC_WAIT64: 
                case I32_ATOMIC_LOAD: 
                case I64_ATOMIC_LOAD: {
                    this.validateMemory(0);
                    break;
                }
            }
            switch (op.opcode()) {
                case I32_ATOMIC_LOAD8_U: 
                case I64_ATOMIC_LOAD8_U: 
                case I32_ATOMIC_STORE8: 
                case I32_ATOMIC_RMW8_CMPXCHG_U: 
                case I64_ATOMIC_RMW8_CMPXCHG_U: 
                case I32_ATOMIC_RMW8_ADD_U: 
                case I32_ATOMIC_RMW8_XCHG_U: 
                case I32_ATOMIC_RMW8_OR_U: 
                case I32_ATOMIC_RMW8_XOR_U: 
                case I32_ATOMIC_RMW8_AND_U: 
                case I32_ATOMIC_RMW8_SUB_U: 
                case I64_ATOMIC_STORE8: 
                case I64_ATOMIC_RMW8_ADD_U: 
                case I64_ATOMIC_RMW8_XCHG_U: 
                case I64_ATOMIC_RMW8_OR_U: 
                case I64_ATOMIC_RMW8_XOR_U: 
                case I64_ATOMIC_RMW8_AND_U: 
                case I64_ATOMIC_RMW8_SUB_U: 
                case ATOMIC_FENCE: {
                    this.validateMemAlign(op.operand(0), 0L);
                    break;
                }
                case I32_ATOMIC_LOAD16_U: 
                case I64_ATOMIC_LOAD16_U: 
                case I32_ATOMIC_STORE16: 
                case I32_ATOMIC_RMW16_CMPXCHG_U: 
                case I64_ATOMIC_RMW16_CMPXCHG_U: 
                case I32_ATOMIC_RMW16_ADD_U: 
                case I32_ATOMIC_RMW16_XCHG_U: 
                case I32_ATOMIC_RMW16_OR_U: 
                case I32_ATOMIC_RMW16_XOR_U: 
                case I32_ATOMIC_RMW16_AND_U: 
                case I32_ATOMIC_RMW16_SUB_U: 
                case I64_ATOMIC_STORE16: 
                case I64_ATOMIC_RMW16_ADD_U: 
                case I64_ATOMIC_RMW16_XCHG_U: 
                case I64_ATOMIC_RMW16_OR_U: 
                case I64_ATOMIC_RMW16_XOR_U: 
                case I64_ATOMIC_RMW16_AND_U: 
                case I64_ATOMIC_RMW16_SUB_U: {
                    this.validateMemAlign(op.operand(0), 1L);
                    break;
                }
                case I64_ATOMIC_LOAD32_U: 
                case I32_ATOMIC_STORE: 
                case I32_ATOMIC_RMW_ADD: 
                case I32_ATOMIC_RMW_CMPXCHG: 
                case I64_ATOMIC_RMW32_CMPXCHG_U: 
                case I32_ATOMIC_RMW_XCHG: 
                case I32_ATOMIC_RMW_OR: 
                case I32_ATOMIC_RMW_XOR: 
                case I32_ATOMIC_RMW_SUB: 
                case I32_ATOMIC_RMW_AND: 
                case I64_ATOMIC_STORE32: 
                case I64_ATOMIC_RMW32_ADD_U: 
                case I64_ATOMIC_RMW32_XCHG_U: 
                case I64_ATOMIC_RMW32_OR_U: 
                case I64_ATOMIC_RMW32_XOR_U: 
                case I64_ATOMIC_RMW32_AND_U: 
                case I64_ATOMIC_RMW32_SUB_U: 
                case MEM_ATOMIC_NOTIFY: 
                case MEM_ATOMIC_WAIT32: 
                case I32_ATOMIC_LOAD: {
                    this.validateMemAlign(op.operand(0), 2L);
                    break;
                }
                case I64_ATOMIC_RMW_CMPXCHG: 
                case I64_ATOMIC_STORE: 
                case I64_ATOMIC_RMW_ADD: 
                case I64_ATOMIC_RMW_XCHG: 
                case I64_ATOMIC_RMW_OR: 
                case I64_ATOMIC_RMW_XOR: 
                case I64_ATOMIC_RMW_SUB: 
                case I64_ATOMIC_RMW_AND: 
                case MEM_ATOMIC_WAIT64: 
                case I64_ATOMIC_LOAD: {
                    this.validateMemAlign(op.operand(0), 3L);
                }
            }
            switch (op.opcode()) {
                case V128_LOAD8_LANE: 
                case V128_STORE8_LANE: {
                    this.validateLane((int)op.operand(2), 16);
                    break;
                }
                case V128_LOAD16_LANE: 
                case V128_STORE16_LANE: {
                    this.validateLane((int)op.operand(2), 8);
                    break;
                }
                case V128_LOAD32_LANE: 
                case V128_STORE32_LANE: {
                    this.validateLane((int)op.operand(2), 4);
                    break;
                }
                case V128_LOAD64_LANE: 
                case V128_STORE64_LANE: {
                    this.validateLane((int)op.operand(2), 2);
                    break;
                }
                case I8x16_REPLACE_LANE: 
                case I8x16_EXTRACT_LANE_S: 
                case I8x16_EXTRACT_LANE_U: {
                    this.validateLane((int)op.operand(0), 16);
                    break;
                }
                case I16x8_REPLACE_LANE: 
                case I16x8_EXTRACT_LANE_S: 
                case I16x8_EXTRACT_LANE_U: {
                    this.validateLane((int)op.operand(0), 8);
                    break;
                }
                case I32x4_REPLACE_LANE: 
                case F32x4_REPLACE_LANE: 
                case I32x4_EXTRACT_LANE: 
                case F32x4_EXTRACT_LANE: {
                    this.validateLane((int)op.operand(0), 4);
                    break;
                }
                case I64x2_REPLACE_LANE: 
                case F64x2_REPLACE_LANE: 
                case I64x2_EXTRACT_LANE: 
                case F64x2_EXTRACT_LANE: {
                    this.validateLane((int)op.operand(0), 2);
                    break;
                }
                case I8x16_SHUFFLE: {
                    byte[] operands = Value.vecTo8(new long[]{op.operand(0), op.operand(1)});
                    for (int j = 0; j < 16; ++j) {
                        this.validateLane(operands[j], 32);
                    }
                    break;
                }
            }
            switch (op.opcode()) {
                case END: 
                case UNREACHABLE: 
                case TRY_TABLE: 
                case THROW: 
                case THROW_REF: 
                case IF: 
                case LOOP: 
                case BLOCK: 
                case ELSE: 
                case BR: 
                case BR_IF: 
                case BR_TABLE: 
                case BR_ON_NULL: 
                case BR_ON_NON_NULL: 
                case RETURN: 
                case RETURN_CALL: 
                case RETURN_CALL_INDIRECT: 
                case RETURN_CALL_REF: 
                case ATOMIC_FENCE: 
                case NOP: {
                    continue block133;
                }
                case DATA_DROP: {
                    this.validateDataSegment((int)op.operand(0));
                    continue block133;
                }
                case DROP: {
                    ValType t2 = this.popVal();
                    if (t2.opcode() != 123) continue block133;
                    op.setOperand(0, 123L);
                    continue block133;
                }
                case MEM_ATOMIC_NOTIFY: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case MEM_ATOMIC_WAIT32: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case MEM_ATOMIC_WAIT64: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_STORE: 
                case I32_ATOMIC_STORE: 
                case I32_STORE8: 
                case I32_ATOMIC_STORE8: 
                case I32_STORE16: 
                case I32_ATOMIC_STORE16: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case MEMORY_GROW: 
                case I32_LOAD: 
                case I32_LOAD8_U: 
                case I32_ATOMIC_LOAD8_U: 
                case I32_LOAD8_S: 
                case I32_LOAD16_U: 
                case I32_ATOMIC_LOAD16_U: 
                case I32_LOAD16_S: 
                case I32_ATOMIC_LOAD: 
                case I32_CLZ: 
                case I32_CTZ: 
                case I32_POPCNT: 
                case I32_EXTEND_8_S: 
                case I32_EXTEND_16_S: 
                case I32_EQZ: {
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_CONST: 
                case MEMORY_SIZE: 
                case TABLE_SIZE: {
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_ATOMIC_RMW_CMPXCHG: 
                case I32_ATOMIC_RMW8_CMPXCHG_U: 
                case I32_ATOMIC_RMW16_CMPXCHG_U: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I64_ATOMIC_RMW_CMPXCHG: 
                case I64_ATOMIC_RMW8_CMPXCHG_U: 
                case I64_ATOMIC_RMW16_CMPXCHG_U: 
                case I64_ATOMIC_RMW32_CMPXCHG_U: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I32_ADD: 
                case I32_SUB: 
                case I32_MUL: 
                case I32_ATOMIC_RMW_ADD: 
                case I32_ATOMIC_RMW_XCHG: 
                case I32_ATOMIC_RMW_OR: 
                case I32_ATOMIC_RMW_XOR: 
                case I32_ATOMIC_RMW_SUB: 
                case I32_ATOMIC_RMW_AND: 
                case I32_ATOMIC_RMW8_ADD_U: 
                case I32_ATOMIC_RMW8_XCHG_U: 
                case I32_ATOMIC_RMW8_OR_U: 
                case I32_ATOMIC_RMW8_XOR_U: 
                case I32_ATOMIC_RMW8_AND_U: 
                case I32_ATOMIC_RMW8_SUB_U: 
                case I32_ATOMIC_RMW16_ADD_U: 
                case I32_ATOMIC_RMW16_XCHG_U: 
                case I32_ATOMIC_RMW16_OR_U: 
                case I32_ATOMIC_RMW16_XOR_U: 
                case I32_ATOMIC_RMW16_AND_U: 
                case I32_ATOMIC_RMW16_SUB_U: 
                case I32_DIV_S: 
                case I32_DIV_U: 
                case I32_REM_S: 
                case I32_REM_U: 
                case I32_AND: 
                case I32_OR: 
                case I32_XOR: 
                case I32_EQ: 
                case I32_NE: 
                case I32_LT_S: 
                case I32_LT_U: 
                case I32_LE_S: 
                case I32_LE_U: 
                case I32_GT_S: 
                case I32_GT_U: 
                case I32_GE_S: 
                case I32_GE_U: 
                case I32_SHL: 
                case I32_SHR_U: 
                case I32_SHR_S: 
                case I32_ROTL: 
                case I32_ROTR: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_WRAP_I64: 
                case I64_EQZ: {
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_TRUNC_F32_S: 
                case I32_TRUNC_F32_U: 
                case I32_TRUNC_SAT_F32_S: 
                case I32_TRUNC_SAT_F32_U: 
                case I32_REINTERPRET_F32: {
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I32_TRUNC_F64_S: 
                case I32_TRUNC_F64_U: 
                case I32_TRUNC_SAT_F64_S: 
                case I32_TRUNC_SAT_F64_U: {
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I64_LOAD: 
                case I64_LOAD8_S: 
                case I64_LOAD8_U: 
                case I64_ATOMIC_LOAD8_U: 
                case I64_ATOMIC_LOAD16_U: 
                case I64_LOAD16_S: 
                case I64_LOAD16_U: 
                case I64_LOAD32_S: 
                case I64_LOAD32_U: 
                case I64_ATOMIC_LOAD32_U: 
                case I64_ATOMIC_LOAD: 
                case I64_EXTEND_I32_U: 
                case I64_EXTEND_I32_S: {
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_ATOMIC_RMW_ADD: 
                case I64_ATOMIC_RMW_XCHG: 
                case I64_ATOMIC_RMW_OR: 
                case I64_ATOMIC_RMW_XOR: 
                case I64_ATOMIC_RMW_SUB: 
                case I64_ATOMIC_RMW_AND: 
                case I64_ATOMIC_RMW8_ADD_U: 
                case I64_ATOMIC_RMW8_XCHG_U: 
                case I64_ATOMIC_RMW8_OR_U: 
                case I64_ATOMIC_RMW8_XOR_U: 
                case I64_ATOMIC_RMW8_AND_U: 
                case I64_ATOMIC_RMW8_SUB_U: 
                case I64_ATOMIC_RMW16_ADD_U: 
                case I64_ATOMIC_RMW16_XCHG_U: 
                case I64_ATOMIC_RMW16_OR_U: 
                case I64_ATOMIC_RMW16_XOR_U: 
                case I64_ATOMIC_RMW16_AND_U: 
                case I64_ATOMIC_RMW16_SUB_U: 
                case I64_ATOMIC_RMW32_ADD_U: 
                case I64_ATOMIC_RMW32_XCHG_U: 
                case I64_ATOMIC_RMW32_OR_U: 
                case I64_ATOMIC_RMW32_XOR_U: 
                case I64_ATOMIC_RMW32_AND_U: 
                case I64_ATOMIC_RMW32_SUB_U: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_CONST: {
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_STORE: 
                case I64_ATOMIC_STORE: 
                case I64_STORE8: 
                case I64_ATOMIC_STORE8: 
                case I64_STORE16: 
                case I64_ATOMIC_STORE16: 
                case I64_STORE32: 
                case I64_ATOMIC_STORE32: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case I64_ADD: 
                case I64_SUB: 
                case I64_MUL: 
                case I64_DIV_S: 
                case I64_DIV_U: 
                case I64_REM_S: 
                case I64_REM_U: 
                case I64_AND: 
                case I64_OR: 
                case I64_XOR: 
                case I64_SHL: 
                case I64_SHR_U: 
                case I64_SHR_S: 
                case I64_ROTL: 
                case I64_ROTR: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_EQ: 
                case I64_NE: 
                case I64_LT_S: 
                case I64_LT_U: 
                case I64_LE_S: 
                case I64_LE_U: 
                case I64_GT_S: 
                case I64_GT_U: 
                case I64_GE_S: 
                case I64_GE_U: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case I64_CLZ: 
                case I64_CTZ: 
                case I64_POPCNT: 
                case I64_EXTEND_8_S: 
                case I64_EXTEND_16_S: 
                case I64_EXTEND_32_S: {
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_REINTERPRET_F64: 
                case I64_TRUNC_F64_S: 
                case I64_TRUNC_F64_U: 
                case I64_TRUNC_SAT_F64_S: 
                case I64_TRUNC_SAT_F64_U: {
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case I64_TRUNC_F32_S: 
                case I64_TRUNC_F32_U: 
                case I64_TRUNC_SAT_F32_S: 
                case I64_TRUNC_SAT_F32_U: {
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case F32_STORE: {
                    this.popVal(ValType.F32);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case F32_CONST: {
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F32_LOAD: 
                case F32_CONVERT_I32_S: 
                case F32_CONVERT_I32_U: 
                case F32_REINTERPRET_I32: {
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F32_CONVERT_I64_S: 
                case F32_CONVERT_I64_U: {
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F64_LOAD: 
                case F64_CONVERT_I32_S: 
                case F64_CONVERT_I32_U: {
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F64_CONVERT_I64_S: 
                case F64_CONVERT_I64_U: 
                case F64_REINTERPRET_I64: {
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F64_PROMOTE_F32: {
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F32_DEMOTE_F64: {
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F32_SQRT: 
                case F32_ABS: 
                case F32_NEG: 
                case F32_CEIL: 
                case F32_FLOOR: 
                case F32_TRUNC: 
                case F32_NEAREST: {
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F32_ADD: 
                case F32_SUB: 
                case F32_MUL: 
                case F32_DIV: 
                case F32_MIN: 
                case F32_MAX: 
                case F32_COPYSIGN: {
                    this.popVal(ValType.F32);
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case F32_EQ: 
                case F32_NE: 
                case F32_LT: 
                case F32_LE: 
                case F32_GT: 
                case F32_GE: {
                    this.popVal(ValType.F32);
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case F64_STORE: {
                    this.popVal(ValType.F64);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case F64_CONST: {
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F64_SQRT: 
                case F64_ABS: 
                case F64_NEG: 
                case F64_CEIL: 
                case F64_FLOOR: 
                case F64_TRUNC: 
                case F64_NEAREST: {
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F64_ADD: 
                case F64_SUB: 
                case F64_MUL: 
                case F64_DIV: 
                case F64_MIN: 
                case F64_MAX: 
                case F64_COPYSIGN: {
                    this.popVal(ValType.F64);
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case F64_EQ: 
                case F64_NE: 
                case F64_LT: 
                case F64_LE: 
                case F64_GT: 
                case F64_GE: {
                    this.popVal(ValType.F64);
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case LOCAL_SET: {
                    int index = (int)op.operand(0);
                    this.popVal(this.getLocalType(index));
                    this.setLocal(index);
                    continue block133;
                }
                case LOCAL_GET: {
                    int index = (int)op.operand(0);
                    this.getLocal(index);
                    this.pushVal(this.getLocalType(index));
                    continue block133;
                }
                case LOCAL_TEE: {
                    int index = (int)op.operand(0);
                    ValType actualType = this.popVal();
                    this.setLocal(index);
                    ValType localType = this.getLocalType(index);
                    if (!ValType.matches(actualType, localType)) {
                        throw new InvalidException("type mismatch: local_tee: " + String.valueOf(actualType) + " " + String.valueOf(localType));
                    }
                    this.pushVal(localType);
                    continue block133;
                }
                case GLOBAL_GET: {
                    Global global = this.getGlobal((int)op.operand(0));
                    this.pushVal(global.valueType());
                    continue block133;
                }
                case GLOBAL_SET: {
                    Global global = this.getGlobal((int)op.operand(0));
                    if (global.mutabilityType() == MutabilityType.Const) {
                        throw new InvalidException("global is immutable, immutable global");
                    }
                    this.popVal(global.valueType());
                    continue block133;
                }
                case CALL: {
                    this.VALIDATE_CALL((int)op.operand(0), false);
                    continue block133;
                }
                case CALL_INDIRECT: {
                    this.VALIDATE_CALL_INDIRECT(op.operand(0), (int)op.operand(1), false);
                    continue block133;
                }
                case CALL_REF: {
                    this.VALIDATE_CALL_REF((int)op.operand(0), false);
                    continue block133;
                }
                case REF_NULL: {
                    int operand = (int)op.operand(0);
                    ValType type = this.valType(99, operand);
                    this.pushVal(type);
                    continue block133;
                }
                case REF_IS_NULL: {
                    this.popRef();
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case REF_AS_NON_NULL: {
                    ValType rt = this.popRef();
                    this.pushVal(this.valType(100, rt.typeIdx()));
                    continue block133;
                }
                case REF_FUNC: {
                    int idx = (int)op.operand(0);
                    if (idx == funcIdx && !this.declaredFunctions.contains(idx)) {
                        throw new InvalidException("undeclared function reference");
                    }
                    this.pushVal(this.valType(100, this.getFunctionType(idx)));
                    continue block133;
                }
                case SELECT: {
                    this.popVal(ValType.I32);
                    ValType t1 = this.popVal();
                    ValType t2 = this.popVal();
                    if (t1.opcode() == 123 && t2.opcode() == 123 || t1.opcode() == 123 && t2.opcode() == -1 || t1.opcode() == -1 && t2.opcode() == 123) {
                        op.setOperand(0, 123L);
                        this.pushVal(ValType.V128);
                        continue block133;
                    }
                    if (!Validator.isNum(t1) || !Validator.isNum(t2)) {
                        throw new InvalidException("type mismatch: select should have numeric arguments but they are " + String.valueOf(t1) + " " + String.valueOf(t2));
                    }
                    if (!(t1.equals(t2) || t1.equals(ValType.BOT) || t2.equals(ValType.BOT))) {
                        throw new InvalidException("type mismatch, in SELECT t1: " + String.valueOf(t1) + ", t2: " + String.valueOf(t2));
                    }
                    if (t1.equals(ValType.BOT)) {
                        this.pushVal(t2);
                        continue block133;
                    }
                    this.pushVal(t1);
                    continue block133;
                }
                case SELECT_T: {
                    this.popVal(ValType.I32);
                    if (op.operands().length <= 0 || op.operands().length > 1) {
                        throw new InvalidException("invalid result arity");
                    }
                    ValType t3 = this.valType(op.operand(0));
                    this.validateValueType(t3);
                    this.popVal(t3);
                    this.popVal(t3);
                    this.pushVal(t3);
                    continue block133;
                }
                case TABLE_COPY: {
                    ValType table1 = this.getTableType((int)op.operand(1));
                    ValType table2 = this.getTableType((int)op.operand(0));
                    if (!ValType.matches(table1, table2)) {
                        throw new InvalidException("type mismatch, table 1 type: " + String.valueOf(table1) + ", table 2 type: " + String.valueOf(table2));
                    }
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case TABLE_INIT: {
                    ValType table = this.getTableType((int)op.operand(1));
                    int elemIdx = (int)op.operand(0);
                    Element elem = this.getElement(elemIdx);
                    if (!ValType.matches(elem.type(), table)) {
                        throw new InvalidException("type mismatch, table type: " + String.valueOf(table) + ", elem type: " + String.valueOf(elem.type()));
                    }
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case MEMORY_COPY: 
                case MEMORY_FILL: 
                case MEMORY_INIT: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case TABLE_FILL: {
                    this.popVal(ValType.I32);
                    this.popVal(this.getTableType((int)op.operand(0)));
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case TABLE_GET: {
                    this.popVal(ValType.I32);
                    this.pushVal(this.getTableType((int)op.operand(0)));
                    continue block133;
                }
                case TABLE_SET: {
                    this.popVal(this.getTableType((int)op.operand(0)));
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case TABLE_GROW: {
                    this.popVal(ValType.I32);
                    this.popVal(this.getTableType((int)op.operand(0)));
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case ELEM_DROP: {
                    int index = (int)op.operand(0);
                    this.getElement(index);
                    continue block133;
                }
                case V128_LOAD: 
                case V128_LOAD8x8_S: 
                case V128_LOAD8x8_U: 
                case V128_LOAD16x4_S: 
                case V128_LOAD16x4_U: 
                case V128_LOAD32x2_S: 
                case V128_LOAD32x2_U: 
                case V128_LOAD8_SPLAT: 
                case V128_LOAD16_SPLAT: 
                case V128_LOAD32_SPLAT: 
                case V128_LOAD64_SPLAT: 
                case V128_LOAD32_ZERO: 
                case V128_LOAD64_ZERO: 
                case I8x16_SPLAT: 
                case I16x8_SPLAT: 
                case I32x4_SPLAT: {
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case F32x4_SPLAT: {
                    this.popVal(ValType.F32);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case I64x2_SPLAT: {
                    this.popVal(ValType.I64);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case F64x2_SPLAT: {
                    this.popVal(ValType.F64);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case V128_CONST: {
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case I8x16_REPLACE_LANE: 
                case I16x8_REPLACE_LANE: 
                case I32x4_REPLACE_LANE: 
                case I8x16_SHL: 
                case I8x16_SHR_S: 
                case I8x16_SHR_U: 
                case I16x8_SHL: 
                case I16x8_SHR_S: 
                case I16x8_SHR_U: 
                case I32x4_SHL: 
                case I32x4_SHR_S: 
                case I32x4_SHR_U: 
                case I64x2_SHL: 
                case I64x2_SHR_S: 
                case I64x2_SHR_U: {
                    this.popVal(ValType.I32);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case I64x2_REPLACE_LANE: {
                    this.popVal(ValType.I64);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case F32x4_REPLACE_LANE: {
                    this.popVal(ValType.F32);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case F64x2_REPLACE_LANE: {
                    this.popVal(ValType.F64);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case I8x16_EXTRACT_LANE_S: 
                case I8x16_EXTRACT_LANE_U: 
                case I16x8_EXTRACT_LANE_S: 
                case I16x8_EXTRACT_LANE_U: 
                case I32x4_EXTRACT_LANE: 
                case I8x16_ALL_TRUE: 
                case I8x16_BITMASK: 
                case I16x8_ALL_TRUE: 
                case I16x8_BITMASK: 
                case I32x4_ALL_TRUE: 
                case I32x4_BITMASK: 
                case I64x2_ALL_TRUE: 
                case I64x2_BITMASK: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case F32x4_EXTRACT_LANE: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.F32);
                    continue block133;
                }
                case I64x2_EXTRACT_LANE: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.I64);
                    continue block133;
                }
                case F64x2_EXTRACT_LANE: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.F64);
                    continue block133;
                }
                case I8x16_SHUFFLE: 
                case I8x16_SWIZZLE: 
                case I8x16_EQ: 
                case I8x16_NE: 
                case I8x16_LT_S: 
                case I8x16_LT_U: 
                case I8x16_GT_S: 
                case I8x16_GT_U: 
                case I8x16_LE_S: 
                case I8x16_LE_U: 
                case I8x16_GE_S: 
                case I8x16_GE_U: 
                case I8x16_MIN_S: 
                case I8x16_MIN_U: 
                case I8x16_MAX_S: 
                case I8x16_MAX_U: 
                case I8x16_AVGR_U: 
                case I8x16_SUB: 
                case I8x16_SUB_SAT_S: 
                case I8x16_SUB_SAT_U: 
                case I8x16_ADD: 
                case I8x16_ADD_SAT_S: 
                case I8x16_ADD_SAT_U: 
                case I8x16_NARROW_I16x8_S: 
                case I8x16_NARROW_I16x8_U: 
                case I16x8_NE: 
                case I16x8_EQ: 
                case I16x8_ADD: 
                case I16x8_ADD_SAT_S: 
                case I16x8_ADD_SAT_U: 
                case I16x8_SUB: 
                case I16x8_SUB_SAT_S: 
                case I16x8_SUB_SAT_U: 
                case I16x8_MUL: 
                case I16x8_LT_S: 
                case I16x8_LT_U: 
                case I16x8_GT_S: 
                case I16x8_GT_U: 
                case I16x8_LE_S: 
                case I16x8_LE_U: 
                case I16x8_GE_S: 
                case I16x8_GE_U: 
                case I16x8_MIN_S: 
                case I16x8_MIN_U: 
                case I16x8_MAX_S: 
                case I16x8_MAX_U: 
                case I16x8_AVGR_U: 
                case I16x8_NARROW_I32x4_S: 
                case I16x8_NARROW_I32x4_U: 
                case I16x8_Q15MULR_SAT_S: 
                case I16x8_EXTMUL_LOW_I8x16_S: 
                case I16x8_EXTMUL_HIGH_I8x16_S: 
                case I16x8_EXTMUL_LOW_I8x16_U: 
                case I16x8_EXTMUL_HIGH_I8x16_U: 
                case F32x4_NE: 
                case F32x4_LT: 
                case F32x4_GT: 
                case F32x4_LE: 
                case F32x4_GE: 
                case F32x4_EQ: 
                case F32x4_MUL: 
                case F32x4_MIN: 
                case F32x4_MAX: 
                case F32x4_PMIN: 
                case F32x4_PMAX: 
                case F32x4_DIV: 
                case F32x4_ADD: 
                case F32x4_SUB: 
                case I32x4_NE: 
                case I32x4_EQ: 
                case I32x4_ADD: 
                case I32x4_SUB: 
                case I32x4_MUL: 
                case I32x4_MIN_S: 
                case I32x4_MIN_U: 
                case I32x4_MAX_S: 
                case I32x4_MAX_U: 
                case I32x4_LT_S: 
                case I32x4_LT_U: 
                case I32x4_LE_S: 
                case I32x4_LE_U: 
                case I32x4_GE_S: 
                case I32x4_GE_U: 
                case I32x4_GT_S: 
                case I32x4_GT_U: 
                case I32x4_DOT_I16x8_S: 
                case I32x4_EXTMUL_LOW_I16x8_S: 
                case I32x4_EXTMUL_HIGH_I16x8_S: 
                case I32x4_EXTMUL_LOW_I16x8_U: 
                case I32x4_EXTMUL_HIGH_I16x8_U: 
                case I64x2_NE: 
                case I64x2_EQ: 
                case I64x2_LT_S: 
                case I64x2_LE_S: 
                case I64x2_GT_S: 
                case I64x2_GE_S: 
                case I64x2_ADD: 
                case I64x2_SUB: 
                case I64x2_MUL: 
                case I64x2_EXTMUL_LOW_I32x4_S: 
                case I64x2_EXTMUL_HIGH_I32x4_S: 
                case I64x2_EXTMUL_LOW_I32x4_U: 
                case I64x2_EXTMUL_HIGH_I32x4_U: 
                case F64x2_NE: 
                case F64x2_LT: 
                case F64x2_GT: 
                case F64x2_LE: 
                case F64x2_GE: 
                case F64x2_DIV: 
                case F64x2_MAX: 
                case F64x2_MIN: 
                case F64x2_PMAX: 
                case F64x2_PMIN: 
                case F64x2_EQ: 
                case F64x2_ADD: 
                case F64x2_SUB: 
                case F64x2_MUL: 
                case V128_AND: 
                case V128_ANDNOT: 
                case V128_OR: 
                case V128_XOR: {
                    this.popVal(ValType.V128);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case I8x16_NEG: 
                case I8x16_ABS: 
                case I8x16_POPCNT: 
                case I16x8_EXTADD_PAIRWISE_I8x16_S: 
                case I16x8_EXTADD_PAIRWISE_I8x16_U: 
                case I16x8_NEG: 
                case I16x8_ABS: 
                case I16x8_EXTEND_LOW_I8x16_S: 
                case I16x8_EXTEND_HIGH_I8x16_S: 
                case I16x8_EXTEND_LOW_I8x16_U: 
                case I16x8_EXTEND_HIGH_I8x16_U: 
                case I32x4_NEG: 
                case I32x4_ABS: 
                case I32x4_EXTEND_LOW_I16x8_S: 
                case I32x4_EXTEND_HIGH_I16x8_S: 
                case I32x4_EXTEND_LOW_I16x8_U: 
                case I32x4_EXTEND_HIGH_I16x8_U: 
                case I32x4_EXTADD_PAIRWISE_I16x8_S: 
                case I32x4_EXTADD_PAIRWISE_I16x8_U: 
                case I64x2_EXTEND_LOW_I32x4_S: 
                case I64x2_EXTEND_HIGH_I32x4_S: 
                case I64x2_EXTEND_LOW_I32x4_U: 
                case I64x2_EXTEND_HIGH_I32x4_U: 
                case F32x4_NEG: 
                case F32x4_ABS: 
                case F32x4_SQRT: 
                case I32x4_TRUNC_SAT_F32X4_S: 
                case I32x4_TRUNC_SAT_F32X4_U: 
                case I32x4_TRUNC_SAT_F64x2_S_ZERO: 
                case I32x4_TRUNC_SAT_F64x2_U_ZERO: 
                case F32x4_CONVERT_I32x4_S: 
                case F32x4_CONVERT_I32x4_U: 
                case F32x4_CEIL: 
                case F32x4_FLOOR: 
                case F32x4_TRUNC: 
                case F32x4_NEAREST: 
                case F64x2_ABS: 
                case F64x2_NEG: 
                case F64x2_SQRT: 
                case F64x2_CEIL: 
                case F64x2_FLOOR: 
                case F64x2_NEAREST: 
                case F64x2_TRUNC: 
                case F64x2_CONVERT_LOW_I32x4_S: 
                case F64x2_CONVERT_LOW_I32x4_U: 
                case F64x2_PROMOTE_LOW_F32x4: 
                case F32x4_DEMOTE_LOW_F64x2_ZERO: 
                case I64x2_NEG: 
                case I64x2_ABS: 
                case V128_NOT: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case V128_BITSELECT: {
                    this.popVal(ValType.V128);
                    this.popVal(ValType.V128);
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                case V128_ANY_TRUE: {
                    this.popVal(ValType.V128);
                    this.pushVal(ValType.I32);
                    continue block133;
                }
                case V128_STORE: 
                case V128_STORE8_LANE: 
                case V128_STORE16_LANE: 
                case V128_STORE32_LANE: 
                case V128_STORE64_LANE: {
                    this.popVal(ValType.V128);
                    this.popVal(ValType.I32);
                    continue block133;
                }
                case V128_LOAD8_LANE: 
                case V128_LOAD16_LANE: 
                case V128_LOAD32_LANE: 
                case V128_LOAD64_LANE: {
                    this.popVal(ValType.V128);
                    this.popVal(ValType.I32);
                    this.pushVal(ValType.V128);
                    continue block133;
                }
                default: {
                    throw new IllegalArgumentException("Missing type validation opcode handling for " + String.valueOf((Object)op.opcode()));
                }
            }
        }
        if (!this.errors.isEmpty()) {
            throw new InvalidException(this.errors.stream().map(Throwable::getMessage).collect(Collectors.joining(" - ")));
        }
        if (this.module.codeSection().isRequiresDataCount() && this.module.dataCountSection().isEmpty()) {
            throw new MalformedException("data count section required");
        }
    }

    private void validateTailCall(List<ValType> funcReturnType) {
        List<ValType> expected = Validator.labelTypes(this.ctrlFrameStack.get(0));
        if (funcReturnType.size() != expected.size()) {
            throw new InvalidException("type mismatch: return arity");
        }
        for (int i = 0; i < funcReturnType.size(); ++i) {
            if (ValType.matches(funcReturnType.get(i), expected.get(i))) continue;
            throw new InvalidException("type mismatch: tail call doesn't match frame type at index " + i);
        }
    }

    private void VALIDATE_CALL(int funcId, boolean isReturn) {
        int typeId = this.getFunctionType(funcId);
        FunctionType types = this.getType(typeId);
        for (int j = types.params().size() - 1; j >= 0; --j) {
            this.popVal(types.params().get(j));
        }
        this.pushVals(types.returns());
        if (isReturn) {
            this.validateTailCall(types.returns());
        }
    }

    private void VALIDATE_CALL_INDIRECT(long typeId, int tableId, boolean isReturn) {
        this.popVal(ValType.I32);
        ValType tableType = this.getTableType(tableId);
        if (!tableType.equals(ValType.FuncRef)) {
            throw new InvalidException("type mismatch expected a table of FuncRefs buf found " + String.valueOf(tableType));
        }
        FunctionType types = this.getType((int)typeId);
        for (int j = types.params().size() - 1; j >= 0; --j) {
            this.popVal(types.params().get(j));
        }
        this.pushVals(types.returns());
        if (isReturn) {
            this.validateTailCall(types.returns());
        }
    }

    private void VALIDATE_CALL_REF(int typeId, boolean isReturn) {
        int idx;
        ValType rt = this.popRef();
        FunctionType funcType = this.getType(typeId);
        this.popVals(funcType.params());
        this.pushVals(funcType.returns());
        if (isReturn) {
            this.validateTailCall(funcType.returns());
        }
        if (rt.typeIdx() != ValType.TypeIdxCode.BOT.code() && (idx = rt.typeIdx()) < 0) {
            throw new InvalidException("type mismatch: call_ref should be called on a defined reference type, got operand: " + idx);
        }
    }

    private void VALIDATE_RETURN() {
        this.popVals(Validator.labelTypes(this.ctrlFrameStack.get(0)));
        this.unreachable();
    }

    private static class CtrlFrame {
        final OpCode opCode;
        final List<ValType> startTypes;
        final List<ValType> endTypes;
        final int height;
        final int initHeight;
        boolean unreachable;
        boolean hasElse;

        CtrlFrame(OpCode opCode, List<ValType> startTypes, List<ValType> endTypes, int height, int initHeight, boolean unreachable, boolean hasElse) {
            this.opCode = opCode;
            this.startTypes = startTypes;
            this.endTypes = endTypes;
            this.initHeight = initHeight;
            this.height = height;
            this.unreachable = unreachable;
            this.hasElse = hasElse;
        }
    }
}

