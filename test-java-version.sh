#!/bin/bash
# Test a single Java version
VERSION=$1

export SDKMAN_DIR="$HOME/.sdkman"
export SDKMAN_OFFLINE_MODE=false
source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null

echo "Testing Java $VERSION..."

# Install if needed
if ! sdk list java 2>/dev/null | grep -q "$VERSION.*local only\|$VERSION.*installed"; then
    echo "Installing $VERSION..."
    echo "y" | sdk install java "$VERSION" 2>&1 | grep -v "^Downloading\|^Installing\|^Setting\|^Done" || true
fi

# Use version
sdk use java "$VERSION" > /dev/null 2>&1
java -version 2>&1 | head -1

# Build
echo "Building..."
mvn -q -Pdev -pl wabt -am -DskipTests package test-compile 2>&1 | tail -1

# Test
echo "Running reproducer..."
if mvn -q -pl wabt exec:java \
    -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
    -Dexec.classpathScope=test 2>&1 | grep -q "Conversion succeeded"; then
    echo "✓ PASSED - Bug is FIXED in $VERSION"
    exit 0
else
    echo "✗ FAILED - Bug still present in $VERSION"
    exit 1
fi
