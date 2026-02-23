/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.CompiledModule;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.WasmModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;

public final class Wat2WasmModule implements CompiledModule {
    public static Machine create(Instance instance) {
        return new Wat2WasmModuleMachine(instance);
    }

    public static WasmModule load() {
        return WasmModuleHolder.INSTANCE;
    }

    @Override
    public Function<Instance, Machine> machineFactory() {
        return Wat2WasmModule::create;
    }

    @Override
    public WasmModule wasmModule() {
        return Wat2WasmModule.load();
    }

    private static class WasmModuleHolder {
        static final WasmModule INSTANCE;

        private WasmModuleHolder() {}

        static {
            try (InputStream in =
                    Wat2WasmModule.class.getResourceAsStream("Wat2WasmModule.meta"); ) {
                INSTANCE = Parser.parse(in);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to load .meta WASM module", e);
            }
        }
    }
}
