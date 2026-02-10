/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.Section;
import com.dylibso.chicory.wasm.types.TagType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TagSection
extends Section {
    private final List<TagType> tags;

    private TagSection(List<TagType> tags) {
        super(13L);
        this.tags = List.copyOf(tags);
    }

    public TagType[] types() {
        return this.tags.toArray(new TagType[0]);
    }

    public int tagCount() {
        return this.tags.size();
    }

    public TagType getTag(int idx) {
        return this.tags.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagSection)) {
            return false;
        }
        TagSection that = (TagSection)o;
        return Objects.equals(this.tags, that.tags);
    }

    public int hashCode() {
        return Objects.hashCode(this.tags);
    }

    public static final class Builder {
        private final List<TagType> tags = new ArrayList<TagType>();

        private Builder() {
        }

        public Builder addTagType(TagType tagType) {
            Objects.requireNonNull(tagType, "tagType");
            this.tags.add(tagType);
            return this;
        }

        public TagSection build() {
            return new TagSection(this.tags);
        }
    }
}

