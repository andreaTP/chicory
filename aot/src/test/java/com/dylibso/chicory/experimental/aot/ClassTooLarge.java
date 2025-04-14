package com.dylibso.chicory.experimental.aot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wabt.Wat2Wasm;
import com.dylibso.chicory.wasm.Parser;
import org.junit.jupiter.api.Test;

public class ClassTooLarge {

    @Test
    public void testFunc50k() {
        var instance =
                Instance.builder(Parser.parse(buildHugeWasm(50_000)))
                        .withMachineFactory(AotMachine::new)
                        .withStart(false)
                        .build();

        ExportFunction func1 = instance.export("func_1");
        assertEquals(42, func1.apply(50_045)[0]);
    }

    @SuppressWarnings("StringConcatToTextBlock")
    private byte[] buildHugeWasm(int funcCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("(module");
        sb.append("\n");
        String function1 =
                "(func $func_1 (param i32) (result i32)\n"
                        + "    local.get 0\n"
                        + "    i32.const 1\n"
                        + "    i32.sub\n"
                        + "    call $func_"
                        + funcCount
                        + "\n"
                        + ")";
        sb.append(function1);
        sb.append("\n");
        String function =
                "(func $func_%d (param i32) (result i32)\n"
                        + "    local.get 0\n"
                        + "    i32.const %d\n"
                        + "    i32.sub\n"
                        + ")";
        for (int i = 2; i < funcCount; i++) {
            sb.append(String.format(function, i, i));
            sb.append("\n");
        }
        String functionEnd =
                "(func $func_"
                        + funcCount
                        + " (param i32) (result i32)\n"
                        + "    local.get 0\n"
                        + "    i32.const "
                        + funcCount
                        + "\n"
                        + "    i32.sub\n"
                        + "    call $func_2\n"
                        + ")";
        sb.append(functionEnd);
        sb.append("\n");

        for (int i = 1; i <= funcCount; i++) {
            sb.append(String.format("(export \"func_%d\" (func $func_%d))", i, i));
        }
        sb.append(")");

        return Wat2Wasm.parse(sb.toString());
    }
}
