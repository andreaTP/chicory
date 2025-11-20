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

    private TypeSection(List<RecType> types) {
        super(SectionId.TYPE);
        this.types = List.copyOf(types);
        this.definedTypes = flatten(types);
        this.functionTypes = buildFunctionTypes(this.definedTypes);
        this.canonicalTypeIds = computeCanonicalTypeIds(this.definedTypes);
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

    /**
     * Computes canonical type IDs by iteratively comparing SubTypes structurally until convergence.
     * Two types get the same canonical ID if they are structurally equivalent.
     * Uses a custom comparator that handles normalization on-the-fly.
     */
    private static int[] computeCanonicalTypeIds(List<SubType> definedTypes) {
        int n = definedTypes.size();
        // Start with raw indices as canonical IDs
        int[] canonicalIds = new int[n];
        for (int i = 0; i < n; i++) {
            canonicalIds[i] = i;
        }

        // Iteratively refine canonical IDs until convergence
        int[] prevIds = Arrays.copyOf(canonicalIds, n);
        int[] nextIds = new int[n];
        final int maxIterations = Math.max(4, n * 4);
        boolean changed = true;
        int iteration = 0;

        while (changed && iteration++ < maxIterations) {
            changed = false;
            // Use wrapper class that implements equals/hashCode with normalization
            Map<NormalizedSubType, Integer> normalizedToId = new HashMap<>();
            for (int i = 0; i < n; i++) {
                NormalizedSubType normalized = new NormalizedSubType(definedTypes.get(i), prevIds);
                nextIds[i] = normalizedToId.computeIfAbsent(normalized, k -> normalizedToId.size());
                if (nextIds[i] != prevIds[i]) {
                    changed = true;
                }
            }
            System.arraycopy(nextIds, 0, prevIds, 0, n);
        }
        return prevIds;
    }

    /**
     * Wrapper class that implements equals/hashCode for SubType using canonical IDs.
     * This allows HashMap to work correctly with structural equivalence.
     */
    private static class NormalizedSubType {
        private final SubType subType;
        private final int[] canonicalIds;

        NormalizedSubType(SubType subType, int[] canonicalIds) {
            this.subType = subType;
            this.canonicalIds = canonicalIds;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NormalizedSubType)) {
                return false;
            }
            NormalizedSubType that = (NormalizedSubType) o;
            return new StructuralTypeComparator(canonicalIds).compare(this.subType, that.subType)
                    == 0;
        }

        @Override
        public int hashCode() {
            // Compute hash based on structural comparison
            // We'll use a simplified hash that matches the comparison logic
            int result = Boolean.hashCode(subType.isFinal());
            result = 31 * result + Arrays.hashCode(normalizeSuperTypes(subType.typeIdx()));
            result = 31 * result + hashCompType(subType.compType());
            return result;
        }

        private int[] normalizeSuperTypes(int[] superTypes) {
            int[] normalized = new int[superTypes.length];
            for (int i = 0; i < superTypes.length; i++) {
                normalized[i] = normalizeTypeIdx(superTypes[i]);
            }
            return normalized;
        }

        private int hashCompType(CompType compType) {
            if (compType == null) {
                return 0;
            }
            if (compType.funcType() != null) {
                return hashFunctionType(compType.funcType());
            }
            if (compType.structType() != null) {
                return hashStructType(compType.structType());
            }
            if (compType.arrayType() != null) {
                return hashArrayType(compType.arrayType());
            }
            return 0;
        }

        private int hashFunctionType(FunctionType funcType) {
            return hashValTypeList(funcType.params()) * 31 + hashValTypeList(funcType.returns());
        }

        private int hashStructType(StructType structType) {
            int result = structType.fieldTypes().length;
            for (FieldType field : structType.fieldTypes()) {
                result = 31 * result + hashFieldType(field);
            }
            return result;
        }

        private int hashArrayType(ArrayType arrayType) {
            return hashFieldType(arrayType.fieldType());
        }

        private int hashFieldType(FieldType fieldType) {
            return fieldType.mut().hashCode() * 31 + hashStorageType(fieldType.storageType());
        }

        private int hashStorageType(StorageType storageType) {
            if (storageType == null) {
                return 0;
            }
            if (storageType.valType() != null) {
                return hashValType(storageType.valType());
            }
            if (storageType.packedType() != null) {
                return storageType.packedType().hashCode();
            }
            return 0;
        }

        private int hashValTypeList(List<ValType> valTypes) {
            int result = valTypes.size();
            for (ValType vt : valTypes) {
                result = 31 * result + hashValType(vt);
            }
            return result;
        }

        private int hashValType(ValType valType) {
            int result = valType.opcode();
            if (valType.isReference() && valType.typeIdx() >= 0) {
                result = 31 * result + normalizeTypeIdx(valType.typeIdx());
            } else {
                result = 31 * result + valType.typeIdx();
            }
            return result;
        }

        private int normalizeTypeIdx(int idx) {
            if (idx >= 0 && idx < canonicalIds.length) {
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

        StructuralTypeComparator(int[] canonicalIds) {
            this.canonicalIds = canonicalIds;
        }

        int compare(SubType a, SubType b) {
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
                int aCanonical = normalizeTypeIdx(aSuperTypes[i]);
                int bCanonical = normalizeTypeIdx(bSuperTypes[i]);
                if (aCanonical != bCanonical) {
                    return Integer.compare(aCanonical, bCanonical);
                }
            }

            // Compare composite types
            return compareCompType(a.compType(), b.compType());
        }

        private int compareCompType(CompType a, CompType b) {
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
                return compareFunctionType(a.funcType(), b.funcType());
            }
            if (a.funcType() != null) {
                return 1;
            }
            if (b.funcType() != null) {
                return -1;
            }

            // Compare struct types
            if (a.structType() != null && b.structType() != null) {
                return compareStructType(a.structType(), b.structType());
            }
            if (a.structType() != null) {
                return 1;
            }
            if (b.structType() != null) {
                return -1;
            }

            // Compare array types
            if (a.arrayType() != null && b.arrayType() != null) {
                return compareArrayType(a.arrayType(), b.arrayType());
            }
            if (a.arrayType() != null) {
                return 1;
            }
            if (b.arrayType() != null) {
                return -1;
            }

            return 0;
        }

        private int compareFunctionType(FunctionType a, FunctionType b) {
            int paramCmp = compareValTypeList(a.params(), b.params());
            if (paramCmp != 0) {
                return paramCmp;
            }
            return compareValTypeList(a.returns(), b.returns());
        }

        private int compareStructType(StructType a, StructType b) {
            FieldType[] aFields = a.fieldTypes();
            FieldType[] bFields = b.fieldTypes();
            if (aFields.length != bFields.length) {
                return Integer.compare(aFields.length, bFields.length);
            }
            for (int i = 0; i < aFields.length; i++) {
                int cmp = compareFieldType(aFields[i], bFields[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        }

        private int compareArrayType(ArrayType a, ArrayType b) {
            return compareFieldType(a.fieldType(), b.fieldType());
        }

        private int compareFieldType(FieldType a, FieldType b) {
            int mutCmp = a.mut().compareTo(b.mut());
            if (mutCmp != 0) {
                return mutCmp;
            }
            return compareStorageType(a.storageType(), b.storageType());
        }

        private int compareStorageType(StorageType a, StorageType b) {
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
                return compareValType(a.valType(), b.valType());
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

        private int compareValTypeList(List<ValType> a, List<ValType> b) {
            if (a.size() != b.size()) {
                return Integer.compare(a.size(), b.size());
            }
            for (int i = 0; i < a.size(); i++) {
                int cmp = compareValType(a.get(i), b.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        }

        private int compareValType(ValType a, ValType b) {
            // Compare opcodes
            int opcodeCmp = Integer.compare(a.opcode(), b.opcode());
            if (opcodeCmp != 0) {
                return opcodeCmp;
            }

            // For reference types, compare using canonical IDs
            if (a.isReference() && a.typeIdx() >= 0 && b.isReference() && b.typeIdx() >= 0) {
                int aCanonical = normalizeTypeIdx(a.typeIdx());
                int bCanonical = normalizeTypeIdx(b.typeIdx());
                return Integer.compare(aCanonical, bCanonical);
            }

            // For abstract heap types or non-reference types, compare typeIdx directly
            return Integer.compare(a.typeIdx(), b.typeIdx());
        }

        private int normalizeTypeIdx(int idx) {
            if (idx >= 0 && idx < canonicalIds.length) {
                return canonicalIds[idx];
            }
            return Integer.MIN_VALUE;
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
