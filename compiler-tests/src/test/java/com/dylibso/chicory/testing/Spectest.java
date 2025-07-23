package com.dylibso.chicory.testing;

import static com.dylibso.chicory.wasm.types.Value.REF_NULL_VALUE;

import com.dylibso.chicory.runtime.ByteBufferMemory;
import com.dylibso.chicory.runtime.GlobalInstance;
import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.ImportGlobal;
import com.dylibso.chicory.runtime.ImportMemory;
import com.dylibso.chicory.runtime.ImportTable;
import com.dylibso.chicory.runtime.ImportTag;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.runtime.TagInstance;
import com.dylibso.chicory.runtime.WasmFunctionHandle;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.TagType;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.util.List;

// https://github.com/WebAssembly/spec/blob/ee82c8e50c5106e0cedada0a083d4cc4129034a2/interpreter/host/spectest.ml
public final class Spectest {
    private static final WasmFunctionHandle noop = (Instance instance, long... args) -> null;

    private Spectest() {}

    public static ImportValues toImportValues() {
        return ImportValues.builder()
                .addFunction(new HostFunction("spectest", "print", FunctionType.empty(), noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i32",
                                FunctionType.of(List.of(ValType.I32), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i32_1",
                                FunctionType.of(List.of(ValType.I32), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i32_2",
                                FunctionType.of(List.of(ValType.I32), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_f32",
                                FunctionType.of(List.of(ValType.F32), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i32_f32",
                                FunctionType.of(List.of(ValType.I32, ValType.F32), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i64",
                                FunctionType.of(List.of(ValType.I64), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i64_1",
                                FunctionType.of(List.of(ValType.I64), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_i64_2",
                                FunctionType.of(List.of(ValType.I64), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_f64",
                                FunctionType.of(List.of(ValType.F64), List.of()),
                                noop))
                .addFunction(
                        new HostFunction(
                                "spectest",
                                "print_f64_f64",
                                FunctionType.of(List.of(ValType.F64, ValType.F64), List.of()),
                                noop))
                .addGlobal(
                        new ImportGlobal(
                                "spectest", "global_i32", new GlobalInstance(Value.i32(666))))
                .addGlobal(
                        new ImportGlobal(
                                "spectest", "global_i64", new GlobalInstance(Value.i64(666))))
                .addGlobal(
                        new ImportGlobal(
                                "spectest",
                                "global_f32",
                                new GlobalInstance(Value.fromFloat(666.6f))))
                .addGlobal(
                        new ImportGlobal(
                                "spectest",
                                "global_f64",
                                new GlobalInstance(Value.fromDouble(666.6))))
                .addMemory(
                        new ImportMemory(
                                "spectest", "memory", new ByteBufferMemory(new MemoryLimits(1, 2))))
                .addMemory(
                        new ImportMemory(
                                "spectest",
                                "shared_memory",
                                new ByteBufferMemory(new MemoryLimits(1, 2, true))))
                .addTable(
                        new ImportTable(
                                "spectest",
                                "table",
                                new TableInstance(
                                        new Table(ValType.FuncRef, new TableLimits(10, 20)),
                                        REF_NULL_VALUE)))
//                .addFunction(
//                        new HostFunction("test", "func", FunctionType.of(List.of(), List.of()), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func-i32", FunctionType.of(List.of(ValType.I32), List.of()), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func-f32", FunctionType.of(List.of(ValType.F32), List.of()), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func->i32", FunctionType.of(List.of(), List.of(ValType.I32)), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func->f32", FunctionType.of(List.of(), List.of(ValType.F32)), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func-i32->i32", FunctionType.of(List.of(ValType.I32), List.of(ValType.I32)), noop)
//                )
//                .addFunction(
//                        new HostFunction("test", "func-i64->i64", FunctionType.of(List.of(ValType.I64), List.of(ValType.I64)), noop)
//                )
//                .addGlobal(
//                        new ImportGlobal(
//                                "test", "global-i32", new GlobalInstance(Value.i32(666))))
//                .addGlobal(
//                        new ImportGlobal(
//                                "test", "global-f32", new GlobalInstance(Value.f32(666))))
//                .addGlobal(
//                        new ImportGlobal(
//                                "test", "global-mut-i64", new GlobalInstance(Value.i64(666), MutabilityType.Var)))
//                .addTable(
//                        new ImportTable(
//                                "test",
//                                "table-10-inf",
//                                new TableInstance(
//                                        new Table(ValType.FuncRef, new TableLimits(10)), REF_NULL_VALUE)))
//                .addTable(
//                        new ImportTable(
//                                "test",
//                                "table-10-20",
//                                new TableInstance(
//                                        new Table(ValType.FuncRef, new TableLimits(10, 20)), REF_NULL_VALUE)))
//                .addMemory(
//                        new ImportMemory(
//                                "test", "memory-2-inf", new ByteBufferMemory(new MemoryLimits(2))))
//                .addMemory(
//                        new ImportMemory(
//                                "test", "memory-2-4", new ByteBufferMemory(new MemoryLimits(2, 4))))
//                // TODO: review the typeIdx of those tags - how are they intended to be used?
//                .addTag(
//                        new ImportTag("test", "tag", new TagInstance(new TagType((byte) 0, 0), FunctionType.of(List.of(), List.of())))
//                )
//                .addTag(
//                        new ImportTag("test", "tag-i32", new TagInstance(new TagType((byte) 0, 0), FunctionType.of(List.of(ValType.I32), List.of())))
//                )
//                .addTag(
//                        new ImportTag("test", "tag-f32", new TagInstance(new TagType((byte) 0, 0), FunctionType.of(List.of(ValType.F32), List.of())))
//                )
                .build();
    }
}
