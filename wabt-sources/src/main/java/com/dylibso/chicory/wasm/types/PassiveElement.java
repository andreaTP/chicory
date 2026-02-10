/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;

public final class PassiveElement
extends Element {
    public PassiveElement(ValType type, List<List<Instruction>> initializers) {
        super(type, initializers);
    }
}

