/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.types.CatchOpCode;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.OpCode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AnnotatedInstruction
extends Instruction {
    public static final int UNDEFINED_LABEL = -1;
    private final int depth;
    private final int labelTrue;
    private final int labelFalse;
    private final List<Integer> labelTable;
    private final List<CatchOpCode.Catch> catches;
    private final Instruction scope;

    private AnnotatedInstruction(int address, OpCode opcode, long[] operands, int depth, int labelTrue, int labelFalse, List<Integer> labelTable, List<CatchOpCode.Catch> catches, Instruction scope) {
        super(address, opcode, operands);
        this.depth = depth;
        this.labelTrue = labelTrue;
        this.labelFalse = labelFalse;
        this.labelTable = labelTable;
        this.catches = catches;
        this.scope = scope;
    }

    public int labelTrue() {
        return this.labelTrue;
    }

    public int labelFalse() {
        return this.labelFalse;
    }

    public List<Integer> labelTable() {
        return this.labelTable;
    }

    public List<CatchOpCode.Catch> catches() {
        return this.catches;
    }

    public int depth() {
        return this.depth;
    }

    public Instruction scope() {
        return this.scope;
    }

    @Override
    public String toString() {
        return "AnnotatedInstruction{instruction=" + super.toString() + ", depth=" + this.depth + ", labelTrue=" + this.labelTrue + ", labelFalse=" + this.labelFalse + ", labelTable=" + String.valueOf(this.labelTable) + ", catches=" + String.valueOf(this.catches) + ", scope=" + String.valueOf(this.scope) + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AnnotatedInstruction)) {
            return false;
        }
        AnnotatedInstruction that = (AnnotatedInstruction)o;
        return this.depth == that.depth && this.labelTrue == that.labelTrue && this.labelFalse == that.labelFalse && Objects.equals(this.labelTable, that.labelTable) && Objects.equals(this.catches, that.catches) && Objects.equals(this.scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.depth, this.labelTrue, this.labelFalse, this.labelTable, this.catches, this.scope);
    }

    public static final class Builder {
        private Instruction base;
        private int depth;
        private Optional<Integer> labelTrue = Optional.empty();
        private Optional<Integer> labelFalse = Optional.empty();
        private Optional<List<Integer>> labelTable = Optional.empty();
        private Optional<List<CatchOpCode.Catch>> catches = Optional.empty();
        private Optional<Instruction> scope = Optional.empty();

        private Builder() {
        }

        public OpCode opcode() {
            return this.base.opcode();
        }

        public Optional<Instruction> scope() {
            return this.scope;
        }

        public Builder from(Instruction ins) {
            this.base = ins;
            return this;
        }

        public Builder withDepth(int depth) {
            this.depth = depth;
            return this;
        }

        public Builder withLabelTrue(int label) {
            this.labelTrue = Optional.of(label);
            return this;
        }

        public Builder withLabelFalse(int label) {
            this.labelFalse = Optional.of(label);
            return this;
        }

        public Builder updateLabelFalse(int label) {
            if (this.labelFalse.equals(this.labelTrue)) {
                this.labelFalse = Optional.of(label);
            }
            return this;
        }

        public Builder withLabelTable(List<Integer> labelTable) {
            this.labelTable = Optional.of(labelTable);
            return this;
        }

        public Builder withCatches(List<CatchOpCode.Catch> catches) {
            this.catches = Optional.of(catches);
            return this;
        }

        public Builder withScope(Instruction scope) {
            this.scope = Optional.of(scope);
            return this;
        }

        public AnnotatedInstruction build() {
            switch (this.base.opcode()) {
                case BLOCK: 
                case LOOP: 
                case END: 
                case IF: 
                case TRY_TABLE: {
                    assert (this.scope.isPresent());
                    break;
                }
                default: {
                    assert (this.scope.isEmpty());
                    break;
                }
            }
            switch (this.base.opcode()) {
                case IF: 
                case BR_IF: 
                case BR_ON_NULL: 
                case BR_ON_NON_NULL: {
                    if (this.labelFalse.isEmpty()) {
                        throw new InvalidException("unknown label " + String.valueOf(this.base));
                    }
                }
                case ELSE: 
                case BR: {
                    if (!this.labelTrue.isEmpty()) break;
                    throw new InvalidException("unknown label " + String.valueOf(this.base));
                }
                default: {
                    assert (this.labelTrue.isEmpty());
                    assert (this.labelFalse.isEmpty());
                    break;
                }
            }
            switch (this.base.opcode()) {
                case BR_TABLE: {
                    if (!this.labelTable.isEmpty()) break;
                    throw new InvalidException("unknown label table " + String.valueOf(this.base));
                }
                default: {
                    assert (this.labelTable.isEmpty());
                    break;
                }
            }
            switch (this.base.opcode()) {
                case TRY_TABLE: {
                    if (!this.catches.isEmpty()) break;
                    throw new InvalidException("unknown catches " + String.valueOf(this.base));
                }
                default: {
                    assert (this.catches.isEmpty());
                    break;
                }
            }
            return new AnnotatedInstruction(this.base.address(), this.base.opcode(), this.base.operands(), this.depth, this.labelTrue.orElse(-1), this.labelFalse.orElse(-1), this.labelTable.orElse(List.of()), this.catches.orElse(null), this.scope.orElse(null));
        }
    }
}

