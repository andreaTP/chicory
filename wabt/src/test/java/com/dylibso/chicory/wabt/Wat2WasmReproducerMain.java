package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasi.WasiExitException;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.WasmModule;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class Wat2WasmReproducerMain {
    private static final WasmModule MODULE = Wat2WasmModule.load();

    private Wat2WasmReproducerMain() {}

    private static String createBigWat(int funcCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("(module\n");

        for (int func = 1; func <= funcCount; func++) {
            sb.append("  (func $func_").append(func)
                    .append(" (export \"func_").append(func).append("\")\n");
            if (func != 1) {
                sb.append("\n");
                sb.append("    call $func_0\n");
            }
            sb.append("  )\n");
        }

        sb.append(")\n");
        return sb.toString();
    }

    private static byte[] parse(InputStream is) {
        try (ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();
             ByteArrayOutputStream stderrStream = new ByteArrayOutputStream()) {

            WasiOptions wasiOpts =
                    WasiOptions.builder()
                            .withStdin(is)
                            .withStdout(stdoutStream)
                            .withStderr(stderrStream)
                            .withArguments(List.of("wat2wasm", "-"))
                            .build();

            try (var wasi = WasiPreview1.builder().withOptions(wasiOpts).build()) {
                ImportValues imports =
                        ImportValues.builder().addFunction(wasi.toHostFunctions()).build();
                Instance.builder(MODULE)
                        .withMachineFactory(Wat2WasmModule::create)
                        .withImportValues(imports)
                        .build();
            } catch (WasiExitException e) {
                if (e.exitCode() != 0) {
                    throw new WatParseException(
                            stdoutStream.toString(StandardCharsets.UTF_8)
                                    + stderrStream.toString(StandardCharsets.UTF_8),
                            e);
                }
            }

            return stdoutStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        int funcCount = 1500;

        String wat = createBigWat(funcCount);
        System.out.println("WAT size (chars): " + wat.length());

        System.out.println("Invoking Wat2Wasm.parse(...)");
        byte[] result = parse(new ByteArrayInputStream(wat.getBytes(StandardCharsets.UTF_8)));
        System.out.println("Conversion succeeded, output bytes: " + result.length);
    }
}
