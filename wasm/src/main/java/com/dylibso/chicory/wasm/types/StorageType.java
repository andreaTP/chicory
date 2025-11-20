package com.dylibso.chicory.wasm.types;

import java.util.Objects;

public final class StorageType {
    private final ValType valType;
    private final PackedType packedType;

    private StorageType(ValType valType, PackedType packedType) {
        this.valType = valType;
        this.packedType = packedType;
    }

    public ValType valType() {
        return valType;
    }

    public PackedType packedType() {
        return packedType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageType)) {
            return false;
        }
        StorageType that = (StorageType) o;
        return Objects.equals(valType, that.valType) && packedType == that.packedType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valType, packedType);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ValType valType;
        private PackedType packedType;

        private Builder() {}

        public Builder withValType(ValType valType) {
            this.valType = valType;
            return this;
        }

        public Builder withPackedType(PackedType packedType) {
            this.packedType = packedType;
            return this;
        }

        public StorageType build() {
            return new StorageType(valType, packedType);
        }
    }
}
