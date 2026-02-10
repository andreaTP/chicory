/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;

public final class ActiveElement
extends Element {
    private final int tableIndex;
    private final List<Instruction> offset;

    public ActiveElement(ValType type, List<List<Instruction>> initializers, int tableIndex, List<Instruction> offset) {
        super(type, initializers);
        this.tableIndex = tableIndex;
        this.offset = List.copyOf(offset);
    }

    public int tableIndex() {
        return this.tableIndex;
    }

    public List<Instruction> offset() {
        return this.offset;
    }
}

