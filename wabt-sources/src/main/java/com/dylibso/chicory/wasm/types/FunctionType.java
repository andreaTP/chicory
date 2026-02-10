/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.types.ValType;
import java.util.List;
import java.util.Objects;

public final class FunctionType {
    private final List<ValType> params;
    private final List<ValType> returns;
    private final int hashCode;
    private static final FunctionType empty = new FunctionType(List.of(), List.of());

    private FunctionType(List<ValType> params, List<ValType> returns) {
        this.params = params;
        this.returns = returns;
        this.hashCode = Objects.hash(params, returns);
    }

    public List<ValType> params() {
        return this.params;
    }

    public List<ValType> returns() {
        return this.returns;
    }

    public boolean paramsMatch(FunctionType other) {
        return this.params.equals(other.params);
    }

    public boolean returnsMatch(FunctionType other) {
        return this.returns.equals(other.returns);
    }

    public boolean equals(Object obj) {
        return obj instanceof FunctionType && this.equals((FunctionType)obj);
    }

    public boolean equals(FunctionType other) {
        return this.hashCode == other.hashCode && this.paramsMatch(other) && this.returnsMatch(other);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public static FunctionType returning(ValType valType) {
        return new FunctionType(List.of(), List.of(valType));
    }

    public static FunctionType accepting(ValType valType) {
        return new FunctionType(List.of(valType), List.of());
    }

    public boolean typesMatch(FunctionType other) {
        return this.paramsMatch(other) && this.returnsMatch(other);
    }

    public static FunctionType of(List<ValType> params, List<ValType> returns) {
        if (params.isEmpty()) {
            if (returns.isEmpty()) {
                return empty;
            }
            if (returns.size() == 1) {
                return FunctionType.returning(returns.get(0));
            }
        } else if (returns.isEmpty() && params.size() == 1) {
            return FunctionType.accepting(params.get(0));
        }
        return new FunctionType(List.copyOf(params), List.copyOf(returns));
    }

    public static FunctionType of(ValType[] params, ValType[] returns) {
        return FunctionType.of(List.of(params), List.of(returns));
    }

    public static FunctionType empty() {
        return empty;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        int nParams = this.params.size();
        for (int i = 0; i < nParams; ++i) {
            builder.append(this.params.get(i).toString());
            if (i >= nParams - 1) continue;
            builder.append(',');
        }
        builder.append(") -> ");
        int nReturns = this.returns.size();
        if (nReturns == 0) {
            builder.append("nil");
        } else {
            builder.append('(');
            for (int i = 0; i < nReturns; ++i) {
                builder.append(this.returns.get(i).toString());
                if (i >= nReturns - 1) continue;
                builder.append(',');
            }
            builder.append(')');
        }
        return builder.toString();
    }
}

