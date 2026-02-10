/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.CustomSection;
import java.util.Objects;

public final class UnknownCustomSection
extends CustomSection {
    private final String name;
    private final byte[] bytes;

    private UnknownCustomSection(String name, byte[] bytes) {
        this.name = Objects.requireNonNull(name, "name");
        this.bytes = (byte[])bytes.clone();
    }

    @Override
    public String name() {
        return this.name;
    }

    public byte[] bytes() {
        return (byte[])this.bytes.clone();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private byte[] bytes;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        public Builder withBytes(byte[] bytes) {
            this.bytes = Objects.requireNonNull(bytes);
            return this;
        }

        public UnknownCustomSection build() {
            return new UnknownCustomSection(this.name, this.bytes);
        }
    }
}

