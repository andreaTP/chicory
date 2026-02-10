/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import java.util.Objects;

public abstract class Import {
    private final String module;
    private final String name;

    Import(String module, String name) {
        this.module = Objects.requireNonNull(module, "moduleName");
        this.name = Objects.requireNonNull(name, "name");
    }

    public String module() {
        return this.module;
    }

    public String name() {
        return this.name;
    }

    public abstract ExternalType importType();

    public boolean equals(Object obj) {
        return obj instanceof Import && this.equals((Import)obj);
    }

    public boolean equals(Import other) {
        return other != null && this.module.equals(other.module) && this.name.equals(other.name);
    }

    public int hashCode() {
        return Objects.hash(this.module, this.name);
    }

    public StringBuilder toString(StringBuilder b) {
        return b.append('<').append(this.module).append('.').append(this.name).append('>');
    }

    public String toString() {
        return this.toString(new StringBuilder()).toString();
    }
}

