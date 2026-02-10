/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.Objects;

public final class TableImport
extends Import {
    private final ValType entryType;
    private final TableLimits limits;

    public TableImport(String moduleName, String name, ValType entryType, TableLimits limits) {
        super(moduleName, name);
        this.entryType = Objects.requireNonNull(entryType, "entryType");
        this.limits = Objects.requireNonNull(limits, "limits");
    }

    @Deprecated(since="1.3.0")
    public TableImport(String moduleName, String name, ValueType entryType, TableLimits limits) {
        super(moduleName, name);
        this.entryType = Objects.requireNonNull(entryType, "entryType").toValType();
        this.limits = Objects.requireNonNull(limits, "limits");
    }

    public ValType entryType() {
        return this.entryType;
    }

    public TableLimits limits() {
        return this.limits;
    }

    @Override
    public ExternalType importType() {
        return ExternalType.TABLE;
    }

    @Override
    public boolean equals(Import other) {
        return other instanceof TableImport && this.equals((TableImport)other);
    }

    public boolean equals(TableImport other) {
        return this == other || super.equals(other) && this.entryType == other.entryType && this.limits.equals(other.limits);
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 19 + this.entryType.hashCode()) * 19 + this.limits.hashCode();
    }

    @Override
    public StringBuilder toString(StringBuilder b) {
        b.append("table (type=").append(this.entryType).append(",limits=");
        this.limits.toString(b);
        b.append(')');
        return super.toString(b);
    }
}

