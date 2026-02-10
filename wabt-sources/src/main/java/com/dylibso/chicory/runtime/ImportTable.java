/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ImportValue;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.Map;

public class ImportTable
implements ImportValue {
    private final String module;
    private final String name;
    private final TableInstance table;

    public ImportTable(String module, String name, TableInstance table) {
        this.module = module;
        this.name = name;
        this.table = table;
    }

    public ImportTable(String module, String name, Map<Integer, Integer> funcRefs) {
        this.module = module;
        this.name = name;
        long maxFuncRef = 0L;
        for (Integer k : funcRefs.keySet()) {
            if ((long)k.intValue() <= maxFuncRef) continue;
            maxFuncRef = k.intValue();
        }
        this.table = new TableInstance(new Table(ValType.FuncRef, new TableLimits(maxFuncRef, maxFuncRef)), -1);
        this.table.reset();
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
        return ImportValue.Type.TABLE;
    }

    public TableInstance table() {
        return this.table;
    }
}

