package com.dylibso.chicory.wasi;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dylibso.chicory.log.Logger;
import com.dylibso.chicory.log.SystemLogger;
import com.dylibso.chicory.runtime.HostImports;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasm.Module;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.Value;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class WasiPreview1Test {
    private final Logger logger = new SystemLogger();

    private static Module loadModule(String fileName) {
        return Parser.parse(WasiPreview1Test.class.getResourceAsStream("/" + fileName));
    }

    @Test
    public void shouldRunWasiModule() {
        // check with: wasmtime src/test/resources/compiled/hello-wasi.wat.wasm
        var fakeStdout = new MockPrintStream();
        var wasi =
                new WasiPreview1(this.logger, WasiOptions.builder().withStdout(fakeStdout).build());
        var imports = new HostImports(wasi.toHostFunctions());
        Instance.builder(loadModule("compiled/hello-wasi.wat.wasm"))
                .withHostImports(imports)
                .build();
        assertEquals(fakeStdout.output().strip(), "hello world");
    }

    @Test
    public void shouldRunWasiRustModule() {
        // check with: wasmtime src/test/resources/compiled/hello-wasi.rs.wasm
        var expected = "Hello, World!";
        var stdout = new MockPrintStream();
        var wasi = new WasiPreview1(this.logger, WasiOptions.builder().withStdout(stdout).build());
        var imports = new HostImports(wasi.toHostFunctions());
        Instance.builder(loadModule("compiled/hello-wasi.rs.wasm"))
                .withHostImports(imports)
                .build(); // run _start and prints Hello, World!
        assertEquals(expected, stdout.output().strip());
    }

    @Test
    public void shouldRunWasiGreetRustModule() {
        // check with: wasmtime src/test/resources/compiled/greet-wasi.rs.wasm
        var fakeStdin = new ByteArrayInputStream("Benjamin".getBytes(UTF_8));
        var wasiOpts = WasiOptions.builder().withStdout(System.out).withStdin(fakeStdin).build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        Instance.builder(loadModule("compiled/greet-wasi.rs.wasm"))
                .withHostImports(imports)
                .build();
    }

    @Test
    public void shouldRunWasiDemoJavyModule() {
        // check with: echo "{ \"n\": 2, \"bar\": \"baz\"}" | wasmtime
        // wasi/src/test/resources/compiled/javy-demo.js.wasm
        var fakeStdin = new ByteArrayInputStream("{ \"n\": 2, \"bar\": \"baz\" }".getBytes(UTF_8));
        var fakeStdout = new MockPrintStream();
        var wasiOpts = WasiOptions.builder().withStdout(fakeStdout).withStdin(fakeStdin).build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        Instance.builder(loadModule("compiled/javy-demo.js.javy.wasm"))
                .withHostImports(imports)
                .build();

        assertEquals(fakeStdout.output(), "{\"foo\":3,\"newBar\":\"baz!\"}");
    }

    @Test
    public void shouldRunTinyGoModule() {
        var wasiOpts = WasiOptions.builder().build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        var module = loadModule("compiled/sum.go.tiny.wasm");
        var instance = Instance.builder(module).withHostImports(imports).build();
        var sum = instance.export("add");
        var result = sum.apply(Value.i32(20), Value.i32(22))[0];

        assertEquals(result.asInt(), 42);
    }

    @Test
    public void shouldRunWasiGoModule() {
        var fakeStdout = new MockPrintStream();
        var wasiOpts = WasiOptions.builder().withStdout(fakeStdout).build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());
        var module = loadModule("compiled/main.go.wasm");
        var exit =
                assertThrows(
                        WasiExitException.class,
                        () -> Instance.builder(module).withHostImports(imports).build());
        assertEquals(0, exit.exitCode());
        assertEquals("Hello, WebAssembly!\n", fakeStdout.output());
    }

    @Test
    public void shouldRunWasiDemoDotnetModule() throws Exception {
        var fakeStdout = new MockPrintStream();
        var wasiOpts =
                WasiOptions.builder()
                        .withStdout(fakeStdout)
                        // Fix for "[MONO] critical: /__w/1/s/src/mono/mono/eglib/gpath.c:134:
                        // assertion 'filename != NULL' failed"
                        // https://jflower.co.uk/running-net-8-on-cloudflare-workers/
                        .withArguments(List.of(""))
                        .build();
        var wasi = new WasiPreview1(this.logger, wasiOpts);
        var imports = new HostImports(wasi.toHostFunctions());

        var module = loadModule("compiled/basic.dotnet.wasm");
        Instance.builder(module).withHostImports(imports).build();

        assertEquals("Hello, Wasi Console!\n", fakeStdout.output());
    }

    @Test
    public void wasiRandom() {
        var seed = 0x12345678;
        var wasi =
                new WasiPreview1(
                        this.logger, WasiOptions.builder().withRandom(new Random(seed)).build());

        var memory = new Memory(new MemoryLimits(8, 8));
        assertEquals(0, wasi.randomGet(memory, 0, 123_456));
        assertEquals(0, wasi.randomGet(memory, 222_222, 87_654));

        var random = new Random(seed);
        byte[] first = new byte[123_456];
        random.nextBytes(first);
        byte[] second = new byte[87_654];
        random.nextBytes(second);

        assertArrayEquals(first, memory.readBytes(0, 123_456));
        assertArrayEquals(second, memory.readBytes(222_222, 87_654));
    }

    @Test
    public void shouldRunOcrs() throws Exception {
        // ocrs /home/aperuffo/Pictures/build_test.png --detect-model
        // /home/aperuffo/Downloads/text-detection.rten --rec-model
        // /home/aperuffo/Downloads/text-recognition.rten
        var module =
                Parser.parse(
                        new File(
                                "/home/aperuffo/workspace/ocrs/target/wasm32-wasi/release/ocrs.wasm"));
        var input = new File("/home/aperuffo/Pictures/build_test.png");
        var detectionModel = new File("/home/aperuffo/Downloads/text-detection.rten");
        var recognitionModel = new File("/home/aperuffo/Downloads/text-recognition.rten");
        try (FileInputStream fis = new FileInputStream(input);
                FileSystem fs =
                        Jimfs.newFileSystem(
                                Configuration.unix().toBuilder()
                                        .setAttributeViews("unix")
                                        .build())) {

            var wasiOpts = WasiOptions.builder();

            wasiOpts.inheritSystem();

            Path inputFolder = fs.getPath("input");
            java.nio.file.Files.createDirectory(inputFolder);
            Path inputPath = inputFolder.resolve(input.getName());
            copy(fis, inputPath, StandardCopyOption.REPLACE_EXISTING);

            // let see if we can re-use some cache ...
            Path detectionModelPath = inputFolder.resolve(detectionModel.getName());
            copy(detectionModel.toPath(), detectionModelPath, StandardCopyOption.REPLACE_EXISTING);

            Path recognitionModelPath = inputFolder.resolve(recognitionModel.getName());
            copy(
                    recognitionModel.toPath(),
                    recognitionModelPath,
                    StandardCopyOption.REPLACE_EXISTING);

            wasiOpts.withDirectory(inputFolder.toString(), inputFolder);

            Path outputFolder = fs.getPath("output");
            java.nio.file.Files.createDirectory(outputFolder);
            wasiOpts.withDirectory(outputFolder.toString(), outputFolder);

            List<String> args = new ArrayList<>();
            args.add("ocrs");
            args.add(inputPath.toString());
            args.add("--detect-model");
            args.add(detectionModelPath.toString());
            args.add("--rec-model");
            args.add(recognitionModelPath.toString());

            wasiOpts.withArguments(args);

            try (var wasi =
                    WasiPreview1.builder().withLogger(logger).withOpts(wasiOpts.build()).build()) {
                HostImports imports = new HostImports(wasi.toHostFunctions());
                Instance.builder(module)
                        .withMachineFactory(com.dylibso.chicory.aot.AotMachine::new)
                        .withHostImports(imports)
                        .build();
            }
        }
    }
}
