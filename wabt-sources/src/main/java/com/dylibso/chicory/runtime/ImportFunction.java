/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportValue;
import com.dylibso.chicory.runtime.WasmFunctionHandle;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.ArrayList;
import java.util.List;

public class ImportFunction
implements ImportValue {
    private final String module;
    private final String name;
    private final List<ValType> paramTypes;
    private final List<ValType> returnTypes;
    private final WasmFunctionHandle handle;

    @Deprecated(since="1.3.0")
    protected static List<ValType> convert(List objs) {
        ArrayList<ValType> result = new ArrayList<ValType>(objs.size());
        for (Object v : objs) {
            if (v instanceof ValType) {
                result.add((ValType)v);
                continue;
            }
            if (v instanceof ValueType) {
                result.add(((ValueType)((Object)v)).toValType());
                continue;
            }
            throw new IllegalArgumentException("Expected ValueType or ValType, but got: " + v.getClass().getCanonicalName());
        }
        return result;
    }

    public ImportFunction(String module, String name, FunctionType type, WasmFunctionHandle handle) {
        this.module = module;
        this.name = name;
        this.paramTypes = type.params();
        this.returnTypes = type.returns();
        this.handle = handle;
    }

    @Deprecated(since="1.3.0")
    public ImportFunction(String module, String name, List paramTypes, List returnTypes, WasmFunctionHandle handle) {
        this.module = module;
        this.name = name;
        this.paramTypes = ImportFunction.convert(paramTypes);
        this.returnTypes = ImportFunction.convert(returnTypes);
        this.handle = handle;
    }

    public WasmFunctionHandle handle() {
        return this.handle;
    }

    @Override
    public String module() {
        return this.module;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ImportValue.Type type() {
        return ImportValue.Type.FUNCTION;
    }

    public List<ValType> paramTypes() {
        return this.paramTypes;
    }

    public List<ValType> returnTypes() {
        return this.returnTypes;
    }

    public FunctionType functionType() {
        return FunctionType.of(this.paramTypes, this.returnTypes);
    }
}

