/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ConstantEvaluators;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.OpCodeIdentifier;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.wasm.types.Element;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.PassiveElement;
import com.dylibso.chicory.wasm.types.ValType;

public final class OpcodeImpl {
    private OpcodeImpl() {
    }

    @OpCodeIdentifier(value=OpCode.I32_CLZ)
    public static int I32_CLZ(int tos) {
        return Integer.numberOfLeadingZeros(tos);
    }

    @OpCodeIdentifier(value=OpCode.I32_CTZ)
    public static int I32_CTZ(int tos) {
        return Integer.numberOfTrailingZeros(tos);
    }

    @OpCodeIdentifier(value=OpCode.I32_DIV_S)
    public static int I32_DIV_S(int a, int b) {
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new WasmRuntimeException("integer overflow");
        }
        if (b == 0) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return a / b;
    }

    @OpCodeIdentifier(value=OpCode.I32_DIV_U)
    public static int I32_DIV_U(int a, int b) {
        if (b == 0) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return Integer.divideUnsigned(a, b);
    }

    @OpCodeIdentifier(value=OpCode.I32_EQ)
    public static int I32_EQ(int b, int a) {
        return a == b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_EQZ)
    public static int I32_EQZ(int a) {
        return a == 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_EXTEND_8_S)
    public static int I32_EXTEND_8_S(int tos) {
        return (byte)tos;
    }

    @OpCodeIdentifier(value=OpCode.I32_EXTEND_16_S)
    public static int I32_EXTEND_16_S(int tos) {
        return (short)tos;
    }

    @OpCodeIdentifier(value=OpCode.I32_GE_S)
    public static int I32_GE_S(int a, int b) {
        return a >= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_GE_U)
    public static int I32_GE_U(int a, int b) {
        return Integer.compareUnsigned(a, b) >= 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_GT_S)
    public static int I32_GT_S(int a, int b) {
        return a > b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_GT_U)
    public static int I32_GT_U(int a, int b) {
        return Integer.compareUnsigned(a, b) > 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_LE_S)
    public static int I32_LE_S(int a, int b) {
        return a <= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_LE_U)
    public static int I32_LE_U(int a, int b) {
        return Integer.compareUnsigned(a, b) <= 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_LT_S)
    public static int I32_LT_S(int a, int b) {
        return a < b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_LT_U)
    public static int I32_LT_U(int a, int b) {
        return Integer.compareUnsigned(a, b) < 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I32_NE)
    public static int I32_NE(int b, int a) {
        return a == b ? 0 : 1;
    }

    @OpCodeIdentifier(value=OpCode.I32_POPCNT)
    public static int I32_POPCNT(int tos) {
        return Integer.bitCount(tos);
    }

    @OpCodeIdentifier(value=OpCode.I32_REINTERPRET_F32)
    public static int I32_REINTERPRET_F32(float x) {
        return Float.floatToRawIntBits(x);
    }

    @OpCodeIdentifier(value=OpCode.I32_REM_S)
    public static int I32_REM_S(int a, int b) {
        if (b == 0) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return a % b;
    }

    @OpCodeIdentifier(value=OpCode.I32_REM_U)
    public static int I32_REM_U(int a, int b) {
        if (b == 0) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return Integer.remainderUnsigned(a, b);
    }

    @OpCodeIdentifier(value=OpCode.I32_ROTR)
    public static int I32_ROTR(int v, int c) {
        return v >>> c | v << 32 - c;
    }

    @OpCodeIdentifier(value=OpCode.I32_ROTL)
    public static int I32_ROTL(int v, int c) {
        return v << c | v >>> 32 - c;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_F32_S)
    public static int I32_TRUNC_F32_S(float x) {
        if (Float.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        if (x < -2.1474836E9f || x >= 2.1474836E9f) {
            throw new WasmRuntimeException("integer overflow");
        }
        return (int)x;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_F32_U)
    public static int I32_TRUNC_F32_U(float x) {
        if (Float.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        long v = (long)x;
        if (v < 0L || v >= 0xFFFFFFFFL) {
            throw new WasmRuntimeException("integer overflow");
        }
        return (int)v;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_F64_S)
    public static int I32_TRUNC_F64_S(double tos) {
        if (Double.isNaN(tos)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        long v = (long)tos;
        if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE) {
            throw new WasmRuntimeException("integer overflow");
        }
        return (int)v;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_F64_U)
    public static int I32_TRUNC_F64_U(double tos) {
        if (Double.isNaN(tos)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        long v = (long)tos;
        if (v < 0L || v > 0xFFFFFFFFL) {
            throw new WasmRuntimeException("integer overflow");
        }
        return (int)v;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_SAT_F32_S)
    public static int I32_TRUNC_SAT_F32_S(float x) {
        if (Float.isNaN(x)) {
            return 0;
        }
        if (x < -2.1474836E9f) {
            return Integer.MIN_VALUE;
        }
        if (x > 2.1474836E9f) {
            return Integer.MAX_VALUE;
        }
        return (int)x;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_SAT_F32_U)
    public static int I32_TRUNC_SAT_F32_U(float x) {
        if (Float.isNaN(x) || x < 0.0f) {
            return 0;
        }
        if (x >= 4.2949673E9f) {
            return -1;
        }
        return (int)x;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_SAT_F64_S)
    public static int I32_TRUNC_SAT_F64_S(double x) {
        if (Double.isNaN(x)) {
            return 0;
        }
        if (x < -2.147483648E9) {
            return Integer.MIN_VALUE;
        }
        if (x > 2.147483647E9) {
            return Integer.MAX_VALUE;
        }
        return (int)x;
    }

    @OpCodeIdentifier(value=OpCode.I32_TRUNC_SAT_F64_U)
    public static int I32_TRUNC_SAT_F64_U(double x) {
        if (Double.isNaN(x) || x < 0.0) {
            return 0;
        }
        if (x > 4.294967295E9) {
            return -1;
        }
        return (int)x;
    }

    @OpCodeIdentifier(value=OpCode.I64_CLZ)
    public static long I64_CLZ(long tos) {
        return Long.numberOfLeadingZeros(tos);
    }

    @OpCodeIdentifier(value=OpCode.I64_CTZ)
    public static long I64_CTZ(long tos) {
        return Long.numberOfTrailingZeros(tos);
    }

    @OpCodeIdentifier(value=OpCode.I64_DIV_S)
    public static long I64_DIV_S(long a, long b) {
        if (a == Long.MIN_VALUE && b == -1L) {
            throw new WasmRuntimeException("integer overflow");
        }
        if (b == 0L) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return a / b;
    }

    @OpCodeIdentifier(value=OpCode.I64_DIV_U)
    public static long I64_DIV_U(long a, long b) {
        if (b == 0L) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return Long.divideUnsigned(a, b);
    }

    @OpCodeIdentifier(value=OpCode.I64_EQ)
    public static int I64_EQ(long b, long a) {
        return a == b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_EQZ)
    public static int I64_EQZ(long a) {
        return a == 0L ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_EXTEND_8_S)
    public static long I64_EXTEND_8_S(long tos) {
        return (byte)tos;
    }

    @OpCodeIdentifier(value=OpCode.I64_EXTEND_16_S)
    public static long I64_EXTEND_16_S(long tos) {
        return (short)tos;
    }

    @OpCodeIdentifier(value=OpCode.I64_EXTEND_32_S)
    public static long I64_EXTEND_32_S(long tos) {
        return (int)tos;
    }

    @OpCodeIdentifier(value=OpCode.I64_EXTEND_I32_U)
    public static long I64_EXTEND_I32_U(int x) {
        return Integer.toUnsignedLong(x);
    }

    @OpCodeIdentifier(value=OpCode.I64_GE_S)
    public static int I64_GE_S(long a, long b) {
        return a >= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_GE_U)
    public static int I64_GE_U(long a, long b) {
        return Long.compareUnsigned(a, b) >= 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_GT_S)
    public static int I64_GT_S(long a, long b) {
        return a > b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_GT_U)
    public static int I64_GT_U(long a, long b) {
        return Long.compareUnsigned(a, b) > 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_LE_S)
    public static int I64_LE_S(long a, long b) {
        return a <= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_LE_U)
    public static int I64_LE_U(long a, long b) {
        return Long.compareUnsigned(a, b) <= 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_LT_S)
    public static int I64_LT_S(long a, long b) {
        return a < b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_LT_U)
    public static int I64_LT_U(long a, long b) {
        return Long.compareUnsigned(a, b) < 0 ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.I64_NE)
    public static int I64_NE(long b, long a) {
        return a == b ? 0 : 1;
    }

    @OpCodeIdentifier(value=OpCode.I64_POPCNT)
    public static long I64_POPCNT(long tos) {
        return Long.bitCount(tos);
    }

    @OpCodeIdentifier(value=OpCode.I64_REINTERPRET_F64)
    public static long I64_REINTERPRET_F64(double x) {
        return Double.doubleToRawLongBits(x);
    }

    @OpCodeIdentifier(value=OpCode.I64_REM_S)
    public static long I64_REM_S(long a, long b) {
        if (b == 0L) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return a % b;
    }

    @OpCodeIdentifier(value=OpCode.I64_REM_U)
    public static long I64_REM_U(long a, long b) {
        if (b == 0L) {
            throw new WasmRuntimeException("integer divide by zero");
        }
        return Long.remainderUnsigned(a, b);
    }

    @OpCodeIdentifier(value=OpCode.I64_ROTR)
    public static long I64_ROTR(long v, long c) {
        return v >>> (int)c | v << (int)(64L - c);
    }

    @OpCodeIdentifier(value=OpCode.I64_ROTL)
    public static long I64_ROTL(long v, long c) {
        return v << (int)c | v >>> (int)(64L - c);
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_F32_S)
    public static long I64_TRUNC_F32_S(float x) {
        if (Float.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        if (x < -9.223372E18f || x >= 9.223372E18f) {
            throw new WasmRuntimeException("integer overflow");
        }
        return (long)x;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_F32_U)
    public static long I64_TRUNC_F32_U(float x) {
        if (Float.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        if (x >= 1.8446744E19f) {
            throw new WasmRuntimeException("integer overflow");
        }
        if (x < 9.223372E18f) {
            long v = (long)x;
            if (v < 0L) {
                throw new WasmRuntimeException("integer overflow");
            }
            return v;
        }
        long v = Long.MAX_VALUE + (long)(x - 9.223372E18f) + 1L;
        if (v >= 0L) {
            throw new WasmRuntimeException("integer overflow");
        }
        return v;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_F64_S)
    public static long I64_TRUNC_F64_S(double x) {
        if (Double.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        if (x == -9.223372036854776E18) {
            return Long.MIN_VALUE;
        }
        long v = (long)x;
        if (v == Long.MIN_VALUE || v == Long.MAX_VALUE) {
            throw new WasmRuntimeException("integer overflow");
        }
        return v;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_F64_U)
    public static long I64_TRUNC_F64_U(double x) {
        if (Double.isNaN(x)) {
            throw new WasmRuntimeException("invalid conversion to integer");
        }
        if (x >= 1.8446744073709552E19) {
            throw new WasmRuntimeException("integer overflow");
        }
        if (x < 9.223372036854776E18) {
            long v = (long)x;
            if (v < 0L) {
                throw new WasmRuntimeException("integer overflow");
            }
            return v;
        }
        long v = Long.MAX_VALUE + (long)(x - 9.223372036854776E18) + 1L;
        if (v >= 0L) {
            throw new WasmRuntimeException("integer overflow");
        }
        return v;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_SAT_F32_S)
    public static long I64_TRUNC_SAT_F32_S(float x) {
        if (Float.isNaN(x)) {
            return 0L;
        }
        if (x <= -9.223372E18f) {
            return Long.MIN_VALUE;
        }
        if (x >= 9.223372E18f) {
            return Long.MAX_VALUE;
        }
        return (long)x;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_SAT_F32_U)
    public static long I64_TRUNC_SAT_F32_U(float x) {
        if (Float.isNaN(x) || x < 0.0f) {
            return 0L;
        }
        if ((double)x > Math.pow(2.0, 64.0) - 1.0) {
            return -1L;
        }
        if (x < 9.223372E18f) {
            return (long)x;
        }
        long v = Long.MAX_VALUE + (long)((double)x - 9.223372036854776E18) + 1L;
        if (v >= 0L) {
            throw new WasmRuntimeException("integer overflow");
        }
        return v;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_SAT_F64_S)
    public static long I64_TRUNC_SAT_F64_S(double x) {
        if (Double.isNaN(x)) {
            return 0L;
        }
        if (x <= -9.223372036854776E18) {
            return Long.MIN_VALUE;
        }
        if (x >= 9.223372036854776E18) {
            return Long.MAX_VALUE;
        }
        return (long)x;
    }

    @OpCodeIdentifier(value=OpCode.I64_TRUNC_SAT_F64_U)
    public static long I64_TRUNC_SAT_F64_U(double x) {
        if (Double.isNaN(x) || x < 0.0) {
            return 0L;
        }
        if (x > Math.pow(2.0, 64.0) - 1.0) {
            return -1L;
        }
        if (x < 9.223372036854776E18) {
            return (long)x;
        }
        long v = Long.MAX_VALUE + (long)(x - 9.223372036854776E18) + 1L;
        if (v >= 0L) {
            throw new WasmRuntimeException("integer overflow");
        }
        return v;
    }

    @OpCodeIdentifier(value=OpCode.F32_ABS)
    public static float F32_ABS(float x) {
        return Math.abs(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_CEIL)
    public static float F32_CEIL(float x) {
        return (float)Math.ceil(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_CONVERT_I32_S)
    public static float F32_CONVERT_I32_S(int x) {
        return x;
    }

    @OpCodeIdentifier(value=OpCode.F32_CONVERT_I32_U)
    public static float F32_CONVERT_I32_U(int x) {
        return Integer.toUnsignedLong(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_CONVERT_I64_S)
    public static float F32_CONVERT_I64_S(long x) {
        return x;
    }

    @OpCodeIdentifier(value=OpCode.F32_CONVERT_I64_U)
    public static float F32_CONVERT_I64_U(long x) {
        if (x >= 0L) {
            return x;
        }
        long sum = x + 0xFFFFFFFFFFL;
        long shiftIn = ((sum ^ x) & Long.MIN_VALUE) >>> 39;
        return Math.scalb(sum >>> 40 | shiftIn, 40);
    }

    @OpCodeIdentifier(value=OpCode.F32_COPYSIGN)
    public static float F32_COPYSIGN(float a, float b) {
        if (b == 4.290773E9f) {
            return Math.copySign(a, -1.0f);
        }
        if (b == 2.1432893E9f) {
            return Math.copySign(a, 1.0f);
        }
        return Math.copySign(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F32_EQ)
    public static int F32_EQ(float a, float b) {
        return a == b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F32_FLOOR)
    public static float F32_FLOOR(float x) {
        return (float)Math.floor(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_GE)
    public static int F32_GE(float a, float b) {
        return a >= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F32_GT)
    public static int F32_GT(float a, float b) {
        return a > b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F32_LE)
    public static int F32_LE(float a, float b) {
        return a <= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F32_LT)
    public static int F32_LT(float a, float b) {
        return a < b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F32_MAX)
    public static float F32_MAX(float a, float b) {
        return Math.max(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F32_MIN)
    public static float F32_MIN(float a, float b) {
        return Math.min(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F32_NE)
    public static int F32_NE(float a, float b) {
        return a == b ? 0 : 1;
    }

    @OpCodeIdentifier(value=OpCode.F32_NEAREST)
    public static float F32_NEAREST(float x) {
        return (float)Math.rint(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_REINTERPRET_I32)
    public static float F32_REINTERPRET_I32(int x) {
        return Float.intBitsToFloat(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_SQRT)
    public static float F32_SQRT(float x) {
        return (float)Math.sqrt(x);
    }

    @OpCodeIdentifier(value=OpCode.F32_TRUNC)
    public static float F32_TRUNC(float x) {
        return (float)(x < 0.0f ? Math.ceil(x) : Math.floor(x));
    }

    @OpCodeIdentifier(value=OpCode.F64_ABS)
    public static double F64_ABS(double x) {
        return Math.abs(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_CEIL)
    public static double F64_CEIL(double x) {
        return Math.ceil(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_CONVERT_I32_S)
    public static double F64_CONVERT_I32_S(int x) {
        return x;
    }

    @OpCodeIdentifier(value=OpCode.F64_CONVERT_I32_U)
    public static double F64_CONVERT_I32_U(int x) {
        return Integer.toUnsignedLong(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_CONVERT_I64_S)
    public static double F64_CONVERT_I64_S(long x) {
        return x;
    }

    @OpCodeIdentifier(value=OpCode.F64_CONVERT_I64_U)
    public static double F64_CONVERT_I64_U(long tos) {
        if (tos >= 0L) {
            return tos;
        }
        long sum = tos + 1023L;
        long shiftIn = ((sum ^ tos) & Long.MIN_VALUE) >>> 10;
        return Math.scalb((double)(sum >>> 11 | shiftIn), 11);
    }

    @OpCodeIdentifier(value=OpCode.F64_COPYSIGN)
    public static double F64_COPYSIGN(double a, double b) {
        if (b == -1.8014398509481984E16) {
            return Math.copySign(a, -1.0);
        }
        if (b == 9.205357638345294E18) {
            return Math.copySign(a, 1.0);
        }
        return Math.copySign(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F64_EQ)
    public static int F64_EQ(double a, double b) {
        return a == b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F64_FLOOR)
    public static double F64_FLOOR(double x) {
        return Math.floor(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_GE)
    public static int F64_GE(double a, double b) {
        return a >= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F64_GT)
    public static int F64_GT(double a, double b) {
        return a > b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F64_LE)
    public static int F64_LE(double a, double b) {
        return a <= b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F64_LT)
    public static int F64_LT(double a, double b) {
        return a < b ? 1 : 0;
    }

    @OpCodeIdentifier(value=OpCode.F64_MAX)
    public static double F64_MAX(double a, double b) {
        return Math.max(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F64_MIN)
    public static double F64_MIN(double a, double b) {
        return Math.min(a, b);
    }

    @OpCodeIdentifier(value=OpCode.F64_NE)
    public static int F64_NE(double a, double b) {
        return a == b ? 0 : 1;
    }

    @OpCodeIdentifier(value=OpCode.F64_NEAREST)
    public static double F64_NEAREST(double x) {
        return Math.rint(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_REINTERPRET_I64)
    public static double F64_REINTERPRET_I64(long x) {
        return Double.longBitsToDouble(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_SQRT)
    public static double F64_SQRT(double x) {
        return Math.sqrt(x);
    }

    @OpCodeIdentifier(value=OpCode.F64_TRUNC)
    public static double F64_TRUNC(double x) {
        return x < 0.0 ? Math.ceil(x) : Math.floor(x);
    }

    public static int TABLE_GET(Instance instance, int tableIndex, int index) {
        TableInstance table = instance.table(tableIndex);
        if (index < 0 || (long)index >= table.limits().max() || index >= table.size()) {
            throw new WasmRuntimeException("out of bounds table access");
        }
        return table.ref(index);
    }

    public static void TABLE_FILL(Instance instance, int tableIndex, int size, int value, int offset) {
        int end = offset + size;
        TableInstance table = instance.table(tableIndex);
        if (size < 0 || end > table.size()) {
            throw new WasmRuntimeException("out of bounds table access");
        }
        for (int i = offset; i < end; ++i) {
            table.setRef(i, value, instance);
        }
    }

    public static void TABLE_COPY(Instance instance, int srcTableIndex, int dstTableIndex, int size, int s, int d) {
        TableInstance src = instance.table(srcTableIndex);
        TableInstance dest = instance.table(dstTableIndex);
        if (size < 0 || s < 0 || size + s > src.size() || d < 0 || size + d > dest.size()) {
            throw new WasmRuntimeException("out of bounds table access");
        }
        for (int i = size - 1; i >= 0; --i) {
            Instance inst;
            int val;
            if (d <= s) {
                val = src.ref(s++);
                inst = src.instance(d);
                dest.setRef(d++, val, inst);
                continue;
            }
            val = src.ref(s + i);
            inst = src.instance(d + i);
            dest.setRef(d + i, val, inst);
        }
    }

    public static void TABLE_INIT(Instance instance, int tableidx, int elementidx, int size, int elemidx, int offset) {
        boolean isOutOfBounds;
        int end = offset + size;
        TableInstance table = instance.table(tableidx);
        int elementCount = instance.elementCount();
        Element currentElement = instance.element(elementidx);
        int currentElementCount = currentElement instanceof PassiveElement ? currentElement.elementCount() : 0;
        boolean bl = isOutOfBounds = size < 0 || elementidx > elementCount || size > 0 && !(currentElement instanceof PassiveElement) || elemidx + size > currentElementCount || end > table.size();
        if (isOutOfBounds) {
            throw new WasmRuntimeException("out of bounds table access");
        }
        if (size == 0) {
            return;
        }
        for (int i = offset; i < end; ++i) {
            Element elem = instance.element(elementidx);
            int val = (int)ConstantEvaluators.computeConstantValue(instance, elem.initializers().get(elemidx++))[0];
            if (table.elementType().equals(ValType.FuncRef)) {
                if (val > instance.functionCount()) {
                    throw new WasmRuntimeException("out of bounds table access");
                }
                table.setRef(i, val, instance);
                continue;
            }
            assert (table.elementType().equals(ValType.ExternRef));
            table.setRef(i, val, instance);
        }
    }
}

