/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.ControlTree;
import com.dylibso.chicory.wasm.Encoding;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.MalformedException;
import com.dylibso.chicory.wasm.ParserListener;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.io.InputStreams;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.ActiveElement;
import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.CatchOpCode;
import com.dylibso.chicory.wasm.types.CodeSection;
import com.dylibso.chicory.wasm.types.CustomSection;
import com.dylibso.chicory.wasm.types.DataCountSection;
import com.dylibso.chicory.wasm.types.DataSection;
import com.dylibso.chicory.wasm.types.DeclarativeElement;
import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.ElementSection;
import com.dylibso.chicory.wasm.types.Export;
import com.dylibso.chicory.wasm.types.ExportSection;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.FunctionImport;
import com.dylibso.chicory.wasm.types.FunctionSection;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Global;
import com.dylibso.chicory.wasm.types.GlobalImport;
import com.dylibso.chicory.wasm.types.GlobalSection;
import com.dylibso.chicory.wasm.types.ImportSection;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.Memory;
import com.dylibso.chicory.wasm.types.MemoryImport;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.MemorySection;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.NameCustomSection;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.PassiveDataSegment;
import com.dylibso.chicory.wasm.types.PassiveElement;
import com.dylibso.chicory.wasm.types.RawSection;
import com.dylibso.chicory.wasm.types.Section;
import com.dylibso.chicory.wasm.types.StartSection;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableImport;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.TableSection;
import com.dylibso.chicory.wasm.types.TagImport;
import com.dylibso.chicory.wasm.types.TagSection;
import com.dylibso.chicory.wasm.types.TagType;
import com.dylibso.chicory.wasm.types.TypeSection;
import com.dylibso.chicory.wasm.types.UnknownCustomSection;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.WasmEncoding;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Parser {
    private static final String DIGEST_ALGORITHM = System.getProperty("com.dylibso.chicory.wasm.Parser.DIGEST_ALGORITHM", "SHA-256");
    static final byte[] MAGIC_BYTES = new byte[]{0, 97, 115, 109};
    static final byte[] VERSION_BYTES = new byte[]{1, 0, 0, 0};
    private final Map<String, Function<byte[], CustomSection>> customParsers;
    private final BitSet includeSections;
    private TypeSection typeSection;
    private static final Map<String, Function<byte[], CustomSection>> DEFAULT_CUSTOM_PARSERS = Map.of("name", NameCustomSection::parse);

    private Parser() {
        this(null, DEFAULT_CUSTOM_PARSERS);
    }

    private Parser(BitSet includeSections, Map<String, Function<byte[], CustomSection>> customParsers) {
        this.includeSections = includeSections;
        this.customParsers = Map.copyOf(customParsers);
        this.typeSection = TypeSection.builder().build();
    }

    private static ByteBuffer readByteBuffer(InputStream is) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(InputStreams.readAllBytes(is));
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            return buffer;
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to read wasm bytes.", e);
        }
    }

    private static void onSection(WasmModule.Builder module, Section s) {
        switch (s.sectionId()) {
            case 0: {
                CustomSection customSection = (CustomSection)s;
                module.addCustomSection(customSection.name(), customSection);
                break;
            }
            case 1: {
                module.setTypeSection((TypeSection)s);
                break;
            }
            case 2: {
                module.setImportSection((ImportSection)s);
                break;
            }
            case 3: {
                module.setFunctionSection((FunctionSection)s);
                break;
            }
            case 4: {
                module.setTableSection((TableSection)s);
                break;
            }
            case 5: {
                module.setMemorySection((MemorySection)s);
                break;
            }
            case 6: {
                module.setGlobalSection((GlobalSection)s);
                break;
            }
            case 7: {
                module.setExportSection((ExportSection)s);
                break;
            }
            case 8: {
                module.setStartSection((StartSection)s);
                break;
            }
            case 9: {
                module.setElementSection((ElementSection)s);
                break;
            }
            case 10: {
                module.setCodeSection((CodeSection)s);
                break;
            }
            case 11: {
                module.setDataSection((DataSection)s);
                break;
            }
            case 12: {
                module.setDataCountSection((DataCountSection)s);
                break;
            }
            case 13: {
                module.setTagSection((TagSection)s);
                break;
            }
            default: {
                module.addIgnoredSection(s.sectionId());
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static WasmModule parse(InputStream input) {
        return new Parser().parse(() -> input);
    }

    public static WasmModule parse(byte[] buffer) {
        return new Parser().parse(() -> new ByteArrayInputStream(buffer));
    }

    public static WasmModule parse(File file) {
        return Parser.parse(file.toPath());
    }

    public static WasmModule parse(Path path) {
        return new Parser().parse(() -> {
            try {
                return Files.newInputStream(path, new OpenOption[0]);
            }
            catch (IOException e) {
                throw new IllegalArgumentException("Error opening file: " + String.valueOf(path), e);
            }
        });
    }

    public WasmModule parse(Supplier<InputStream> inputStreamSupplier) {
        WasmModule.Builder moduleBuilder = WasmModule.builder();
        MessageDigest messageDigest = null;
        try (InputStream is = inputStreamSupplier.get();){
            InputStream maybeDigestedInputStream = is;
            if (!"none".equals(DIGEST_ALGORITHM)) {
                messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
                maybeDigestedInputStream = new DigestInputStream(is, messageDigest);
            }
            this.parse(maybeDigestedInputStream, s -> Parser.onSection(moduleBuilder, s));
        }
        catch (IOException | NoSuchAlgorithmException e) {
            throw new ChicoryException(e);
        }
        catch (MalformedException e) {
            throw new MalformedException("section size mismatch, unexpected end of section or function, " + e.getMessage(), e);
        }
        if (messageDigest != null) {
            String algo = DIGEST_ALGORITHM.toLowerCase(Locale.getDefault()).replace(":", "");
            moduleBuilder.withDigest(algo + ":" + Base64.getEncoder().encodeToString(messageDigest.digest()));
        }
        return moduleBuilder.build();
    }

    public void parse(InputStream in, ParserListener listener) {
        this.parse(in, listener, true);
    }

    private void parse(InputStream in, ParserListener listener, boolean decode) {
        Objects.requireNonNull(listener, "listener");
        SectionsValidator validator = new SectionsValidator();
        ByteBuffer buffer = Parser.readByteBuffer(in);
        byte[] magic = new byte[4];
        Encoding.readBytes(buffer, magic);
        if (!Arrays.equals(magic, MAGIC_BYTES)) {
            throw new MalformedException("magic header not detected, found: " + Arrays.toString(magic) + " expected: " + Arrays.toString(MAGIC_BYTES));
        }
        byte[] version = new byte[4];
        Encoding.readBytes(buffer, version);
        if (!Arrays.equals(version, VERSION_BYTES)) {
            throw new MalformedException("unknown binary version, found: " + Arrays.toString(version) + " expected: " + Arrays.toString(VERSION_BYTES));
        }
        boolean firstTime = true;
        while (buffer.hasRemaining()) {
            byte sectionId = Encoding.readByte(buffer);
            long sectionSize = Encoding.readVarUInt32(buffer);
            validator.validateSectionType(sectionId);
            ByteBuffer sectionByteBuffer = buffer.asReadOnlyBuffer();
            sectionByteBuffer.order(buffer.order());
            int sectionLimit = sectionByteBuffer.position() + (int)sectionSize;
            if (buffer.capacity() < sectionLimit) {
                throw new MalformedException("length out of bounds for section" + sectionId);
            }
            buffer.position(sectionLimit);
            if (!this.shouldParseSection(sectionId)) continue;
            sectionByteBuffer.limit(sectionLimit);
            if (!decode) {
                listener.onSection(Parser.parseRawSection(sectionByteBuffer, sectionId, sectionSize));
                continue;
            }
            switch (sectionId) {
                case 0: {
                    CustomSection customSection = this.parseCustomSection(sectionByteBuffer, sectionSize, firstTime);
                    firstTime = false;
                    listener.onSection(customSection);
                    break;
                }
                case 1: {
                    TypeSection typeSection;
                    this.typeSection = typeSection = Parser.parseTypeSection(sectionByteBuffer);
                    listener.onSection(typeSection);
                    break;
                }
                case 2: {
                    ImportSection importSection = Parser.parseImportSection(sectionByteBuffer, this.typeSection);
                    listener.onSection(importSection);
                    break;
                }
                case 3: {
                    FunctionSection funcSection = Parser.parseFunctionSection(sectionByteBuffer);
                    listener.onSection(funcSection);
                    break;
                }
                case 4: {
                    TableSection tableSection = Parser.parseTableSection(sectionByteBuffer, this.typeSection);
                    listener.onSection(tableSection);
                    break;
                }
                case 5: {
                    MemorySection memorySection = Parser.parseMemorySection(sectionByteBuffer);
                    listener.onSection(memorySection);
                    break;
                }
                case 13: {
                    TagSection tagSection = Parser.parseTagSection(sectionByteBuffer);
                    listener.onSection(tagSection);
                    break;
                }
                case 6: {
                    GlobalSection globalSection = Parser.parseGlobalSection(sectionByteBuffer, this.typeSection);
                    listener.onSection(globalSection);
                    break;
                }
                case 7: {
                    ExportSection exportSection = Parser.parseExportSection(sectionByteBuffer);
                    listener.onSection(exportSection);
                    break;
                }
                case 8: {
                    StartSection startSection = Parser.parseStartSection(sectionByteBuffer);
                    listener.onSection(startSection);
                    break;
                }
                case 9: {
                    ElementSection elementSection = Parser.parseElementSection(sectionByteBuffer, sectionSize, this.typeSection);
                    listener.onSection(elementSection);
                    break;
                }
                case 10: {
                    CodeSection codeSection = Parser.parseCodeSection(sectionByteBuffer, this.typeSection);
                    listener.onSection(codeSection);
                    break;
                }
                case 11: {
                    DataSection dataSection = Parser.parseDataSection(sectionByteBuffer);
                    listener.onSection(dataSection);
                    break;
                }
                case 12: {
                    DataCountSection dataCountSection = Parser.parseDataCountSection(sectionByteBuffer);
                    listener.onSection(dataCountSection);
                    break;
                }
                default: {
                    throw new MalformedException("section size mismatch, malformed section id " + sectionId);
                }
            }
            if (!sectionByteBuffer.hasRemaining()) continue;
            throw new MalformedException("section size mismatch");
        }
    }

    public static void parseWithoutDecoding(byte[] bytes, ParserListener listener) {
        new Parser().parseWithoutDecoding(new ByteArrayInputStream(bytes), listener);
    }

    public void parseWithoutDecoding(InputStream in, ParserListener listener) {
        this.parse(in, listener, false);
    }

    private boolean shouldParseSection(int sectionId) {
        return this.includeSections == null || this.includeSections.get(sectionId);
    }

    private CustomSection parseCustomSection(ByteBuffer buffer, long sectionSize, boolean checkMalformed) {
        int sectionPos = buffer.position();
        String name = Encoding.readName(buffer, checkMalformed);
        long size = sectionSize - (long)(buffer.position() - sectionPos);
        if (size < 0L) {
            throw new MalformedException("unexpected end");
        }
        byte[] bytes = new byte[(int)size];
        Encoding.readBytes(buffer, bytes);
        Function<byte[], CustomSection> parser = this.customParsers.get(name);
        return parser == null ? UnknownCustomSection.builder().withName(name).withBytes(bytes).build() : parser.apply(bytes);
    }

    private static RawSection parseRawSection(ByteBuffer buffer, byte sectionId, long sectionSize) {
        byte[] bytes = new byte[Math.toIntExact(sectionSize)];
        Encoding.readBytes(buffer, bytes);
        return new RawSection(sectionId, bytes);
    }

    private static TypeSection parseTypeSection(ByteBuffer buffer) {
        long typeCount = Encoding.readVarUInt32(buffer);
        TypeSection.Builder typeSection = TypeSection.builder();
        int i = 0;
        while ((long)i < typeCount) {
            long form = Encoding.readVarUInt32(buffer);
            if (form > 127L) {
                throw new MalformedException("integer representation too long");
            }
            if (form != 96L) {
                throw new MalformedException("We don't support non func types. Form " + String.format("0x%02X", form) + " was given but we expected 0x60");
            }
            int paramCount = (int)Encoding.readVarUInt32(buffer);
            ValType.Builder[] paramsBuilder = new ValType.Builder[paramCount];
            for (int j = 0; j < paramCount; ++j) {
                paramsBuilder[j] = Parser.readValueTypeBuilder(buffer);
            }
            int returnCount = (int)Encoding.readVarUInt32(buffer);
            ValType.Builder[] returnsBuilder = new ValType.Builder[returnCount];
            for (int j = 0; j < returnCount; ++j) {
                returnsBuilder[j] = Parser.readValueTypeBuilder(buffer);
            }
            int maxTypeIdx = i - 1;
            Stream.concat(Arrays.stream(paramsBuilder), Arrays.stream(returnsBuilder)).forEach(v -> {
                int typeIdx;
                if (v.isReference() && (typeIdx = v.typeIdx()) > maxTypeIdx) {
                    throw new InvalidException("unknown type: recursive type, type mismatch");
                }
            });
            Function<ValType.Builder, ValType> build = builder -> builder.build(typeSection.getTypes()::get);
            ValType[] params = (ValType[])Arrays.stream(paramsBuilder).map(build).toArray(ValType[]::new);
            ValType[] returns = (ValType[])Arrays.stream(returnsBuilder).map(build).toArray(ValType[]::new);
            typeSection.addFunctionType(FunctionType.of(params, returns));
            ++i;
        }
        return typeSection.build();
    }

    private static ImportSection parseImportSection(ByteBuffer buffer, TypeSection typeSection) {
        long importCount = Encoding.readVarUInt32(buffer);
        ImportSection.Builder importSection = ImportSection.builder();
        int i = 0;
        while ((long)i < importCount) {
            ExternalType descType;
            String moduleName = Encoding.readName(buffer);
            String importName = Encoding.readName(buffer);
            try {
                descType = ExternalType.byId((int)Encoding.readVarUInt32(buffer));
            }
            catch (RuntimeException e) {
                throw new MalformedException("malformed import kind", e);
            }
            switch (descType) {
                case FUNCTION: {
                    if (moduleName.isEmpty() && importName.isEmpty()) {
                        throw new MalformedException("malformed import kind");
                    }
                    importSection.addImport(new FunctionImport(moduleName, importName, (int)Encoding.readVarUInt32(buffer)));
                    break;
                }
                case TABLE: {
                    ValType rawTableType = Parser.readValueType(buffer, typeSection);
                    byte limitType = Encoding.readByte(buffer);
                    int min = (int)Encoding.readVarUInt32(buffer);
                    TableLimits limits = null;
                    switch (limitType) {
                        case 0: {
                            limits = new TableLimits(min);
                            break;
                        }
                        case 1: 
                        case 3: {
                            limits = new TableLimits(min, Encoding.readVarUInt32(buffer), limitType == 3);
                            break;
                        }
                        default: {
                            throw new MalformedException("integer too large, invalid table limit: " + limitType);
                        }
                    }
                    importSection.addImport(new TableImport(moduleName, importName, rawTableType, limits));
                    break;
                }
                case MEMORY: {
                    MemoryLimits limits = Parser.parseMemoryLimits(buffer);
                    importSection.addImport(new MemoryImport(moduleName, importName, limits));
                    break;
                }
                case GLOBAL: {
                    ValType globalValType = Parser.readValueType(buffer, typeSection);
                    MutabilityType globalMut = MutabilityType.forId(Encoding.readByte(buffer));
                    importSection.addImport(new GlobalImport(moduleName, importName, globalMut, globalValType));
                    break;
                }
                case TAG: {
                    try {
                        byte attribute = Encoding.readByte(buffer);
                        int tagTypeIdx = (int)Encoding.readVarUInt32(buffer);
                        importSection.addImport(new TagImport(moduleName, importName, attribute, tagTypeIdx));
                        break;
                    }
                    catch (MalformedException e) {
                        throw new MalformedException("malformed import kind", e);
                    }
                }
                default: {
                    throw new MalformedException("malformed import kind");
                }
            }
            ++i;
        }
        return importSection.build();
    }

    private static FunctionSection parseFunctionSection(ByteBuffer buffer) {
        long functionCount = Encoding.readVarUInt32(buffer);
        FunctionSection.Builder functionSection = FunctionSection.builder();
        int i = 0;
        while ((long)i < functionCount) {
            long typeIndex = Encoding.readVarUInt32(buffer);
            functionSection.addFunctionType((int)typeIndex);
            ++i;
        }
        return functionSection.build();
    }

    private static TableLimits readTableLimits(ByteBuffer buffer) {
        byte limitType = Encoding.readByte(buffer);
        if (limitType != 0 && limitType != 1) {
            throw new MalformedException("integer representation too long, integer too large");
        }
        long min = Encoding.readVarUInt32(buffer);
        TableLimits limits = limitType > 0 ? new TableLimits(min, Encoding.readVarUInt32(buffer)) : new TableLimits(min);
        return limits;
    }

    private static TableSection parseTableSection(ByteBuffer buffer, TypeSection typeSection) {
        long tableCount = Encoding.readVarUInt32(buffer);
        TableSection.Builder tableSection = TableSection.builder();
        int i = 0;
        while ((long)i < tableCount) {
            int firstByte = (int)Encoding.readVarUInt32(buffer);
            if (firstByte == 64) {
                long secondByte = Encoding.readVarUInt32(buffer);
                assert (secondByte == 0L);
                ValType tableType = Parser.readValueType(buffer, typeSection);
                TableLimits limits = Parser.readTableLimits(buffer);
                Instruction[] init = Parser.parseExpression(buffer);
                tableSection.addTable(new Table(tableType, limits, List.of(init)));
            } else {
                ValType tableType = Parser.readValueTypeFromOpCode(buffer, firstByte, typeSection);
                TableLimits limits = Parser.readTableLimits(buffer);
                tableSection.addTable(new Table(tableType, limits));
            }
            ++i;
        }
        return tableSection.build();
    }

    private static MemorySection parseMemorySection(ByteBuffer buffer) {
        long memoryCount = Encoding.readVarUInt32(buffer);
        MemorySection.Builder memorySection = MemorySection.builder();
        int i = 0;
        while ((long)i < memoryCount) {
            MemoryLimits limits = Parser.parseMemoryLimits(buffer);
            memorySection.addMemory(new Memory(limits));
            ++i;
        }
        return memorySection.build();
    }

    private static MemoryLimits parseMemoryLimits(ByteBuffer buffer) {
        byte limitType = Encoding.readByte(buffer);
        int initial = (int)Encoding.readVarUInt32(buffer);
        switch (limitType) {
            case 0: {
                return new MemoryLimits(initial);
            }
            case 1: 
            case 3: {
                int maximum = (int)Encoding.readVarUInt32(buffer);
                return new MemoryLimits(initial, maximum, limitType == 3);
            }
            case 2: {
                throw new InvalidException("shared memory must have maximum");
            }
        }
        if (limitType > 0) {
            throw new MalformedException("integer too large, invalid memory limit: " + limitType);
        }
        throw new MalformedException("integer representation too long: " + limitType);
    }

    private static GlobalSection parseGlobalSection(ByteBuffer buffer, TypeSection typeSection) {
        long globalCount = Encoding.readVarUInt32(buffer);
        GlobalSection.Builder globalSection = GlobalSection.builder();
        int i = 0;
        while ((long)i < globalCount) {
            ValType valueType = Parser.readValueType(buffer, typeSection);
            MutabilityType mutabilityType = MutabilityType.forId(Encoding.readByte(buffer));
            Instruction[] init = Parser.parseExpression(buffer);
            globalSection.addGlobal(new Global(valueType, mutabilityType, List.of(init)));
            ++i;
        }
        return globalSection.build();
    }

    private static ExportSection parseExportSection(ByteBuffer buffer) {
        long exportCount = Encoding.readVarUInt32(buffer);
        ExportSection.Builder exportSection = ExportSection.builder();
        int i = 0;
        while ((long)i < exportCount) {
            String name = Encoding.readName(buffer, false);
            ExternalType exportType = ExternalType.byId((int)Encoding.readVarUInt32(buffer));
            int index = (int)Encoding.readVarUInt32(buffer);
            exportSection.addExport(new Export(name, index, exportType));
            ++i;
        }
        return exportSection.build();
    }

    private static StartSection parseStartSection(ByteBuffer buffer) {
        return StartSection.builder().setStartIndex(Encoding.readVarUInt32(buffer)).build();
    }

    private static ElementSection parseElementSection(ByteBuffer buffer, long sectionSize, TypeSection typeSection) {
        int initialPosition = buffer.position();
        long elementCount = Encoding.readVarUInt32(buffer);
        ElementSection.Builder elementSection = ElementSection.builder();
        int i = 0;
        while ((long)i < elementCount) {
            elementSection.addElement(Parser.parseSingleElement(buffer, typeSection));
            ++i;
        }
        if ((long)buffer.position() != (long)initialPosition + sectionSize) {
            throw new MalformedException("section size mismatch");
        }
        return elementSection.build();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Element parseSingleElement(ByteBuffer buffer, TypeSection typeSection) {
        ValType type;
        int flags = (int)Encoding.readVarUInt32(buffer);
        boolean active = (flags & 1) == 0;
        boolean declarative = !active && (flags & 2) != 0;
        boolean passive = !active && !declarative;
        boolean hasTableIdx = active && (flags & 2) != 0;
        boolean alwaysFuncRef = active && !hasTableIdx;
        boolean exprInit = (flags & 4) != 0;
        boolean hasElemKind = !exprInit && !alwaysFuncRef;
        boolean hasRefType = exprInit && !alwaysFuncRef;
        int tableIdx = 0;
        List<Instruction> offset = List.of();
        if (active) {
            if (hasTableIdx) {
                tableIdx = Math.toIntExact(Encoding.readVarUInt32(buffer));
            }
            offset = List.of(Parser.parseExpression(buffer));
        }
        if (alwaysFuncRef) {
            type = exprInit ? ValType.FuncRef : ValType.builder().withOpcode(100).withTypeIdx(ValType.TypeIdxCode.FUNC.code()).build();
        } else if (hasElemKind) {
            int ek = (int)Encoding.readVarUInt32(buffer);
            if (ek != 0) throw new ChicoryException("Invalid element kind");
            type = ValType.builder().withOpcode(100).withTypeIdx(ValType.TypeIdxCode.FUNC.code()).build();
        } else {
            assert (hasRefType);
            type = Parser.readValueType(buffer, typeSection);
            if (!type.isReference()) {
                throw new MalformedException("malformed reference type: element section has non-reference type");
            }
        }
        int initCnt = Math.toIntExact(Encoding.readVarUInt32(buffer));
        ArrayList<List<Instruction>> inits = new ArrayList<List<Instruction>>(initCnt);
        if (exprInit) {
            for (int i = 0; i < initCnt; ++i) {
                inits.add(List.of(Parser.parseExpression(buffer)));
            }
        } else {
            for (int i = 0; i < initCnt; ++i) {
                inits.add(List.of(new Instruction(-1, OpCode.REF_FUNC, new long[]{Encoding.readVarUInt32(buffer)}), new Instruction(-1, OpCode.END, Instruction.EMPTY_OPERANDS)));
            }
        }
        if (declarative) {
            return new DeclarativeElement(type, inits);
        }
        if (passive) {
            return new PassiveElement(type, inits);
        }
        assert (active);
        return new ActiveElement(type, inits, tableIdx, offset);
    }

    private static List<ValType> parseCodeSectionLocalTypes(ByteBuffer buffer, TypeSection typeSection) {
        long distinctTypesCount = Encoding.readVarUInt32(buffer);
        ArrayList<ValType> locals = new ArrayList<ValType>();
        int i = 0;
        while ((long)i < distinctTypesCount) {
            long numberOfLocals = Encoding.readVarUInt32(buffer);
            if (numberOfLocals > 50000L) {
                throw new MalformedException("too many locals");
            }
            ValType type = Parser.readValueType(buffer, typeSection);
            int j = 0;
            while ((long)j < numberOfLocals) {
                locals.add(type);
                ++j;
            }
            ++i;
        }
        return locals;
    }

    private static CodeSection parseCodeSection(ByteBuffer buffer, TypeSection typeSection) {
        long funcBodyCount = Encoding.readVarUInt32(buffer);
        ControlTree root = new ControlTree();
        CodeSection.Builder codeSection = CodeSection.builder();
        int i = 0;
        while ((long)i < funcBodyCount) {
            ArrayDeque<Instruction> blockScope = new ArrayDeque<Instruction>();
            int depth = 0;
            long funcEndPoint = Encoding.readVarUInt32(buffer) + (long)buffer.position();
            List<ValType> locals = Parser.parseCodeSectionLocalTypes(buffer, typeSection);
            ArrayList<AnnotatedInstruction.Builder> instructions = new ArrayList<AnnotatedInstruction.Builder>();
            boolean lastInstruction = false;
            ControlTree currentControlFlow = null;
            do {
                Instruction baseInstruction = Parser.parseInstruction(buffer);
                AnnotatedInstruction.Builder instruction = AnnotatedInstruction.builder().from(baseInstruction);
                boolean bl = lastInstruction = (long)buffer.position() >= funcEndPoint;
                if (instructions.isEmpty()) {
                    currentControlFlow = root.spawn(0, instruction);
                }
                switch (baseInstruction.opcode()) {
                    case MEMORY_INIT: 
                    case DATA_DROP: {
                        codeSection.setRequiresDataCount(true);
                    }
                }
                switch (baseInstruction.opcode()) {
                    case BLOCK: 
                    case LOOP: 
                    case IF: 
                    case TRY_TABLE: {
                        instruction.withDepth(++depth);
                        blockScope.push(baseInstruction);
                        instruction.withScope((Instruction)blockScope.peek());
                        break;
                    }
                    case END: {
                        instruction.withDepth(depth);
                        --depth;
                        instruction.withScope(blockScope.isEmpty() ? baseInstruction : (Instruction)blockScope.pop());
                        break;
                    }
                    default: {
                        instruction.withDepth(depth);
                    }
                }
                switch (baseInstruction.opcode()) {
                    case BLOCK: 
                    case LOOP: {
                        currentControlFlow = currentControlFlow.spawn(instructions.size(), instruction);
                        break;
                    }
                    case IF: {
                        currentControlFlow = currentControlFlow.spawn(instructions.size(), instruction);
                        int defaultJmp = instructions.size() + 1;
                        currentControlFlow.addCallback(end -> instruction.updateLabelFalse((int)end));
                        instruction.withLabelTrue(defaultJmp);
                        instruction.withLabelFalse(defaultJmp);
                        break;
                    }
                    case ELSE: {
                        currentControlFlow.instruction().withLabelFalse(instructions.size() + 1);
                        currentControlFlow.addCallback(instruction::withLabelTrue);
                        break;
                    }
                    case BR_IF: 
                    case BR_ON_NULL: 
                    case BR_ON_NON_NULL: {
                        instruction.withLabelFalse(instructions.size() + 1);
                    }
                    case BR: {
                        ControlTree reference = currentControlFlow;
                        for (int offset = (int)baseInstruction.operand(0); offset > 0; --offset) {
                            if (reference == null) {
                                throw new InvalidException("unknown label");
                            }
                            reference = reference.parent();
                        }
                        reference.addCallback(instruction::withLabelTrue);
                        break;
                    }
                    case BR_TABLE: {
                        int finalIdx;
                        ControlTree reference;
                        int length = baseInstruction.operandCount();
                        ArrayList<Integer> labelTable = new ArrayList<Integer>();
                        int idx = 0;
                        while (idx < length) {
                            labelTable.add(null);
                            reference = currentControlFlow;
                            for (int offset = (int)baseInstruction.operand(idx); offset > 0; --offset) {
                                if (reference == null) {
                                    throw new InvalidException("unknown label");
                                }
                                reference = reference.parent();
                            }
                            finalIdx = idx++;
                            reference.addCallback(end -> labelTable.set(finalIdx, (Integer)end));
                        }
                        instruction.withLabelTable(labelTable);
                        break;
                    }
                    case TRY_TABLE: {
                        int finalIdx;
                        ControlTree reference;
                        List<CatchOpCode.Catch> catches = CatchOpCode.decode(baseInstruction.operands());
                        List<Integer> allLabels = CatchOpCode.allLabels(baseInstruction.operands());
                        int idx = 0;
                        while (idx < allLabels.size()) {
                            Integer offset = allLabels.get(idx);
                            reference = currentControlFlow;
                            while (offset > 0) {
                                if (reference == null) {
                                    throw new InvalidException("unknown label");
                                }
                                reference = reference.parent();
                                Integer finalIdx2 = offset;
                                offset = offset - 1;
                            }
                            finalIdx = idx++;
                            reference.addCallback(end -> ((CatchOpCode.Catch)catches.get(finalIdx)).resolvedLabel((int)end));
                        }
                        instruction.withCatches(catches);
                        currentControlFlow = currentControlFlow.spawn(instructions.size(), instruction);
                        break;
                    }
                    case END: {
                        AnnotatedInstruction.Builder former;
                        currentControlFlow.setFinalInstructionNumber(instructions.size(), instruction);
                        currentControlFlow = currentControlFlow.parent();
                        if (!lastInstruction || instructions.size() <= 1 || (former = (AnnotatedInstruction.Builder)instructions.get(instructions.size() - 1)).opcode() != OpCode.END) break;
                        instruction.withScope(former.scope().get());
                    }
                }
                if (lastInstruction && instruction.opcode() != OpCode.END) {
                    throw new MalformedException("END opcode expected, section size mismatch");
                }
                instructions.add(instruction);
            } while (!lastInstruction);
            if (depth > 0) {
                throw new MalformedException("unexpected end");
            }
            FunctionBody functionBody = new FunctionBody(locals, Collections.unmodifiableList(instructions.stream().map(ins -> ins.build()).collect(Collectors.toList())));
            codeSection.addFunctionBody(functionBody);
            ++i;
        }
        return codeSection.build();
    }

    private static DataSection parseDataSection(ByteBuffer buffer) {
        long dataSegmentCount = Encoding.readVarUInt32(buffer);
        DataSection.Builder dataSection = DataSection.builder();
        int i = 0;
        while ((long)i < dataSegmentCount) {
            long mode = Encoding.readVarUInt32(buffer);
            if (mode == 0L) {
                Instruction[] offset = Parser.parseExpression(buffer);
                byte[] data = new byte[(int)Encoding.readVarUInt32(buffer)];
                Encoding.readBytes(buffer, data);
                dataSection.addDataSegment(new ActiveDataSegment(0L, List.of(offset), data));
            } else if (mode == 1L) {
                byte[] data = new byte[(int)Encoding.readVarUInt32(buffer)];
                Encoding.readBytes(buffer, data);
                dataSection.addDataSegment(new PassiveDataSegment(data));
            } else if (mode == 2L) {
                long memoryId = Encoding.readVarUInt32(buffer);
                Instruction[] offset = Parser.parseExpression(buffer);
                byte[] data = new byte[(int)Encoding.readVarUInt32(buffer)];
                Encoding.readBytes(buffer, data);
                dataSection.addDataSegment(new ActiveDataSegment(memoryId, List.of(offset), data));
            } else {
                throw new ChicoryException("Failed to parse data segment with data mode: " + mode);
            }
            ++i;
        }
        return dataSection.build();
    }

    private static DataCountSection parseDataCountSection(ByteBuffer buffer) {
        long dataCount = Encoding.readVarUInt32(buffer);
        return DataCountSection.builder().withDataCount((int)dataCount).build();
    }

    private static TagSection parseTagSection(ByteBuffer buffer) {
        long tagsCount = Encoding.readVarUInt32(buffer);
        TagSection.Builder tagSection = TagSection.builder();
        int i = 0;
        while ((long)i < tagsCount) {
            byte attribute = Encoding.readByte(buffer);
            int typeIdx = (int)Encoding.readVarUInt32(buffer);
            tagSection.addTagType(new TagType(attribute, typeIdx));
            ++i;
        }
        return tagSection.build();
    }

    private static Instruction parseInstruction(ByteBuffer buffer) {
        OpCode op;
        int address = buffer.position();
        int b = Encoding.readByte(buffer) & 0xFF;
        if (b >= 252 && b < 255) {
            b = (int)((long)(b << 8) + Encoding.readVarUInt32(buffer));
        }
        if ((op = OpCode.byOpCode(b)) == null) {
            throw new MalformedException("illegal opcode, op value " + String.format("%02X ", b));
        }
        List<WasmEncoding> signature = OpCode.signature(op);
        switch (op) {
            case MEMORY_GROW: 
            case MEMORY_SIZE: {
                byte zero = Encoding.readByte(buffer);
                if (zero == 0) break;
                throw new MalformedException("zero byte expected");
            }
        }
        switch (op) {
            case DROP: 
            case SELECT: {
                return new Instruction(address, op, new long[]{0L});
            }
        }
        if (signature.isEmpty()) {
            return new Instruction(address, op, Instruction.EMPTY_OPERANDS);
        }
        ArrayList<Long> operands = new ArrayList<Long>();
        block23: for (WasmEncoding sig : signature) {
            switch (sig) {
                case BYTE: {
                    operands.add(Byte.toUnsignedLong(Encoding.readByte(buffer)));
                    break;
                }
                case VARUINT: {
                    operands.add(Encoding.readVarUInt32(buffer));
                    break;
                }
                case VARSINT32: {
                    operands.add(Encoding.readVarSInt32(buffer));
                    break;
                }
                case VARSINT64: {
                    operands.add(Encoding.readVarSInt64(buffer));
                    break;
                }
                case FLOAT64: {
                    operands.add(Encoding.readFloat64(buffer));
                    break;
                }
                case FLOAT32: {
                    operands.add(Encoding.readFloat32(buffer));
                    break;
                }
                case VEC_VARUINT: {
                    int j2;
                    int vcount = (int)Encoding.readVarUInt32(buffer);
                    for (j2 = 0; j2 < vcount; ++j2) {
                        operands.add(Encoding.readVarUInt32(buffer));
                    }
                    continue block23;
                }
                case VEC_CATCH: {
                    long n = Encoding.readVarUInt32(buffer);
                    operands.add(n);
                    int j3 = 0;
                    while ((long)j3 < n) {
                        byte catchOp = Encoding.readByte(buffer);
                        operands.add(0L | (long)catchOp);
                        CatchOpCode catchOpcode = CatchOpCode.byOpCode(catchOp);
                        switch (catchOpcode) {
                            case CATCH: 
                            case CATCH_REF: {
                                operands.add(Encoding.readVarUInt32(buffer));
                            }
                            case CATCH_ALL: 
                            case CATCH_ALL_REF: {
                                operands.add(Encoding.readVarUInt32(buffer));
                            }
                        }
                        ++j3;
                    }
                    continue block23;
                }
                case V128: {
                    int j2;
                    byte[] bytes = new byte[16];
                    for (j2 = 0; j2 < 16; ++j2) {
                        bytes[j2] = Encoding.readByte(buffer);
                    }
                    long[] j2 = Value.bytesToVec(bytes);
                    int j3 = j2.length;
                    for (int i = 0; i < j3; ++i) {
                        long val = j2[i];
                        operands.add(val);
                    }
                    continue block23;
                }
                case BLOCK_TYPE: {
                    int operand = (int)Encoding.readVarUInt32(buffer);
                    if (ValType.ID.isValidOpcode(operand)) {
                        ValType.Builder v = Parser.readValueTypeBuilderFromOpCode(buffer, operand);
                        operands.add(v.id());
                        break;
                    }
                    operands.add(Long.valueOf(operand));
                    break;
                }
                case VEC_VALUE_TYPE: {
                    int j3;
                    int vcount = (int)Encoding.readVarUInt32(buffer);
                    for (j3 = 0; j3 < vcount; ++j3) {
                        operands.add(Parser.readValueTypeBuilder(buffer).id());
                    }
                    break;
                }
            }
        }
        long[] operandsArray = new long[operands.size()];
        for (int i = 0; i < operands.size(); ++i) {
            operandsArray[i] = (Long)operands.get(i);
        }
        Parser.verifyAlignment(op, operandsArray);
        return new Instruction(address, op, operandsArray);
    }

    private static void verifyAlignment(OpCode op, long[] operands) {
        int align = -1;
        switch (op) {
            case I32_LOAD8_U: 
            case I32_LOAD8_S: 
            case I64_LOAD8_U: 
            case I64_LOAD8_S: 
            case I32_STORE8: 
            case I64_STORE8: 
            case V128_LOAD8_SPLAT: 
            case V128_STORE8_LANE: 
            case V128_LOAD8_LANE: {
                align = 8;
                break;
            }
            case I32_LOAD16_U: 
            case I32_LOAD16_S: 
            case I64_LOAD16_U: 
            case I64_LOAD16_S: 
            case I32_STORE16: 
            case I64_STORE16: 
            case V128_LOAD16_SPLAT: 
            case V128_STORE16_LANE: 
            case V128_LOAD16_LANE: {
                align = 16;
                break;
            }
            case I32_LOAD: 
            case F32_LOAD: 
            case I64_LOAD32_U: 
            case I64_LOAD32_S: 
            case I64_STORE32: 
            case I32_STORE: 
            case F32_STORE: 
            case V128_LOAD32_SPLAT: 
            case V128_STORE32_LANE: 
            case V128_LOAD32_LANE: {
                align = 32;
                break;
            }
            case I64_LOAD: 
            case F64_LOAD: 
            case I64_STORE: 
            case F64_STORE: 
            case V128_LOAD8x8_S: 
            case V128_LOAD8x8_U: 
            case V128_LOAD16x4_S: 
            case V128_LOAD16x4_U: 
            case V128_LOAD32x2_S: 
            case V128_LOAD32x2_U: 
            case V128_LOAD64_SPLAT: 
            case V128_STORE64_LANE: 
            case V128_LOAD64_LANE: {
                align = 64;
                break;
            }
            case V128_LOAD: 
            case V128_STORE: {
                align = 128;
            }
        }
        if (align > 0) {
            int operand0 = (int)operands[0];
            int offset = 1 << operand0;
            if (operand0 >= align) {
                throw new MalformedException("malformed memop flags");
            }
            if (offset < 0 || offset > align >> 3) {
                throw new InvalidException("alignment must not be larger than natural alignment (" + operand0 + ")");
            }
        }
    }

    private static ValType.Builder readValueTypeBuilderFromOpCode(ByteBuffer buffer, int valueTypeOpCode) {
        ValType.Builder builder = ValType.builder().withOpcode(valueTypeOpCode);
        if (valueTypeOpCode == 100 || valueTypeOpCode == 99) {
            return builder.withTypeIdx((int)Encoding.readVarSInt32(buffer));
        }
        return builder;
    }

    private static ValType.Builder readValueTypeBuilder(ByteBuffer buffer) {
        int valueTypeOpCode = (int)Encoding.readVarUInt32(buffer);
        return Parser.readValueTypeBuilderFromOpCode(buffer, valueTypeOpCode);
    }

    private static ValType readValueTypeFromOpCode(ByteBuffer buffer, int valueTypeOpCode, TypeSection typeSection) {
        return Parser.readValueTypeBuilderFromOpCode(buffer, valueTypeOpCode).build(typeSection::getType);
    }

    private static ValType readValueType(ByteBuffer buffer, TypeSection typeSection) {
        return Parser.readValueTypeBuilder(buffer).build(typeSection::getType);
    }

    private static Instruction[] parseExpression(ByteBuffer buffer) {
        ArrayList<Instruction> expr = new ArrayList<Instruction>();
        while (buffer.hasRemaining()) {
            Instruction i = Parser.parseInstruction(buffer);
            if (i.opcode() == OpCode.END) {
                return expr.toArray(new Instruction[0]);
            }
            expr.add(i);
        }
        throw new MalformedException("illegal opcode: expected end opcode");
    }

    public static final class Builder {
        private Map<String, Function<byte[], CustomSection>> customParsers;
        private BitSet includeSections;

        private Builder() {
        }

        public Builder includeSectionId(int sectionId) {
            if (this.includeSections == null) {
                this.includeSections = new BitSet();
            }
            this.includeSections.set(sectionId);
            return this;
        }

        public Builder withCustomParsers(Map<String, Function<byte[], CustomSection>> customParsers) {
            this.customParsers = customParsers;
            return this;
        }

        public Parser build() {
            if (this.customParsers == null) {
                this.customParsers = DEFAULT_CUSTOM_PARSERS;
            }
            return new Parser(this.includeSections, this.customParsers);
        }
    }

    private static class SectionsValidator {
        private List<Integer> sectionsOrder = new ArrayList<Integer>();
        private int maxSection = -1;

        SectionsValidator() {
            this.sectionsOrder.add(1);
            this.sectionsOrder.add(2);
            this.sectionsOrder.add(3);
            this.sectionsOrder.add(4);
            this.sectionsOrder.add(5);
            this.sectionsOrder.add(6);
            this.sectionsOrder.add(7);
            this.sectionsOrder.add(8);
            this.sectionsOrder.add(9);
            this.sectionsOrder.add(12);
            this.sectionsOrder.add(10);
            this.sectionsOrder.add(11);
        }

        void validateSectionType(byte sectionId) {
            if (this.sectionsOrder.contains(sectionId)) {
                if (this.maxSection < 0 || this.sectionsOrder.indexOf(sectionId) > this.maxSection) {
                    this.maxSection = this.sectionsOrder.indexOf(sectionId);
                } else {
                    throw new MalformedException("unexpected content after last section");
                }
            }
        }
    }
}

