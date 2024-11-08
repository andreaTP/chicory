package com.dylibso.chicory.wasm;

import static java.lang.Integer.toUnsignedLong;

import com.dylibso.chicory.wasm.types.RawSection;
import java.io.ByteArrayOutputStream;

public final class WasmWriter {
    private static final byte[] MAGIC = {0x00, 0x61, 0x73, 0x6D};
    private static final byte[] VERSION = {0x01, 0x00, 0x00, 0x00};

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public WasmWriter() {
        out.writeBytes(MAGIC);
        out.writeBytes(VERSION);
    }

    public void writeSection(RawSection section) {
        writeSection(section.sectionId(), section.contents());
    }

    public void writeSection(int sectionId, byte[] contents) {
        out.write(sectionId);
        writeVarUInt32(out, contents.length);
        out.writeBytes(contents);
    }

    public byte[] bytes() {
        return out.toByteArray();
    }

    public static void writeVarUInt32(ByteArrayOutputStream out, int value) {
        long x = toUnsignedLong(value);
        while (true) {
            if (x < 0x80) {
                out.write((int) x);
                break;
            }
            out.write((int) ((x & 0x7F) | 0x80));
            x >>= 7;
        }
    }
}
