#!/bin/bash

export JAVA_HOME="/home/andreatp/workspace/jdk/jdk17-buggy-fastdebug/jdk"
export PATH="${JAVA_HOME}/bin:${PATH}"

cd "$(dirname "$0")" || exit 1

/home/andreatp/workspace/jdk/jdk17-buggy-fastdebug/jdk/bin/java \
    -cp "$(cat cp.txt):target/classes" \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:+ReplayCompiles \
    -XX:+PrintCompilation -XX:+PrintInlining -XX:+PrintOptoAssembly \
    -XX:ReplayDataFile=/home/andreatp/workspace/chicory6/wabt/replay_FIXED.log
