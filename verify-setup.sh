#!/bin/bash
# Quick verification script to check setup before building

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REPO_DIR="openjdk-bisect"
FIX_COMMIT="a81c5d3a23163164a79763421935d0262a36f27e"

echo "=========================================="
echo "Verifying Setup for JDK-8376400 Validation"
echo "=========================================="
echo ""

# Check repository
echo "1. Checking repository..."
if [ ! -d "$REPO_DIR" ]; then
    echo "   ✗ Repository does not exist"
    echo "   Will clone it..."
else
    echo "   ✓ Repository exists"
    cd "$REPO_DIR"
    echo "   Current commit: $(git log --oneline -1)"
    echo "   Current branch: $(git branch --show-current 2>/dev/null || echo 'detached HEAD')"
    cd "$SCRIPT_DIR"
fi
echo ""

# Check commits
echo "2. Checking commits..."
cd "$REPO_DIR" 2>/dev/null || {
    echo "   Repository not available yet"
    cd "$SCRIPT_DIR"
    echo ""
    exit 0
}

echo "   Fetching commits..."
git fetch https://github.com/openjdk/jdk.git "$FIX_COMMIT" 2>&1 | tail -2 || true

if git cat-file -e "$FIX_COMMIT" 2>/dev/null; then
    echo "   ✓ Fix commit exists: $(git log --oneline -1 $FIX_COMMIT)"
    
    if git cat-file -e "$FIX_COMMIT^" 2>/dev/null; then
        BEFORE_COMMIT=$(git rev-parse "$FIX_COMMIT^")
        echo "   ✓ Before commit (parent): $(git log --oneline -1 $BEFORE_COMMIT)"
        echo ""
        echo "   Commits to test:"
        echo "   - BEFORE (has bug): $BEFORE_COMMIT"
        echo "   - AFTER (fixed):    $FIX_COMMIT"
    else
        echo "   ✗ Could not find parent commit"
    fi
else
    echo "   ✗ Fix commit not found - need to fetch"
fi
echo ""

# Check boot JDK (Java 18 or 19 required for building Java 18)
echo "3. Checking boot JDK (Java 18 or 19 required)..."
if command -v sdk &> /dev/null; then
    export SDKMAN_DIR="$HOME/.sdkman"
    source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || true
    BOOT_JDK=$(sdk home java 19.0.1-tem 2>/dev/null || \
               sdk home java 19.0.2-tem 2>/dev/null || \
               sdk home java 18.0.1-tem 2>/dev/null || \
               sdk home java 18.0.2-tem 2>/dev/null || \
               echo "")
    if [ -n "$BOOT_JDK" ] && [ -d "$BOOT_JDK" ]; then
        JAVA_VERSION=$("$BOOT_JDK/bin/java" -version 2>&1 | head -1)
        echo "   ✓ Boot JDK: $BOOT_JDK"
        echo "   $JAVA_VERSION"
        if echo "$JAVA_VERSION" | grep -qE "version \"(18|19)"; then
            echo "   ✓ Java 18/19 detected (required)"
        else
            echo "   ✗ Wrong version - need Java 18 or 19"
            echo "   Install: sdk install java 19.0.1-tem"
        fi
    else
        echo "   ✗ Boot JDK not found via SDKMAN"
        echo "   Install: sdk install java 19.0.1-tem"
        echo "   Or: sdk install java 18.0.1-tem"
    fi
else
    if [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ]; then
        JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | head -1)
        echo "   ✓ Boot JDK: $JAVA_HOME"
        echo "   $JAVA_VERSION"
        if echo "$JAVA_VERSION" | grep -qE "version \"(18|19)"; then
            echo "   ✓ Java 18/19 detected (required)"
        else
            echo "   ✗ Wrong version - need Java 18 or 19"
        fi
    else
        echo "   ✗ No boot JDK found"
        echo "   Set JAVA_HOME to a Java 18 or 19 installation"
    fi
fi
echo ""

# Check dependencies
echo "4. Checking build dependencies..."
MISSING=""
for dep in gcc g++ make autoconf; do
    if command -v $dep &> /dev/null; then
        echo "   ✓ $dep: $(command -v $dep)"
    else
        echo "   ✗ $dep: NOT FOUND"
        MISSING="$MISSING $dep"
    fi
done

if [ -n "$MISSING" ]; then
    echo ""
    echo "   Install missing dependencies:"
    echo "   sudo dnf install$MISSING"
fi
echo ""

cd "$SCRIPT_DIR"
echo "=========================================="
echo "Setup verification complete"
echo "=========================================="
