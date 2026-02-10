/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

public interface ImportValue {
    public String module();

    public String name();

    public Type type();

    public static enum Type {
        FUNCTION,
        GLOBAL,
        MEMORY,
        TABLE,
        TAG;

    }
}

