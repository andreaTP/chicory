package com.dylibso.chicory.wabt;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        String wat = generateBigWat(500_000);
        // (1500);
        //        try {
        Wat2Wasm.parse(wat);
        //        } catch (Exception e) {
        //            // exit
        //            e.printStackTrace();
        //        }
    }

    private static String generateBigWat(int funcCount) {
        StringBuilder wat = new StringBuilder();

        wat.append("(module\n");
        for (int func = 1; func <= funcCount; func++) {
            wat.append("  (func $func_")
                    .append(func)
                    .append(" (export \"func_")
                    .append(func)
                    .append("\") (param i32) (result i32)\n");
            wat.append("    local.get 0\n");
            wat.append("    i32.const ").append(func).append('\n');
            wat.append("    i32.add\n");

            if (func != 1) {
                wat.append("    call $func_").append(func - 1).append('\n');
            }

            wat.append("  )\n");
        }
        wat.append(")\n");

        return wat.toString();
    }
}
