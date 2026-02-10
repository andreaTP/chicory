/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.Objects;

public class Value {
    public static final long TRUE = 1L;
    public static final long FALSE = 0L;
    public static final int REF_NULL_VALUE = -1;
    public static final long[] EMPTY_VALUES = new long[0];
    private final ValType type;
    private final long data;

    public long raw() {
        return this.data;
    }

    public ValType type() {
        return this.type;
    }

    public static long floatToLong(float data) {
        return Float.floatToRawIntBits(data);
    }

    public static float longToFloat(long data) {
        return Float.intBitsToFloat((int)data);
    }

    public static long doubleToLong(double data) {
        return Double.doubleToRawLongBits(data);
    }

    public static double longToDouble(long data) {
        return Double.longBitsToDouble(data);
    }

    public static Value fromFloat(float data) {
        return Value.f32(Value.floatToLong(data));
    }

    public int asInt() {
        assert (this.type == ValType.I32);
        return (int)this.data;
    }

    public long asLong() {
        assert (this.type == ValType.I64);
        return this.data;
    }

    public float asFloat() {
        assert (this.type == ValType.F32);
        return Value.longToFloat(this.data);
    }

    public double asDouble() {
        assert (this.type == ValType.F64);
        return Value.longToDouble(this.data);
    }

    public static Value fromDouble(double data) {
        return Value.f64(Value.doubleToLong(data));
    }

    public static Value i32(int data) {
        return Value.i32((long)data);
    }

    public static Value i32(long data) {
        return new Value(ValType.I32, data);
    }

    public static Value i64(long data) {
        return new Value(ValType.I64, data);
    }

    public static Value f32(long data) {
        return new Value(ValType.F32, data);
    }

    public static Value f64(long data) {
        return new Value(ValType.F64, data);
    }

    public static Value externRef(long data) {
        return new Value(ValType.ExternRef, data);
    }

    public static Value funcRef(long data) {
        return new Value(ValType.FuncRef, data);
    }

    @Deprecated(since="1.3.0")
    public Value(ValueType type, long value) {
        this.type = Objects.requireNonNull(type, "type").toValType();
        this.data = value;
    }

    public Value(ValType type, long value) {
        this.type = Objects.requireNonNull(type, "type");
        this.data = value;
    }

    public static byte[] vecTo8(long[] values) {
        byte[] result = new byte[values.length * 8];
        int valueIdx = 0;
        for (int i = 0; i < result.length; ++i) {
            long v = values[valueIdx++];
            result[i] = (byte)(v & 0xFFL);
            result[++i] = (byte)(v >> 8 & 0xFFL);
            result[++i] = (byte)(v >> 16 & 0xFFL);
            result[++i] = (byte)(v >> 24 & 0xFFL);
            result[++i] = (byte)(v >> 32 & 0xFFL);
            result[++i] = (byte)(v >> 40 & 0xFFL);
            result[++i] = (byte)(v >> 48 & 0xFFL);
            result[++i] = (byte)(v >> 56 & 0xFFL);
        }
        return result;
    }

    public static long[] bytesToVec(byte[] bytes) {
        long[] result = new long[bytes.length / 8];
        int valueIdx = 0;
        for (int i = 0; i < bytes.length; ++i) {
            result[valueIdx++] = Byte.toUnsignedLong(bytes[i]) + (Byte.toUnsignedLong(bytes[++i]) << 8) | Byte.toUnsignedLong(bytes[++i]) << 16 | Byte.toUnsignedLong(bytes[++i]) << 24 | Byte.toUnsignedLong(bytes[++i]) << 32 | Byte.toUnsignedLong(bytes[++i]) << 40 | Byte.toUnsignedLong(bytes[++i]) << 48 | Byte.toUnsignedLong(bytes[++i]) << 56;
        }
        return result;
    }

    public static int[] vecTo16(long[] values) {
        int[] result = new int[values.length * 4];
        int valueIdx = 0;
        for (int i = 0; i < result.length; ++i) {
            long v = values[valueIdx++];
            result[i] = (int)(v & 0xFFFFL);
            result[++i] = (int)(v >> 16 & 0xFFFFL);
            result[++i] = (int)(v >> 32 & 0xFFFFL);
            result[++i] = (int)(v >> 48 & 0xFFFFL);
        }
        return result;
    }

    public static long[] vecTo32(long[] values) {
        long[] result = new long[values.length * 2];
        int valueIdx = 0;
        for (int i = 0; i < result.length; ++i) {
            long v = values[valueIdx++];
            result[i] = v & 0xFFFFFFFFL;
            result[++i] = v >> 32 & 0xFFFFFFFFL;
        }
        return result;
    }

