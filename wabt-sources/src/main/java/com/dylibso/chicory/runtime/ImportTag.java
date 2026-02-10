/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportValue;
import com.dylibso.chicory.runtime.TagInstance;

public class ImportTag
implements ImportValue {
    private final String module;
    private final String name;
    private final TagInstance tag;

    public ImportTag(String module, String name, TagInstance tag) {
        this.module = module;
        this.name = name;
        this.tag = tag;
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
        return ImportValue.Type.TAG;
    }

    public TagInstance tag() {
        return this.tag;
    }
}

