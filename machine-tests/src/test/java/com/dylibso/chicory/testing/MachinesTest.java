package com.dylibso.chicory.testing;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import io.roastedroot.zerofs.Configuration;
import io.roastedroot.zerofs.ZeroFs;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class MachinesTest {

    //    private WasmModule loadModule(String fileName) {
    //        return Parser.parse(CorpusResources.getResource(fileName));
    //    }

    //    private Instance.Builder quickJsInstanceBuilder() {
    //        return Instance.builder(loadModule("compiled/quickjs-provider.javy-dynamic.wasm"));
    //    }
    //
    //    private Instance.Builder moduleInstanceBuilder() {
    //        return Instance.builder(loadModule("compiled/hello-world.js.javy-dynamic.wasm"));
    //    }
    //
    //    private WasiPreview1 setupWasi(ByteArrayOutputStream stderr) {
    //        InputStream stdin = InputStream.nullInputStream();
    //        var stdout = new ByteArrayOutputStream();
    //
    //        var wasiOpts =
    //                WasiOptions.builder()
    //                        .withStdout(stdout)
    //                        .withStderr(stderr)
    //                        .withStdin(stdin)
    //                        .build();
    //
    //        return WasiPreview1.builder().withOptions(wasiOpts).build();
    //    }
    //
    //    private static final String expectedOutput = "Hello world dynamic Javy!\n";

    // quickjs -> build time compiled
    // module -> interpreter / runtime compiled
    //    @Test
    //    public void shouldRunQuickJsPrecompiled() {
    //        var stderr = new ByteArrayOutputStream();
    //
    //        var wasi = setupWasi(stderr);
    //        // using the pre-compiled version of QuickJS
    //        var quickjs =
    //                quickJsInstanceBuilder()
    //                        .withMachineFactory(QuickJS::create)
    //                        .withImportValues(
    //
    // ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
    //                        .build();
    //
    //        var store = new Store().register("javy_quickjs_provider_v1", quickjs);
    //
    //        // the module is going to use the interpreter instead
    //        moduleInstanceBuilder().withImportValues(store.toImportValues()).build();
    //
    //        // stderr?
    //        assertEquals(expectedOutput, stderr.toString(UTF_8));
    //
    //        // and now runtime AOT
    //        moduleInstanceBuilder()
    //                .withMachineFactory(MachineFactoryCompiler::compile)
    //                .withImportValues(store.toImportValues())
    //                .build();
    //
    //        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    //    }
    //
    //    // quickjs -> interpreter
    //    // module -> build time compiler / runtime compiler
    //    @Test
    //    public void shouldRunQuickJsInterpreted() {
    //        var stderr = new ByteArrayOutputStream();
    //
    //        var wasi = setupWasi(stderr);
    //        // using the pre-compiled version of QuickJS
    //        var quickjs =
    //                quickJsInstanceBuilder()
    //                        .withImportValues(
    //
    // ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
    //                        .build();
    //
    //        var store = new Store().register("javy_quickjs_provider_v1", quickjs);
    //
    //        // the module is going to use the build time compiled
    //        moduleInstanceBuilder()
    //                .withMachineFactory(DynamicHelloJS::create)
    //                .withImportValues(store.toImportValues())
    //                .build();
    //
    //        // stderr?
    //        assertEquals(expectedOutput, stderr.toString(UTF_8));
    //
    //        // and now runtime compiler
    //        moduleInstanceBuilder()
    //                .withMachineFactory(MachineFactoryCompiler::compile)
    //                .withImportValues(store.toImportValues())
    //                .build();
    //
    //        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    //    }
    //
    //    // quickjs -> runtime compiler
    //    // module -> build time compiler / interpreter
    //    @Test
    //    public void shouldRunQuickJsRuntimeCompiled() {
    //        var stderr = new ByteArrayOutputStream();
    //
    //        var wasi = setupWasi(stderr);
    //        // using the runtime compiled version of QuickJS
    //        var quickjs =
    //                quickJsInstanceBuilder()
    //                        .withMachineFactory(MachineFactoryCompiler::compile)
    //                        .withImportValues(
    //
    // ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
    //                        .build();
    //
    //        var store = new Store().register("javy_quickjs_provider_v1", quickjs);
    //
    //        // the module is going to use the build time compiled
    //        moduleInstanceBuilder()
    //                .withMachineFactory(DynamicHelloJS::create)
    //                .withImportValues(store.toImportValues())
    //                .build();
    //
    //        // stderr?
    //        assertEquals(expectedOutput, stderr.toString(UTF_8));
    //
    //        // and now the interpreter
    //        moduleInstanceBuilder().withImportValues(store.toImportValues()).build();
    //
    //        assertEquals(expectedOutput + expectedOutput, stderr.toString(UTF_8));
    //    }
    //
    //    @Test
    //    public void shouldUseMachineCallOnlyForExport() throws Exception {
    //        ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();
    //
    //        var watFile = new File("../wasm-corpus/src/main/resources/wat/iterfact.wat");
    //
    //        try (FileSystem fs =
    //                ZeroFs.newFileSystem(
    //                        Configuration.unix().toBuilder().setAttributeViews("unix").build())) {
    //
    //            Path target = fs.getPath("tmp");
    //            java.nio.file.Files.createDirectory(target);
    //            Path path = target.resolve(watFile.getName());
    //            copy(new FileInputStream(watFile), path, StandardCopyOption.REPLACE_EXISTING);
    //
    //            WasiOptions wasiOpts =
    //                    WasiOptions.builder()
    //                            .withStdout(stdoutStream)
    //                            .withStderr(stdoutStream)
    //                            .withDirectory(target.toString(), target)
    //                            .withArguments(List.of("wat2wasm", path.toString(), "--output=-"))
    //                            .build();
    //            try (var wasi = WasiPreview1.builder().withOptions(wasiOpts).build()) {
    //                ImportValues imports =
    //                        ImportValues.builder().addFunction(wasi.toHostFunctions()).build();
    //                var wat2WasmModule = Parser.parse(new
    // File("../wabt/src/main/resources/wat2wasm"));
    //                var startFunctionIndex = new AtomicInteger();
    //                for (int i = 0; i < wat2WasmModule.exportSection().exportCount(); i++) {
    //                    var export = wat2WasmModule.exportSection().getExport(i);
    //                    if (export.name().equals("_start")
    //                            && export.exportType() == ExternalType.FUNCTION) {
    //                        startFunctionIndex.set(export.index());
    //                    }
    //                }
    //                // This index is subject to change when we update the wat2wasm version
    //                assertEquals(18, startFunctionIndex.get());
    //
    //                Instance.builder(Parser.parse(new
    // File("../wabt/src/main/resources/wat2wasm")))
    //                        .withMachineFactory(
    //                                (inst) -> {
    //                                    var machine = Wat2Wasm.create(inst);
    //                                    return (funcId, args) -> {
    //                                        assertEquals(startFunctionIndex.get(), funcId);
    //                                        return machine.call(funcId, args);
    //                                    };
    //                                })
    //                        .withImportValues(imports)
    //                        .build();
    //            }
    //
    //            var result = stdoutStream.toByteArray();
    //
    //            assertTrue(result.length > 0);
    //            assertTrue(new String(result, UTF_8).contains("iterFact"));
    //        }
    //    }
    //
    //    @Test
    //    public void shouldCallIndirectInterpreterToAot() {
    //        var store = new Store();
    //        var table =
    //                new TableInstance(
    //                        new Table(ValType.FuncRef, new TableLimits(3, 3)), REF_NULL_VALUE);
    //        store.addTable(new ImportTable("test", "table", table));
    //
    //        var instance =
    //                Instance.builder(loadModule("compiled/call_indirect-export.wat.wasm"))
    //                        .withImportValues(store.toImportValues())
    //                        .withMachineFactory(InterpreterMachine::new)
    //                        .build();
    //        store.register("test", instance);
    //
    //        Instance.builder(loadModule("compiled/call_indirect-import.wat.wasm"))
    //                .withImportValues(store.toImportValues())
    //                .withMachineFactory(MachineFactoryCompiler::compile)
    //                .build();
    //
    //        assertEquals(42, instance.export("call-self").apply()[0]);
    //        assertEquals(88, instance.export("call-other").apply()[0]);
    //
    //        var ex = assertThrows(TrapException.class, instance.export("call-other-fail")::apply);
    //        var className = ex.getStackTrace()[0].getClassName();
    //        assertTrue(className.contains("CompiledMachine"), className);
    //    }

    //    @Test
    //    public void shouldCallIndirectAotToInterpreter() {
    //        var store = new Store();
    //        var table =
    //                new TableInstance(
    //                        new Table(ValType.FuncRef, new TableLimits(3, 3)), REF_NULL_VALUE);
    //        store.addTable(new ImportTable("test", "table", table));
    //
    //        var instance =
    //                Instance.builder(loadModule("compiled/call_indirect-export.wat.wasm"))
    //                        .withImportValues(store.toImportValues())
    //                        .withMachineFactory(MachineFactoryCompiler::compile)
    //                        .build();
    //        store.register("test", instance);
    //
    //        Instance.builder(loadModule("compiled/call_indirect-import.wat.wasm"))
    //                .withImportValues(store.toImportValues())
    //                .withMachineFactory(InterpreterMachine::new)
    //                .build();
    //
    //        assertEquals(42, instance.export("call-self").apply()[0]);
    //        assertEquals(88, instance.export("call-other").apply()[0]);
    //
    //        var ex = assertThrows(TrapException.class, instance.export("call-other-fail")::apply);
    //        var className = ex.getStackTrace()[0].getClassName();
    //        assertTrue(className.contains("InterpreterMachine"), className);
    //    }

    private static final String PGLITE_ARCHIVE =
            "/home/andreatp/workspace/pglite4j/wasm-build/output/sdk-dist/pglite-wasi.tar.xz";

    /**
     * PGLite test - runs PostgreSQL in WebAssembly using WIRE PROTOCOL.
     *
     * CMA mode (channel >= 0) requires wire protocol. Text mode only works in file transport mode.
     *
     * Wire protocol sequence:
     * 1. pgl_initdb() - create database cluster
     * 2. pgl_backend() - initialize backend (may trap - expected)
     * 3. use_wire(1) - enable wire protocol
     * 4. Send StartupMessage, handle auth, wait for ReadyForQuery
     * 5. Send Query message, get response
     */
    @Test
    public void shouldRunPGLite(@TempDir Path tempDir) throws Exception {
        // === EXTRACT ARCHIVE ===
        Process proc =
                new ProcessBuilder("tar", "-xf", PGLITE_ARCHIVE, "-C", tempDir.toString())
                        .inheritIO()
                        .start();
        assertEquals(0, proc.waitFor(), "Failed to extract pglite archive");
        Path extracted = tempDir.resolve("tmp/pglite");

        // === SETUP FILESYSTEM ===
        FileSystem fs =
                ZeroFs.newFileSystem(
                        Configuration.unix().toBuilder().setAttributeViews("unix").build());
        Path pgroot = fs.getPath("tmp");
        Files.createDirectories(pgroot);
        com.dylibso.chicory.wasi.Files.copyDirectory(extracted, pgroot.resolve("pglite"));
        Path pgdata = pgroot.resolve("pglite/base");
        Files.createDirectories(pgdata);
        Path dev = fs.getPath("dev");
        Files.createDirectories(dev);
        Files.write(dev.resolve("urandom"), new byte[128]);

        // === SETUP WASI ===
        var wasi =
                WasiPreview1.builder()
                        .withOptions(
                                WasiOptions.builder()
                                        .inheritSystem()
                                        .withDirectory(pgroot.toString(), pgroot)
                                        .withDirectory(pgdata.toString(), pgdata)
                                        .withDirectory(dev.toString(), dev)
                                        .withEnvironment("ENVIRONMENT", "wasm32_wasi_preview1")
                                        .withEnvironment("PREFIX", "/tmp/pglite")
                                        .withEnvironment("PGDATA", "/tmp/pglite/base")
                                        .withEnvironment("PGSYSCONFDIR", "/tmp/pglite")
                                        .withEnvironment("PGUSER", "postgres")
                                        .withEnvironment("PGDATABASE", "template1")
                                        .withEnvironment("MODE", "REACT")
                                        .withEnvironment("REPL", "N")
                                        .withEnvironment("TZ", "UTC")
                                        .withEnvironment("PGTZ", "UTC")
                                        .withEnvironment("PATH", "/tmp/pglite/bin")
                                        .withArguments(
                                                List.of(
                                                        "/tmp/pglite/bin/postgres",
                                                        "--single",
                                                        "postgres"))
                                        .build())
                        .build();

        // === CREATE INSTANCE ===
        var instance =
                Instance.builder(PostgresModule.load())
                        .withImportValues(
                                ImportValues.builder().addFunction(wasi.toHostFunctions()).build())
                        .withMachineFactory(PostgresModule::create)
                        .withMemoryLimits(new MemoryLimits(100))
                        .build();

        // === STEP 1: pgl_initdb (matching pglite-oxide: single call) ===
        System.out.println("pgl_initdb: " + instance.exports().function("pgl_initdb").apply()[0]);
        assertEquals("17\n", Files.readString(pgdata.resolve("PG_VERSION")));

        // === STEP 2: pgl_backend (may trap - expected in WASI due to OpenPipeStream) ===
        try {
            instance.exports().function("pgl_backend").apply();
            System.out.println("pgl_backend: OK");
        } catch (RuntimeException e) {
            // pglite-oxide: "pgl_backend may emit warnings about locale operations
            // (OpenPipeStream) because WASI doesn't support process spawning.
            // These are safe to ignore."
            System.out.println("pgl_backend trapped (expected): " + e.getMessage());
        }

        // === STEP 3: Get CMA buffer ===
        int channel = (int) instance.exports().function("get_channel").apply()[0];
        int addr = (int) instance.exports().function("get_buffer_addr").apply(channel)[0];
        System.out.println("CMA: channel=" + channel + " addr=" + addr);

        // === STEP 4: Wire protocol handshake ===
        // Clear CMA state before handshake (like pglite-oxide's clear_wire_pending)
        instance.exports().function("interactive_write").apply(0);

        // Send startup message
        byte[] startup = wireStartup("postgres", "template1");
        int pendingLen = wireSendCma(instance, addr, startup);

        // Drain responses until ReadyForQuery
        boolean ready = false;
        for (int round = 0; round < 100 && !ready; round++) {
            instance.exports().function("interactive_one").apply();
            byte[] resp = wireRecvCma(instance, addr, pendingLen);
            if (resp != null) {
                pendingLen = 0; // Clear after successful read (like pglite-oxide)
                int[] auth = wireGetAuth(resp);
                System.out.println("Handshake: " + wireParseSimple(resp) + " auth=" + auth[0]);
                if (auth[0] == 5) { // MD5 password
                    byte[] salt = {(byte) auth[1], (byte) auth[2], (byte) auth[3], (byte) auth[4]};
                    pendingLen =
                            wireSendCma(
                                    instance, addr, wireMd5Password("password", "postgres", salt));
                } else if (auth[0] == 3) { // Cleartext
                    pendingLen = wireSendCma(instance, addr, wirePassword("password"));
                }
                if (wireHasReadyForQuery(resp)) {
                    ready = true;
                }
            }
        }
        System.out.println("Handshake complete: " + ready);

        // === STEP 5: Run query (matching pglite-oxide's forward_wire pattern) ===
        byte[] query = wireQuery("select 1;");
        int queryPending = wireSendCma(instance, addr, query);

        // Poll for response using pglite-oxide's forward_wire pattern:
        // loop { collect_replies(); run_once(); collect_replies(); if nothing -> break }
        byte[] queryResp = null;
        for (int tick = 0; tick < 256; tick++) {
            queryResp = wireRecvCma(instance, addr, queryPending);
            if (queryResp != null) {
                break;
            }
            instance.exports().function("interactive_one").apply();
            queryResp = wireRecvCma(instance, addr, queryPending);
            if (queryResp != null) {
                break;
            }
        }

        System.out.println("Query response: " + wireParseSimple(queryResp));
        System.out.println("Query has ReadyForQuery: " + wireHasReadyForQuery(queryResp));
    }

    // Send wire message CMA style (returns pending length for reads)
    // Note: pglite-oxide calls use_wire(true) before EACH send in forward_wire
    private int wireSendCma(Instance inst, int addr, byte[] msg) {
        inst.exports().function("use_wire").apply(1); // Enable wire mode before each send
        inst.memory().write(addr, msg);
        inst.exports().function("interactive_write").apply(msg.length);
        return msg.length;
    }

    // Receive wire message CMA style (matching pglite-oxide's try_recv_wire_cma)
    // Response is at buffer_addr + pending + 1
    // After reading, clears CMA state via interactive_write(0)
    private byte[] wireRecvCma(Instance inst, int addr, int pendingLen) {
        int len = (int) inst.exports().function("interactive_read").apply()[0];
        if (len <= 0) {
            return null;
        }
        byte[] resp = inst.memory().readBytes(addr + pendingLen + 1, len);
        // Clear CMA state after reading (like pglite-oxide)
        inst.exports().function("interactive_write").apply(0);
        return resp;
    }

    // Dump memory as hex
    private String dumpMemoryHex(Instance inst, int addr, int len) {
        StringBuilder sb = new StringBuilder();
        byte[] data = inst.memory().readBytes(addr, len);
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02x ", data[i] & 0xFF));
        }
        sb.append(" | ");
        for (int i = 0; i < data.length; i++) {
            char c = (char) (data[i] & 0xFF);
            sb.append(c >= 32 && c < 127 ? c : '.');
        }
        return sb.toString();
    }

    // Get auth code and salt from response
    private int[] wireGetAuth(byte[] data) {
        int i = 0;
        while (i + 5 <= data.length) {
            char tag = (char) data[i];
            int len =
                    ((data[i + 1] & 0xFF) << 24)
                            | ((data[i + 2] & 0xFF) << 16)
                            | ((data[i + 3] & 0xFF) << 8)
                            | (data[i + 4] & 0xFF);
            if (len < 4) {
                break;
            }
            if (tag == 'R' && len >= 8) {
                int code =
                        ((data[i + 5] & 0xFF) << 24)
                                | ((data[i + 6] & 0xFF) << 16)
                                | ((data[i + 7] & 0xFF) << 8)
                                | (data[i + 8] & 0xFF);
                if (code == 5 && len >= 12) { // MD5 with salt
                    return new int[] {
                        code,
                        data[i + 9] & 0xFF,
                        data[i + 10] & 0xFF,
                        data[i + 11] & 0xFF,
                        data[i + 12] & 0xFF
                    };
                }
                return new int[] {code, 0, 0, 0, 0};
            }
            i += 1 + len;
        }
        return new int[] {-1, 0, 0, 0, 0};
    }

    // Check if response contains ReadyForQuery and print transaction status
    private boolean wireHasReadyForQuery(byte[] data) {
        int i = 0;
        while (i + 5 <= data.length) {
            char tag = (char) data[i];
            int len =
                    ((data[i + 1] & 0xFF) << 24)
                            | ((data[i + 2] & 0xFF) << 16)
                            | ((data[i + 3] & 0xFF) << 8)
                            | (data[i + 4] & 0xFF);
            if (len < 4) {
                break;
            }
            if (tag == 'Z') {
                // ReadyForQuery contains a single byte: transaction status
                // 'I' = idle, 'T' = in transaction, 'E' = failed transaction
                if (len == 5 && i + 6 <= data.length) {
                    char txStatus = (char) data[i + 5];
                    System.out.println("ReadyForQuery: transaction status = '" + txStatus + "'");
                }
                return true;
            }
            i += 1 + len;
        }
        return false;
    }

    // Parse wire response for display
    private String wireParseSimple(byte[] data) {
        if (data.length == 0) {
            return "(empty)";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i + 5 <= data.length) {
            char tag = (char) data[i];
            int len =
                    ((data[i + 1] & 0xFF) << 24)
                            | ((data[i + 2] & 0xFF) << 16)
                            | ((data[i + 3] & 0xFF) << 8)
                            | (data[i + 4] & 0xFF);
            if (len < 4 || i + 1 + len > data.length) {
                break;
            }
            sb.append("[").append(tag).append("] ");
            if (tag == 'D' && len > 6) { // DataRow - extract values
                int pos = i + 7;
                int fc = ((data[i + 5] & 0xFF) << 8) | (data[i + 6] & 0xFF);
                for (int f = 0; f < fc && pos + 4 <= i + 1 + len; f++) {
                    int fl =
                            ((data[pos] & 0xFF) << 24)
                                    | ((data[pos + 1] & 0xFF) << 16)
                                    | ((data[pos + 2] & 0xFF) << 8)
                                    | (data[pos + 3] & 0xFF);
                    pos += 4;
                    if (fl > 0 && pos + fl <= i + 1 + len) {
                        sb.append("'").append(new String(data, pos, fl, UTF_8)).append("' ");
                        pos += fl;
                    }
                }
            }
            i += 1 + len;
        }
        return sb.toString().trim();
    }

    // Wire protocol: StartupMessage (matching pglite-oxide)
    private byte[] wireStartup(String user, String db) {
        // Parameters: user, database, client_encoding, application_name (like pglite-oxide)
        String params =
                "user\0"
                        + user
                        + "\0"
                        + "database\0"
                        + db
                        + "\0"
                        + "client_encoding\0UTF8\0"
                        + "application_name\0chicory\0"
                        + "\0"; // Final null terminator
        byte[] paramsBytes = params.getBytes(UTF_8);
        byte[] msg = new byte[4 + 4 + paramsBytes.length];
        int len = msg.length;
        msg[0] = (byte) (len >> 24);
        msg[1] = (byte) (len >> 16);
        msg[2] = (byte) (len >> 8);
        msg[3] = (byte) len;
        msg[4] = 0;
        msg[5] = 3;
        msg[6] = 0;
        msg[7] = 0; // Protocol 3.0 = 196608 = 0x00030000
        System.arraycopy(paramsBytes, 0, msg, 8, paramsBytes.length);
        return msg;
    }

    // Wire protocol: PasswordMessage
    private byte[] wirePassword(String pw) {
        byte[] pwBytes = (pw + "\0").getBytes(UTF_8);
        byte[] msg = new byte[1 + 4 + pwBytes.length];
        msg[0] = 'p';
        int len = 4 + pwBytes.length;
        msg[1] = (byte) (len >> 24);
        msg[2] = (byte) (len >> 16);
        msg[3] = (byte) (len >> 8);
        msg[4] = (byte) len;
        System.arraycopy(pwBytes, 0, msg, 5, pwBytes.length);
        return msg;
    }

    // Wire protocol: MD5 PasswordMessage
    private byte[] wireMd5Password(String password, String user, byte[] salt) {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            md5.update(password.getBytes(UTF_8));
            md5.update(user.getBytes(UTF_8));
            String innerHex = bytesToHex(md5.digest());
            md5.reset();
            md5.update(innerHex.getBytes(UTF_8));
            md5.update(salt);
            return wirePassword("md5" + bytesToHex(md5.digest()));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    // Wire protocol: Query
    private byte[] wireQuery(String sql) {
        byte[] sqlBytes = (sql + "\0").getBytes(UTF_8);
        byte[] msg = new byte[1 + 4 + sqlBytes.length];
        msg[0] = 'Q';
        int len = 4 + sqlBytes.length;
        msg[1] = (byte) (len >> 24);
        msg[2] = (byte) (len >> 16);
        msg[3] = (byte) (len >> 8);
        msg[4] = (byte) len;
        System.arraycopy(sqlBytes, 0, msg, 5, sqlBytes.length);
        return msg;
    }
}
