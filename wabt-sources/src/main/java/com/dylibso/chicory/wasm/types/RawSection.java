/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;

public class RawSection
extends Section {
    private final byte[] contents;

    public RawSection(long id, byte[] contents) {
        super(id);
        this.contents = (byte[])contents.clone();
    }

    public byte[] contents() {
        return (byte[])this.contents.clone();
    }
}

