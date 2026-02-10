/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.wasm.ChicoryException;

@FunctionalInterface
public interface Machine {
    public long[] call(int var1, long[] var2) throws ChicoryException;
}

