/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.Objects;

public final class GlobalImport
extends Import {
    private final MutabilityType mutabilityType;
    private final ValType type;

    public GlobalImport(String moduleName, String name, MutabilityType mutabilityType, ValType type) {
        super(moduleName, name);
        this.mutabilityType = Objects.requireNonNull(mutabilityType, "mutabilityType");
        this.type = Objects.requireNonNull(type, "type");
    }

    @Deprecated(since="1.3.0")
    public GlobalImport(String moduleName, String name, MutabilityType mutabilityType, ValueType type) {
        super(moduleName, name);
        this.mutabilityType = Objects.requireNonNull(mutabilityType, "mutabilityType");
        this.type = Objects.requireNonNull(type, "type").toValType();
    }

    public MutabilityType mutabilityType() {
        return this.mutabilityType;
    }

    public ValType type() {
        return this.type;
    }

    @Override
    public ExternalType importType() {
        return ExternalType.GLOBAL;
    }

    @Override
    public boolean equals(Import other) {
        return other instanceof GlobalImport && this.equals((GlobalImport)other);
    }

    public boolean equals(GlobalImport other) {
        return this == other || super.equals(other) && this.mutabilityType == other.mutabilityType && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 19 + this.mutabilityType.hashCode()) * 19 + this.type.hashCode();
    }

    @Override
    public StringBuilder toString(StringBuilder b) {
        b.append("global (type=").append(this.type).append(",mut=").append((Object)this.mutabilityType).append(')');
        return super.toString(b);
    }
}

