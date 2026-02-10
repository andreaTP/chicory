/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime.alloc;

@Deprecated
public interface MemAllocStrategy {
    public int initial(int var1);

    public int next(int var1, int var2);
}

