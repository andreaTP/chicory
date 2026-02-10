/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;

public final class FunctionImport
extends Import {
    private final int typeIndex;

    public FunctionImport(String moduleName, String name, int typeIndex) {
        super(moduleName, name);
        this.typeIndex = typeIndex;
    }

    public int typeIndex() {
        return this.typeIndex;
    }

    @Override
    public ExternalType importType() {
        return ExternalType.FUNCTION;
    }

    @Override
    public boolean equals(Import other) {
        return other instanceof FunctionImport && this.equals((FunctionImport)other);
    }

    public boolean equals(FunctionImport other) {
        return this == other || super.equals(other) && this.typeIndex == other.typeIndex;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 19 + this.typeIndex;
    }

    @Override
    public StringBuilder toString(StringBuilder b) {
        b.append("func (type=").append(this.typeIndex).append(')');
        return super.toString(b);
    }
}

