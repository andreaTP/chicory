package com.dylibso.chicory.cranelift.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wabt.Wat2Wasm;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.wasm.types.ValType;
import org.junit.jupiter.api.Test;

public class CallTest {

    @Test
    public void shouldCallInternalFunction() {
        var wat =
                "(module"
                        + "  (func $add (param $a i32) (param $b i32) (result i32)"
                        + "    (i32.add (local.get $a) (local.get $b))"
                        + "  )"
                        + "  (func (export \"call_add\") (param $x i32) (param $y i32) (result i32)"
                        + "    (call $add (local.get $x) (local.get $y))"
                        + "  )"
                        + ")";

        var module = Parser.parse(Wat2Wasm.parse(wat));
        var instance =
                Instance.builder(module).withMachineFactory(NativeMachineFactory::compile).build();

        var callAdd = instance.export("call_add");
        long[] result = callAdd.apply(17, 25);
        assertEquals(42L, result[0]);
    }

    @Test
    public void shouldCallImportFunction() {
        var wat =
                "(module"
                        + "  (import \"env\" \"double\" (func $double (param i32) (result i32)))"
                        + "  (func (export \"call_double\") (param $x i32) (result i32)"
                        + "    (call $double (local.get $x))"
                        + "  )"
                        + ")";

        var module = Parser.parse(Wat2Wasm.parse(wat));
        var importFunc =
                new ImportFunction(
                        "env",
                        "double",
                        FunctionType.of(
                                java.util.List.of(ValType.I32), java.util.List.of(ValType.I32)),
                        (inst, args) -> new long[] {args[0] * 2});
        var instance =
                Instance.builder(module)
                        .withImportValues(ImportValues.builder().addFunction(importFunc).build())
                        .withMachineFactory(NativeMachineFactory::compile)
                        .build();

        var callDouble = instance.export("call_double");
        long[] result = callDouble.apply(21);
        assertEquals(42L, result[0]);
    }
}
