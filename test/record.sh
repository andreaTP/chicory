
# -XX:CompileCommand=option,ReplayTest::hotMethod,DumpReplay \

/home/andreatp/workspace/jdk/build/linux-x86_64-server-fastdebug/jdk/bin/java \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:ReplayDataFile=replay.txt \
    -XX:CompileThreshold=1 \
    -XX:CompileCommand=option,DumpReplay \
    -XX:+PrintCompilation \
    ReplayTest
