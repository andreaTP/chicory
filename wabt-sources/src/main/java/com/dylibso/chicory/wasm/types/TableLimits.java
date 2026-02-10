/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.InvalidException;

public class TableLimits {
    public static final long LIMIT_MAX = 10000000L;
    private static final TableLimits UNBOUNDED = new TableLimits(0L);
    private long min;
    private final long max;
    private final boolean shared;

    public TableLimits(long min) {
        this(min, 10000000L);
    }

    public TableLimits(long min, long max) {
        this(min, max, false);
    }

    public TableLimits(long min, long max, boolean shared) {
        if (min > max) {
            throw new InvalidException("size minimum must not be greater than maximum");
        }
        this.min = Math.min(Math.max(0L, min), 10000000L);
        this.max = Math.min(max, 10000000L);
        this.shared = shared;
    }

    public void grow(int size) {
        this.min += (long)size;
    }

    public long min() {
        return this.min;
    }

    public long max() {
        return this.max;
    }

    public boolean shared() {
        return this.shared;
    }

    public boolean equals(Object obj) {
        return obj instanceof TableLimits && this.equals((TableLimits)obj);
    }

    public boolean equals(TableLimits other) {
        return this == other || other != null && this.min == other.min && this.max == other.max;
    }

    public int hashCode() {
        return Long.hashCode(this.min) * 19 + Long.hashCode(this.max);
    }

    public String toString() {
        return this.toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder b) {
        b.append("[").append(this.min).append(',');
        if (this.max == 10000000L) {
            b.append("max");
        } else {
            b.append(this.max);
        }
        return b.append(']');
    }

    public static TableLimits unbounded() {
        return UNBOUNDED;
    }
}

