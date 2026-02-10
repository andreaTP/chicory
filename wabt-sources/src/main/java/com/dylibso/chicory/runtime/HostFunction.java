/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.WasmFunctionHandle;
import com.dylibso.chicory.wasm.types.FunctionType;
import java.util.List;

public class HostFunction
extends ImportFunction {
    @Deprecated(since="1.3.0")
    public HostFunction(String moduleName, String symbolName, List paramTypes, List returnTypes, WasmFunctionHandle handle) {
        super(moduleName, symbolName, FunctionType.of(HostFunction.convert(paramTypes), HostFunction.convert(returnTypes)), handle);
    }

    public HostFunction(String moduleName, String symbolName, FunctionType type, WasmFunctionHandle handle) {
        super(moduleName, symbolName, type, handle);
    }
}

