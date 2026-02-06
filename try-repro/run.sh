
java \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:CompileThreshold=1 \
  -XX:-TieredCompilation \
  UnsignedCompareBug
