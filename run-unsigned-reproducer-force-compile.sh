#!/bin/bash
# Force compilation with aggressive flags

cd "$(dirname "$0")"

mvn -q -pl wabt test-compile -Dspotless.check.skip=true -Dspotless.apply.skip=true

CLASSPATH=$(mvn -q -pl wabt dependency:build-classpath -Dmdep.outputFile=/dev/stdout -Dspotless.check.skip=true -Dspotless.apply.skip=true 2>/dev/null)
CLASSPATH="wabt/target/test-classes:wabt/target/classes:$CLASSPATH"

# More aggressive compilation flags
java \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:+PrintCompilation \
  -XX:-TieredCompilation \
  -XX:CompileThreshold=100 \
  -XX:CompileCommand="compileonly,com.dylibso.chicory.wabt.UnsignedComparisonReproducer::*" \
  -XX:CompileCommand="compileonly,GeneratedComparison::*" \
  -Xbatch \
  -cp "$CLASSPATH" \
  com.dylibso.chicory.wabt.UnsignedComparisonReproducer
