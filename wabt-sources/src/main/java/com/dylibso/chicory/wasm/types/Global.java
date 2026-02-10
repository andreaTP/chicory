/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.List;
import java.util.Objects;

public final class Global {
    private final ValType valType;
    private final MutabilityType mutabilityType;
    private final List<Instruction> init;

    public Global(ValType valType, MutabilityType mutabilityType, List<Instruction> init) {
        this.valType = valType;
        this.mutabilityType = mutabilityType;
        this.init = List.copyOf(init);
    }

    @Deprecated(since="1.3.0")
    public Global(ValueType valueType, MutabilityType mutabilityType, List<Instruction> init) {
        this.valType = valueType.toValType();
        this.mutabilityType = mutabilityType;
        this.init = List.copyOf(init);
    }

    public MutabilityType mutabilityType() {
        return this.mutabilityType;
    }

    public ValType valueType() {
        return this.valType;
    }

    public List<Instruction> initInstructions() {
        return this.init;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Global)) {
            return false;
        }
        Global global = (Global)o;
        return this.valType.equals(global.valType) && this.mutabilityType == global.mutabilityType && Objects.equals(this.init, global.init);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.valType, this.mutabilityType, this.init});
    }
}

