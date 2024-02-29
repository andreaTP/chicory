package com.dylibso.chicory.runtime;

import org.junit.jupiter.api.Test;

import java.io.File;

public class DummyTest {

    @Test
    public void test() {
        var module = Module.builder(new File("/Users/aperuffo/workspace/llama2.c-web/llama2c.wasm")).build();
        // var module = Module.builder(new File("/Users/aperuffo/workspace/llama-cpp-wasm/docs/llama-st/main.wasm")).build();

        var instance = module.instantiate();
    }
}
