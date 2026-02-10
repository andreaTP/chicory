/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.DataSegment;

public final class PassiveDataSegment
extends DataSegment {
    public static final PassiveDataSegment EMPTY = new PassiveDataSegment(new byte[0]);

    public PassiveDataSegment(byte[] data) {
        super(data);
    }
}

