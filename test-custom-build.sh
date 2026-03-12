#!/bin/bash
# Test the reproducer with built JDK 17

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JDK_PATH="/home/andreatp/workspace/jdk/build/linux-x86_64-server-release/jdk"

echo "=========================================="
echo "Testing JDK-8376400 with Built JDK 17"
echo "=========================================="
echo ""

# Check if JDK exists
if [ ! -d "$JDK_PATH" ]; then
    echo "✗ ERROR: JDK not found at: $JDK_PATH"
    exit 1
fi

if [ ! -f "$JDK_PATH/bin/java" ]; then
    echo "✗ ERROR: java binary not found at: $JDK_PATH/bin/java"
    exit 1
fi

# Set up environment
export JAVA_HOME="$JDK_PATH"
export PATH="$JAVA_HOME/bin:$PATH"

echo "JDK Path: $JAVA_HOME"
echo ""
echo "Java version:"
java -version 2>&1 | head -3
echo ""

# Check which commit this JDK was built from
if [ -d "/home/andreatp/workspace/jdk/.git" ]; then
    echo "JDK built from commit:"
    (cd /home/andreatp/workspace/jdk && git -c core.pager=cat log --oneline -1 2>/dev/null || echo "Could not determine")
    echo "Checking commit: $(cd /home/andreatp/workspace/jdk && git -c core.pager=cat log --oneline -1)" >> bisect.log
    echo ""
fi

# Build the project (skip spotless)
echo "Building wabt and dependencies..."
mvn -q -Pdev -pl wabt -am \
    -DskipTests \
    -Dspotless.check.skip=true \
    -Dspotless.apply.skip=true \
    -Dmaven.compiler.failOnError=false \
    package test-compile 2>&1 | tail -5

if [ ${PIPESTATUS[0]} -ne 0 ]; then
    echo "✗ Build failed"
    exit 1
fi

echo "✓ Build complete"
echo ""

# Run reproducer
echo "=========================================="
echo "Running Reproducer"
echo "=========================================="
echo ""

if mvn -q -pl wabt exec:java \
    -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
    -Dexec.classpathScope=test \
    -Dspotless.check.skip=true \
    -Dspotless.apply.skip=true \
    2>&1 | tee /tmp/reproducer-jdk17.log; then

    echo "RESULT:" >> bisect.log
    cat /tmp/reproducer-jdk17.log >> bisect.log

    if grep -q "out of bounds memory access" /tmp/reproducer-jdk17.log; then
        echo "✗✗✗ TEST FAILED - Bug REPRODUCED!"
        echo "   Error: out of bounds memory access"
        echo "   This confirms the bug exists in this JDK build"
        echo ""
        echo "Your build process is working correctly!"
        exit 1
    else
        echo "✗✗✗ TEST FAILED"
        echo "   Check the error above"
        tail -10 /tmp/reproducer-jdk17.log
        exit 1
    fi
else
    echo ""
    echo ""
    echo "✓✓✓ TEST PASSED"
    echo "   Bug NOT triggered"
    exit 0
fi
