#!/bin/bash
# Quick test with different function counts

cd /home/andreatp/workspace/chicory4
mvn -q -pl wabt,wasm-corpus -DskipTests test-compile > /dev/null 2>&1

for count in 50000 25000 10000 5000 1000 500 100 50 10 5 1; do
    echo "Testing $count functions..."
    java -cp "wasm-corpus/target/test-classes:wasm-corpus/target/classes:$(mvn -q -pl wasm-corpus dependency:build-classpath -Dmdep.outputFile=/dev/stdout 2>/dev/null)" \
         com.dylibso.chicory.corpus.WatGenerator $count 0 > /tmp/test-$count.wat 2>&1
    
    timeout 30 mvn -q -pl wabt exec:java \
        -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
        -Dexec.classpathScope=test \
        -Dspotless.check.skip=true \
        -Dspotless.apply.skip=true \
        2>&1 | grep -E "(out of bounds|Conversion succeeded|ERROR)" | head -1 && echo "  → Result above" || echo "  → No clear result"
    echo
done
