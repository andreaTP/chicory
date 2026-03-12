# Running the Reproducer on a Buggy JVM

## Quick Start

### Option 1: Using the Script (Recommended)

```bash
# Simple version
./run-unsigned-reproducer-simple.sh

# Or full version with explicit classpath
./run-unsigned-reproducer.sh
```

### Option 2: Manual Command

```bash
# Build first
mvn -q -pl wabt test-compile -Dspotless.check.skip=true -Dspotless.apply.skip=true

# Run with buggy JDK
JAVA_HOME=/path/to/buggy/jdk ./run-unsigned-reproducer.sh
```

## Using a Specific Buggy JDK

### If you have a custom-built JDK:

```bash
# Set JAVA_HOME to the buggy JDK
export JAVA_HOME=/path/to/buggy/jdk
export PATH=$JAVA_HOME/bin:$PATH

# Verify JDK version
java -version

# Run the reproducer
./run-unsigned-reproducer.sh
```

### Example with custom JDK:

```bash
# If you built JDK from commit dfacda488bf (known to reproduce)
export JAVA_HOME=/home/andreatp/workspace/jdk/build/linux-x86_64-server-release/images/jdk
export PATH=$JAVA_HOME/bin:$PATH

# Run
./run-unsigned-reproducer.sh
```

## JVM Flags Used

The reproducer uses these flags to force C2 compilation:

- `-XX:+UnlockDiagnosticVMOptions` - Enable diagnostic flags
- `-XX:+PrintCompilation` - Print compilation events
- `-XX:-TieredCompilation` - Disable tiered compilation (C1/C2), use C2 only
- `-XX:CompileThreshold=1000` - Lower threshold to trigger compilation faster

## Expected Behavior

### On Buggy JDK (before fix commit f3eb5014aa7):
```
Test 1: Comparing -2147483648 (0x80000000) vs 0
  Result: -1  (or 0, or wrong value)
  Expected: 1
  ✗ BUG REPRODUCED! Got -1 but expected 1
```

### On Fixed JDK:
```
Test 1: Comparing -2147483648 (0x80000000) vs 0
  Result: 1
  Expected: 1
Test 2: Comparing -1 (0xffffffff) vs 0
  Result: 1
  Expected: 1
✓ All comparisons correct
```

## Function Invocations

- **Warmup**: 50,000 invocations (ensures C2 compilation)
- **Test 1**: 10,000 invocations (MIN_VALUE vs 0)
- **Test 2**: 10,000 invocations (-1 vs 0)
- **Total**: 70,000 invocations

With `-XX:CompileThreshold=1000`, C2 compilation should trigger after ~1,000 invocations.

## Pattern Generated

The reproducer generates bytecode that creates:
```
Integer.compare(a + MIN_VALUE, b + MIN_VALUE)
```

This creates the C2 IR pattern:
```
cmp (add a min_jint) (add b min_jint)
```

Which triggers the buggy `BoolNode::Ideal` optimization on buggy JDKs.
