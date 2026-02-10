/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;

public class WasmException
extends RuntimeException {
    private final int tagIdx;
    private final long[] args;
    private final Instance instance;

    public WasmException(Instance instance, int tagIdx, long[] args) {
        this.instance = instance;
        this.tagIdx = tagIdx;
        this.args = (long[])args.clone();
        this.setStackTrace(new StackTraceElement[0]);
    }

    public Instance instance() {
        return this.instance;
    }

    public int tagIdx() {
        return this.tagIdx;
    }

    public long[] args() {
        return this.args;
    }
}

