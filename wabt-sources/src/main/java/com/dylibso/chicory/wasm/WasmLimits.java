/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

public final class WasmLimits {
    public static final int MAX_TYPES = 1000000;
    public static final int MAX_FUNCTIONS = 1000000;
    public static final int MAX_IMPORTS = 100000;
    public static final int MAX_EXPORTS = 100000;
    public static final int MAX_EXCEPTIONS = 100000;
    public static final int MAX_GLOBALS = 1000000;
    public static final int MAX_DATA_SEGMENTS = 100000;
    public static final int MAX_STRUCT_FIELD_COUNT = 10000;
    public static final int MAX_ARRAY_NEW_FIXED_ARGS = 10000;
    public static final int MAX_RECURSION_GROUP_COUNT = 1000000;
    public static final int MAX_NUMBER_OF_RECURSION_GROUPS = 1000000;
    public static final int MAX_SUBTYPE_SUPERTYPE_COUNT = 1;
    public static final int MAX_SUBTYPE_DEPTH = 63;
    public static final int MAX_STRING_SIZE = 100000;
    public static final int MAX_MODULE_SIZE = 0x40000000;
    public static final int MAX_FUNCTION_SIZE = 7654321;
    public static final int MAX_FUNCTION_LOCALS = 50000;
    public static final int MAX_FUNCTION_PARAMS = 1000;
    public static final int MAX_FUNCTION_RETURNS = 1000;
    public static final int MAX_TABLE_ENTRIES = 10000000;
    public static final int MAX_TABLES = 1000000;

    private WasmLimits() {
    }
}

