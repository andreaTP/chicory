/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.MemoryLimits;

public final class MemoryImport
extends Import {
    private final MemoryLimits limits;

    public MemoryImport(String moduleName, String name, MemoryLimits limits) {
        super(moduleName, name);
        this.limits = limits;
    }

    public MemoryLimits limits() {
        return this.limits;
    }

    @Override
    public ExternalType importType() {
        return ExternalType.MEMORY;
    }

    @Override
    public boolean equals(Import other) {
        return other instanceof MemoryImport && this.equals((MemoryImport)other);
    }

    public boolean equals(MemoryImport other) {
        return this == other || super.equals(other) && this.limits.equals(other.limits);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 19 + this.limits.hashCode();
    }

    @Override
    public StringBuilder toString(StringBuilder b) {
        b.append("memory (limits=");
        this.limits.toString(b);
        b.append(')');
        return super.toString(b);
    }
}

