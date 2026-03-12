import com.dylibso.chicory.corpus.WatGenerator;
import com.dylibso.chicory.wabt.Wat2Wasm;

public class ReduceTest {
    public static void main(String[] args) {
        // Test different function counts
        int[] counts = {50000, 25000, 10000, 5000, 1000, 500, 100, 50, 10, 5, 1};
        
        for (int count : counts) {
            System.out.print("Testing " + count + " functions... ");
            try {
                String wat = WatGenerator.bigWat(count, 0);
                System.out.print("(size: " + wat.length() + " chars) ");
                byte[] result = Wat2Wasm.parse(wat);
                System.out.println("✓ OK");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("out of bounds")) {
                    System.out.println("✗ BUG REPRODUCED!");
                    System.out.println("  Minimal count: " + count);
                    break;
                } else {
                    System.out.println("✗ Error: " + e.getClass().getSimpleName());
                }
            }
        }
    }
}
