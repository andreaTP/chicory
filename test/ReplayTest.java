public class ReplayTest {
    public static int sink;

    // target method to capture
    public static void hotMethod(int iters) {
        int x = 0;
        for (int i = 0; i < iters; i++) {
            x = x + i;
        }
        sink = x;
    }

    public static void main(String[] args) {
        // call the method many times so it's hot
        for (int i = 0; i < 10_000; i++) {
            hotMethod(1000);
        }
        System.out.println(sink);
    }
}
