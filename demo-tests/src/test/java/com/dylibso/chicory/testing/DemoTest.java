package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public final class DemoTest {

    private String unpackString(Instance instance, long packed) {
        var addr = (int) ((packed >>> 32) & 0xFFFFFFFFL);
        var len = (int) (packed & 0xFFFFFFFFL);
        var result = instance.memory().readString(addr, len);
        instance.exports().function("wasm_free").apply(addr);
        return result;
    }

    @Test
    public void lumisExample() {
        var wasi =
                WasiPreview1.builder()
                        .withOptions(WasiOptions.builder().inheritSystem().build())
                        .build();

        var instance =
                Instance.builder(Lumis.load())
                        .withImportValues(
                                ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                        .withMachineFactory(Lumis::create)
                        .build();

        byte[] code =
                ("function greet(name) {\n" + "    console.log(`Hello ${name}!`);\n" + "}\n")
                        .getBytes(StandardCharsets.UTF_8);

        var codePtr = (int) instance.exports().function("wasm_malloc").apply(code.length)[0];
        instance.memory().write(codePtr, code);
        //        long result = 0;
        //        for (int i = 0; i < 3; i++) {
        var before = System.currentTimeMillis();
        long result = instance.exports().function("demo").apply(codePtr, code.length)[0];
        var after = System.currentTimeMillis();

        System.out.println("elapsed: " + (after - before));
        //        }

        instance.exports().function("wasm_free").apply(codePtr);

        var string = unpackString(instance, result);
        System.out.println(string);
    }

    //    @Test
    //    public void syntasticaExample() {
    //        var instance =
    //
    // Instance.builder(Syntastica.load()).withMachineFactory(Syntastica::create).build();

    //        long result = 0;
    //        for (int i = 0; i < 3; i++) {
    //        var before = System.currentTimeMillis();
    //        long result = instance.exports().function("demo").apply()[0];
    //        var after = System.currentTimeMillis();
    //
    //        System.out.println("elapsed: " + (after - before));
    //        }
    //
    //        var string = unpackString(instance, result);
    //        System.out.println(string);
    //    }
}
