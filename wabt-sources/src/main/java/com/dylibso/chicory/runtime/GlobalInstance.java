/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;

public class GlobalInstance {
    private long valueLow;
    private long valueHigh;
    private final ValType valType;
    private Instance instance;
    private final MutabilityType mutabilityType;

    public GlobalInstance(Value value) {
        this(value, MutabilityType.Const);
    }

    public GlobalInstance(Value value, MutabilityType mutabilityType) {
        this.valueLow = value.raw();
        this.valueHigh = 0L;
        this.valType = value.type();
        this.mutabilityType = mutabilityType;
    }

    @Deprecated(since="1.3.0")
    public GlobalInstance(long valueLow, long valueHigh, ValueType valueType, MutabilityType mutabilityType) {
        this.valueLow = valueLow;
        this.valueHigh = valueHigh;
        this.valType = valueType.toValType();
        this.mutabilityType = mutabilityType;
    }

    public GlobalInstance(long valueLow, long valueHigh, ValType valType, MutabilityType mutabilityType) {
        this.valueLow = valueLow;
        this.valueHigh = valueHigh;
        this.valType = valType;
        this.mutabilityType = mutabilityType;
    }

    public long getValueLow() {
        return this.valueLow;
    }

    public long getValueHigh() {
        return this.valueHigh;
    }

    public long getValue() {
        return this.valueLow;
    }

    public ValType getType() {
        return this.valType;
    }

    public void setValue(Value value) {
        assert (value.type() == this.valType);
        this.valueLow = value.raw();
    }

    public void setValue(long value) {
        this.valueLow = value;
    }

    public void setValueLow(long value) {
        this.valueLow = value;
    }

    public void setValueHigh(long value) {
        this.valueHigh = value;
    }

    public Instance getInstance() {
        return this.instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public MutabilityType getMutabilityType() {
        return this.mutabilityType;
    }
}

