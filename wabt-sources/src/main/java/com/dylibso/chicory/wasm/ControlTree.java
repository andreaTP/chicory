/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.OpCode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class ControlTree {
    private final AnnotatedInstruction.Builder instruction;
    private final int initialInstructionNumber;
    private final ControlTree parent;
    private final List<ControlTree> nested;
    private final List<Consumer<Integer>> callbacks;

    ControlTree() {
        this.instruction = null;
        this.initialInstructionNumber = 0;
        this.parent = null;
        this.nested = new ArrayList<ControlTree>();
        this.callbacks = new ArrayList<Consumer<Integer>>();
    }

    private ControlTree(int initialInstructionNumber, AnnotatedInstruction.Builder instruction, ControlTree parent) {
        this.instruction = instruction;
        this.initialInstructionNumber = initialInstructionNumber;
        this.parent = parent;
        this.nested = new ArrayList<ControlTree>();
        this.callbacks = new ArrayList<Consumer<Integer>>();
    }

    ControlTree spawn(int initialInstructionNumber, AnnotatedInstruction.Builder instruction) {
        ControlTree node = new ControlTree(initialInstructionNumber, instruction, this);
        this.addNested(node);
        return node;
    }

    AnnotatedInstruction.Builder instruction() {
        return this.instruction;
    }

    int instructionNumber() {
        return this.initialInstructionNumber;
    }

    void addNested(ControlTree nested) {
        this.nested.add(nested);
    }

    ControlTree parent() {
        return this.parent;
    }

    void addCallback(Consumer<Integer> callback) {
        this.callbacks.add(callback);
    }

    void setFinalInstructionNumber(int finalInstructionNumber, AnnotatedInstruction.Builder end) {
        if (end.scope().isPresent() && end.scope().get().opcode() == OpCode.LOOP) {
            int lastLoopInstruction = 0;
            if (this.parent != null) {
                for (ControlTree ct : this.parent.nested) {
                    if (ct.instruction().opcode() != OpCode.LOOP) continue;
                    lastLoopInstruction = ct.instructionNumber();
                }
            }
            finalInstructionNumber = lastLoopInstruction + 1;
        }
        for (Consumer<Integer> callback : this.callbacks) {
            callback.accept(finalInstructionNumber);
        }
    }
}

