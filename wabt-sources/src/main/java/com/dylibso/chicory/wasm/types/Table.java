/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.List;
import java.util.Objects;

public class Table {
    private final ValType elementType;
    private final TableLimits limits;
    private final List<Instruction> init;

    public Table(ValType elementType, TableLimits limits) {
        this(elementType, limits, List.of(new Instruction(-1, OpCode.REF_NULL, new long[]{elementType.typeIdx()})));
    }

    @Deprecated(since="1.3.0")
    public Table(ValueType elementType, TableLimits limits) {
        this(elementType.toValType(), limits, List.of(new Instruction(-1, OpCode.REF_NULL, new long[]{elementType.toValType().typeIdx()})));
    }

    public Table(ValType elementType, TableLimits limits, List<Instruction> init) {
        this.elementType = Objects.requireNonNull(elementType, "elementType");
        if (!elementType.isReference()) {
            throw new IllegalArgumentException("Table element type must be a reference type");
        }
        this.limits = Objects.requireNonNull(limits, "limits");
        this.init = init;
    }

    public ValType elementType() {
        return this.elementType;
    }

    public TableLimits limits() {
        return this.limits;
    }

    public List<Instruction> initialize() {
        return this.init;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Table)) {
            return false;
        }
        Table table = (Table)o;
        return this.elementType.equals(table.elementType) && Objects.equals(this.limits, table.limits);
    }

    public int hashCode() {
        return Objects.hash(this.elementType, this.limits);
    }
}

