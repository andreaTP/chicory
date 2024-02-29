package com.dylibso.chicory.wasi;

import com.dylibso.chicory.runtime.HostGlobal;
import com.dylibso.chicory.runtime.HostImports;
import com.dylibso.chicory.runtime.HostMemory;
import com.dylibso.chicory.runtime.HostTable;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.Module;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.dylibso.chicory.wasm.types.MemoryLimits.MAX_PAGES;

public class Llama2Test {

    @Test
    public void test() {
        var module = Module.builder(new File("/Users/aperuffo/workspace/llama2.c-web/llama2c.wasm")).build();
        var wasi = new WasiPreview1(module.logger(), WasiOptions.builder().inheritSystem().build());
        var imports = new HostImports(
                wasi.toHostFunctions(),
                new HostGlobal[]{},
                new HostMemory[]{
                        new HostMemory("env", "memory", new Memory(new MemoryLimits(32, MAX_PAGES)))
                },
                new HostTable[]{}
        );
        var instance = module.instantiate(imports);

        var res = instance.export("run").apply();
        System.out.println("Res is " + res[0]);
    }
}
