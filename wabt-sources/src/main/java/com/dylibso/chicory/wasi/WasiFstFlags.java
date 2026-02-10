/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

final class WasiFstFlags {
    public static final int ATIM = WasiFstFlags.bit(0);
    public static final int ATIM_NOW = WasiFstFlags.bit(1);
    public static final int MTIM = WasiFstFlags.bit(2);
    public static final int MTIM_NOW = WasiFstFlags.bit(3);

    private WasiFstFlags() {
    }

    private static int bit(int n) {
        return 1 << n;
    }
}

