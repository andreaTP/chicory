/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.types.FunctionType;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class ValType {
    private static final int NULL_TYPEIDX = 0;
    private static final long OPCODE_MASK = 0xFFFFFFFFL;
    private static final long TYPEIDX_SHIFT = 32L;
    public static ValType BOT = new ValType(-1);
    public static ValType F64 = new ValType(124);
    public static ValType F32 = new ValType(125);
    public static ValType I64 = new ValType(126);
    public static ValType I32 = new ValType(127);
    public static ValType V128 = new ValType(123);
    public static ValType FuncRef = new ValType(112);
    public static ValType ExnRef = new ValType(105);
    public static ValType ExternRef = new ValType(111);
    public static ValType RefBot = new ValType(100, TypeIdxCode.BOT.code());
    private final long id;
    private final FunctionType resolvedFunctionType;

    private ValType(int opcode) {
        this(opcode, 0, null);
    }

    private ValType(int opcode, int typeIdx) {
        this(opcode, typeIdx, null);
    }

    private ValType(int opcode, int typeIdx, FunctionType resolvedFunctionType) {
        if (opcode == 112) {
            typeIdx = TypeIdxCode.FUNC.code();
            opcode = 99;
        } else if (opcode == 111) {
            typeIdx = TypeIdxCode.EXTERN.code();
            opcode = 99;
        } else if (opcode == 105) {
            typeIdx = TypeIdxCode.EXN.code();
            opcode = 99;
        } else if ((opcode == 99 || opcode == 100) && typeIdx >= 0) {
            Objects.requireNonNull(resolvedFunctionType);
        }
        this.resolvedFunctionType = resolvedFunctionType;
        this.id = ValType.createId(opcode, typeIdx);
    }

    private static long createId(int opcode, int typeIdx) {
        return (long)typeIdx << 32 | (long)opcode & 0xFFFFFFFFL;
    }

    public long id() {
        return this.id;
    }

    private static int opcode(long id) {
        return (int)(id & 0xFFFFFFFFL);
    }

    public int opcode() {
        return ValType.opcode(this.id);
    }

    private static int typeIdx(long id) {
        return (int)(id >>> 32);
    }

    public int typeIdx() {
        return ValType.typeIdx(this.id);
    }

    public int size() {
        switch (this.opcode()) {
            case 124: 
            case 126: {
                return 8;
            }
            case 125: 
            case 127: {
                return 4;
            }
            case 123: {
                return 16;
            }
        }
        throw new IllegalStateException("Type does not have size");
    }

    public boolean isNumeric() {
        switch (this.opcode()) {
            case 124: 
            case 125: 
            case 126: 
            case 127: {
                return true;
            }
        }
        return false;
    }

    public boolean isInteger() {
        switch (this.opcode()) {
            case 126: 
            case 127: {
                return true;
            }
        }
        return false;
    }

    public boolean isFloatingPoint() {
        switch (this.opcode()) {
            case 124: 
            case 125: {
                return true;
            }
        }
        return false;
    }

    private static boolean isReference(int opcode) {
        switch (opcode) {
            case 99: 
            case 100: 
            case 105: {
                return true;
            }
        }
        return false;
    }

    public boolean isReference() {
        return ValType.isReference(this.opcode());
    }

    private static boolean isValidOpcode(int opcode) {
        switch (opcode) {
            case 99: 
            case 100: 
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

    public static boolean isValid(long id) {
        return ValType.isValidOpcode(ValType.opcode(id));
    }

    public static int sizeOf(List<ValType> args) {
        int total = 0;
        for (ValType a : args) {
            if (a.opcode() == 123) {
                total += 2;
                continue;
            }
            ++total;
        }
        return total;
    }

    private static boolean matchesNull(boolean null1, boolean null2) {
        return null1 == null2 || null2;
    }

    public static boolean matchesRef(ValType t1, ValType t2) {
        boolean matchesNull = ValType.matchesNull(t1.isNullable(), t2.isNullable());
        if (!matchesNull) {
            return false;
        }
        if (t1.typeIdx() >= 0 && t2.typeIdx() == TypeIdxCode.FUNC.code()) {
            return true;
        }
        if (t1.typeIdx() >= 0 && t2.typeIdx() >= 0) {
            return t1.resolvedFunctionType.equals(t2.resolvedFunctionType);
        }
        if (t1.typeIdx() == TypeIdxCode.BOT.code()) {
            return true;
        }
        return t1.typeIdx() == t2.typeIdx();
    }

    public static boolean matches(ValType t1, ValType t2) {
        if (t1.isReference() && t2.isReference()) {
            return ValType.matchesRef(t1, t2);
        }
        if (t1.opcode() == -1) {
            return true;
        }
        return t1.id() == t2.id();
    }

    public boolean isNullable() {
        switch (this.opcode()) {
            case 100: {
                return false;
            }
            case 99: {
                return true;
            }
        }
        throw new IllegalArgumentException("got non-reference type to isNullable(): " + String.valueOf(this));
    }

    public int hashCode() {
        if (this.resolvedFunctionType != null) {
            return this.resolvedFunctionType.hashCode();
        }
        return Long.hashCode(this.id);
    }

    public boolean equals(Object other) {
        if (!(other instanceof ValType)) {
            return false;
        }
        ValType that = (ValType)other;
        if (this.resolvedFunctionType != null && that.resolvedFunctionType != null) {
            return ValType.opcode(this.id) == ValType.opcode(that.id) && this.resolvedFunctionType.equals(that.resolvedFunctionType);
        }
        if (this.resolvedFunctionType == null && that.resolvedFunctionType == null) {
            return this.id == that.id;
        }
        return false;
    }

    public String toString() {
        switch (this.opcode()) {
            case 99: 
            case 100: {
                return ID.toName(this.opcode()) + "[" + this.typeIdx() + "]";
            }
        }
        return ID.toName(this.opcode());
    }

    public String name() {
        return ID.toName(this.opcode());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class ID {
        public static final int BOT = -1;
        public static final int RefNull = 99;
        public static final int Ref = 100;
        public static final int ExternRef = 111;
        public static final int ExnRef = 105;
        public static final int FuncRef = 112;
        public static final int V128 = 123;
        public static final int F64 = 124;
        public static final int F32 = 125;
        public static final int I64 = 126;
        public static final int I32 = 127;

        private ID() {
        }

        public static String toName(int opcode) {
            switch (opcode) {
                case -1: {
                    return "Bot";
                }
                case 99: {
                    return "RefNull";
                }
                case 100: {
                    return "Ref";
                }
                case 105: {
                    return "ExnRef";
                }
                case 123: {
                    return "V128";
                }
                case 124: {
                    return "F64";
                }
                case 125: {
                    return "F32";
                }
                case 126: {
                    return "I64";
                }
                case 127: {
                    return "I32";
                }
            }
            throw new IllegalArgumentException("got invalid opcode in ValType.toName: " + opcode);
        }

        public static boolean isValidOpcode(int opcode) {
            return opcode == 99 || opcode == 100 || opcode == 111 || opcode == 112 || opcode == 105 || opcode == 123 || opcode == 124 || opcode == 125 || opcode == 126 || opcode == 127;
        }
    }

    public static enum TypeIdxCode {
        EXTERN(-17),
        FUNC(-16),
        EXN(-23),
        BOT(-1);

        private final int code;

        private TypeIdxCode(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }
    }

    public static final class Builder {
        private int opcode;
        private int typeIdx = 0;

        private Builder() {
        }

        public Builder withOpcode(int opcode) {
            this.opcode = opcode;
            return this;
        }

        public Builder withTypeIdx(int typeIdx) {
            this.typeIdx = typeIdx;
            return this;
        }

        public Builder fromId(long id) {
            this.opcode = ValType.opcode(id);
            this.typeIdx = ValType.typeIdx(id);
            return this;
        }

        public long id() {
            return ValType.createId(this.opcode, this.typeIdx);
        }

        public int typeIdx() {
            return this.typeIdx;
        }

        public boolean isReference() {
            return ValType.isReference(this.opcode);
        }

        public ValType build() {
            return this.build(i -> {
                throw new ChicoryException("build with empty context tried resolving " + i);
            });
        }

        public ValType build(Function<Integer, FunctionType> context) {
            if (!ValType.isValidOpcode(this.opcode)) {
                throw new ChicoryException("Invalid type opcode: " + this.opcode);
            }
            FunctionType resolvedFunctionType = this.substitute(this.opcode, this.typeIdx, context);
            return new ValType(this.opcode, this.typeIdx, resolvedFunctionType);
        }

        public FunctionType substitute(int opcode, int typeIdx, Function<Integer, FunctionType> context) {
            if (ValType.isReference(opcode) && typeIdx >= 0) {
                try {
                    return context.apply(typeIdx);
                }
                catch (IndexOutOfBoundsException e) {
                    throw new InvalidException("unknown type: " + typeIdx);
                }
            }
            return null;
        }
    }
}

