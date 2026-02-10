/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.types.RawSection;
import java.io.ByteArrayOutputStream;

public final class WasmWriter {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public WasmWriter() {
        this.out.writeBytes(Parser.MAGIC_BYTES);
        this.out.writeBytes(Parser.VERSION_BYTES);
    }

    public void writeSection(RawSection section) {
        this.writeSection(section.sectionId(), section.contents());
    }

    public void writeSection(int sectionId, byte[] contents) {
        this.out.write(sectionId);
        WasmWriter.writeVarUInt32(this.out, contents.length);
        this.out.writeBytes(contents);
    }

    public byte[] bytes() {
        return this.out.toByteArray();
    }

    public static void writeVarUInt32(ByteArrayOutputStream out, int value) {
        long x = Integer.toUnsignedLong(value);
        while (true) {
            if (x < 128L) break;
            out.write((int)(x & 0x7FL | 0x80L));
            x >>= 7;
        }
        out.write((int)x);
    }
}

