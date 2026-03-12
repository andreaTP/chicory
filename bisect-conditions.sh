#!/bin/bash
# Systematic approach to bisect the conditions that trigger the bug

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

# Compile the reproducer
echo "Compiling MinimalReproducer.java..."
"$JAVA_HOME_17/bin/javac" MinimalReproducer.java

# Test scenarios
test_scenario() {
    local name="$1"
    shift
    local jvm_flags=("$@")
    
    echo "========================================="
    echo "Testing: $name"
    echo "JVM flags: ${jvm_flags[*]}"
    echo "-----------------------------------------"
    
    if "$JAVA_HOME_17/bin/java" "${jvm_flags[@]}" MinimalReproducer 2>&1 | tee /tmp/test_output.txt | grep -q "BUG DETECTED\|ERROR:"; then
        echo "✓ BUG REPRODUCED!"
        echo "  Flags: ${jvm_flags[*]}"
        return 0
    else
        echo "✗ Bug not reproduced"
        return 1
    fi
}

# Test different compilation scenarios
echo "Systematically testing compilation conditions..."
echo

# 1. Default (tiered compilation)
test_scenario "Default (tiered)" || true

# 2. C2 only (no C1)
test_scenario "C2 only" -XX:-TieredCompilation -XX:+UseC2 || true

# 3. C1 only (should not trigger bug)
test_scenario "C1 only" -XX:-TieredCompilation -XX:+UseC1 || true

# 4. Force compilation at different thresholds
test_scenario "Low compile threshold" -XX:CompileThreshold=100 || true
test_scenario "Very low compile threshold" -XX:CompileThreshold=10 || true

# 5. Disable specific optimizations
test_scenario "No inlining" -XX:-Inline || true
test_scenario "No loop opts" -XX:-LoopOpts || true

# 6. Force OSR compilation
test_scenario "OSR compilation" -XX:CompileThreshold=100 -XX:OnStackReplacePercentage=100 || true

# 7. Different optimization levels
test_scenario "Aggressive opts" -XX:+AggressiveOpts || true
test_scenario "Disable opts" -XX:-OptimizeStringConcat || true

# 8. Force recompilation
test_scenario "Force recompilation" -XX:CompileThreshold=100 -XX:CompileCommand="compileonly,MinimalReproducer::testPattern" || true

# 9. With compilation logging
echo "========================================="
echo "Testing with compilation logging..."
echo "-----------------------------------------"
"$JAVA_HOME_17/bin/java" \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:+LogCompilation \
    -XX:LogFile=/tmp/compilation.log \
    MinimalReproducer > /tmp/test_output2.txt 2>&1 || true

if grep -q "BUG DETECTED\|ERROR:" /tmp/test_output2.txt; then
    echo "✓ BUG REPRODUCED with logging!"
    echo "  Check /tmp/compilation.log for details"
fi

# 10. Print compilation log summary
if [ -f /tmp/compilation.log ]; then
    echo
    echo "Compilation events:"
    grep -E "(compile|made not entrant)" /tmp/compilation.log | head -20 || true
fi

echo
echo "========================================="
echo "Summary: Check above for 'BUG REPRODUCED'"
echo "If bug reproduced, note the JVM flags used"
