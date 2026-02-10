/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime.alloc;

import com.dylibso.chicory.runtime.alloc.MemAllocStrategy;

@Deprecated
public final class ExactMemAllocStrategy
implements MemAllocStrategy {
    @Override
    public int initial(int min) {
        return min;
    }

    @Override
    public int next(int current, int target) {
        return target;
    }
}

