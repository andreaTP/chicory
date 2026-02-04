#!/bin/bash
# Test a specific OpenJDK commit with the reproducer

set -e

COMMIT=$1
if [ -z "$COMMIT" ]; then
    echo "Usage: $0 <commit-hash>"
    echo "Example: $0 3ffec3a50b7"
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REPO_DIR="openjdk-bisect"
BUILD_BASE="$SCRIPT_DIR/jdk-build-$COMMIT"

echo "=========================================="
echo "Testing OpenJDK Commit: $COMMIT"
echo "=========================================="
echo ""

# Setup boot JDK
if command -v sdk &> /dev/null; then
    export SDKMAN_DIR="$HOME/.sdkman"
    source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
    JAVA_HOME=$(sdk home java 19.0.1-tem 2>/dev/null || sdk home java 18.0.1-tem 2>/dev/null || echo "")
    if [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ]; then
        export JAVA_HOME
    fi
fi

if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ]; then
    echo "✗ ERROR: JAVA_HOME not set or invalid"
    exit 1
fi

echo "Boot JDK: $JAVA_HOME"
echo ""

cd "$REPO_DIR"

# Checkout commit
echo "Checking out commit $COMMIT..."
git fetch https://github.com/openjdk/jdk.git "$COMMIT" 2>&1 | tail -2 || true
git checkout "$COMMIT" 2>&1 | tail -2

COMMIT_INFO=$(git log --oneline -1)
echo "Commit: $COMMIT_INFO"
echo ""

# Configure
echo "Configuring..."
bash configure --with-boot-jdk="$JAVA_HOME" \
    --disable-warnings-as-errors \
    --with-debug-level=release \
    > "$BUILD_BASE-configure.log" 2>&1

if [ $? -ne 0 ]; then
    echo "✗ Configure failed - check $BUILD_BASE-configure.log"
    exit 1
fi

echo "✓ Configured"
echo ""

# Build
echo "Building (this takes 30-60 minutes)..."
echo "Build log: $BUILD_BASE-build.log"
make images 2>&1 | tee "$BUILD_BASE-build.log" | grep -E "(Building|Compiling|Linking|Error|error)" | tail -5

if [ ${PIPESTATUS[0]} -ne 0 ]; then
    echo "✗ Build failed - check $BUILD_BASE-build.log"
    exit 1
fi

# Find built JDK
JDK_PATH=$(find build -name "images" -type d | head -1)/jdk
if [ ! -d "$JDK_PATH" ]; then
    echo "✗ Could not find built JDK"
    exit 1
fi

JDK_ABS="$SCRIPT_DIR/$REPO_DIR/$JDK_PATH"
echo "✓ Build complete: $JDK_ABS"
echo ""

# Test
cd "$SCRIPT_DIR"
export JAVA_HOME="$JDK_ABS"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Java version:"
java -version 2>&1 | head -1
echo ""

echo "Running reproducer..."
if mvn -q -pl wabt exec:java \
    -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
    -Dexec.classpathScope=test \
    -Dspotless.check.skip=true \
    -Dspotless.apply.skip=true \
    2>&1 | tee "$BUILD_BASE-test.log"; then
    if grep -q "Conversion succeeded" "$BUILD_BASE-test.log"; then
        echo ""
        echo "✓✓✓ TEST PASSED - Bug is FIXED in this commit"
        exit 0
    else
        echo ""
        echo "✗✗✗ TEST FAILED - Bug is PRESENT in this commit"
        exit 1
    fi
else
    echo ""
    echo "✗✗✗ TEST FAILED - Bug is PRESENT in this commit"
    exit 1
fi
