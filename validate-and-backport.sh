#!/bin/bash
# Complete validation and backport script for JDK-8376400

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REPO_DIR="openjdk-bisect"
FIX_COMMIT="a81c5d3a23163164a79763421935d0262a36f27e"
# BEFORE_COMMIT is the parent of FIX_COMMIT (has the bug, before fix)
BEFORE_COMMIT=""  # Will be determined dynamically
BUILD_DIR_BEFORE="$SCRIPT_DIR/jdk18-before-fix"
BUILD_DIR_AFTER="$SCRIPT_DIR/jdk18-after-fix"

echo "=========================================="
echo "JDK-8376400 Validation and Backport"
echo "=========================================="
echo ""

# Setup boot JDK (Java 18+ required for building Java 18)
setup_boot_jdk() {
    echo "Setting up boot JDK (Java 18 or 19 required)..."
    
    if command -v sdk &> /dev/null; then
        export SDKMAN_DIR="$HOME/.sdkman"
        source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
        
        # Try Java 19 first, then 18, then current
        for jdk_ver in "19.0.1-tem" "19.0.2-tem" "18.0.1-tem" "18.0.2-tem" "current"; do
            BOOT_JDK=$(sdk home java "$jdk_ver" 2>/dev/null || echo "")
            if [ -n "$BOOT_JDK" ] && [ -d "$BOOT_JDK" ] && [ -f "$BOOT_JDK/bin/java" ]; then
                # Verify it's Java 18 or 19
                JAVA_VER=$("$BOOT_JDK/bin/java" -version 2>&1 | head -1)
                if echo "$JAVA_VER" | grep -qE "version \"(18|19)"; then
                    export JAVA_HOME="$BOOT_JDK"
                    echo "✓ Boot JDK: $JAVA_HOME"
                    echo "  $JAVA_VER"
                    return 0
                fi
            fi
        done
    fi
    
    # Check existing JAVA_HOME
    if [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ] && [ -f "$JAVA_HOME/bin/java" ]; then
        JAVA_VER=$("$JAVA_HOME/bin/java" -version 2>&1 | head -1)
        if echo "$JAVA_VER" | grep -qE "version \"(18|19)"; then
            echo "✓ Boot JDK: $JAVA_HOME"
            echo "  $JAVA_VER"
            return 0
        else
            echo "✗ Boot JDK version incorrect: $JAVA_VER"
            echo "  Required: Java 18 or 19"
        fi
    fi
    
    echo "✗ ERROR: No valid boot JDK found"
    echo ""
    echo "Please install Java 18 or 19:"
    echo "  sdk install java 19.0.1-tem"
    echo "  sdk install java 18.0.1-tem"
    echo ""
    echo "Or set JAVA_HOME to a Java 18/19 installation:"
    echo "  export JAVA_HOME=/path/to/java18"
    return 1
}

# Check build dependencies
check_dependencies() {
    echo "Checking build dependencies..."
    MISSING=""
    for dep in gcc g++ make autoconf; do
        if ! command -v $dep &> /dev/null; then
            MISSING="$MISSING $dep"
        fi
    done
    
    if [ -n "$MISSING" ]; then
        echo "✗ Missing dependencies:$MISSING"
        echo "Install with: sudo dnf install$MISSING"
        return 1
    fi
    
    echo "✓ Build dependencies OK"
    return 0
}

