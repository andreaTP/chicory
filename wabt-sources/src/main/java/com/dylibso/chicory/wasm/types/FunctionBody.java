/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;
import java.util.Objects;

public final class FunctionBody {
    private final List<ValType> locals;
    private final List<AnnotatedInstruction> instructions;

    public FunctionBody(List<ValType> locals, List<AnnotatedInstruction> instructions) {
        this.locals = List.copyOf(locals);
        this.instructions = List.copyOf(instructions);
    }

    public List<ValType> localTypes() {
        return this.locals;
    }

    public List<AnnotatedInstruction> instructions() {
        return this.instructions;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof FunctionBody)) {
            return false;
        }
        FunctionBody that = (FunctionBody)o;
        return Objects.equals(this.locals, that.locals) && Objects.equals(this.instructions, that.instructions);
    }

    public int hashCode() {
        return Objects.hash(this.locals, this.instructions);
    }
}

