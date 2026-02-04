#!/bin/bash
# Bisect OpenJDK commits to find where the bug was fixed

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REPO_DIR="openjdk-bisect"
BUILD_DIR="$SCRIPT_DIR/jdk-build"

echo "=========================================="
echo "OpenJDK Commit Bisection for JDK-8376400"
echo "=========================================="
echo ""

# Setup boot JDK
setup_boot_jdk() {
    if command -v sdk &> /dev/null; then
        export SDKMAN_DIR="$HOME/.sdkman"
        source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
        BOOT_JDK=$(sdk home java 19.0.1-tem 2>/dev/null || sdk home java 18.0.1-tem 2>/dev/null || echo "")
        if [ -n "$BOOT_JDK" ] && [ -d "$BOOT_JDK" ]; then
            export JAVA_HOME="$BOOT_JDK"
            return 0
        fi
    fi
    
    if [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ]; then
        return 0
    fi
    
    echo "✗ ERROR: No boot JDK found"
    return 1
}

# Test a commit
test_commit() {
    local commit=$1
    local commit_short=$(echo "$commit" | cut -c1-8)
    
    echo ""
    echo "=========================================="
    echo "Testing commit: $commit_short"
    echo "=========================================="
    
    cd "$REPO_DIR"
    
    # Checkout commit
    git checkout "$commit" 2>&1 | tail -2
    
    # Quick build (just hotspot to save time)
    echo "Building (this may take 20-30 minutes)..."
    export JAVA_HOME
    bash configure --with-boot-jdk="$JAVA_HOME" \
        --disable-warnings-as-errors \
        --with-debug-level=release \
        > /dev/null 2>&1
    
    if [ $? -ne 0 ]; then
        echo "✗ Configure failed"
        return 2
    fi
    
    # Build just the JVM (faster than full images)
    make hotspot 2>&1 | tee "/tmp/build-$commit_short.log" | tail -5
    
    if [ ${PIPESTATUS[0]} -ne 0 ]; then
        echo "✗ Build failed"
        return 2
    fi
    
    # Find the built JDK
    local jdk_path=$(find build -name "jvm" -type d | head -1 | xargs dirname | xargs dirname)/images/jdk
    if [ ! -d "$jdk_path" ]; then
        # Try full build
        make images 2>&1 | tail -5
        jdk_path=$(find build -name "images" -type d | head -1)/jdk
    fi
    
    if [ ! -d "$jdk_path" ]; then
        echo "✗ Could not find built JDK"
        return 2
    fi
    
    echo "✓ Build complete: $jdk_path"
    
    # Test with reproducer
    cd "$SCRIPT_DIR"
    export JAVA_HOME="$SCRIPT_DIR/$REPO_DIR/$jdk_path"
    export PATH="$JAVA_HOME/bin:$PATH"
    
    echo "Testing with reproducer..."
    if mvn -q -pl wabt exec:java \
        -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
        -Dexec.classpathScope=test \
        -Dspotless.check.skip=true \
        -Dspotless.apply.skip=true \
        2>&1 | grep -q "Conversion succeeded"; then
        echo "✓ PASSED (bug is fixed)"
        return 0
    else
        echo "✗ FAILED (bug present)"
        return 1
    fi
}

# Main bisect
main() {
    if ! setup_boot_jdk; then
        exit 1
    fi
    
    cd "$REPO_DIR"
    
    # Find a good starting point - latest Java 17 that should have the bug
    echo "Finding Java 17 commits..."
    
    # Try to find jdk-17+35 or similar
    JAVA17_COMMIT=""
    for tag in $(git tag | grep "^jdk-17" | sort -V | tail -5); do
        echo "Checking tag: $tag"
        JAVA17_COMMIT=$(git rev-parse "$tag" 2>/dev/null || echo "")
        if [ -n "$JAVA17_COMMIT" ]; then
            echo "Found: $tag -> $JAVA17_COMMIT"
            break
        fi
    done
    
    if [ -z "$JAVA17_COMMIT" ]; then
        # Use a commit from around Java 17.0.18 timeframe
        JAVA17_COMMIT=$(git log --oneline --all --since="2022-04-01" --until="2022-04-19" | tail -1 | awk '{print $1}')
        echo "Using commit from April 2022: $JAVA17_COMMIT"
    fi
    
    # Known fixed commit
    FIX_COMMIT="a81c5d3a23163164a79763421935d0262a36f27e"
    
    echo ""
    echo "Bisecting between:"
    echo "  BAD (has bug):  $JAVA17_COMMIT"
    echo "  GOOD (fixed):   $FIX_COMMIT"
    echo ""
    
    # Start git bisect
    git bisect start
    git bisect bad "$JAVA17_COMMIT"
    git bisect good "$FIX_COMMIT"
    
    echo ""
    echo "Git bisect started. Testing commits..."
    echo ""
    
    # Bisect loop
    while true; do
        CURRENT=$(git rev-parse HEAD)
        CURRENT_SHORT=$(echo "$CURRENT" | cut -c1-8)
        
        echo "=========================================="
        echo "Bisecting: $CURRENT_SHORT"
        echo "=========================================="
        
        if test_commit "$CURRENT"; then
            # Bug is fixed (good)
            git bisect good
        else
            # Bug present (bad)
            git bisect bad
        fi
        
        # Check if bisect is done
        if git bisect log | grep -q "first bad commit"; then
            break
        fi
    done
    
    echo ""
    echo "=========================================="
    echo "Bisect complete!"
    echo "=========================================="
    git bisect log | tail -10
    git bisect reset
}

main "$@"
