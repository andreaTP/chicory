/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;
import java.util.Objects;

public abstract class Element {
    private final ValType type;
    private final List<List<Instruction>> initializers;

    Element(ValType type, List<List<Instruction>> initializers) {
        this.type = Objects.requireNonNull(type, "type");
        this.initializers = List.copyOf(initializers);
    }

    public ValType type() {
        return this.type;
    }

    public List<List<Instruction>> initializers() {
        return this.initializers;
    }

    public int elementCount() {
        return this.initializers().size();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Element)) {
            return false;
        }
        Element element = (Element)o;
        return this.type.equals(element.type) && Objects.equals(this.initializers, element.initializers);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.initializers);
    }
}

