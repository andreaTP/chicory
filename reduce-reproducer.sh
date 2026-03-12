#!/bin/bash
# Systematically reduce the WAT file size while keeping the bug

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Use Java 17
export JAVA_HOME="${JAVA_HOME:-$(sdk home java 17.0.18-tem 2>/dev/null || echo "")}"
if [ -z "$JAVA_HOME" ] && command -v sdk &> /dev/null; then
    source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
    sdk use java 17.0.18-tem > /dev/null 2>&1
    export JAVA_HOME="$HOME/.sdkman/candidates/java/17.0.18-tem"
fi

echo "=== Reducing WAT file size to find minimal reproducer ==="
echo "Java: $JAVA_HOME"
echo

# Compile once
mvn -q -pl wabt,wasm-corpus -DskipTests test-compile > /dev/null 2>&1

# Test function
test_wat() {
    local func_count=$1
    local func_size=$2
    local wat_file="/tmp/test-${func_count}-${func_size}.wat"
    
    # Generate WAT using WatGenerator
    java -cp "wasm-corpus/target/test-classes:wasm-corpus/target/classes:$(mvn -q -pl wasm-corpus dependency:build-classpath -Dmdep.outputFile=/dev/stdout 2>/dev/null)" \
         com.dylibso.chicory.corpus.WatGenerator $func_count $func_size > "$wat_file" 2>/dev/null || {
        # Fallback: use Maven to generate
        mvn -q -pl wasm-corpus exec:java \
            -Dexec.mainClass="com.dylibso.chicory.corpus.WatGenerator" \
            -Dexec.args="$func_count $func_size" \
            -Dexec.classpathScope=test > "$wat_file" 2>&1
    }
    
    local wat_size=$(wc -c < "$wat_file" 2>/dev/null || echo "0")
    echo -n "  Testing func_count=$func_count, func_size=$func_size (${wat_size} bytes)... "
    
    # Test with Wat2Wasm.parse
    local result=$(timeout 60 mvn -q -pl wabt exec:java \
        -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
        -Dexec.classpathScope=test \
        -Dexec.args="$wat_file" \
        -Dspotless.check.skip=true \
        -Dspotless.apply.skip=true \
        2>&1 | tee /tmp/test-result.log)
    
    if grep -q "out of bounds memory access" /tmp/test-result.log; then
        echo "✓ BUG REPRODUCED"
        return 0
    elif grep -q "Conversion succeeded" /tmp/test-result.log; then
        echo "✗ No bug"
        return 1
    else
        echo "? Error/Timeout"
        return 2
    fi
}

# Start with original: 50,000 functions, 0 instructions
echo "Step 1: Testing original (50,000 functions, 0 instructions)..."
if test_wat 50000 0; then
    echo "✓ Original reproduces bug"
    CURRENT_FUNC=50000
else
    echo "✗ Original doesn't reproduce - may need buggy JDK"
    echo "Continuing with reduction anyway..."
    CURRENT_FUNC=50000
fi

# Binary search: reduce function count
echo
echo "Step 2: Binary search to find minimum function count..."
MIN_FUNC=1
MAX_FUNC=$CURRENT_FUNC
BEST_FUNC=$CURRENT_FUNC

while [ $MIN_FUNC -lt $MAX_FUNC ]; do
    MID=$(( (MIN_FUNC + MAX_FUNC) / 2 ))
    echo "  Testing $MID functions..."
    if test_wat $MID 0; then
        BEST_FUNC=$MID
        MAX_FUNC=$MID
        echo "    → Bug still reproduces at $MID"
    else
        MIN_FUNC=$((MID + 1))
        echo "    → Bug doesn't reproduce at $MID"
    fi
done

echo
echo "=== Result ==="
echo "Minimum function count that reproduces bug: $BEST_FUNC"
echo "Creating minimal reproducer..."

# Create minimal reproducer with best count
cat > MinimalReproducer.java << EOF
package com.dylibso.chicory.wabt;

import com.dylibso.chicory.corpus.WatGenerator;

public class MinimalReproducer {
    public static void main(String[] args) throws Exception {
        String wat = WatGenerator.bigWat($BEST_FUNC, 0);
        System.out.println("WAT size: " + wat.length() + " chars");
        System.out.println("Function count: $BEST_FUNC");
        byte[] result = Wat2Wasm.parse(wat);
        System.out.println("Conversion succeeded, output bytes: " + result.length);
    }
}
EOF

echo "Created MinimalReproducer.java with $BEST_FUNC functions"
