// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import com.dylibso.chicory.log.SystemLogger;
import com.dylibso.chicory.wasi.WasiExitException;
import java.util.function.Function;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.wasi.WasiPreview1;
import java.util.List;
import java.io.OutputStream;
import com.dylibso.chicory.wasi.WasiOptions;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.FileInputStream;
import java.io.File;
import com.dylibso.chicory.wasm.WasmModule;
import com.dylibso.chicory.log.Logger;

public final class Wat2Wasm
{
    private static final Logger logger;
    private static final WasmModule MODULE;
    
    private Wat2Wasm() {
    }
    
    public static byte[] parse(final File file) {
        try (final InputStream is = new FileInputStream(file)) {
            return parse(is);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static byte[] parse(final String wat) {
        try (final InputStream is = new ByteArrayInputStream(wat.getBytes(StandardCharsets.UTF_8))) {
            return parse(is);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private static byte[] parse(final InputStream is) {
        try (final ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();
             final ByteArrayOutputStream stderrStream = new ByteArrayOutputStream()) {
            final WasiOptions wasiOpts = WasiOptions.builder().withStdin(is).withStdout((OutputStream)stdoutStream).withStderr((OutputStream)stderrStream).withArguments((List)List.of("wat2wasm", "-", "--output=-")).build();
            try (final WasiPreview1 wasi = WasiPreview1.builder().withLogger(Wat2Wasm.logger).withOptions(wasiOpts).build()) {
                final ImportValues imports = ImportValues.builder().addFunction((ImportFunction[])wasi.toHostFunctions()).build();
                Instance.builder(Wat2Wasm.MODULE).withMachineFactory(Wat2WasmModule::create).withImportValues(imports).build();
            }
            catch (final WasiExitException e) {
                if (e.exitCode() != 0) {
                    throw new WatParseException(stdoutStream.toString(StandardCharsets.UTF_8) + stderrStream.toString(StandardCharsets.UTF_8), (Throwable)e);
                }
            }
            return stdoutStream.toByteArray();
        }
        catch (final IOException e2) {
            throw new UncheckedIOException(e2);
        }
    }
    
    static {
        logger = (Logger)new SystemLogger();
        MODULE = Wat2WasmModule.load();
    }
}
