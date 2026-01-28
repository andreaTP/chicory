package com.dylibso.chicory.wabt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Wat2WasmReproducerMain {

    private Wat2WasmReproducerMain() {}

    private static String loadPreGeneratedWat() throws IOException {
        try (InputStream is = Wat2WasmReproducerMain.class.getResourceAsStream("big-50k-0.wat")) {
            if (is == null) {
                throw new IOException("Resource big-50k-0.wat not found on classpath");
            }
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Loading pre-generated WAT from classpath: big-50k-0.wat");
        String wat = loadPreGeneratedWat();
        System.out.println("WAT size (chars): " + wat.length());

        System.out.println("Invoking Wat2Wasm.parse(...)");
        byte[] result = Wat2Wasm.parse(wat);
        System.out.println("Conversion succeeded, output bytes: " + result.length);
    }
}
