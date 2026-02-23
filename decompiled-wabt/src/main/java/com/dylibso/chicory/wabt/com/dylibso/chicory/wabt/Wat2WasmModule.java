// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import java.io.InputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import com.dylibso.chicory.wasm.Parser;
import java.util.function.Function;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.runtime.Machine;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.CompiledModule;

public final class Wat2WasmModule implements CompiledModule
{
    public static Machine create(final Instance instance) {
        return (Machine)new Wat2WasmModuleMachine(instance);
    }
    
    public static WasmModule load() {
        return WasmModuleHolder.INSTANCE;
    }
    
    public Function<Instance, Machine> machineFactory() {
        return Wat2WasmModule::create;
    }
    
    public WasmModule wasmModule() {
        return load();
    }
    
    private static class WasmModuleHolder
    {
        static final WasmModule INSTANCE;
        
        static {
            try (final InputStream in = Wat2WasmModule.class.getResourceAsStream("Wat2WasmModule.meta")) {
                INSTANCE = Parser.parse(in);
            }
            catch (final IOException e) {
                throw new UncheckedIOException("Failed to load .meta WASM module", e);
            }
        }
    }
}
