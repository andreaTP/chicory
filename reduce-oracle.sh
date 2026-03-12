#!/usr/bin/env bash
set -euo pipefail

if mvn -q -pl wabt clean test-compile exec:java \
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
        exit 0
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
    exit 1
fi