# Build OpenJDK at a specific commit
build_jdk() {
    local commit=$1
    local build_dir=$2
    local label=$3
    
    echo ""
    echo "=========================================="
    echo "Building Java 18 $label"
    echo "=========================================="
    echo "Commit: $commit"
    echo "Build dir: $build_dir"
    echo ""
    
    cd "$SCRIPT_DIR/$REPO_DIR" || {
        echo "✗ ERROR: Could not cd to $SCRIPT_DIR/$REPO_DIR"
        return 1
    }
    
    echo "Checking out commit..."
    git fetch https://github.com/openjdk/jdk.git "$commit" 2>&1 | tail -2 || true
    git checkout "$commit" 2>&1 | tail -3
    
    # Verify we're on the right commit
    CURRENT=$(git rev-parse HEAD)
    EXPECTED=$(git rev-parse "$commit")
    if [ "$CURRENT" != "$EXPECTED" ]; then
        echo "✗ ERROR: Checkout failed - expected $EXPECTED, got $CURRENT"
        return 1
    fi
    echo "✓ Verified: $(git log --oneline -1)"
    
    echo ""
    echo "Cleaning previous build..."
    make clean 2>&1 | tail -5 || true
    
    echo ""
    echo "Configuring..."
    
    # Ensure JAVA_HOME is set and exported
    if [ -z "$JAVA_HOME" ]; then
        echo "✗ ERROR: JAVA_HOME is not set"
        return 1
    fi
    
    # Verify boot JDK exists and has required files
    if [ ! -d "$JAVA_HOME" ]; then
        echo "✗ ERROR: Boot JDK directory does not exist: $JAVA_HOME"
        return 1
    fi
    
    if [ ! -f "$JAVA_HOME/bin/java" ]; then
        echo "✗ ERROR: Boot JDK java binary not found: $JAVA_HOME/bin/java"
        return 1
    fi
    
    if [ ! -f "$JAVA_HOME/bin/javac" ]; then
        echo "✗ ERROR: Boot JDK javac not found: $JAVA_HOME/bin/javac"
        echo "  This is required for building OpenJDK"
        return 1
    fi
    
    # Use absolute path for boot JDK
    BOOT_JDK_ABS=$(cd "$JAVA_HOME" && pwd)
    echo "Using boot JDK: $BOOT_JDK_ABS"
    
    # Show boot JDK version
    echo "Boot JDK version:"
    "$BOOT_JDK_ABS/bin/java" -version 2>&1 | head -3
    echo ""
    
    # Export JAVA_HOME to ensure configure can use it
    export JAVA_HOME="$BOOT_JDK_ABS"
    
    # Disable warnings as errors - this only affects C/C++ warnings
    bash configure --with-boot-jdk="$BOOT_JDK_ABS" \
        --disable-warnings-as-errors \
        --with-debug-level=release \
        2>&1 | tee "$build_dir-configure.log" | tail -30
    
    # Check if configure succeeded
    CONFIGURE_EXIT=${PIPESTATUS[0]}
    if [ $CONFIGURE_EXIT -ne 0 ]; then
        echo ""
        echo "✗ Configuration failed (exit code: $CONFIGURE_EXIT)"
        echo ""
        echo "Last 20 lines of configure output:"
        tail -20 "$build_dir-configure.log"
        echo ""
        echo "Full log: $build_dir-configure.log"
        echo ""
        echo "Common issues:"
        echo "- Boot JDK missing javac: $BOOT_JDK_ABS/bin/javac"
        echo "- Boot JDK version not 18 or 19"
        echo "- Boot JDK missing include directory"
        return 1
    fi
    
    echo ""
    echo "Configuration complete"
    
    if [ ${PIPESTATUS[0]} -ne 0 ]; then
        echo "✗ Configuration failed"
        return 1
    fi
    
    echo ""
    echo "Building (this takes 30-60 minutes)..."
    echo "Progress will be saved to: $build_dir-build.log"
    echo "Starting build at $(date)..."
    echo ""
    echo "Note: If build fails with Feature.FOREIGN warnings, we'll patch the source"
    echo ""
    
    # Build OpenJDK
    if make images 2>&1 | tee "$build_dir-build.log"; then
        echo ""
        echo "✓ Build succeeded"
    else
        BUILD_EXIT=${PIPESTATUS[0]}
        echo ""
        echo "Build exited with code: $BUILD_EXIT"
        
        # Check if it's Feature.FOREIGN warnings
        if grep -q "Feature.FOREIGN" "$build_dir-build.log" && grep -q "warnings found and -Werror" "$build_dir-build.log"; then
            echo ""
            echo "Build failed due to Feature.FOREIGN warnings (Java 19 boot JDK compatibility)"
            echo "Patching source to suppress these warnings..."
            
            # Find and patch the files causing warnings
            WARNING_FILES=$(grep "Feature.FOREIGN" "$build_dir-build.log" | grep -oE "[A-Za-z0-9_/]+\.java" | sort -u | head -5)
            if [ -n "$WARNING_FILES" ]; then
                for file in $WARNING_FILES; do
                    # Find the actual file path
                    ACTUAL_FILE=$(find . -name "$(basename $file)" -path "*/java.compiler.interim/*" 2>/dev/null | head -1)
                    if [ -n "$ACTUAL_FILE" ] && [ -f "$ACTUAL_FILE" ]; then
                        echo "  Patching: $ACTUAL_FILE"
                        # Add @SuppressWarnings("removal") or comment out the problematic line
                        sed -i 's/Feature\.FOREIGN/\/\/ Feature.FOREIGN \/\/ Suppressed for Java 19 boot JDK compatibility/g' "$ACTUAL_FILE" 2>/dev/null || true
                    fi
                done
                
                echo "Patched source files. Rebuilding..."
                if make images 2>&1 | tee -a "$build_dir-build.log" | tail -20; then
                    echo ""
                    echo "✓ Build succeeded after patching"
                else
                    echo ""
                    echo "✗ Build still failed after patching"
                    echo "Check the build log: $build_dir-build.log"
                    return 1
                fi
            else
                echo "Could not identify files to patch"
                return 1
            fi
        else
            echo ""
            echo "Build failed - check the build log: $build_dir-build.log"
            return 1
        fi
    fi
    
    echo ""
    echo "Build completed at $(date)"
    
    if [ ${PIPESTATUS[0]} -ne 0 ]; then
        echo "✗ Build failed - check $build_dir-build.log"
        return 1
    fi
    
    # Find the actual build directory
    local actual_build_dir=$(find build -name "images" -type d | head -1 | xargs dirname)
    if [ -n "$actual_build_dir" ]; then
        local jdk_path="$SCRIPT_DIR/$REPO_DIR/$actual_build_dir/images/jdk"
        echo "✓ Build complete: $jdk_path"
        echo "$jdk_path" > "$build_dir.path"
    else
        echo "✗ Could not find build output"
        return 1
    fi
    
    return 0
}

