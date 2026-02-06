public class UnsignedCompareBug {
    static volatile int sink;

    public static int f(long a, long b) {
        // mix signed range info
        if (a >= 0) sink++;
        if (b >= 0) sink++;

        return Long.compareUnsigned(a, b);
    }

    public static void main(String[] args) {
        long limit = -1L; // all-ones unsigned
        long count = 0;
        long x = 0;

        for (long i = 0; i < (limit & 0xFFFF_FFFFL) + 1 ; i++, count++) {
            int r = f(i, limit);
            // this should always be <0 until i wraps,
            // but buggy C2 may compile incorrectly
            if (r >= 0) {
                System.out.println("BUG at i=" + i + " r=" + r);
                return;
            }
        }
        System.out.println("Done " + count);
    }
}
