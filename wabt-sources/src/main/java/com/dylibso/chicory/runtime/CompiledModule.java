/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.wasm.WasmModule;
import java.util.function.Function;

public interface CompiledModule {
    public WasmModule wasmModule();

    public Function<Instance, Machine> machineFactory();
}

