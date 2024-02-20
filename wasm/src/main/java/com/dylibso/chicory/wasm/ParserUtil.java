package com.dylibso.chicory.wasm;

import java.nio.charset.StandardCharsets;

public class ParserUtil {

    private ParserUtil() {}

    private static long getByte(byte b) {
        return b & 0xFF;
    }

    public static boolean isValidIdentifier(byte[] bytes) {
        try {
            if (bytes.length <= 0) {
                return false;
            }

            for (int i = 0; i < bytes.length; i++) {
                long c = getByte(bytes[i]);

                if (c < 0x80) { // one byte encoding
                    continue;
                } else {
                    var b1 = c;
                    var b2 = getByte(bytes[i++]);
                    if (b2 >= 0xc0) {
                        return false;
                    }
                    var c1 = 2 ^ 6 * (b1 - 0xC0) + (b2 - 0x80);
                    if (c1 > 0x800) { // two bytes encoding
                        continue;
                    } else {

                        var b3 = getByte(bytes[i++]);
                        if (b3 >= 0xc0) {
                            return false;
                        }
                        var c2 = (2 ^ 12 * (b1 - 0xE0)) + (2 ^ 6 * (b2 - 0x80)) + (b2 - 0x80);

                        if (c2 >= 0xD800 && c2 <= 0xE000) { // surrogate
                            return false;
                        } else if (c < 0x10000) { // three bytes encoding
                            continue;
                        } else {
                            var b4 = getByte(bytes[i++]);
                            if (b4 >= 0xc0) {
                                return false;
                            }
                            var c3 =
                                    (2 ^ 16 * (b1 - 0xF0))
                                            + (2 ^ 12 * (b2 - 0x80))
                                            + (2 ^ 5 * (b3 - 0x80))
                                            + (b4 - 0x80);
                            if (c3 >= 0x110000) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException oob) {
            return false;
        }
    }

    private static boolean isValidIdentifierChar(char ch) {
        return (ch >= '\u0000' && ch <= '\ud7ff') || (ch >= '\ue000' && ch <= '\ufffc');
    }
}
