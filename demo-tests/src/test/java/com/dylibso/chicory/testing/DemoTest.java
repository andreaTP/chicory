package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.Instance;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public final class DemoTest {

    private String unpackString(Instance instance, long packed) {
        var addr = (int) ((packed >>> 32) & 0xFFFFFFFFL);
        var len = (int) (packed & 0xFFFFFFFFL);

        var result = instance.memory().readString(addr, len);
        instance.exports().function("wasm_free").apply(addr, len);
        return result;
    }

    @Test
    public void ratatuiExample() {
        System.out.println("[DEBUG] Starting ratatui demo...");

        var instance = Instance.builder(Ratatui.load()).withMachineFactory(Ratatui::create).build();
        System.out.println("[DEBUG] Instance created");

        // Initialize the application
        instance.exports().function("init").apply();
        System.out.println("[DEBUG] Application initialized");

        // Initial render
        System.out.println("[DEBUG] Performing initial render...");
        long result = instance.exports().function("render").apply()[0];
        var output = unpackString(instance, result);
        System.out.print(output);
        System.out.println("[DEBUG] Initial render complete");

        // Interactive loop
        try (var scanner = new Scanner(System.in)) {
            System.out.println("[DEBUG] Entering interactive loop. Press 'q' to quit.");

            while (true) {
                System.out.print("[DEBUG] Waiting for input... ");
                System.out.flush();

                String input = scanner.nextLine();
                System.out.println(
                        "[DEBUG] Received input: '" + input + "' (length: " + input.length() + ")");

                if (input.isEmpty()) {
                    System.out.println("[DEBUG] Empty input, continuing...");
                    continue;
                }

                byte key = (byte) input.charAt(0);
                System.out.println(
                        "[DEBUG] Processing key: '" + (char) key + "' (byte value: " + key + ")");

                // Handle the key
                long shouldQuit = instance.exports().function("handle_key").apply(key)[0];
                System.out.println("[DEBUG] handle_key returned: " + shouldQuit);

                if (shouldQuit == 1) {
                    System.out.println("[DEBUG] Quit signal received, exiting loop");
                    break;
                }

                // Render updated state
                System.out.println("[DEBUG] Rendering updated state...");
                result = instance.exports().function("render").apply()[0];
                output = unpackString(instance, result);
                System.out.print(output);
                System.out.println("[DEBUG] Render complete");
            }
        }

        System.out.println("\n[DEBUG] Goodbye!");
    }
}
