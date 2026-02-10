/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;

public abstract class CustomSection
extends Section {
    CustomSection() {
        super(0L);
    }

    public abstract String name();
}

