package com.dylibso.chicory.wabt;

import com.dylibso.chicory.corpus.WatGenerator;

public final class Main {

    private Main() {}

    public static void main(String... args) {
        System.out.println("Enter something to continue the execution");
        System.console().readLine();

        String wat = WatGenerator.bigWat(10, 15_000);
        Wat2Wasm.parse(wat);
    }
}
