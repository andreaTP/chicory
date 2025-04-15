package com.dylibso.chicory.experimental.aot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wabt.Wat2Wasm;
import com.dylibso.chicory.wasm.Parser;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ClassTooLarge {

    @Test
    public void testFunc50k() throws IOException {
        var funcCount = 50_000;
        var instance =
                Instance.builder(Parser.parse(buildHugeWasm(funcCount, 0)))
                        .withMachineFactory(AotMachine::new)
                        .withStart(false)
                        .build();

        funcCount = 1000;
        var expected = 0;
        for (int i = 1; i <= funcCount; i++) {
            expected += i;
        }
        ExportFunction func1 = instance.export("func_" + funcCount);
        assertEquals(expected, func1.apply(0)[0]);
    }

    @Test
    public void testManyBigFuncs() throws IOException {
        var funcCount = 10;
        var instance =
                Instance.builder(Parser.parse(buildHugeWasm(funcCount, 15_000)))
                        .withMachineFactory(AotMachine::new)
                        .withStart(false)
                        .build();

        var expected = 0;
        for (int i = 1; i <= funcCount; i++) {
            expected += i;
        }
        ExportFunction func1 = instance.export("func_" + funcCount);
        assertEquals(expected, func1.apply(0)[0]);
    }

    public static final class Context {
        public final ArrayList<Integer> functions = new ArrayList<>();
        public final ArrayList<Integer> instructions = new ArrayList<>();
    }

    private byte[] buildHugeWasm(int funcCount, int funcSize) throws IOException {
        var ctx = new Context();
        for (int i = 0; i < funcCount; i++) {
            ctx.functions.add(i + 1);
        }
        for (int i = 0; i < funcSize; i++) {
            ctx.instructions.add(i + 1);
        }

        String TAB = "  ";
        String TAB2 = TAB + TAB;
        StringWriter out = new StringWriter();
        var pw = new PrintWriter(out);
        pw.println("(module");
        for (int i = 0; i < ctx.functions.size(); i++) {
            var func = ctx.functions.get(i);

            pw.printf(
                    TAB + "(func $func_%d (export \"func_%d\") (param i32) (result i32)",
                    func,
                    func);
            pw.println();
            pw.println(TAB2 + "local.get 0");
            pw.printf(TAB2 + "i32.const %d", func);
            pw.println();
            pw.println(TAB2 + "i32.add");
            pw.println();
            for (int j = 0; j < ctx.instructions.size(); j++) {
                pw.println(TAB2 + "i32.const 1");
                pw.println(TAB2 + "i32.add");
                pw.println(TAB2 + "i32.const 1");
                pw.println(TAB2 + "i32.sub");
            }
            if (func != 1) {
                pw.printf(TAB2 + "call $func_%d", func - 1);
                pw.println();
            }
            pw.println(TAB + ")");
        }
        pw.println(")");

        pw.flush();
        String wat = out.toString();

        return Wat2Wasm.parse(wat);
    }
}
