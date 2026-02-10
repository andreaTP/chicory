/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.GlobalInstance;
import com.dylibso.chicory.runtime.ImportValue;

public class ImportGlobal
implements ImportValue {
    private final GlobalInstance instance;
    private final String module;
    private final String name;

    public ImportGlobal(String module, String name, GlobalInstance instance) {
        this.instance = instance;
        this.module = module;
        this.name = name;
    }

    public GlobalInstance instance() {
        return this.instance;
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
        return ImportValue.Type.GLOBAL;
    }
}

