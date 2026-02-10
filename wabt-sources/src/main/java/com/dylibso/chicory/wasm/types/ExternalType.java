/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import java.util.List;

public final class ExternalType
extends Enum<ExternalType> {
    public static final /* enum */ ExternalType FUNCTION = new ExternalType(0);
    public static final /* enum */ ExternalType TABLE = new ExternalType(1);
    public static final /* enum */ ExternalType MEMORY = new ExternalType(2);
    public static final /* enum */ ExternalType GLOBAL = new ExternalType(3);
    public static final /* enum */ ExternalType TAG = new ExternalType(4);
    private final int id;
    private static final List<ExternalType> values;
    private static final /* synthetic */ ExternalType[] $VALUES;

    public static ExternalType[] values() {
        return (ExternalType[])$VALUES.clone();
    }

    public static ExternalType valueOf(String name) {
        return Enum.valueOf(ExternalType.class, name);
    }

    private ExternalType(int id) {
        this.id = id;
        assert (this.ordinal() == id);
    }

    public int id() {
        return this.id;
    }

    public static ExternalType byId(int id) {
        return values.get(id);
    }

    private static /* synthetic */ ExternalType[] $values() {
        return new ExternalType[]{FUNCTION, TABLE, MEMORY, GLOBAL, TAG};
    }

    static {
        $VALUES = ExternalType.$values();
        values = List.of(ExternalType.values());
    }
}

