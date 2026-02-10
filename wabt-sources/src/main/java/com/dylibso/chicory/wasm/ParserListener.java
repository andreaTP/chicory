/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.types.Section;

@FunctionalInterface
public interface ParserListener {
    public void onSection(Section var1);
}

