/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.InvalidException;

public final class MemoryLimits {
    public static final int MAX_PAGES = 65536;
    private static final MemoryLimits DEFAULT_LIMITS = new MemoryLimits(0, 65536);
    private final int initial;
    private final int maximum;
    private final boolean shared;

    public MemoryLimits(int initial) {
        this(initial, 65536);
    }

    public MemoryLimits(int initial, int maximum) {
        this(initial, maximum, false);
    }

    public MemoryLimits(int initial, int maximum, boolean shared) {
        if (initial > 65536 || maximum > 65536 || initial < 0 || maximum < 0) {
            throw new InvalidException("memory size must be at most 65536 pages (4GiB)");
        }
        if (initial > maximum) {
            throw new InvalidException("size minimum must not be greater than maximum");
        }
        this.initial = initial;
        this.maximum = maximum;
        this.shared = shared;
    }

    public static MemoryLimits defaultLimits() {
        return DEFAULT_LIMITS;
    }

    public int initialPages() {
        return this.initial;
    }

    public int maximumPages() {
        return this.maximum;
    }

    public boolean shared() {
        return this.shared;
    }

    public boolean equals(Object obj) {
        return obj instanceof MemoryLimits && this.equals((MemoryLimits)obj);
    }

    public boolean equals(MemoryLimits other) {
        return this == other || other != null && this.initial == other.initial && this.maximum == other.maximum;
    }

    public int hashCode() {
        return this.maximum * 19 + this.initial;
    }

    public String toString() {
        return this.toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder b) {
        b.append("[").append(this.initial).append(',');
        if (this.maximum == 65536) {
            b.append("max");
        } else {
            b.append(this.maximum);
        }
        b.append(']');
        return b;
    }
}

