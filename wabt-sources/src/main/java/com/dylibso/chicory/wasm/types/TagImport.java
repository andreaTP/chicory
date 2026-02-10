/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.Import;
import com.dylibso.chicory.wasm.types.TagType;
import java.util.Objects;

public final class TagImport
extends Import {
    private final TagType tagType;

    public TagImport(String moduleName, String name, byte attribute, int tagTypeIdx) {
        super(moduleName, name);
        this.tagType = new TagType(attribute, tagTypeIdx);
    }

    public TagType tagType() {
        return this.tagType;
    }

    @Override
    public ExternalType importType() {
        return ExternalType.TAG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TagImport tagImport = (TagImport)o;
        return Objects.equals(this.tagType, tagImport.tagType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.tagType);
    }

    @Override
    public String toString() {
        return "TagImport{tagType=" + String.valueOf(this.tagType) + "}";
    }
}

