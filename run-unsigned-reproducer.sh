#!/bin/bash
set -e

# Script to run UnsignedComparisonReproducer with C2 compilation flags
# This reproducer tests the JDK-8376400 bug (Integer.compareUnsigned() optimization)

cd "$(dirname "$0")"

echo "=== Running UnsignedComparisonReproducer ==="
echo ""
echo "JVM Flags:"
echo "  -XX:+UnlockDiagnosticVMOptions"
echo "  -XX:+PrintCompilation"
echo "  -XX:-TieredCompilation"
echo "  -XX:CompileThreshold=1000"
echo ""

# Build the project first
echo "Building project..."
mvn -q -pl wabt test-compile -Dspotless.check.skip=true -Dspotless.apply.skip=true

# Get the classpath
CLASSPATH=$(mvn -q -pl wabt dependency:build-classpath -Dmdep.outputFile=/dev/stdout -Dspotless.check.skip=true -Dspotless.apply.skip=true 2>/dev/null)
CLASSPATH="wabt/target/test-classes:wabt/target/classes:$CLASSPATH"

# Run with C2 compilation flags
echo "Running reproducer..."
echo ""

java \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:+PrintCompilation \
  -XX:-TieredCompilation \
  -XX:CompileThreshold=100 \
  -XX:OnStackReplacePercentage=100 \
  -Xbatch \
  -cp "$CLASSPATH" \
  com.dylibso.chicory.wabt.UnsignedComparisonReproducer

echo ""
echo "=== Done ==="
