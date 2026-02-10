/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.MalformedException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class Encoding {
    public static final int MAX_VARINT_LEN_32 = 5;
    public static final int MAX_VARINT_LEN_64 = 10;
    public static final long MIN_SIGNED_INT = Integer.MIN_VALUE;
    public static final long MAX_SIGNED_INT = Integer.MAX_VALUE;
    public static final long MAX_UNSIGNED_INT = 0xFFFFFFFFL;

    private Encoding() {
    }

    static int readInt(ByteBuffer buffer) {
        if (buffer.remaining() < 4) {
            throw new MalformedException("length out of bounds");
        }
        return buffer.getInt();
    }

    static byte readByte(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            throw new MalformedException("length out of bounds");
        }
        return buffer.get();
    }

    static void readBytes(ByteBuffer buffer, byte[] dest) {
        if (buffer.remaining() < dest.length) {
            throw new MalformedException("length out of bounds");
        }
        buffer.get(dest);
    }

    public static long readVarUInt32(ByteBuffer buffer) {
        long value = Encoding.readUnsignedLeb128(buffer, 5);
        if (value < 0L || value > 0xFFFFFFFFL) {
            throw new MalformedException("integer too large");
        }
        return value;
    }

    public static long readVarSInt32(ByteBuffer buffer) {
        long value = Encoding.readSigned32Leb128(buffer);
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new MalformedException("integer too large");
        }
        return value;
    }

    public static long readVarSInt64(ByteBuffer buffer) {
        return Encoding.readSigned64Leb128(buffer);
    }

    public static long readFloat64(ByteBuffer buffer) {
        return buffer.getLong();
    }

    public static long readFloat32(ByteBuffer buffer) {
        return Encoding.readInt(buffer);
    }

    public static String readName(ByteBuffer buffer) {
        return Encoding.readName(buffer, true);
    }

    public static String readName(ByteBuffer buffer, boolean checkMalformed) {
        int length = (int)Encoding.readVarUInt32(buffer);
        byte[] bytes = new byte[length];
        Encoding.readBytes(buffer, bytes);
        String name = new String(bytes, StandardCharsets.UTF_8);
        if (checkMalformed && !Encoding.isValidIdentifier(name)) {
            throw new MalformedException("malformed UTF-8 encoding");
        }
        return name;
    }

    private static boolean isValidIdentifier(String string) {
        return string.chars().allMatch(ch -> ch < 128 || Character.isUnicodeIdentifierPart(ch));
    }

    public static long readUnsignedLeb128(ByteBuffer byteBuffer, int maxVarInt) {
        long result = 0L;
        int shift = 0;
        int i = 0;
        while (true) {
            ++i;
            if (byteBuffer.remaining() == 0) {
                throw new MalformedException("integer too large, integer representation too long, length out of bounds");
            }
            byte b = byteBuffer.get();
            result |= (long)(b & 0x7F) << shift;
            if ((b & 0x80) == 0) break;
            if (i >= maxVarInt || byteBuffer.remaining() == 0) {
                throw new MalformedException("integer representation too long");
            }
            shift += 7;
        }
        return result;
    }

    public static long readSigned32Leb128(ByteBuffer byteBuffer) {
        byte currentByte;
        long result = 0L;
        int shift = 0;
        int i = 0;
        do {
            ++i;
            if (byteBuffer.remaining() == 0) {
                throw new MalformedException("integer representation too long, length out of bounds");
            }
            currentByte = byteBuffer.get();
            if ((currentByte & 0x80) != 0 && i >= 5) {
                throw new MalformedException("integer representation too long");
            }
            result |= (long)(currentByte & 0x7F) << shift;
            shift += 7;
        } while ((currentByte & 0x80) != 0);
        if ((currentByte & 0x40) != 0) {
            result |= -(1L << shift);
        }
        return result;
    }

    public static long readSigned64Leb128(ByteBuffer byteBuffer) {
        byte currentByte;
        long result = 0L;
        int shift = 0;
        int i = 0;
        int size = 64;
        do {
            ++i;
            if (byteBuffer.remaining() == 0) {
                throw new MalformedException("integer representation too long, length out of bounds");
            }
            currentByte = byteBuffer.get();
            if ((currentByte & 0x80) != 0 && i >= 10) {
                throw new MalformedException("integer representation too long");
            }
            result |= (long)(currentByte & 0x7F) << shift;
            shift += 7;
        } while ((currentByte & 0x80) != 0);
        if (shift < size && (currentByte & 0x40) != 0) {
            result |= -1L << shift;
        }
        if (i >= 10 && currentByte != 0 && currentByte < 127) {
            throw new MalformedException("integer too large");
        }
        return result;
    }
}

