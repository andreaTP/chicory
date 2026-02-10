/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportValue;
import com.dylibso.chicory.runtime.Memory;

public class ImportMemory
implements ImportValue {
    private final String module;
    private final String name;
    private final Memory memory;

    public ImportMemory(String module, String name, Memory memory) {
        this.module = module;
        this.name = name;
        this.memory = memory;
    }

    @Override
    public String module() {
        return this.module;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ImportValue.Type type() {
        return ImportValue.Type.MEMORY;
    }

    public Memory memory() {
        return this.memory;
    }
}

