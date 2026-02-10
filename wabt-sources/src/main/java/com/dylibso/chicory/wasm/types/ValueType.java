/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.MalformedException;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;

@Deprecated(since="1.3.0")
public enum ValueType {
    UNKNOWN(-1),
    F64(124),
    F32(125),
    I64(126),
    I32(127),
    V128(123),
    FuncRef(112),
    ExnRef(105),
    ExternRef(111);

    private final int id;

    private ValueType(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }

    public int size() {
        switch (this) {
            case F64: 
            case I64: {
                return 8;
            }
            case F32: 
            case I32: {
                return 4;
            }
            case V128: {
                return 16;
            }
        }
        throw new IllegalStateException("Type does not have size");
    }

    public boolean isNumeric() {
        switch (this) {
            case F64: 
            case I64: 
            case F32: 
            case I32: {
                return true;
            }
        }
        return false;
    }

    public boolean isInteger() {
        switch (this) {
            case I64: 
            case I32: {
                return true;
            }
        }
        return false;
    }

    public boolean isFloatingPoint() {
        switch (this) {
            case F64: 
            case F32: {
                return true;
            }
        }
        return false;
    }

    public boolean isReference() {
        switch (this) {
            case FuncRef: 
            case ExnRef: 
            case ExternRef: {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(int typeId) {
        switch (typeId) {
            case 105: 
            case 111: 
            case 112: 
            case 123: 
            case 124: 
            case 125: 
            case 126: 
            case 127: {
                return true;
            }
        }
        return false;
    }

    public static ValueType forId(int id) {
        switch (id) {
            case 124: {
                return F64;
            }
            case 125: {
                return F32;
            }
            case 126: {
                return I64;
            }
            case 127: {
                return I32;
            }
            case 123: {
                return V128;
            }
            case 112: {
                return FuncRef;
            }
            case 105: {
                return ExnRef;
            }
            case 111: {
                return ExternRef;
            }
        }
        throw new IllegalArgumentException("Invalid value type " + id);
    }

    public ValType toValType() {
        switch (this.id) {
            case 124: {
                return ValType.F64;
            }
            case 125: {
                return ValType.F32;
            }
            case 126: {
                return ValType.I64;
            }
            case 127: {
                return ValType.I32;
            }
            case 123: {
                return ValType.V128;
            }
            case 112: {
                return ValType.FuncRef;
            }
            case 105: {
                return ValType.ExnRef;
            }
            case 111: {
                return ValType.ExternRef;
            }
        }
        throw new IllegalArgumentException("Invalid value type " + this.id);
    }

    public static ValueType refTypeForId(int id) {
        switch (id) {
            case 112: {
                return FuncRef;
            }
            case 111: {
                return ExternRef;
            }
            case 105: {
                return ExnRef;
            }
        }
        throw new MalformedException("malformed reference type " + id);
    }

    public static int sizeOf(List<ValueType> args) {
        int total = 0;
        for (ValueType a : args) {
            if (a == V128) {
                total += 2;
                continue;
            }
            ++total;
        }
        return total;
    }

    static final class ID {
        static final int ExternRef = 111;
        static final int ExnRef = 105;
        static final int FuncRef = 112;
        static final int V128 = 123;
        static final int F64 = 124;
        static final int F32 = 125;
        static final int I64 = 126;
        static final int I32 = 127;

        private ID() {
        }
    }
}

