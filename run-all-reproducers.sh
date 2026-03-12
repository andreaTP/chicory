#!/bin/bash
# Run all reproducers systematically

set -e

JAVA17="${JAVA17:-17.0.18-tem}"

if command -v sdk &> /dev/null; then
    source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
    sdk use java "$JAVA17" > /dev/null 2>&1
fi

echo "Testing all reproducers on Java 17..."
echo "Java version:"
java -version
echo

# Compile all
echo "Compiling..."
javac MinimalReproducer.java ReproducerWithIntrinsic.java TestOptimizationPhases.java 2>&1 || true
echo

# Test 1: Minimal pattern
echo "========================================="
echo "Test 1: MinimalReproducer (direct pattern)"
echo "========================================="
java MinimalReproducer 2>&1 | tail -5
echo

# Test 2: With intrinsic
echo "========================================="
echo "Test 2: ReproducerWithIntrinsic (Integer.compareUnsigned)"
echo "========================================="
java ReproducerWithIntrinsic 2>&1 | tail -5
echo

# Test 3: Different contexts
echo "========================================="
echo "Test 3: TestOptimizationPhases (different contexts)"
echo "========================================="
java TestOptimizationPhases 2>&1 | tail -5
echo

echo "========================================="
echo "Summary: Check above for 'BUG DETECTED' or 'ERROR:'"
echo "If any test shows a bug, note which one and the conditions"
