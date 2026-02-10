/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DataSection
extends Section {
    private final List<DataSegment> dataSegments;

    private DataSection(List<DataSegment> dataSegments) {
        super(11L);
        this.dataSegments = List.copyOf(dataSegments);
    }

    public DataSegment[] dataSegments() {
        return this.dataSegments.toArray(new DataSegment[0]);
    }

    public int dataSegmentCount() {
        return this.dataSegments.size();
    }

    public DataSegment getDataSegment(int idx) {
        return this.dataSegments.get(idx);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DataSection)) {
            return false;
        }
        DataSection that = (DataSection)o;
        return Objects.equals(this.dataSegments, that.dataSegments);
    }

    public int hashCode() {
        return Objects.hashCode(this.dataSegments);
    }

    public static final class Builder {
        private final List<DataSegment> dataSegments = new ArrayList<DataSegment>();

        private Builder() {
        }

        public Builder addDataSegment(DataSegment dataSegment) {
            Objects.requireNonNull(dataSegment, "dataSegment");
            this.dataSegments.add(dataSegment);
            return this;
        }

        public DataSection build() {
            return new DataSection(this.dataSegments);
        }
    }
}

