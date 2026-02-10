/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.MStack;
import com.dylibso.chicory.wasm.types.Instruction;

@FunctionalInterface
public interface ExecutionListener {
    public void onExecution(Instruction var1, MStack var2);
}

