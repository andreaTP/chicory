/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.wasm.types.OpCode;

final class CtrlFrame {
    public final OpCode opCode;
    public final int startValues;
    public final int endValues;
    public final int height;
    public final int pc;

    public CtrlFrame(OpCode opCode, int startValues, int endValues, int height) {
        this(opCode, startValues, endValues, height, 0);
    }

    public CtrlFrame(OpCode opCode, int startValues, int endValues, int height, int pc) {
        this.opCode = opCode;
        this.startValues = startValues;
        this.endValues = endValues;
        this.height = height;
        this.pc = pc;
    }
}

