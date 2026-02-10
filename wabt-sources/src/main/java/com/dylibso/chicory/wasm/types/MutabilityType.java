/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.MalformedException;

public enum MutabilityType {
    Const(0),
    Var(1);

    private final int id;

    private MutabilityType(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }

    public static MutabilityType forId(int id) {
        switch (id) {
            case 0: {
                return Const;
            }
            case 1: {
                return Var;
            }
        }
        throw new MalformedException("Global malformed mutability");
    }

    static final class ID {
        static final int Const = 0;
        static final int Var = 1;

        private ID() {
        }
    }
}

