#!/bin/bash
# Simple script to run UnsignedComparisonReproducer

cd "$(dirname "$0")"

# Build and run
mvn -q -pl wabt exec:java \
  -Dexec.mainClass="com.dylibso.chicory.wabt.UnsignedComparisonReproducer" \
  -Dexec.classpathScope=test \
  -Dspotless.check.skip=true \
  -Dspotless.apply.skip=true \
  -Dexec.jvmArgs="-XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation -XX:-TieredCompilation -XX:CompileThreshold=1000"
