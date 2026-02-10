/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.OpcodeImpl;

public final class MemCopyWorkaround {
    private MemCopyWorkaround() {
    }

    public static boolean shouldUseMemWorkaround() {
        return false;
    }

    public static boolean shouldUseMemWorkaround(String version) {
        return false;
    }

    public static void memoryCopy(int destination, int offset, int size, Memory memory) {
        memory.copy(destination, offset, size);
    }

    public static int i32_ge_u(int a, int b) {
        return OpcodeImpl.I32_GE_U(a, b);
    }
}

