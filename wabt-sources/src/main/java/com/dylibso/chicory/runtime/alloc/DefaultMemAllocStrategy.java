/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime.alloc;

import com.dylibso.chicory.runtime.alloc.MemAllocStrategy;

@Deprecated
public final class DefaultMemAllocStrategy
implements MemAllocStrategy {
    private final int max;

    public DefaultMemAllocStrategy(int max) {
        this.max = max;
    }

    @Override
    public int initial(int min) {
        return min;
    }

    @Override
    public int next(int current, int target) {
        int next;
        int n = next = current <= 0 ? target : current;
        while (next < target && next < this.max) {
            if ((next <<= 1) >= 0) continue;
            return this.max;
        }
        return Math.min(this.max, next);
    }
}

