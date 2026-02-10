/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

final class WasiFdFlags {
    public static final int APPEND = WasiFdFlags.bit(0);
    public static final int DSYNC = WasiFdFlags.bit(1);
    public static final int NONBLOCK = WasiFdFlags.bit(2);
    public static final int RSYNC = WasiFdFlags.bit(3);
    public static final int SYNC = WasiFdFlags.bit(4);

    private WasiFdFlags() {
    }

    private static int bit(int n) {
        return 1 << n;
    }
}

