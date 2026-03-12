#!/bin/bash
# More targeted approach: test specific conditions that might trigger the bug
# Based on the fix location in BoolNode::Ideal

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JAVA17="${JAVA17:-17.0.18-tem}"
JAVA_HOME_17=""

# Find Java 17
if command -v sdk &> /dev/null; then
    source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
    if sdk list java | grep -q "$JAVA17"; then
        sdk use java "$JAVA17" > /dev/null 2>&1
        JAVA_HOME_17="$JAVA_HOME"
    fi
fi

if [ -z "$JAVA_HOME_17" ]; then
    echo "Error: Java 17 not found. Set JAVA_HOME_17 or install via SDKMAN"
    exit 1
fi

echo "Using Java: $JAVA_HOME_17"
"$JAVA_HOME_17/bin/java" -version
echo

# Compile
"$JAVA_HOME_17/bin/javac" MinimalReproducer.java

# Create a test that forces the pattern through different optimization phases
cat > TestOptimizationPhases.java << 'EOF'
/**
 * Test different ways the pattern might be optimized
 */
public class TestOptimizationPhases {
    private static final int INT_MIN = Integer.MIN_VALUE;
    private static final int MIN_VAL = Integer.MIN_VALUE;
    private static final int MAX_VAL = Integer.MAX_VALUE;
    
    // Pattern in different contexts
    public static boolean testInIf(int x, int y) {
        if (x + INT_MIN < y + INT_MIN) {
            return true;
        }
        return false;
    }
    
    public static boolean testInTernary(int x, int y) {
        return (x + INT_MIN < y + INT_MIN) ? true : false;
    }
    
    public static boolean testInAnd(int x, int y) {
        return (x + INT_MIN < y + INT_MIN) && true;
    }
    
    public static boolean testInOr(int x, int y) {
        return (x + INT_MIN < y + INT_MIN) || false;
    }
    
    public static boolean testInLoop(int x, int y) {
        while ((x + INT_MIN < y + INT_MIN) && false) {
            // never executes
        }
        return x + INT_MIN < y + INT_MIN;
    }
    
    public static boolean testCritical() {
        return MIN_VAL + INT_MIN > MAX_VAL + INT_MIN;
    }
    
    public static void main(String[] args) {
        System.out.println("Testing pattern in different contexts...");
        
        // Warm up
        for (int i = 0; i < 100_000; i++) {
            testInIf(0, 1);
            testInTernary(0, 1);
            testInAnd(0, 1);
            testInOr(0, 1);
            testInLoop(0, 1);
        }
        
        // Test critical case
        boolean critical = testCritical();
        if (critical != true) {
            System.err.println("BUG: Critical test failed!");
            System.err.println("  Context: direct return");
            System.exit(1);
        }
        
        // Test in different contexts
        int[] testVals = {0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE};
        for (int x : testVals) {
            for (int y : testVals) {
                boolean expected = Integer.compareUnsigned(x, y) < 0;
                
                if (testInIf(x, y) != expected) {
                    System.err.printf("BUG in if context: x=0x%08X, y=0x%08X%n", x, y);
                    System.exit(1);
                }
                if (testInTernary(x, y) != expected) {
                    System.err.printf("BUG in ternary context: x=0x%08X, y=0x%08X%n", x, y);
                    System.exit(1);
                }
                if (testInAnd(x, y) != expected) {
                    System.err.printf("BUG in and context: x=0x%08X, y=0x%08X%n", x, y);
                    System.exit(1);
                }
                if (testInOr(x, y) != expected) {
                    System.err.printf("BUG in or context: x=0x%08X, y=0x%08X%n", x, y);
                    System.exit(1);
                }
                if (testInLoop(x, y) != expected) {
                    System.err.printf("BUG in loop context: x=0x%08X, y=0x%08X%n", x, y);
                    System.exit(1);
                }
            }
        }
        
        System.out.println("All tests passed");
    }
}
EOF

"$JAVA_HOME_17/bin/javac" TestOptimizationPhases.java

echo "Testing with different JVM flags..."
echo

# Test with various flags that might affect BoolNode::Ideal optimization order
FLAGS=(
    ""
    "-XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining"
    "-XX:CompileThreshold=100"
    "-XX:-TieredCompilation"
    "-XX:+AggressiveOpts"
    "-XX:-LoopOpts"
    "-XX:-Inline"
    "-XX:CompileCommand=compileonly,TestOptimizationPhases::testCritical"
    "-XX:CompileCommand=compileonly,TestOptimizationPhases::testInIf"
)

for flags in "${FLAGS[@]}"; do
    echo "Testing with: $flags"
    if "$JAVA_HOME_17/bin/java" $flags TestOptimizationPhases 2>&1 | grep -q "BUG"; then
        echo "  ✓ BUG REPRODUCED with flags: $flags"
        echo "  This is the condition that triggers the bug!"
        exit 0
    fi
done

echo
echo "Bug not reproduced with standard flags."
echo "The bug may require very specific conditions or the original WASM execution context."
