/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import java.util.Arrays;
import java.util.Objects;

public abstract class DataSegment {
    private final byte[] data;

    DataSegment(byte[] data) {
        this.data = (byte[])data.clone();
    }

    public byte[] data() {
        return (byte[])this.data.clone();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DataSegment)) {
            return false;
        }
        DataSegment that = (DataSegment)o;
        return Objects.deepEquals(this.data, that.data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
}

