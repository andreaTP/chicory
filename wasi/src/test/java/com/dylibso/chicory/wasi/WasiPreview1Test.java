package com.dylibso.chicory.wasi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dylibso.chicory.log.Logger;
import com.dylibso.chicory.log.SystemLogger;
import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.HostImports;
import com.dylibso.chicory.runtime.HostMemory;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.Module;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;

public class WasiPreview1Test {
    private final Logger logger = new SystemLogger();

    @Test
    public void shouldRunWasiModule() {
        // check with: wasmtime src/test/resources/compiled/hello-wasi.wat.wasm
        var fakeStdout = new MockPrintStream();
        var wasi =
                new WasiPreview1(this.logger, WasiOptions.builder().withStdout(fakeStdout).build());
        var imports = new HostImports(wasi.toHostFunctions());
        var module =
                Module.builder("compiled/hello-wasi.wat.wasm").withHostImports(imports).build();
        module.instantiate();
        assertEquals(fakeStdout.output().strip(), "hello world");
    }

    @Test
    public void shouldRunWasiRustModule() {
        // check with: wasmtime src/test/resources/compiled/hello-wasi.rs.wasm
        var expected = "Hello, World!";
        var stdout = new MockPrintStream();
        var wasi = new WasiPreview1(this.logger, WasiOptions.builder().withStdout(stdout).build());
        var imports = new HostImports(wasi.toHostFunctions());
        var module = Module.builder("compiled/hello-wasi.rs.wasm").withHostImports(imports).build();
        module.instantiate(); // run _start and prints Hello, World!
        assertEquals(expected, stdout.output().strip());
    }

    @Test
    public void shouldRunWasiGreetRustModule() {
        // check with: wasmtime src/test/resources/compiled/greet-wasi.rs.wasm
        var fakeStdin = new ByteArrayInputStream("Benjamin".getBytes());
        var wasiOpts = WasiOptions.builder().withStdout(System.out).withStdin(fakeStdin).build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        var module = Module.builder("compiled/greet-wasi.rs.wasm").withHostImports(imports).build();
        module.instantiate();
    }

    @Test
    public void shouldRunWasiDemoJavyModule() {
        // check with: echo "{ \"n\": 2, \"bar\": \"baz\"}" | wasmtime
        // wasi/src/test/resources/compiled/javy-demo.js.wasm
        var fakeStdin = new ByteArrayInputStream("{ \"n\": 2, \"bar\": \"baz\" }".getBytes());
        var fakeStdout = new MockPrintStream();
        var wasiOpts = WasiOptions.builder().withStdout(fakeStdout).withStdin(fakeStdin).build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        var module =
                Module.builder("compiled/javy-demo.js.javy.wasm").withHostImports(imports).build();
        module.instantiate();

        assertEquals(fakeStdout.output(), "{\"foo\":3,\"newBar\":\"baz!\"}");
    }

    @Test
    public void shouldRunTinyGoModule() {
        var wasiOpts = WasiOptions.builder().build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        var module = Module.builder("compiled/sum.go.tiny.wasm").withHostImports(imports).build();
        var instance = module.instantiate();
        var sum = instance.export("add");
        var result = sum.apply(Value.i32(20), Value.i32(22))[0];

        assertEquals(result.asInt(), 42);
    }