# Test with reproducer
test_reproducer() {
    local jdk_path=$1
    local label=$2
    
    echo ""
    echo "=========================================="
    echo "Testing with reproducer - $label"
    echo "=========================================="
    echo "JDK: $jdk_path"
    echo ""
    
    export JAVA_HOME="$jdk_path"
    export PATH="$JAVA_HOME/bin:$PATH"
    
    echo "Java version:"
    java -version 2>&1 | head -3
    echo ""
    
    echo "Running reproducer..."
    if ./reproducer.sh 2>&1 | tee "test-$label.log"; then
        echo ""
        echo "✓ TEST PASSED - Bug is FIXED"
        return 0
    else
        echo ""
        echo "✗ TEST FAILED - Bug still present"
        return 1
    fi
}

# Setup repository
setup_repository() {
    echo "Setting up OpenJDK repository..."
    
    if [ ! -d "$REPO_DIR" ]; then
        echo "Cloning OpenJDK repository (this may take a while)..."
        git clone https://github.com/openjdk/jdk.git "$REPO_DIR"
    else
        echo "Repository exists, updating..."
        cd "$REPO_DIR"
        git fetch origin --tags 2>&1 | tail -3
        cd "$SCRIPT_DIR"
    fi
    
    cd "$REPO_DIR"
    
    # Determine the parent of FIX_COMMIT (the "before" commit)
    echo "Finding parent commit of fix..."
    if git cat-file -e "$FIX_COMMIT^" 2>/dev/null; then
        BEFORE_COMMIT=$(git rev-parse "$FIX_COMMIT^")
        echo "✓ Before commit (has bug): $(git log --oneline -1 $BEFORE_COMMIT)"
    else
        echo "✗ Could not find parent of fix commit"
        echo "Fetching commit..."
        git fetch https://github.com/openjdk/jdk.git "$FIX_COMMIT" 2>&1 | tail -3
        if git cat-file -e "$FIX_COMMIT^" 2>/dev/null; then
            BEFORE_COMMIT=$(git rev-parse "$FIX_COMMIT^")
            echo "✓ Before commit (has bug): $(git log --oneline -1 $BEFORE_COMMIT)"
        else
            echo "✗ ERROR: Could not determine before commit"
            return 1
        fi
    fi
    
    echo "Fix commit: $(git log --oneline -1 $FIX_COMMIT)"
    echo ""
    
    cd "$SCRIPT_DIR"
    return 0
}

