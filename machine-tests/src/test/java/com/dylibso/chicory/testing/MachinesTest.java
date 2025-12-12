package com.dylibso.chicory.testing;

import static com.dylibso.chicory.wasm.types.Value.REF_NULL_VALUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.copy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dylibso.chicory.compiler.MachineFactoryCompiler;
import com.dylibso.chicory.corpus.CorpusResources;
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
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.wasm.types.ExternalType;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import io.roastedroot.zerofs.Configuration;
import io.roastedroot.zerofs.ZeroFs;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public final class MachinesTest {

    private WasmModule loadModule(String fileName) {
        return Parser.parse(CorpusResources.getResource(fileName));
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

    @Test
    public void shouldRunPGLite() throws Exception {
        FileSystem fs =
                ZeroFs.newFileSystem(
                        Configuration.unix().toBuilder().setAttributeViews("unix").build());

        Path pgroot = fs.getPath("tmp");
        java.nio.file.Files.createDirectories(pgroot);

        // uncompressed .tar.gz
        // copy(Path.of("/home/andreatp/workspace/pglite-oxide/assets/tmp"), pgroot);
        Path postgresBin = pgroot.resolve("pglite").resolve("bin");
        java.nio.file.Files.createDirectories(postgresBin);
        Path initdbFile = postgresBin.resolve("initdb");
        Files.copy(
                Path.of("/home/andreatp/workspace/pglite-oxide/assets/tmp/pglite/bin/initdb"),
                initdbFile,
                StandardCopyOption.COPY_ATTRIBUTES);
        Path postgresFile = postgresBin.resolve("postgres");
        Files.copy(
                Path.of("/home/andreatp/workspace/pglite-oxide/assets/tmp/pglite/bin/postgres"),
                postgresFile,
                StandardCopyOption.COPY_ATTRIBUTES);

        Path postgresShare = pgroot.resolve("pglite").resolve("share");
        com.dylibso.chicory.wasi.Files.copyDirectory(
                Path.of("/home/andreatp/workspace/pglite-oxide/assets/tmp/pglite/share"),
                postgresShare);

        Path postgresPassword = pgroot.resolve("pglite").resolve("password");
        Files.writeString(postgresPassword, "password");

        Path pgdata = pgroot.resolve("pglite").resolve("base");
        java.nio.file.Files.createDirectories(pgdata);
        Path dev = fs.getPath("dev");
        java.nio.file.Files.createDirectories(dev);

        // Create necessary /dev/urandom
        Path urandom = dev.resolve("urandom");
        byte[] randomBytes = new byte[128];
        new Random().nextBytes(randomBytes);
        Files.write(urandom, randomBytes);

        WasiOptions wasiOpts =
                WasiOptions.builder()
                        // stdin/out for debugging
                        .inheritSystem()

                        // mapping pre-opened dirs
                        .withDirectory(pgroot.toString(), pgroot)
                        .withDirectory(pgdata.toString(), pgdata)
                        .withDirectory(dev.toString(), dev)

                        // env vars
                        .withEnvironment("ENVIRONMENT", "wasm32_wasi_preview1")
                        .withEnvironment("PREFIX", "/tmp/pglite")
                        .withEnvironment("PGDATA", "/tmp/pglite/base")
                        .withEnvironment("PGSYSCONFDIR", "/tmp/pglite")
                        .withEnvironment("PGUSER", "postgres")
                        .withEnvironment("PGDATABASE", "template1")
                        .withEnvironment("MODE", "REACT")
                        .withEnvironment("REPL", "N")
                        // .withEnvironment("REPL", "Y")
                        .withEnvironment("TZ", "UTC")
                        .withEnvironment("PGTZ", "UTC")
                        .withEnvironment("PATH", "/tmp/pglite/bin")

                        // args
                        .withArguments(List.of("/tmp/pglite/bin/postgres", "--single", "postgres"))
                        .build();
        var wasi = WasiPreview1.builder().withOptions(wasiOpts).build();
        ImportValues imports = ImportValues.builder().addFunction(wasi.toHostFunctions()).build();

        var module = PostgresModule.load();
        var instance =
                Instance.builder(module)
                        .withImportValues(imports)
                        .withMachineFactory(PostgresModule::create)
                        .withMemoryLimits(new MemoryLimits(100))
                        .build();
        // TODO: check on pure interpreter
        //
        // Instance.builder(Parser.parse(Path.of("/home/andreatp/workspace/pglite-oxide/assets/tmp/pglite/bin/pglite-opt.wasi")))
        //                        .withImportValues(imports)
        //                        .build();

        // initdb
        var initdb = instance.exports().function("pgl_initdb");
        int rc = (int) initdb.apply()[0];
        System.out.println("[java] pgl_initdb returned: " + rc);
        // IDB_CALLED 0b0010: initdb was called and db created

        // verify PG_VERSION got created
        var pg_version = Files.readString(pgdata.resolve("PG_VERSION"));
        System.out.println("PG_VERSION: " + pg_version);

        assertEquals("17\n", pg_version);

        // instance.exports().function("pgl_backend").apply();

        var channel = (int) instance.exports().function("get_channel").apply()[0];
        var bufferAddr = (int) instance.exports().function("get_buffer_addr").apply(channel)[0];
        assert (bufferAddr >= 0); // CMA mode available
        var bufferSize = (int) instance.exports().function("get_buffer_size").apply(channel)[0];
        System.out.println("Buffer size: " + bufferSize);

        var query = "SELECT 1".getBytes(UTF_8);
//        var queryString = "SELECT 1;";
//        var query =
//                Arrays.copyOf(queryString.getBytes(UTF_8), queryString.getBytes(UTF_8).length + 1);
//        query[queryString.getBytes(UTF_8).length] = 0;

        // 51 = 'Q' (Query tag)
        // 00 00 00 09 = Length = 9 bytes (big-endian)
        // 53 45 4c 45 43 54 20 31 3b = "SELECT 1;" (ASCII)
        // 00 = Null terminator
        // Wire mode!
//        var query = new byte[] {
//                0x51, 0x00, 0x00, 0x00, 0x09, 0x53, 0x45, 0x4c, 0x45, 0x43, 0x54, 0x20, 0x31, 0x3b, 0x00
//        };
        // startup message?
//        var query = new byte[] {
//                // Length: 88 bytes (0x00000058) - big-endian
//                0x00, 0x00, 0x00, 0x58,
//
//                // Protocol version: 196608 (0x00030000) - big-endian
//                0x00, 0x03, 0x00, 0x00,
//
//                // "user\0postgres\0"
//                0x75, 0x73, 0x65, 0x72, 0x00,                    // "user\0"
//                0x70, 0x6f, 0x73, 0x74, 0x67, 0x72, 0x65, 0x73, 0x00,  // "postgres\0"
//
//                // "database\0template1\0"
//                0x64, 0x61, 0x74, 0x61, 0x62, 0x61, 0x73, 0x65, 0x00,  // "database\0"
//                0x74, 0x65, 0x6d, 0x70, 0x6c, 0x61, 0x74, 0x65, 0x31, 0x00,  // "template1\0"
//
//                // "client_encoding\0UTF8\0"
//                0x63, 0x6c, 0x69, 0x65, 0x6e, 0x74, 0x5f, 0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67, 0x00,  // "client_encoding\0"
//                0x55, 0x54, 0x46, 0x38, 0x00,  // "UTF8\0"
//
//                // "application_name\0pglite-oxide\0"
//                0x61, 0x70, 0x70, 0x6c, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x5f, 0x6e, 0x61, 0x6d, 0x65, 0x00,  // "application_name\0"
//                0x70, 0x67, 0x6c, 0x69, 0x74, 0x65, 0x2d, 0x6f, 0x78, 0x69, 0x64, 0x65, 0x00,  // "pglite-oxide\0"
//
//                // Final null terminator
//                0x00
//        };


        // instance.memory().write(bufferAddr, query);
        instance.memory().write(bufferAddr, query);

        // use_wire
        instance.exports().function("use_wire").apply(1);

        // use_cma
        // instance.exports().function("use_cma").apply(1);

        // instance.exports().function("pgl_backend").apply();

        // TODO: attempt ...
        // instance.exports().function("clear_error").apply(0);

        var closed = instance.exports().function("pgl_closed").apply()[0];
        System.out.println("is closed? " + closed);
        assertEquals(0, closed);

        // System.out.println("DEBUG: " + instance.memory().readCString(bufferAddr));

        instance.exports().function("interactive_write").apply(query.length);
        instance.exports().function("interactive_one").apply();

        var responseLen = (int) instance.exports().function("interactive_read").apply()[0];
        var responseBytes = instance.memory().readBytes(bufferAddr + query.length + 1, responseLen);
        System.out.println("RESPONSE: " + new String(responseBytes));

        // Clear buffer?
        //        instance.exports().function("interactive_write").apply(0);

        // backend
        // TODO: breaks? different env vars etc.?
        //        var backend = instance.exports().function("pgl_backend");
        //        System.out.println("backend: " + backend.apply()[0]);

        // shutdown
        // something break here as well?
        //        var shutdown = instance.exports().function("pgl_shutdown");
        //        System.out.println("shutdown: " + shutdown.apply()[0]);
    }
}