    @Test
    public void shouldRunOpaRuleModule() throws Exception {

        final class OpaWasm {
            // error codes
            public static final int OPA_ERR_OK = 0; // OPA_ERR_OK	No error.
            public static final int OPA_ERR_INTERNAL = 1; // Unrecoverable internal error.
            public static final int OPA_ERR_INVALID_TYPE = 2; // Invalid value type was encountered.
            public static final int OPA_ERR_INVALID_PATH = 3; // Invalid object path reference.

            // Imports
            private final HostMemory memory =
                    new HostMemory("env", "memory", new Memory(new MemoryLimits(10, 100)));
            private final HostFunction opaAbort =
                    new HostFunction(
                            (Instance instance, Value... args) -> {
                                var errorMessage = instance.memory().readCString(args[0].asInt());
                                throw new RuntimeException("opa_abort - " + errorMessage);
                            },
                            "env",
                            "opa_abort",
                            List.of(ValueType.I32),
                            List.of());
            private final HostFunction opaPrintln =
                    new HostFunction(
                            (Instance instance, Value... args) -> {
                                var message = instance.memory().readCString(args[0].asInt());
                                System.out.println("opa_println - " + message);
                                return new Value[] {};
                            },
                            "env",
                            "opa_println",
                            List.of(ValueType.I32),
                            List.of());

            private final Module module;
            private final Instance instance;

            OpaWasm() {
                this.module =
                        Module.builder(new File("src/test/resources/opa/policy.wasm"))
                                .withHostImports(
                                        HostImports.builder()
                                                .addMemory(memory)
                                                .addFunction(opaAbort)
                                                .addFunction(opaPrintln)
                                                .build())
                                .build();
                this.instance = module.instantiate();
            }

            // exports
            public int opaWasmAbiVersion() {
                return instance.export("opa_wasm_abi_version").apply()[0].asInt();
            }

            public int opaWasmAbiMinorVersion() {
                return instance.export("opa_wasm_abi_minor_version").apply()[0].asInt();
            }

            public int opaJsonDump(int addr) {
                return instance.export("opa_json_dump").apply(Value.i32(addr))[0].asInt();
            }

            public int builtins() {
                return instance.export("builtins").apply()[0].asInt();
            }

            public int opaEvalCtxNew() {
                return instance.export("opa_eval_ctx_new").apply()[0].asInt();
            }

            public int opaMalloc(int capacity) {
                return instance.export("opa_malloc").apply(Value.i32(capacity))[0].asInt();
            }

            public void opaFree(int addr) {
                instance.export("opa_free").apply(Value.i32(addr));
            }

            public void opaValueFree(int addr) {
                instance.export("opa_value_free").apply(Value.i32(addr));
            }

            public int opaJsonParse(int addr, int size) {
                return instance.export("opa_json_parse")
                        .apply(Value.i32(addr), Value.i32(size))[0]
                        .asInt();
            }

            public void opaEvalCtxSetInput(int ctxAddr, int valueAddr) {
                instance.export("opa_eval_ctx_set_input")
                        .apply(Value.i32(ctxAddr), Value.i32(valueAddr));
            }

            public void opaEvalCtxSetData(int ctxAddr, int valueAddr) {
                instance.export("opa_eval_ctx_set_data")
                        .apply(Value.i32(ctxAddr), Value.i32(valueAddr));
            }

            public int eval(int ctxAddr) {
                return instance.export("eval").apply(Value.i32(ctxAddr))[0].asInt();
            }

            public int opaEvalCtxGetResult(int ctxAddr) {
                return instance.export("opa_eval_ctx_get_result")
                        .apply(Value.i32(ctxAddr))[0]
                        .asInt();
            }
        }

        var opa = new OpaWasm();

        assertEquals(opa.opaWasmAbiVersion(), 1);
        assertEquals(opa.opaWasmAbiMinorVersion(), 3);
        // TODO: a convenient way to construct OpaWasm with the builtins map
        var builtinsAddr = opa.builtins();
        var builtinsStringAddr = opa.opaJsonDump(builtinsAddr);
        assertEquals("{}", opa.instance.memory().readCString(builtinsStringAddr));

        // Following the instructions here:
        // https://www.openpolicyagent.org/docs/latest/wasm/#evaluation
        var ctxAddr = opa.opaEvalCtxNew();

        var input = "{\"user\": \"alice\"}";
        var inputStrAddr = opa.opaMalloc(input.length());
        opa.instance.memory().writeCString(inputStrAddr, input);
        var inputAddr = opa.opaJsonParse(inputStrAddr, input.length());
        opa.opaFree(inputStrAddr);

        opa.opaEvalCtxSetInput(ctxAddr, inputAddr);

        var data = "{ \"role\" : { \"alice\" : \"admin\", \"bob\" : \"user\" } }";
        var dataStrAddr = opa.opaMalloc(data.length());
        opa.instance.memory().writeCString(dataStrAddr, data);
        var dataAddr = opa.opaJsonParse(dataStrAddr, data.length());
        opa.opaFree(dataStrAddr);
        opa.opaEvalCtxSetData(ctxAddr, dataAddr);

        // TODO: add entrypoint handling
        int evalResult = opa.eval(ctxAddr);
        assertEquals(OpaWasm.OPA_ERR_OK, evalResult);

        int resultAddr = opa.opaEvalCtxGetResult(ctxAddr);
        int resultStrAddr = opa.opaJsonDump(resultAddr);
        var resultStr = opa.instance.memory().readCString(resultStrAddr);
        opa.opaFree(resultStrAddr);
        assertEquals("[{\"result\":true}]", resultStr);

        // TODO: is it a full cleanup?
        opa.opaValueFree(inputAddr);
        opa.opaValueFree(dataAddr);
        opa.opaValueFree(resultAddr);

        // now let's try a higher level user experience on the rule
        final class DemoOpaRule implements Closeable {
            private final OpaWasm opa;
            private final int dataAddr;
            private final int ctxAddr;
            private int inputAddr = -1;
            private int resultAddr = -1;

            DemoOpaRule(String data) {
                opa = new OpaWasm();

                ctxAddr = opa.opaEvalCtxNew();
                var dataStrAddr = opa.opaMalloc(data.length());
                opa.instance.memory().writeCString(dataStrAddr, data);
                dataAddr = opa.opaJsonParse(dataStrAddr, data.length());
                opa.opaFree(dataStrAddr);
                opa.opaEvalCtxSetData(ctxAddr, dataAddr);
            }

            public void setInput(String input) {
                var inputStrAddr = opa.opaMalloc(input.length());
                opa.instance.memory().writeCString(inputStrAddr, input);
                inputAddr = opa.opaJsonParse(inputStrAddr, input.length());
                opa.opaFree(inputStrAddr);
                opa.opaEvalCtxSetInput(ctxAddr, inputAddr);
            }

            public String evaluate() {
                int evalResult = opa.eval(ctxAddr);
                assertEquals(OpaWasm.OPA_ERR_OK, evalResult);

                resultAddr = opa.opaEvalCtxGetResult(ctxAddr);
                int resultStrAddr = opa.opaJsonDump(resultAddr);
                var resultStr = opa.instance.memory().readCString(resultStrAddr);
                opa.opaFree(resultStrAddr);
                return resultStr;
            }

            @Override
            public void close() throws IOException {
                opa.opaValueFree(dataAddr);
                if (inputAddr != -1) {
                    opa.opaValueFree(inputAddr);
                    inputAddr = -1;
                }
                if (resultAddr != -1) {
                    opa.opaValueFree(resultAddr);
                    resultAddr = -1;
                }
            }
        }

        class User {
            @JsonProperty private String user;

            User() {}

            User(String name) {
                this.user = name;
            }
        }

        class Role {
            @JsonProperty private Map<String, String> role;

            Role() {}

            Role(Map<String, String> roles) {
                role = roles;
            }
        }

        BiFunction<User, Role, Boolean> evalRule =
                (user, role) -> {
                    var objectMapper = new ObjectMapper();
                    try (var demoRule = new DemoOpaRule(objectMapper.writeValueAsString(role))) {
                        demoRule.setInput(objectMapper.writeValueAsString(user));
                        return objectMapper
                                .readTree(demoRule.evaluate())
                                .elements()
                                .next()
                                .findValue("result")
                                .asBoolean();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };

        var user = new User("alice");
        var role = new Role(Map.of("alice", "admin"));
        assertTrue(evalRule.apply(user, role));

        user = new User("bob");
        role = new Role(Map.of("alice", "admin"));
        assertFalse(evalRule.apply(user, role));

        user = new User("bob");
        role = new Role(Map.of("bob", "admin"));
        assertTrue(evalRule.apply(user, role));
    }
}
