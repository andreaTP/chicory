/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;

@FunctionalInterface
public interface WasmFunctionHandle {
    public long[] apply(Instance var1, long ... var2);
}

