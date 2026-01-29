package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.Instance;
import org.junit.jupiter.api.Test;

public final class DemoTest {

    private String unpackString(Instance instance, long packed) {
        var addr = (int) ((packed >>> 32) & 0xFFFFFFFFL);
        var len = (int) (packed & 0xFFFFFFFFL);
        return instance.memory().readString(addr, len);
    }

    @Test
    public void ratatuiExample() {
        var instance = Instance.builder(Ratatui.load()).withMachineFactory(Ratatui::create).build();

        var before = System.currentTimeMillis();
        long result = instance.exports().function("demo").apply()[0];
        var after = System.currentTimeMillis();

        System.out.println("elapsed: " + (after - before));

        var string = unpackString(instance, result);
        System.out.println(string);
    }
}
