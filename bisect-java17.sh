#!/bin/bash
# Git bisect on Java 17 branch to find where JDK-8376400 bug appears

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REPO_DIR="openjdk-bisect"
GOOD_COMMIT="jdk-17-ga"  # Java 17 GA - test first
BAD_COMMIT="dfacda488bf"  # Java 17.0.18 (jdk-17+35) - known to have bug

echo "=========================================="
echo "Git Bisect for JDK-8376400 on Java 17 Branch"
echo "=========================================="
echo ""
echo "GOOD (test first): $GOOD_COMMIT"
echo "BAD (known to have bug): $BAD_COMMIT (Java 17.0.18)"
echo ""

cd "$REPO_DIR"

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

# Function to test a commit
test_commit() {
    local commit=$1
    local commit_short=$(echo "$commit" | cut -c1-12)
    
    echo ""
    echo "=========================================="
    echo "Testing: $commit_short"
    echo "=========================================="
    git log --oneline -1 "$commit"
    echo ""
    
    # Checkout
    git checkout "$commit" > /dev/null 2>&1
    
    BUILD_DIR="build/linux-x86_64-server-release/images/jdk"
    
    # Build
    echo "Building (this takes 30-60 minutes)..."
    bash configure --with-boot-jdk="$JAVA_HOME" \
        --disable-warnings-as-errors \
        --with-debug-level=release \
        > /dev/null 2>&1
    
    if [ $? -ne 0 ]; then
        echo "✗ Configure failed"
        return 2
    fi
    
    make images 2>&1 | tee "/tmp/build-$commit_short.log" | tail -3
    
    if [ ${PIPESTATUS[0]} -ne 0 ]; then
        echo "✗ Build failed"
        return 2
    fi
    
    # Test
    JDK_PATH="$SCRIPT_DIR/$REPO_DIR/$BUILD_DIR"
    if [ ! -d "$JDK_PATH" ]; then
        echo "✗ JDK not found"
        return 2
    fi
    
    cd "$SCRIPT_DIR"
    export JAVA_HOME="$JDK_PATH"
    export PATH="$JAVA_HOME/bin:$PATH"
    
    echo "Testing with reproducer..."
    if mvn -q -pl wabt exec:java \
        -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
        -Dexec.classpathScope=test \
        -Dspotless.check.skip=true \
        -Dspotless.apply.skip=true \
        2>&1 | tee "/tmp/test-$commit_short.log" | tail -3; then
        
        if grep -q "Conversion succeeded" "/tmp/test-$commit_short.log"; then
            echo "✓ PASSED (no bug)"
            cd "$REPO_DIR"
            return 0
        fi
    fi
    
    if grep -q "out of bounds memory access" "/tmp/test-$commit_short.log"; then
        echo "✗ FAILED - Bug PRESENT"
        cd "$REPO_DIR"
        return 1
    else
        echo "? UNKNOWN - Check logs"
        cd "$REPO_DIR"
        return 2
    fi
}

# Start bisect
echo "Starting git bisect on Java 17 branch..."
git bisect start
git bisect good "$GOOD_COMMIT"
git bisect bad "$BAD_COMMIT"

echo ""
echo "Bisecting (this will take several hours as each commit needs to be built)..."
echo ""

# Bisect loop
ITERATION=0
MAX_ITERATIONS=15  # Safety limit

while [ $ITERATION -lt $MAX_ITERATIONS ]; do
    CURRENT=$(git rev-parse HEAD)
    CURRENT_SHORT=$(echo "$CURRENT" | cut -c1-12)
    
    echo "=========================================="
    echo "Bisect iteration $((ITERATION + 1)): $CURRENT_SHORT"
    echo "=========================================="
    
    test_commit "$CURRENT"
    EXIT_CODE=$?
    
    if [ "$EXIT_CODE" -eq 0 ]; then
        echo "Marking as GOOD (no bug)"
        git bisect good
    elif [ "$EXIT_CODE" -eq 1 ]; then
        echo "Marking as BAD (has bug)"
        git bisect bad
    else
        echo "Skipping (build/test issue)"
        git bisect skip
    fi
    
    # Check if bisect is done
    if git bisect log 2>/dev/null | grep -q "first bad commit"; then
        break
    fi
    
    ITERATION=$((ITERATION + 1))
done

echo ""
echo "=========================================="
echo "Bisect Complete!"
echo "=========================================="
echo ""
git bisect log | tail -20
echo ""
echo "First bad commit (where bug appears):"
git bisect log | grep "first bad commit" -A 5
echo ""
git bisect reset
