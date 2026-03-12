/**
 * Runs all operations from TestUnsignedComparison.java
 * and prints the results.
 * 
 * Based on: https://github.com/openjdk/jdk/commit/f3eb5014aa75af4463308f52f2bc6e9fcd2da36c
 */
public class TestUnsignedComparisonRunner {
    private static final int INT_MIN = Integer.MIN_VALUE;
    private static final long LONG_MIN = Long.MIN_VALUE;

    // Integers are sorted in unsignedly increasing order
    private static final int[] INT_DATA = {
        0,
        1,
        2,
        3,
        0x8000_0000,
        0x8000_0001,
        0x8000_0002,
        0x8000_0003,
        0xFFFF_FFFE,
        0xFFFF_FFFF,
    };

    // Longs are sorted in unsignedly increasing order
    private static final long[] LONG_DATA = {
        0L,
        1L,
        2L,
        3L,
        0x00000000_80000000L,
        0x00000000_FFFFFFFFL,
        0x00000001_00000000L,
        0x80000000_00000000L,
        0x80000000_00000001L,
        0x80000000_00000002L,
        0x80000000_00000003L,
        0x80000000_80000000L,
        0xFFFFFFFF_FFFFFFFEL,
        0xFFFFFFFF_FFFFFFFFL,
    };

    // Constants to compare against, add MIN_VALUE beforehand for convenience
    private static final int CONST_INDEX = 6;
    private static final int INT_CONST = INT_DATA[CONST_INDEX] + INT_MIN;
    private static final long LONG_CONST = LONG_DATA[CONST_INDEX] + LONG_MIN;

    // Integer variable comparison operations
    public static boolean testIntVarEQ(int x, int y) {
        return x + INT_MIN == y + INT_MIN;
    }

    public static boolean testIntVarNE(int x, int y) {
        return x + INT_MIN != y + INT_MIN;
    }

    public static boolean testIntVarLT(int x, int y) {
        return x + INT_MIN < y + INT_MIN;
    }

    public static boolean testIntVarLE(int x, int y) {
        return x + INT_MIN <= y + INT_MIN;
    }

    public static boolean testIntVarGT(int x, int y) {
        return x + INT_MIN > y + INT_MIN;
    }

    public static boolean testIntVarGE(int x, int y) {
        return x + INT_MIN >= y + INT_MIN;
    }

    // Integer constant comparison operations
    public static boolean testIntConEQ(int x) {
        return x + INT_MIN == INT_CONST;
    }

    public static boolean testIntConNE(int x) {
        return x + INT_MIN != INT_CONST;
    }

    public static boolean testIntConLT(int x) {
        return x + INT_MIN < INT_CONST;
    }

    public static boolean testIntConLE(int x) {
        return x + INT_MIN <= INT_CONST;
    }

    public static boolean testIntConGT(int x) {
        return x + INT_MIN > INT_CONST;
    }

    public static boolean testIntConGE(int x) {
        return x + INT_MIN >= INT_CONST;
    }

    // Long variable comparison operations
    public static boolean testLongVarEQ(long x, long y) {
        return x + LONG_MIN == y + LONG_MIN;
    }

    public static boolean testLongVarNE(long x, long y) {
        return x + LONG_MIN != y + LONG_MIN;
    }

    public static boolean testLongVarLT(long x, long y) {
        return x + LONG_MIN < y + LONG_MIN;
    }

    public static boolean testLongVarLE(long x, long y) {
        return x + LONG_MIN <= y + LONG_MIN;
    }

    public static boolean testLongVarGT(long x, long y) {
        return x + LONG_MIN > y + LONG_MIN;
    }

    public static boolean testLongVarGE(long x, long y) {
        return x + LONG_MIN >= y + LONG_MIN;
    }

    // Long constant comparison operations
    public static boolean testLongConEQ(long x) {
        return x + LONG_MIN == LONG_CONST;
    }

    public static boolean testLongConNE(long x) {
        return x + LONG_MIN != LONG_CONST;
    }

    public static boolean testLongConLT(long x) {
        return x + LONG_MIN < LONG_CONST;
    }

    public static boolean testLongConLE(long x) {
        return x + LONG_MIN <= LONG_CONST;
    }

    public static boolean testLongConGT(long x) {
        return x + LONG_MIN > LONG_CONST;
    }

    public static boolean testLongConGE(long x) {
        return x + LONG_MIN >= LONG_CONST;
    }

