package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.InvalidException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TypeSection extends Section {
    private final List<RecType> types;
    private final List<SubType> definedTypes;
    private final FunctionType[] functionTypes;
    private final int[] canonicalTypeIds;
    private final int[] groupStart;
    private final int[] groupOffset;

    private TypeSection(List<RecType> types) {
        super(SectionId.TYPE);
        this.types = List.copyOf(types);
        this.definedTypes = flatten(types);
        this.functionTypes = buildFunctionTypes(this.definedTypes);
        GroupMetadata metadata = computeGroupMetadata(types);
        this.groupStart = metadata.groupStart;
        this.groupOffset = metadata.groupOffset;
        validateForwardReferences(this.definedTypes, groupStart);
        this.canonicalTypeIds =
                computeCanonicalTypeIds(types, this.definedTypes, groupStart, metadata.groupOffset);
    }

    private static List<SubType> flatten(List<RecType> recTypes) {
        var all = new ArrayList<SubType>();
        for (var recType : recTypes) {
            all.addAll(Arrays.asList(recType.subTypes()));
        }
        return List.copyOf(all);
    }

    private static FunctionType[] buildFunctionTypes(List<SubType> definedTypes) {
        var functionTypes = new FunctionType[definedTypes.size()];
        for (int i = 0; i < definedTypes.size(); i++) {
            var compType = definedTypes.get(i).compType();
            if (compType != null && compType.funcType() != null) {
                functionTypes[i] = compType.funcType();
            }
        }
        return functionTypes;
    }

    private static GroupMetadata computeGroupMetadata(List<RecType> recTypes) {
        int total = recTypes.stream().mapToInt(rt -> rt.subTypes().length).sum();
        int[] groupStart = new int[total];
        int[] groupOffset = new int[total];
        int index = 0;
        for (RecType recType : recTypes) {
            var subTypes = recType.subTypes();
            int start = index;
            for (int i = 0; i < subTypes.length; i++) {
                groupStart[index] = start;
                groupOffset[index] = i;
                index++;
            }
        }
        return new GroupMetadata(groupStart, groupOffset);
    }

    private static void validateForwardReferences(List<SubType> definedTypes, int[] groupStart) {
        int typeCount = definedTypes.size();
        for (int i = 0; i < typeCount; i++) {
            SubType subType = definedTypes.get(i);

            for (int superIdx : subType.typeIdx()) {
                if (superIdx < 0 || superIdx >= typeCount) {
                    throw new InvalidException("unknown type " + superIdx);
                }
                if (groupStart[superIdx] > groupStart[i]) {
                    throw new InvalidException("unknown type " + superIdx);
                }
            }

            validateCompTypeForwardRefs(i, subType.compType(), groupStart, typeCount);
        }
    }

    private static void validateCompTypeForwardRefs(
            int contextIdx, CompType compType, int[] groupStart, int typeCount) {
        if (compType == null) {
            return;
        }
        if (compType.funcType() != null) {
            validateFunctionTypeForwardRefs(contextIdx, compType.funcType(), groupStart, typeCount);
        } else if (compType.structType() != null) {
            for (FieldType field : compType.structType().fieldTypes()) {
                validateFieldTypeForwardRefs(contextIdx, field, groupStart, typeCount);
            }
        } else if (compType.arrayType() != null) {
            validateFieldTypeForwardRefs(
                    contextIdx, compType.arrayType().fieldType(), groupStart, typeCount);
        }
    }

    private static void validateFunctionTypeForwardRefs(
            int contextIdx, FunctionType functionType, int[] groupStart, int typeCount) {
        functionType
                .params()
                .forEach(v -> validateValTypeForwardRef(contextIdx, v, groupStart, typeCount));
        functionType
                .returns()
                .forEach(v -> validateValTypeForwardRef(contextIdx, v, groupStart, typeCount));
    }

    private static void validateFieldTypeForwardRefs(
            int contextIdx, FieldType fieldType, int[] groupStart, int typeCount) {
        var storageType = fieldType.storageType();
        if (storageType == null) {
            return;
        }
        if (storageType.valType() != null) {
            validateValTypeForwardRef(contextIdx, storageType.valType(), groupStart, typeCount);
        }
    }

    private static void validateValTypeForwardRef(
            int contextIdx, ValType valType, int[] groupStart, int typeCount) {
        if (!valType.isReference()) {
            return;
        }
        int refIdx = valType.typeIdx();
        if (refIdx < 0) {
            return;
        }
        if (refIdx >= typeCount) {
            throw new InvalidException("unknown type " + refIdx);
        }
        if (groupStart[refIdx] > groupStart[contextIdx]) {
            throw new InvalidException("unknown type " + refIdx);
        }
    }

    /**
     * Computes canonical IDs by processing recursion groups with deterministic fingerprints.
     * Uses iterative refinement: groups are normalized using current canonical IDs, and
     * equivalent groups are assigned the same canonical IDs. Process continues until convergence.
     */
    private static int[] computeCanonicalTypeIds(
            List<RecType> recTypes,
            List<SubType> definedTypes,
            int[] groupStart,
            int[] groupOffset) {
        int total = definedTypes.size();
        int[] canonicalIds = new int[total];
        // Initialize with raw indices
        for (int i = 0; i < total; i++) {
            canonicalIds[i] = i;
        }

        // Iteratively refine until convergence
        int[] prevIds = Arrays.copyOf(canonicalIds, total);
        int[] nextIds = new int[total];
        final int maxIterations = Math.max(4, total * 4);
        boolean changed = true;
        int iteration = 0;

        while (changed && iteration++ < maxIterations) {
            changed = false;
            Map<NormalizedGroup, int[]> groupToCanonicalIds = new HashMap<>();
            int nextCanonicalId = 0;
            int definedIndex = 0;

            // Process each recursion group
            for (RecType recType : recTypes) {
                var subTypes = recType.subTypes();
                NormalizedSubType[] normalizedSubTypes = new NormalizedSubType[subTypes.length];
                for (int j = 0; j < subTypes.length; j++) {
                    normalizedSubTypes[j] =
                            new NormalizedSubType(
                                    subTypes[j],
                                    definedIndex + j,
                                    prevIds,
                                    groupStart,
                                    groupOffset);
                }
                NormalizedGroup signature = new NormalizedGroup(normalizedSubTypes);
                int[] assignedIds = groupToCanonicalIds.get(signature);
                if (assignedIds == null) {
                    assignedIds = new int[subTypes.length];
                    for (int j = 0; j < subTypes.length; j++) {
                        assignedIds[j] = nextCanonicalId++;
                    }
                    groupToCanonicalIds.put(signature, assignedIds);
                }
                for (int j = 0; j < subTypes.length; j++) {
                    nextIds[definedIndex + j] = assignedIds[j];
                    if (nextIds[definedIndex + j] != prevIds[definedIndex + j]) {
                        changed = true;
                    }
                }
                definedIndex += subTypes.length;
            }

            System.arraycopy(nextIds, 0, prevIds, 0, total);
        }

        return prevIds;
    }

    /**
     * Wrapper class that implements equals/hashCode for SubType using canonical IDs.
     * This allows HashMap to work correctly with structural equivalence.
     */
    private static class NormalizedSubType {
        private final SubType subType;
        private final int subTypeIndex;
        private final int[] canonicalIds;
        private final int[] groupStart;
        private final int[] groupOffset;

        NormalizedSubType(
                SubType subType,
                int subTypeIndex,
                int[] canonicalIds,
                int[] groupStart,
                int[] groupOffset) {
            this.subType = subType;
            this.subTypeIndex = subTypeIndex;
            this.canonicalIds = canonicalIds;
            this.groupStart = groupStart;
            this.groupOffset = groupOffset;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NormalizedSubType)) {
                return false;
            }
            NormalizedSubType that = (NormalizedSubType) o;
            return new StructuralTypeComparator(canonicalIds, groupStart, groupOffset)
                            .compare(
                                    this.subTypeIndex,
                                    this.subType,
                                    that.subTypeIndex,
                                    that.subType)
                    == 0;
        }

        @Override
        public int hashCode() {
            // Compute hash based on structural comparison
            // We'll use a simplified hash that matches the comparison logic
            int result = Boolean.hashCode(subType.isFinal());
            result =
                    31 * result
                            + Arrays.hashCode(normalizeSuperTypes(subTypeIndex, subType.typeIdx()));
            result = 31 * result + hashCompType(subTypeIndex, subType.compType());
            return result;
        }

        private int[] normalizeSuperTypes(int contextIdx, int[] superTypes) {
            int[] normalized = new int[superTypes.length];
            for (int i = 0; i < superTypes.length; i++) {
                normalized[i] = normalizeTypeIdx(contextIdx, superTypes[i]);
            }
            return normalized;
        }

        private int hashCompType(int contextIdx, CompType compType) {
            if (compType == null) {
                return 0;
            }
            if (compType.funcType() != null) {
                return hashFunctionType(contextIdx, compType.funcType());
            }
            if (compType.structType() != null) {
                return hashStructType(contextIdx, compType.structType());
            }
            if (compType.arrayType() != null) {
                return hashArrayType(contextIdx, compType.arrayType());
            }
            return 0;
        }

        private int hashFunctionType(int contextIdx, FunctionType funcType) {
            return hashValTypeList(contextIdx, funcType.params()) * 31
                    + hashValTypeList(contextIdx, funcType.returns());
        }

        private int hashStructType(int contextIdx, StructType structType) {
            int result = structType.fieldTypes().length;
            for (FieldType field : structType.fieldTypes()) {
                result = 31 * result + hashFieldType(contextIdx, field);
            }
            return result;
        }

        private int hashArrayType(int contextIdx, ArrayType arrayType) {
            return hashFieldType(contextIdx, arrayType.fieldType());
        }

        private int hashFieldType(int contextIdx, FieldType fieldType) {
            return fieldType.mut().hashCode() * 31
                    + hashStorageType(contextIdx, fieldType.storageType());
        }

        private int hashStorageType(int contextIdx, StorageType storageType) {
            if (storageType == null) {
                return 0;
            }
            if (storageType.valType() != null) {
                return hashValType(contextIdx, storageType.valType());
            }
            if (storageType.packedType() != null) {
                return storageType.packedType().hashCode();
            }
            return 0;
        }

        private int hashValTypeList(int contextIdx, List<ValType> valTypes) {
            int result = valTypes.size();
            for (ValType vt : valTypes) {
                result = 31 * result + hashValType(contextIdx, vt);
            }
            return result;
        }

        private int hashValType(int contextIdx, ValType valType) {
            int result = valType.opcode();
            if (valType.isReference()) {
                int typeIdx = valType.typeIdx();
                if (typeIdx >= 0) {
                    result = 31 * result + normalizeTypeIdx(contextIdx, typeIdx);
                } else {
                    result = 31 * result + typeIdx;
                }
            } else {
                result = 31 * result + valType.typeIdx();
            }
            return result;
        }

        private int normalizeTypeIdx(int contextIdx, int idx) {
            if (idx >= 0 && idx < canonicalIds.length) {
                if (groupStart[idx] == groupStart[contextIdx]) {
                    return -(groupOffset[idx] + 1);
                }
                if (groupStart[idx] > groupStart[contextIdx]) {
                    throw new InvalidException("unknown type " + idx);
                }
                return canonicalIds[idx];
            }
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Custom comparator that compares SubTypes structurally, using canonical IDs for type references.
     * This handles normalization on-the-fly without creating intermediate normalized objects.
     */
    private static class StructuralTypeComparator {
        private final int[] canonicalIds;
        private final int[] groupStart;
        private final int[] groupOffset;

        StructuralTypeComparator(int[] canonicalIds, int[] groupStart, int[] groupOffset) {
            this.canonicalIds = canonicalIds;
            this.groupStart = groupStart;
            this.groupOffset = groupOffset;
        }

        int compare(int idxA, SubType a, int idxB, SubType b) {
            // Compare final flag
            if (a.isFinal() != b.isFinal()) {
                return Boolean.compare(a.isFinal(), b.isFinal());
            }

            // Compare supertype indices using canonical IDs
            int[] aSuperTypes = a.typeIdx();
            int[] bSuperTypes = b.typeIdx();
            if (aSuperTypes.length != bSuperTypes.length) {
                return Integer.compare(aSuperTypes.length, bSuperTypes.length);
            }
            for (int i = 0; i < aSuperTypes.length; i++) {
                int aCanonical = normalizeTypeIdx(idxA, aSuperTypes[i]);
                int bCanonical = normalizeTypeIdx(idxB, bSuperTypes[i]);
                if (aCanonical != bCanonical) {
                    return Integer.compare(aCanonical, bCanonical);
                }
            }

            // Compare composite types
            return compareCompType(idxA, a.compType(), idxB, b.compType());
        }

        private int compareCompType(int idxA, CompType a, int idxB, CompType b) {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }

            // Compare function types
            if (a.funcType() != null && b.funcType() != null) {
                return compareFunctionType(idxA, a.funcType(), idxB, b.funcType());
            }
            if (a.funcType() != null) {
                return 1;
            }
            if (b.funcType() != null) {
                return -1;
            }

            // Compare struct types
            if (a.structType() != null && b.structType() != null) {
                return compareStructType(idxA, a.structType(), idxB, b.structType());
            }
            if (a.structType() != null) {
                return 1;
            }
            if (b.structType() != null) {
                return -1;
            }

            // Compare array types
            if (a.arrayType() != null && b.arrayType() != null) {
                return compareArrayType(idxA, a.arrayType(), idxB, b.arrayType());
            }
            if (a.arrayType() != null) {
                return 1;
            }
            if (b.arrayType() != null) {
                return -1;
            }

            return 0;
        }

        private int compareFunctionType(int idxA, FunctionType a, int idxB, FunctionType b) {
            int paramCmp = compareValTypeList(idxA, a.params(), idxB, b.params());
            if (paramCmp != 0) {
                return paramCmp;
            }
            return compareValTypeList(idxA, a.returns(), idxB, b.returns());
        }

        private int compareStructType(int idxA, StructType a, int idxB, StructType b) {
            FieldType[] aFields = a.fieldTypes();
            FieldType[] bFields = b.fieldTypes();
            if (aFields.length != bFields.length) {
                return Integer.compare(aFields.length, bFields.length);
            }
            for (int i = 0; i < aFields.length; i++) {
                int cmp = compareFieldType(idxA, aFields[i], idxB, bFields[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        }

        private int compareArrayType(int idxA, ArrayType a, int idxB, ArrayType b) {
            return compareFieldType(idxA, a.fieldType(), idxB, b.fieldType());
        }

        private int compareFieldType(int idxA, FieldType a, int idxB, FieldType b) {
            int mutCmp = a.mut().compareTo(b.mut());
            if (mutCmp != 0) {
                return mutCmp;
            }
            return compareStorageType(idxA, a.storageType(), idxB, b.storageType());
        }

        private int compareStorageType(int idxA, StorageType a, int idxB, StorageType b) {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }

            if (a.valType() != null && b.valType() != null) {
                return compareValType(idxA, a.valType(), idxB, b.valType());
            }
            if (a.valType() != null) {
                return 1;
            }
            if (b.valType() != null) {
                return -1;
            }

            if (a.packedType() != null && b.packedType() != null) {
                return a.packedType().compareTo(b.packedType());
            }
            if (a.packedType() != null) {
                return 1;
            }
            if (b.packedType() != null) {
                return -1;
            }

            return 0;
        }

        private int compareValTypeList(int idxA, List<ValType> a, int idxB, List<ValType> b) {
            if (a.size() != b.size()) {
                return Integer.compare(a.size(), b.size());
            }
            for (int i = 0; i < a.size(); i++) {
                int cmp = compareValType(idxA, a.get(i), idxB, b.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        }

        private int compareValType(int idxA, ValType a, int idxB, ValType b) {
            // Compare opcodes
            int opcodeCmp = Integer.compare(a.opcode(), b.opcode());
            if (opcodeCmp != 0) {
                return opcodeCmp;
            }

            // For reference types, compare using canonical IDs
            if (a.isReference() && a.typeIdx() >= 0 && b.isReference() && b.typeIdx() >= 0) {
                int aCanonical = normalizeTypeIdx(idxA, a.typeIdx());
                int bCanonical = normalizeTypeIdx(idxB, b.typeIdx());
                return Integer.compare(aCanonical, bCanonical);
            }

            // For abstract heap types or non-reference types, compare typeIdx directly
            return Integer.compare(a.typeIdx(), b.typeIdx());
        }

        private int normalizeTypeIdx(int contextIdx, int idx) {
            if (idx >= 0 && idx < canonicalIds.length) {
                if (groupStart[idx] == groupStart[contextIdx]) {
                    return -(groupOffset[idx] + 1);
                }
                if (groupStart[idx] > groupStart[contextIdx]) {
                    throw new InvalidException("unknown type " + idx);
                }
                return canonicalIds[idx];
            }
            return Integer.MIN_VALUE;
        }
    }

    private static final class GroupMetadata {
        final int[] groupStart;
        final int[] groupOffset;

        GroupMetadata(int[] groupStart, int[] groupOffset) {
            this.groupStart = groupStart;
            this.groupOffset = groupOffset;
        }
    }

    private static final class NormalizedGroup {
        private final NormalizedSubType[] subTypes;

        NormalizedGroup(NormalizedSubType[] subTypes) {
            this.subTypes = subTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NormalizedGroup)) {
                return false;
            }
            NormalizedGroup that = (NormalizedGroup) o;
            if (this.subTypes.length != that.subTypes.length) {
                return false;
            }
            for (int i = 0; i < this.subTypes.length; i++) {
                if (!this.subTypes[i].equals(that.subTypes[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (NormalizedSubType subType : subTypes) {
                result = 31 * result + subType.hashCode();
            }
            return result;
        }
    }

    public FunctionType[] types() {
        return functionTypes.clone();
    }

    public int typeCount() {
        return types.size();
    }

    public int definedTypeCount() {
        return definedTypes.size();
    }

    public int canonicalTypeId(int idx) {
        if (idx < 0 || idx >= canonicalTypeIds.length) {
            throw new InvalidException("unknown type " + idx);
        }
        return canonicalTypeIds[idx];
    }

    int[] groupOffset() {
        return groupOffset;
    }

    public FunctionType getType(int idx) {
        if (idx < 0 || idx >= definedTypeCount()) {
            throw new InvalidException("unknown type " + idx);
        }
        var fn = functionTypes[idx];
        if (fn == null) {
            throw new InvalidException("type " + idx + " is not a function type");
        }
        return fn;
    }

    public RecType getRecType(int idx) {
        return types.get(idx);
    }

    public SubType getSubType(int idx) {
        if (idx < 0 || idx >= definedTypes.size()) {
            throw new InvalidException("unknown type " + idx);
        }
        return definedTypes.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<RecType> types = new ArrayList<>();

        private Builder() {}

        @Deprecated
        public List<FunctionType> getTypes() {
            return types.stream()
                    .filter(RecType::isLegacy)
                    .map(RecType::legacy)
                    .collect(Collectors.toList());
        }

        /**
         * Add a function type definition to this section.
         *
         * @param functionType the function type to add to this section (must not be {@code null})
         * @return the Builder
         */
        public Builder addFunctionType(FunctionType functionType) {
            Objects.requireNonNull(functionType, "functionType");
            var type =
                    RecType.builder()
                            .withSubTypes(
                                    new SubType[] {
                                        SubType.builder()
                                                .withTypeIdx(new int[] {})
                                                .withFinal(true)
                                                .withCompType(
                                                        CompType.builder()
                                                                .withFuncType(functionType)
                                                                .build())
                                                .build()
                                    })
                            .build();
            types.add(type);
            return this;
        }

        public Builder addRecType(RecType recType) {
            Objects.requireNonNull(recType, "recType");
            types.add(recType);
            return this;
        }

        public TypeSection build() {
            return new TypeSection(types);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof TypeSection)) {
            return false;
        }
        TypeSection that = (TypeSection) o;
        return Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(types);
    }
}