    public static float[] vecToF32(long[] values) {
        float[] result = new float[values.length * 2];
        int valueIdx = 0;
        for (int i = 0; i < result.length; ++i) {
            long v = values[valueIdx++];
            result[i] = Float.intBitsToFloat((int)(v & 0xFFFFFFFFL));
            result[++i] = Float.intBitsToFloat((int)(v >> 32 & 0xFFFFFFFFL));
        }
        return result;
    }

    public static double[] vecToF64(long[] values) {
        double[] result = new double[values.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Double.longBitsToDouble(values[i]);
        }
        return result;
    }

    public static long[] i8ToVec(long[] ... vec) {
        long[] result = new long[vec.length * 2];
        int i = 0;
        while (i < result.length) {
            long[] v = vec[i];
            result[i++] = v[0] & 0xFFL | (v[1] & 0xFFL) << 8 | (v[2] & 0xFFL) << 16 | (v[3] & 0xFFL) << 24 | (v[4] & 0xFFL) << 32 | (v[5] & 0xFFL) << 40 | (v[6] & 0xFFL) << 48 | (v[7] & 0xFFL) << 56;
            result[i++] = v[8] & 0xFFL | (v[9] & 0xFFL) << 8 | (v[10] & 0xFFL) << 16 | (v[11] & 0xFFL) << 24 | (v[12] & 0xFFL) << 32 | (v[13] & 0xFFL) << 40 | (v[14] & 0xFFL) << 48 | (v[15] & 0xFFL) << 56;
        }
        return result;
    }

    public static long[] i16ToVec(long[] ... vec) {
        long[] result = new long[vec.length * 2];
        int i = 0;
        while (i < result.length) {
            long[] v = vec[i];
            result[i++] = v[0] & 0xFFFFL | (v[1] & 0xFFFFL) << 16 | (v[2] & 0xFFFFL) << 32 | (v[3] & 0xFFFFL) << 48;
            result[i++] = v[4] & 0xFFFFL | (v[5] & 0xFFFFL) << 16 | (v[6] & 0xFFFFL) << 32 | (v[7] & 0xFFFFL) << 48;
        }
        return result;
    }

    public static long[] i32ToVec(long[] ... vec) {
        long[] result = new long[vec.length * 2];
        int i = 0;
        while (i < result.length) {
            long[] v = vec[i];
            result[i++] = (v[1] & 0xFFFFFFFFL) << 32 | v[0] & 0xFFFFFFFFL;
            result[i++] = (v[3] & 0xFFFFFFFFL) << 32 | v[2] & 0xFFFFFFFFL;
        }
        return result;
    }

    public static long[] i64ToVec(long[] ... vec) {
        long[] result = new long[vec.length * 2];
        int i = 0;
        while (i < result.length) {
            long[] v = vec[i];
            result[i++] = v[0];
            result[i++] = v[1];
        }
        return result;
    }

    public static long[] f32ToVec(long[] ... vec) {
        return Value.i32ToVec(vec);
    }

    public static long[] f64ToVec(long[] ... vec) {
        return Value.i64ToVec(vec);
    }

    public static long zero(ValType valType) {
        switch (valType.opcode()) {
            case 124: 
            case 125: 
            case 126: 
            case 127: {
                return 0L;
            }
            case 99: 
            case 100: 
            case 105: {
                return -1L;
            }
        }
        throw new IllegalArgumentException("Can't create a zero value for type " + String.valueOf(valType));
    }

    public String toString() {
        switch (this.type.opcode()) {
            case 127: {
                return (int)this.data + "@i32";
            }
            case 126: {
                return this.data + "@i64";
            }
            case 125: {
                return Value.longToFloat(this.data) + "@f32";
            }
            case 124: {
                return Value.longToDouble(this.data) + "@f64";
            }
            case 123: {
                return this.data + "@v128";
            }
            case 100: {
                return "ref[" + (int)this.data + "]";
            }
            case 99: {
                return "refnull[" + (int)this.data + "]";
            }
        }
        throw new AssertionError((Object)("Unhandled type: " + String.valueOf(this.type)));
    }

    public final boolean equals(Object v) {
        if (v == this) {
            return true;
        }
        if (!(v instanceof Value)) {
            return false;
        }
        Value other = (Value)v;
        return this.type.id() == other.type.id() && this.data == other.data;
    }

    public final int hashCode() {
        return Objects.hash(this.type.id(), this.data);
    }
}

