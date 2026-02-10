/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.FunctionBody;
import com.dylibso.chicory.wasm.types.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CodeSection
extends Section {
    private final List<FunctionBody> functionBodies;
    private final boolean requiresDataCount;

    private CodeSection(List<FunctionBody> functionBodies, boolean requiresDataCount) {
        super(10L);
        this.functionBodies = List.copyOf(functionBodies);
        this.requiresDataCount = requiresDataCount;
    }

    public FunctionBody[] functionBodies() {
        return this.functionBodies.toArray(new FunctionBody[0]);
    }

    public int functionBodyCount() {
        return this.functionBodies.size();
    }

    public FunctionBody getFunctionBody(int idx) {
        return this.functionBodies.get(idx);
    }

    public boolean isRequiresDataCount() {
        return this.requiresDataCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof CodeSection)) {
            return false;
        }
        CodeSection that = (CodeSection)o;
        return this.requiresDataCount == that.requiresDataCount && Objects.equals(this.functionBodies, that.functionBodies);
    }

    public int hashCode() {
        return Objects.hash(this.functionBodies, this.requiresDataCount);
    }

    public static final class Builder {
        private final List<FunctionBody> functionBodies = new ArrayList<FunctionBody>();
        private boolean requiresDataCount;

        private Builder() {
        }

        public Builder addFunctionBody(FunctionBody functionBody) {
            Objects.requireNonNull(functionBody, "functionBody");
            this.functionBodies.add(functionBody);
            return this;
        }

        public Builder setRequiresDataCount(boolean requiresDataCount) {
            this.requiresDataCount = requiresDataCount;
            return this;
        }

        public CodeSection build() {
            return new CodeSection(this.functionBodies, this.requiresDataCount);
        }
    }
}

