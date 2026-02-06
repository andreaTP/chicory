#!/bin/bash

export JAVA_HOME="/home/andreatp/workspace/jdk/jdk17-buggy-fastdebug/jdk"
export PATH="${JAVA_HOME}/bin:${PATH}"

cd "$(dirname "$0")" || exit 1

# mvn compile dependency:build-classpath -pl wabt \
#   -DincludeScope=runtime \
#   -Dmdep.outputFile=cp.txt

# -XX:+UnlockDiagnosticVMOptions \
#   -XX:+ReplayCompiles \
#   -XX:ReplayDataFile=replay.txt \

# Run Main with desired JVM options
# /home/andreatp/workspace/jdk/build/linux-x86_64-server-fastdebug/jdk/bin/java \
# -XX:+UnlockDiagnosticVMOptions \
#   -XX:+ReplayCompiles \
#   -XX:ReplayDataFile=/home/andreatp/workspace/chicory6/wabt/buggy-replay.txt \
#   -XX:+PrintCompilation \
#   -cp "$(cat cp.txt):target/classes" \
#   com.dylibso.chicory.wabt.Main

# /home/andreatp/workspace/jdk/build/linux-x86_64-server-fastdebug/jdk/bin/java \

# WORKING COMMAND
# /home/andreatp/workspace/jdk/jdk17-fixed-fastdebug/jdk/bin/java \
#     -XX:+UnlockDiagnosticVMOptions \
#     -XX:ReplayDataFile=replay.txt \
#     -XX:CompileThreshold=1 \
#     -XX:CompileCommand=option,java/lang/Integer.compareUnsigned,DumpReplay \
#     -XX:CompileCommand=option,java/lang/Long.compareUnsigned,DumpReplay \
#     -XX:+PrintCompilation \
#     -cp "$(cat cp.txt):target/classes" \
#     com.dylibso.chicory.wabt.Main

/home/andreatp/workspace/jdk/jdk17-buggy-fastdebug/jdk/bin/java \
    -cp "$(cat cp.txt):target/classes" \
    com.dylibso.chicory.wabt.Main
