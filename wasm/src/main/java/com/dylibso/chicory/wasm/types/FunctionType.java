package com.dylibso.chicory.wasm.types;

import java.util.List;
import java.util.Objects;

public final class FunctionType {
    private final List<ValType> params;
    private final List<ValType> returns;

    private FunctionType(List<ValType> params, List<ValType> returns) {
        this.params = params;
        this.returns = returns;
    }

    public List<ValType> params() {
        return params;
    }

    public List<ValType> returns() {
        return returns;
    }

    public boolean paramsMatch(FunctionType other) {
        return params.equals(other.params);
    }

    public boolean returnsMatch(FunctionType other) {
        return returns.equals(other.returns);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FunctionType && equals((FunctionType) obj);
    }

    public boolean equals(FunctionType other) {
        // For type equivalence, we need structural comparison even when hashCodes differ
        // (e.g., when ValTypes have different typeIdx but same structure)
        // So we do structural comparison first, not relying on hashCode
        return paramsMatch(other) && returnsMatch(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params, returns);
    }

    private static final FunctionType empty = new FunctionType(List.of(), List.of());

    public static FunctionType returning(ValType valType) {
        return new FunctionType(List.of(), List.of(valType));
    }

    public static FunctionType accepting(ValType valType) {
        return new FunctionType(List.of(valType), List.of());
    }

    public boolean typesMatch(FunctionType other) {
        return paramsMatch(other) && returnsMatch(other);
    }

    /**
     * Check if this function type matches another using ValType.matches() semantics.
     * This is used for link-time and runtime type matching per the WebAssembly spec.
     * For GC types, this uses canonical IDs for comparison.
     *
     * This method checks if 'other' (actual) matches 'this' (expected).
     * For params: actual must match expected (contravariant) - matches(actualParam, expectedParam)
     * For returns: expected must match actual (covariant) - matches(expectedReturn, actualReturn)
     */
    public boolean matches(FunctionType other) {
        if (this.params.size() != other.params.size()
                || this.returns.size() != other.returns.size()) {
            return false;
        }

        // Check params: actual must match expected (contravariant)
        // matches(actualParam, expectedParam) = matches(other.params[i], this.params[i])
        for (int i = 0; i < this.params.size(); i++) {
            if (!ValType.matches(other.params.get(i), this.params.get(i))) {
                return false;
            }
        }

        // Check returns: expected must match actual (covariant)
        // matches(expectedReturn, actualReturn) = matches(this.returns[i], other.returns[i])
        for (int i = 0; i < this.returns.size(); i++) {
            if (!ValType.matches(this.returns.get(i), other.returns.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static FunctionType of(List<ValType> params, List<ValType> returns) {
        if (params.isEmpty()) {
            if (returns.isEmpty()) {
                return empty;
            }
            if (returns.size() == 1) {
                return returning(returns.get(0));
            }
        } else if (returns.isEmpty()) {
            if (params.size() == 1) {
                return accepting(params.get(0));
            }
        }
        return new FunctionType(List.copyOf(params), List.copyOf(returns));
    }

    public static FunctionType of(ValType[] params, ValType[] returns) {
        return of(List.of(params), List.of(returns));
    }

    public static FunctionType empty() {
        return empty;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append('(');
        var nParams = this.params.size();
        for (var i = 0; i < nParams; i++) {
            builder.append(this.params.get(i).toString());
            if (i < nParams - 1) {
                builder.append(',');
            }
        }
        builder.append(") -> ");
        var nReturns = this.returns.size();
        if (nReturns == 0) {
            builder.append("nil");
        } else {
            builder.append('(');
            for (var i = 0; i < nReturns; i++) {
                builder.append(this.returns.get(i).toString());
                if (i < nReturns - 1) {
                    builder.append(',');
                }
            }
            builder.append(')');
        }
        return builder.toString();
    }
}
