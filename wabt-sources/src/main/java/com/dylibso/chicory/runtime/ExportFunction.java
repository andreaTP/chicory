/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.wasm.ChicoryException;

@FunctionalInterface
public interface ExportFunction {
    public long[] apply(long ... var1) throws ChicoryException;
}

