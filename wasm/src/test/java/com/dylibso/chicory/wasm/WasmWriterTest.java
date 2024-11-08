package com.dylibso.chicory.wasm;

import static com.dylibso.chicory.wasm.Parser.parseWithoutDecoding;
import static com.dylibso.chicory.wasm.ParserTest.wasmCorpusFiles;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WasmWriterTest {

    @Test
    public void shouldRoundTrip() throws IOException {
        for (var file : wasmCorpusFiles()) {
            // uses non-canonical size encodings
            if (file.getName().endsWith("main.go.wasm")) {
                continue;
            }

            byte[] wasm = Files.readAllBytes(file.toPath());
            var writer = new WasmWriter();
            parseWithoutDecoding(wasm, writer::writeSection);
            Parser.parse(writer.bytes());
            assertArrayEquals(wasm, writer.bytes());
        }
    }
}
