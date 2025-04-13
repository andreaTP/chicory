package com.dylibso.chicory.experimental.aot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wabt.Wat2Wasm;
import com.dylibso.chicory.wasm.Parser;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

        public List<Integer> getFunctions() {
            return functions;
        }

        public List<Integer> getInstructions() {
            return instructions;
        }
    }

    @SuppressWarnings("StringConcatToTextBlock")
    private byte[] buildHugeWasm(int funcCount, int funcSize) throws IOException {
        var handlebars = new Handlebars();
        handlebars.registerHelpers(ConditionalHelpers.class);
        handlebars.registerHelper(
                "minus",
                (value, options) -> {
                    var a = (Integer) value;
                    var b = (Integer) options.param(0, null);
                    return a - b;
                });
        var ctx = new Context();
        for (int i = 0; i < funcCount; i++) {
            ctx.functions.add(i + 1);
        }
        for (int i = 0; i < funcSize; i++) {
            ctx.instructions.add(i + 1);
        }

        var template = handlebars.compileInline(stringResource("class-too-large.wat"));
        String wat = template.apply(ctx);
        //        System.out.println(wat);
        return Wat2Wasm.parse(wat);
    }

    private static String stringResource(String resource) throws IOException {
        try (InputStream is = ClassTooLarge.class.getResourceAsStream(resource)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
