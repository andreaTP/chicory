/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import java.util.Objects;

public class Export {
    private final String name;
    private final int index;
    private final ExternalType exportType;

    public Export(String name, int index, ExternalType exportType) {
        this.name = Objects.requireNonNull(name, "name");
        this.index = index;
        this.exportType = Objects.requireNonNull(exportType, "type");
    }

    public String name() {
        return this.name;
    }

    public int index() {
        return this.index;
    }

    public ExternalType exportType() {
        return this.exportType;
    }

    public int hashCode() {
        return (this.name.hashCode() * 31 + this.index) * 31 + this.exportType.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof Export && this.equals((Export)obj);
    }

    public boolean equals(Export other) {
        return this == other || other != null && this.index == other.index && this.exportType == other.exportType && this.name.equals(other.name);
    }
}

