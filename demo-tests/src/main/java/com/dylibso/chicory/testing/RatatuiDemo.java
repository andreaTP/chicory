package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.Instance;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public final class RatatuiDemo {

    private final Instance instance;
    private final Terminal terminal;

    public RatatuiDemo() throws Exception {
        System.out.println("[DEBUG] Starting ratatui demo...");
        this.instance =
                Instance.builder(Ratatui.load()).withMachineFactory(Ratatui::create).build();
        System.out.println("[DEBUG] Instance created");

        // Initialize jline terminal for raw input
        this.terminal = TerminalBuilder.builder().system(true).build();
        terminal.enterRawMode();
    }

    private String unpackString(long packed) {
        var addr = (int) ((packed >>> 32) & 0xFFFFFFFFL);
        var len = (int) (packed & 0xFFFFFFFFL);

        var result = instance.memory().readString(addr, len);
        instance.exports().function("wasm_free").apply(addr, len);
        return result;
    }

    private void processKey(byte key) {
        // Handle the key
        long shouldQuit = instance.exports().function("handle_key").apply(key)[0];

        if (shouldQuit == 1) {
            return;
        }

        // Render updated state immediately
        long result = instance.exports().function("render").apply()[0];
        String output = unpackString(result);
        System.out.print(output);
        System.out.flush();
    }

    public void run() throws Exception {
        // Get terminal size
        int width = terminal.getWidth();
        int height = terminal.getHeight();
        System.out.println("[DEBUG] Terminal size: " + width + "x" + height);

        // Set terminal size in WASM module
        instance.exports().function("set_terminal_size").apply(width, height);

        // Initialize the application
        instance.exports().function("init").apply();

        // Initial render
        long result = instance.exports().function("render").apply()[0];
        String output = unpackString(result);
        System.out.print(output);
        System.out.flush();

        // Real-time input loop using jline - reads characters immediately
        try {
            var reader = terminal.reader();

            while (true) {
                int ch = reader.read();
                if (ch == -1) {
                    break;
                }

                // Handle ANSI escape sequences (arrow keys)
                if (ch == 27) { // ESC
                    int next = reader.read(100); // Read with timeout
                    if (next == '[') {
                        int code = reader.read(100);
                        // Map arrow keys to j/k
                        switch (code) {
                            case 'A': // Up arrow -> k
                                processKey((byte) 'k');
                                continue;
                            case 'B': // Down arrow -> j
                                processKey((byte) 'j');
                                continue;
                            default:
                                // Skip other escape sequences
                                continue;
                        }
                    }
                    continue;
                }

                // Skip control characters except Ctrl+C
                if (ch < 32 && ch != 3) { // 3 is Ctrl+C
                    if (ch == 3) { // Ctrl+C
                        break;
                    }
                    continue;
                }

                byte key = (byte) ch;
                processKey(key);

                // Check if we should quit
                if (key == 'q' || key == 'Q') {
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        } finally {
            // Restore terminal
            terminal.close();
        }

        // Restore cursor and clear
        System.out.print("\u001B[?25h\u001B[2J\u001B[H");
        System.out.println("Goodbye!");
    }

    public static void main(String[] args) {
        try {
            new RatatuiDemo().run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