# Main execution
main() {
    echo "Phase 1: Validation with Java 18"
    echo "=================================="
    echo ""
    
    if ! setup_repository; then
        exit 1
    fi
    
    if ! setup_boot_jdk; then
        exit 1
    fi
    
    if ! check_dependencies; then
        echo "Continue anyway? (y/n)"
        read -r answer
        if [ "$answer" != "y" ]; then
            exit 1
        fi
    fi
    
    cd "$SCRIPT_DIR"
    
    # Step 1: Build Java 18 BEFORE the fix
    echo ""
    echo "Step 1: Building Java 18 BEFORE fix (should have bug)..."
    echo "Using commit: $BEFORE_COMMIT"
    if build_jdk "$BEFORE_COMMIT" "$BUILD_DIR_BEFORE" "BEFORE FIX"; then
        BEFORE_JDK="$(cat $BUILD_DIR_BEFORE.path)"
        echo ""
        echo "Testing BEFORE fix..."
        if test_reproducer "$BEFORE_JDK" "before-fix"; then
            echo "WARNING: Test passed, but we expected it to fail!"
        else
            echo "✓ Confirmed: Bug exists before fix"
        fi
    else
        echo "Build failed - skipping test"
    fi
    
    # Step 2: Build Java 18 WITH the fix
    echo ""
    echo "Step 2: Building Java 18 WITH fix (should be fixed)..."
    if build_jdk "$FIX_COMMIT" "$BUILD_DIR_AFTER" "AFTER FIX"; then
        AFTER_JDK="$(cat $BUILD_DIR_AFTER.path)"
        echo ""
        echo "Testing WITH fix..."
        if test_reproducer "$AFTER_JDK" "after-fix"; then
            echo "✓ Confirmed: Bug is fixed!"
        else
            echo "WARNING: Test failed, but we expected it to pass!"
        fi
    else
        echo "Build failed - skipping test"
    fi
    
    echo ""
    echo "=========================================="
    echo "Phase 1 Complete"
    echo "=========================================="
    echo ""
    echo "Results:"
    echo "  Before fix: $(if [ -f test-before-fix.log ]; then grep -q 'Conversion succeeded' test-before-fix.log && echo 'PASSED' || echo 'FAILED'; else echo 'NOT TESTED'; fi)"
    echo "  After fix:  $(if [ -f test-after-fix.log ]; then grep -q 'Conversion succeeded' test-after-fix.log && echo 'PASSED' || echo 'FAILED'; else echo 'NOT TESTED'; fi)"
    echo ""
    
    echo "Phase 2: Explore Java 17 backport"
    echo "=================================="
    echo ""
    echo "The fix modifies inline_divmod_methods() which doesn't exist in Java 17."
    echo "However, the same bug pattern might exist elsewhere."
    echo ""
    echo "Would you like to explore adapting the fix for Java 17? (y/n)"
    read -r answer
    if [ "$answer" = "y" ]; then
        explore_java17_backport
    fi
}

# Explore Java 17 backport
explore_java17_backport() {
    echo ""
    echo "Exploring Java 17 backport..."
    echo ""
    
    cd "$SCRIPT_DIR/$REPO_DIR" || {
        echo "✗ ERROR: Could not cd to repository"
        return 1
    }
    
    # Checkout Java 17
    JAVA17_TAG=$(git tag | grep -E "^jdk-17\+35$" | head -1)
    if [ -z "$JAVA17_TAG" ]; then
        JAVA17_TAG=$(git tag | grep "^jdk-17" | sort -V | tail -1)
    fi
    
    echo "Checking out Java 17: $JAVA17_TAG"
    git checkout "$JAVA17_TAG" 2>&1 | tail -3
    
    echo ""
    echo "The fix changes 'return false' to 'return true' when stopped() is true."
    echo "Searching for similar patterns in Java 17..."
    echo ""
    
    # Search for the pattern
    echo "Patterns found in library_call.cpp:"
    grep -n "if (stopped())" src/hotspot/share/opto/library_call.cpp | grep -A 2 "return false" | head -20 || echo "No exact matches found"
    
    echo ""
    echo "The bug might be in how compareUnsigned is handled."
    echo "In Java 17, compareUnsigned is not an intrinsic, so the bug might be"
    echo "in general inlining behavior rather than intrinsic-specific code."
    echo ""
    echo "To proceed with Java 17 backport, we would need to:"
    echo "1. Identify where the same control flow issue exists"
    echo "2. Apply similar fix pattern"
    echo "3. Build and test"
    echo ""
}

# Run main
main "$@"
