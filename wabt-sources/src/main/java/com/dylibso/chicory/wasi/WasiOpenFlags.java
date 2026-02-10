/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

final class WasiOpenFlags {
    public static final int CREAT = WasiOpenFlags.bit(0);
    public static final int DIRECTORY = WasiOpenFlags.bit(1);
    public static final int EXCL = WasiOpenFlags.bit(2);
    public static final int TRUNC = WasiOpenFlags.bit(3);

    private WasiOpenFlags() {
    }

    private static int bit(int n) {
        return 1 << n;
    }
}