    public static void main(String[] args) {
        System.out.println("=== TestUnsignedComparison Operations ===");
        System.out.println();

        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 100_000_000; b++) {
                for (int i = 0; i < INT_DATA.length; i++) {
                    for (int j = 0; j < INT_DATA.length; j++) {
                        int x = INT_DATA[i];
                        int y = INT_DATA[j];

                        boolean resultEQ = testIntVarEQ(x, y);
                        boolean resultNE = testIntVarNE(x, y);
                        boolean resultLT = testIntVarLT(x, y);
                        boolean resultLE = testIntVarLE(x, y);
                        boolean resultGT = testIntVarGT(x, y);
                        boolean resultGE = testIntVarGE(x, y);
                    }
                }
            }
        }

        // Test Integer Variable Comparisons
        System.out.println("--- Integer Variable Comparisons (x + INT_MIN <op> y + INT_MIN) ---");
        for (int i = 0; i < INT_DATA.length; i++) {
            for (int j = 0; j < INT_DATA.length; j++) {
                int x = INT_DATA[i];
                int y = INT_DATA[j];
                boolean expectedEQ = (i == j);
                boolean expectedNE = (i != j);
                boolean expectedLT = (i < j);
                boolean expectedLE = (i <= j);
                boolean expectedGT = (i > j);
                boolean expectedGE = (i >= j);

                boolean resultEQ = testIntVarEQ(x, y);
                boolean resultNE = testIntVarNE(x, y);
                boolean resultLT = testIntVarLT(x, y);
                boolean resultLE = testIntVarLE(x, y);
                boolean resultGT = testIntVarGT(x, y);
                boolean resultGE = testIntVarGE(x, y);

                System.out.printf("x=0x%08X (idx=%d), y=0x%08X (idx=%d):%n", x, i, y, j);
                System.out.printf("  EQ: %5s (expected %5s) %s%n", resultEQ, expectedEQ, resultEQ == expectedEQ ? "✓" : "✗");
                System.out.printf("  NE: %5s (expected %5s) %s%n", resultNE, expectedNE, resultNE == expectedNE ? "✓" : "✗");
                System.out.printf("  LT: %5s (expected %5s) %s%n", resultLT, expectedLT, resultLT == expectedLT ? "✓" : "✗");
                System.out.printf("  LE: %5s (expected %5s) %s%n", resultLE, expectedLE, resultLE == expectedLE ? "✓" : "✗");
                System.out.printf("  GT: %5s (expected %5s) %s%n", resultGT, expectedGT, resultGT == expectedGT ? "✓" : "✗");
                System.out.printf("  GE: %5s (expected %5s) %s%n", resultGE, expectedGE, resultGE == expectedGE ? "✓" : "✗");
                System.out.println();
            }
        }

        // Test Integer Constant Comparisons
        System.out.println("--- Integer Constant Comparisons (x + INT_MIN <op> INT_CONST) ---");
        for (int i = 0; i < INT_DATA.length; i++) {
            int x = INT_DATA[i];
            boolean expectedEQ = (i == CONST_INDEX);
            boolean expectedNE = (i != CONST_INDEX);
            boolean expectedLT = (i < CONST_INDEX);
            boolean expectedLE = (i <= CONST_INDEX);
            boolean expectedGT = (i > CONST_INDEX);
            boolean expectedGE = (i >= CONST_INDEX);

            boolean resultEQ = testIntConEQ(x);
            boolean resultNE = testIntConNE(x);
            boolean resultLT = testIntConLT(x);
            boolean resultLE = testIntConLE(x);
            boolean resultGT = testIntConGT(x);
            boolean resultGE = testIntConGE(x);

            System.out.printf("x=0x%08X (idx=%d), CONST_INDEX=%d:%n", x, i, CONST_INDEX);
            System.out.printf("  EQ: %5s (expected %5s) %s%n", resultEQ, expectedEQ, resultEQ == expectedEQ ? "✓" : "✗");
            System.out.printf("  NE: %5s (expected %5s) %s%n", resultNE, expectedNE, resultNE == expectedNE ? "✓" : "✗");
            System.out.printf("  LT: %5s (expected %5s) %s%n", resultLT, expectedLT, resultLT == expectedLT ? "✓" : "✗");
            System.out.printf("  LE: %5s (expected %5s) %s%n", resultLE, expectedLE, resultLE == expectedLE ? "✓" : "✗");
            System.out.printf("  GT: %5s (expected %5s) %s%n", resultGT, expectedGT, resultGT == expectedGT ? "✓" : "✗");
            System.out.printf("  GE: %5s (expected %5s) %s%n", resultGE, expectedGE, resultGE == expectedGE ? "✓" : "✗");
            System.out.println();
        }

        // Test Long Variable Comparisons
        System.out.println("--- Long Variable Comparisons (x + LONG_MIN <op> y + LONG_MIN) ---");
        for (int i = 0; i < LONG_DATA.length; i++) {
            for (int j = 0; j < LONG_DATA.length; j++) {
                long x = LONG_DATA[i];
                long y = LONG_DATA[j];
                boolean expectedEQ = (i == j);
                boolean expectedNE = (i != j);
                boolean expectedLT = (i < j);
                boolean expectedLE = (i <= j);
                boolean expectedGT = (i > j);
                boolean expectedGE = (i >= j);

                boolean resultEQ = testLongVarEQ(x, y);
                boolean resultNE = testLongVarNE(x, y);
                boolean resultLT = testLongVarLT(x, y);
                boolean resultLE = testLongVarLE(x, y);
                boolean resultGT = testLongVarGT(x, y);
                boolean resultGE = testLongVarGE(x, y);

                System.out.printf("x=0x%016X (idx=%d), y=0x%016X (idx=%d):%n", x, i, y, j);
                System.out.printf("  EQ: %5s (expected %5s) %s%n", resultEQ, expectedEQ, resultEQ == expectedEQ ? "✓" : "✗");
                System.out.printf("  NE: %5s (expected %5s) %s%n", resultNE, expectedNE, resultNE == expectedNE ? "✓" : "✗");
                System.out.printf("  LT: %5s (expected %5s) %s%n", resultLT, expectedLT, resultLT == expectedLT ? "✓" : "✗");
                System.out.printf("  LE: %5s (expected %5s) %s%n", resultLE, expectedLE, resultLE == expectedLE ? "✓" : "✗");
                System.out.printf("  GT: %5s (expected %5s) %s%n", resultGT, expectedGT, resultGT == expectedGT ? "✓" : "✗");
                System.out.printf("  GE: %5s (expected %5s) %s%n", resultGE, expectedGE, resultGE == expectedGE ? "✓" : "✗");
                System.out.println();
            }
        }

        // Test Long Constant Comparisons
        System.out.println("--- Long Constant Comparisons (x + LONG_MIN <op> LONG_CONST) ---");
        for (int i = 0; i < LONG_DATA.length; i++) {
            long x = LONG_DATA[i];
            boolean expectedEQ = (i == CONST_INDEX);
            boolean expectedNE = (i != CONST_INDEX);
            boolean expectedLT = (i < CONST_INDEX);
            boolean expectedLE = (i <= CONST_INDEX);
            boolean expectedGT = (i > CONST_INDEX);
            boolean expectedGE = (i >= CONST_INDEX);

            boolean resultEQ = testLongConEQ(x);
            boolean resultNE = testLongConNE(x);
            boolean resultLT = testLongConLT(x);
            boolean resultLE = testLongConLE(x);
            boolean resultGT = testLongConGT(x);
            boolean resultGE = testLongConGE(x);

            System.out.printf("x=0x%016X (idx=%d), CONST_INDEX=%d:%n", x, i, CONST_INDEX);
            System.out.printf("  EQ: %5s (expected %5s) %s%n", resultEQ, expectedEQ, resultEQ == expectedEQ ? "✓" : "✗");
            System.out.printf("  NE: %5s (expected %5s) %s%n", resultNE, expectedNE, resultNE == expectedNE ? "✓" : "✗");
            System.out.printf("  LT: %5s (expected %5s) %s%n", resultLT, expectedLT, resultLT == expectedLT ? "✓" : "✗");
            System.out.printf("  LE: %5s (expected %5s) %s%n", resultLE, expectedLE, resultLE == expectedLE ? "✓" : "✗");
            System.out.printf("  GT: %5s (expected %5s) %s%n", resultGT, expectedGT, resultGT == expectedGT ? "✓" : "✗");
            System.out.printf("  GE: %5s (expected %5s) %s%n", resultGE, expectedGE, resultGE == expectedGE ? "✓" : "✗");
            System.out.println();
        }

        System.out.println("=== All operations completed ===");
    }
}
