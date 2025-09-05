package com.dylibso.chicory.testing;

import static com.dylibso.chicory.wasm.types.Value.REF_NULL_VALUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dylibso.chicory.compiler.MachineFactoryCompiler;
import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportTable;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.InterpreterMachine;
import com.dylibso.chicory.runtime.Store;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.testing.gen.DynamicHelloJS;
import com.dylibso.chicory.testing.gen.QuickJS;
import com.dylibso.chicory.wabt.Wat2Wasm;
import com.dylibso.chicory.wasi.Files;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import io.roastedroot.zerofs.Configuration;
import io.roastedroot.zerofs.ZeroFs;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public final class MachinesTest {

    private WasmModule loadModule(String fileName) {
        return Parser.parse(getClass().getResourceAsStream("/" + fileName));
    }

    private Instance.Builder quickJsInstanceBuilder() {
        return Instance.builder(loadModule("compiled/quickjs-provider.javy-dynamic.wasm"));
    }

    private Instance.Builder moduleInstanceBuilder() {
        return Instance.builder(loadModule("compiled/hello-world.js.javy-dynamic.wasm"));
    }

    private WasiPreview1 setupWasi(ByteArrayOutputStream stderr) {
        InputStream stdin = InputStream.nullInputStream();
        var stdout = new ByteArrayOutputStream();

        var wasiOpts =
                WasiOptions.builder()
                        .withStdout(stdout)
                        .withStderr(stderr)
                        .withStdin(stdin)
                        .build();

        return WasiPreview1.builder().withOptions(wasiOpts).build();
    }

    private static final String expectedOutput = "Hello world dynamic Javy!\n";

    // quickjs -> build time compiled
    // module -> interpreter / runtime compiled
    @Test
    public void shouldRunQuickJsPrecompiled() {
        var stderr = new ByteArrayOutputStream();

        var wasi = setupWasi(stderr);
        // using the pre-compiled version of QuickJS
        var quickjs =
                quickJsInstanceBuilder()
                        .withMachineFactory(QuickJS::create)
                        .withImportValues(
                                ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                        .build();

        var store = new Store().register("javy_quickjs_provider_v1", quickjs);

        // the module is going to use the interpreter instead
        moduleInstanceBuilder().withImportValues(store.toImportValues()).build();

        // stderr?
        assertEquals(expectedOutput, stderr.toString(UTF_8));

        // and now runtime AOT
        moduleInstanceBuilder()
                .withMachineFactory(MachineFactoryCompiler::compile)
                .withImportValues(store.toImportValues())
                .build();

        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    }

    // quickjs -> interpreter
    // module -> build time compiler / runtime compiler
    @Test
    public void shouldRunQuickJsInterpreted() {
        var stderr = new ByteArrayOutputStream();

        var wasi = setupWasi(stderr);
        // using the pre-compiled version of QuickJS
        var quickjs =
                quickJsInstanceBuilder()
                        .withImportValues(
                                ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                        .build();

        var store = new Store().register("javy_quickjs_provider_v1", quickjs);

        // the module is going to use the build time compiled
        moduleInstanceBuilder()
                .withMachineFactory(DynamicHelloJS::create)
                .withImportValues(store.toImportValues())
                .build();

        // stderr?
        assertEquals(expectedOutput, stderr.toString(UTF_8));

        // and now runtime compiler
        moduleInstanceBuilder()
                .withMachineFactory(MachineFactoryCompiler::compile)
                .withImportValues(store.toImportValues())
                .build();

        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    }

    // quickjs -> runtime compiler
    // module -> build time compiler / interpreter
    @Test
    public void shouldRunQuickJsRuntimeCompiled() {
        var stderr = new ByteArrayOutputStream();

        var wasi = setupWasi(stderr);
        // using the runtime compiled version of QuickJS
        var quickjs =
                quickJsInstanceBuilder()
                        .withMachineFactory(MachineFactoryCompiler::compile)
                        .withImportValues(
                                ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                        .build();

        var store = new Store().register("javy_quickjs_provider_v1", quickjs);

        // the module is going to use the build time compiled
        moduleInstanceBuilder()
                .withMachineFactory(DynamicHelloJS::create)
                .withImportValues(store.toImportValues())
                .build();

        // stderr?
        assertEquals(expectedOutput, stderr.toString(UTF_8));

        // and now the interpreter
        moduleInstanceBuilder().withImportValues(store.toImportValues()).build();

        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    }

    @Test
    public void shouldUseMachineCallOnlyForExport() throws Exception {
        ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();

        var watFile = new File("../wasm-corpus/src/main/resources/wat/iterfact.wat");

        try (FileSystem fs =
                ZeroFs.newFileSystem(
                        Configuration.unix().toBuilder().setAttributeViews("unix").build())) {

            Path target = fs.getPath("tmp");
            java.nio.file.Files.createDirectory(target);
            Path path = target.resolve(watFile.getName());
            copy(new FileInputStream(watFile), path, StandardCopyOption.REPLACE_EXISTING);

            WasiOptions wasiOpts =
                    WasiOptions.builder()
                            .withStdout(stdoutStream)
                            .withStderr(stdoutStream)
                            .withDirectory(target.toString(), target)
                            .withArguments(List.of("wat2wasm", path.toString(), "--output=-"))
                            .build();
            try (var wasi = WasiPreview1.builder().withOptions(wasiOpts).build()) {
                ImportValues imports =
                        ImportValues.builder().addFunction(wasi.toHostFunctions()).build();
                var wat2WasmModule = Parser.parse(new File("../wabt/src/main/resources/wat2wasm"));
                var startFunctionIndex = new AtomicInteger();
                for (int i = 0; i < wat2WasmModule.exportSection().exportCount(); i++) {
                    var export = wat2WasmModule.exportSection().getExport(i);
                    if (export.name().equals("_start")
                            && export.exportType() == ExternalType.FUNCTION) {
                        startFunctionIndex.set(export.index());
                    }
                }
                // This index is subject to change when we update the wat2wasm version
                assertEquals(18, startFunctionIndex.get());

                Instance.builder(Parser.parse(new File("../wabt/src/main/resources/wat2wasm")))
                        .withMachineFactory(
                                (inst) -> {
                                    var machine = Wat2Wasm.create(inst);
                                    return (funcId, args) -> {
                                        assertEquals(startFunctionIndex.get(), funcId);
                                        return machine.call(funcId, args);
                                    };
                                })
                        .withImportValues(imports)
                        .build();
            }

            var result = stdoutStream.toByteArray();

            assertTrue(result.length > 0);
            assertTrue(new String(result, UTF_8).contains("iterFact"));
        }
    }

    @Test
    public void shouldCallIndirectInterpreterToAot() {
        var store = new Store();
        var table =
                new TableInstance(
                        new Table(ValType.FuncRef, new TableLimits(3, 3)), REF_NULL_VALUE);
        store.addTable(new ImportTable("test", "table", table));

        var instance =
                Instance.builder(loadModule("compiled/call_indirect-export.wat.wasm"))
                        .withImportValues(store.toImportValues())
                        .withMachineFactory(InterpreterMachine::new)
                        .build();
        store.register("test", instance);

        Instance.builder(loadModule("compiled/call_indirect-import.wat.wasm"))
                .withImportValues(store.toImportValues())
                .withMachineFactory(MachineFactoryCompiler::compile)
                .build();

        assertEquals(42, instance.export("call-self").apply()[0]);
        assertEquals(88, instance.export("call-other").apply()[0]);

        var ex = assertThrows(TrapException.class, instance.export("call-other-fail")::apply);
        var className = ex.getStackTrace()[0].getClassName();
        assertTrue(className.contains("CompiledMachine"), className);
    }

    @Test
    public void shouldCallIndirectAotToInterpreter() {
        var store = new Store();
        var table =
                new TableInstance(
                        new Table(ValType.FuncRef, new TableLimits(3, 3)), REF_NULL_VALUE);
        store.addTable(new ImportTable("test", "table", table));

        var instance =
                Instance.builder(loadModule("compiled/call_indirect-export.wat.wasm"))
                        .withImportValues(store.toImportValues())
                        .withMachineFactory(MachineFactoryCompiler::compile)
                        .build();
        store.register("test", instance);

        Instance.builder(loadModule("compiled/call_indirect-import.wat.wasm"))
                .withImportValues(store.toImportValues())
                .withMachineFactory(InterpreterMachine::new)
                .build();

        assertEquals(42, instance.export("call-self").apply()[0]);
        assertEquals(88, instance.export("call-other").apply()[0]);

        var ex = assertThrows(TrapException.class, instance.export("call-other-fail")::apply);
        var className = ex.getStackTrace()[0].getClassName();
        assertTrue(className.contains("InterpreterMachine"), className);
    }

    private long[] invokeBuiltin(Instance instance, long[] args) {
        String moduleName = readJavyString((int) args[0], (int) args[1]);
        String funcName = readJavyString((int) args[2], (int) args[3]);
        String argsString = readJavyString((int) args[4], (int) args[5]);

        if (!builtins.containsKey(moduleName)) {
            throw new IllegalArgumentException("Failed to find builtin module name " + moduleName);
        }
        if (builtins.get(moduleName).byName(funcName) == null) {
            throw new IllegalArgumentException(
                    "Failed to find function with name " + funcName + " in module " + moduleName);
        }
        var receiver = builtins.get(moduleName).byName(funcName);

        var argsList = new ArrayList<>();
        try {
            JsonNode tree = mapper.readTree(argsString);

            for (int i = 0; i < receiver.paramTypes().size(); i++) {
                var clazz = receiver.paramTypes().get(i);
                JsonNode value = null;
                if (tree.size() > i) {
                    value = tree.get(i);
                }

                if (clazz == HostRef.class) {
                    argsList.add(javaRefs.get(value.intValue()));
                } else {
                    argsList.add(mapper.treeToValue(value, clazz));
                }
            }

            var res = receiver.invoke(argsList);

            // Converting Java references into pointers for JS
            var returnType = receiver.returnType();
            if (returnType == HostRef.class) {
                returnType = Integer.class;
                if (res instanceof HostRef) {
                    res = ((HostRef) res).pointer();
                } else {
                    javaRefs.add(res);
                    res = javaRefs.size() - 1;
                }
            }

            var returnStr =
                    (returnType == Void.class)
                            ? "null"
                            : mapper.writerFor(returnType).writeValueAsString(res);
            var returnBytes = returnStr.getBytes();

            var returnPtr =
                    exports.canonicalAbiRealloc(
                            0, // original_ptr
                            0, // original_size
                            ALIGNMENT, // alignment
                            returnBytes.length // new size
                    );
            exports.memory().write(returnPtr, returnBytes);

            var LEN = 8;
            var widePtr =
                    exports.canonicalAbiRealloc(
                            0, // original_ptr
                            0, // original_size
                            ALIGNMENT, // alignment
                            LEN // new size
                    );

            instance.memory().writeI32(widePtr, returnPtr);
            instance.memory().writeI32(widePtr + 4, returnBytes.length);

            return new long[] {widePtr};
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final HostFunction invokeFn =
            new HostFunction(
                    "chicory",
                    "invoke",
                    FunctionType.of(
                    List.of(
                            ValType.I32,
                            ValType.I32,
                            ValType.I32,
                            ValType.I32,
                            ValType.I32,
                            ValType.I32),
                    List.of(ValType.I32)),
                    this::invokeBuiltin);


    @Test
    public void testPyO3() throws IOException {
        var pyo3Path = Path.of("/home/andreatp/workspace/cpython4j/pyo3-plugin/target/wasm32-wasip1/release/pyo3_plugin.wasm");

        WasmModule module = Parser.parse(pyo3Path);

        var pythonCode = "print(\"Hello from Python inside WASM!\")\n" +
                "import pyo3_plugin; print(pyo3_plugin.invoke(\"com.example.MyClass\", \"myMethod\", '{\"arg1\": \"value1\"}'))\n";
//        +
//                "\n" +
//                "# Non-trivial: list comprehension, dict, and f-string\n" +
//                "nums = [i * 2 for i in range(5)]\n" +
//                "data = {i: n for i, n in enumerate(nums)}\n" +
//                "\n" +
//                "print(f\"Generated numbers: {nums}\")\n" +
//                "print(f\"As dictionary: {data}\")\n" +
//                "print(\"Sum:\", sum(nums))";

        var wasiOpts = WasiOptions.builder().inheritSystem();

        try (FileSystem fs =
                     ZeroFs.newFileSystem(
                             Configuration.unix().toBuilder()
                                     .setAttributeViews("unix")
                                     .build())) {

            Path inputFolder = fs.getPath("/usr");
            // can we load different versions of Python in this way?
            // or is it better to bake it into the wasm payload?
            Path copyFrom = Path.of("/home/andreatp/workspace/python-pdk/lib/target/wasm32-wasi/wasi-deps/usr");
            Files.copyDirectory(copyFrom, inputFolder);
            wasiOpts.withDirectory(inputFolder.toString(), inputFolder);

            try (var wasi = WasiPreview1.builder().withOptions(wasiOpts.build()).build()) {
                var pythonInstance = Instance.builder(module)
                        .withImportValues(ImportValues.builder()
                                .addFunction(wasi.toHostFunctions())
                                // TODO: change the names
                                .addFunction(new HostFunction("chicory", "wasm_invoke", FunctionType.of(
                                        List.of(ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32, ValType.I32),
                                        List.of(ValType.I32)),
                                        (inst, args) -> {

                                            return new long[] {};
                                        }
                                ))
                                .build())
                        .build();

                // Initialize the plugin
                pythonInstance.exports().function("plugin_init").apply();

                var codeLen = pythonCode.getBytes(UTF_8).length;
                var pythonPtr = (int) pythonInstance.exports().function("plugin_malloc").apply(codeLen)[0];
                pythonInstance.memory().writeCString(pythonPtr, pythonCode);

                pythonInstance.exports().function("plugin_eval").apply(pythonPtr, codeLen);
            }
        }
    }
}
