# Chicory Native PoC: Cranelift + Panama

Proof of concept for executing Cranelift-compiled native machine code from Java via Panama FFM, with zero shipped native libraries.

## What this proves

The full pipeline works end-to-end with zero native libraries:

1. Cranelift (itself compiled to Wasm) **runs inside Chicory** as a WASI module
2. It compiles a Wasm function to native x86_64 machine code
3. The native code is made executable via `mmap` + `mprotect` (called through Panama)
4. Panama executes the native code via a `downcallHandle`

All four execution paths produce the same result:
```
=== Chicory Native PoC ===
Testing: add(17, 25) = 42

--- Chicory Interpreter ---
Result: 42
OK!

--- Chicory AOT Compiler ---
Result: 42
OK!

--- Cranelift-in-Chicory compilation ---
  Cranelift stderr: Compiled 41 bytes of wasm to 13472 bytes of native code
  Cranelift produced 13472 bytes of ELF
  Extracted 4096 bytes from .text section

--- Panama Native (Cranelift-in-Chicory compiled) ---
Result: 42
OK!

=== All four paths produced correct results! ===
  Cranelift compiled Wasm to native x86_64 INSIDE Chicory,
  then Panama executed the native code. Zero native libs.
```

## Architecture

```
add.wasm
    |
    +-- Chicory Interpreter -----------> result (42)
    |
    +-- Chicory AOT (JVM bytecode) ----> result (42)
    |
    +-- cranelift-compiler.wasm          (Cranelift compiled to Wasm)
        |   running inside Chicory via WASI
        |
        +-> native x86_64 ELF
            |
            +-- extract .text section
            |
            +-- Panama mmap + mprotect
            |
            +-- Panama downcall -------> result (42)
```

## How the native code is produced

```
add.wat  --[wat2wasm]-->  add.wasm  --[wasmtime compile]-->  add.cwasm (ELF)  --[objcopy]-->  add_func.bin
```

1. Write a Wasm function in WAT (`native/add.wat`)
2. Compile to `.wasm` with `wat2wasm`
3. Compile to native x86_64 with `wasmtime compile` (uses Cranelift) — produces a `.cwasm` ELF file
4. Extract the raw function bytes from the `.text` section with `objcopy -O binary -j .text`

The resulting `add_func.bin` is just 12 bytes of x86_64 machine code:
```asm
push   %rbp
mov    %rsp,%rbp
lea    (%rdx,%rcx,1),%eax   # eax = a + b
mov    %rbp,%rsp
pop    %rbp
ret
```

## How Panama executes it

### 1. Allocate writable memory (no native lib needed)

Panama can call libc functions directly via `Linker.nativeLinker().defaultLookup()`:

```java
var mmap = linker.downcallHandle(lookup.find("mmap").orElseThrow(), ...);
MemorySegment codeAddr = (MemorySegment) mmap.invoke(
    MemorySegment.NULL, pageSize,
    PROT_READ | PROT_WRITE,
    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0L);
```

### 2. Copy the native code bytes

```java
var writableCode = codeAddr.reinterpret(pageSize);
MemorySegment.copy(MemorySegment.ofArray(codeBytes), 0, writableCode, 0, codeBytes.length);
```

### 3. Make it executable (W^X safe)

```java
mprotect.invoke(codeAddr, pageSize, PROT_READ | PROT_EXEC);
```

### 4. Create a downcall handle and call it

```java
var funcDescriptor = FunctionDescriptor.of(
    JAVA_INT,    // return: i32 in eax
    ADDRESS,     // arg0 → rdi: vmctx (unused, pass NULL)
    JAVA_LONG,   // arg1 → rsi: dummy (unused)
    JAVA_INT,    // arg2 → rdx: param a
    JAVA_INT     // arg3 → rcx: param b
);
MethodHandle nativeAdd = linker.downcallHandle(writableCode, funcDescriptor);
int result = (int) nativeAdd.invoke(MemorySegment.NULL, 0L, 17, 25);
// result = 42
```

Panama generates a trampoline stub that places Java arguments into CPU registers
following the System V x86_64 ABI (`rdi`, `rsi`, `rdx`, `rcx`, `r8`, `r9`), then
jumps to the mmapped address.

### Why the dummy `rsi` argument?

Cranelift's wasmtime calling convention reserves `rdi` for VMContext and `rsi` for
caller VMContext. Wasm parameters start at `rdx`. We pad with a dummy `rsi` arg so
our actual params (`a`, `b`) land in `rdx` and `rcx` where Cranelift expects them.

## How to build and run

```bash
# Compile
mvn compile -pl native-poc -am -Pdev -q

# Get the full classpath
mvn -pl native-poc dependency:build-classpath -Pdev -q -Dmdep.outputFile=/tmp/cp.txt

# Run (requires Java 21+ with preview features)
java --enable-preview --enable-native-access=ALL-UNNAMED \
  -cp "native-poc/target/classes:$(cat /tmp/cp.txt)" \
  com.dylibso.chicory.nativepoc.NativePoc
```

## Regenerating native code from WAT

```bash
cd native-poc/native
wat2wasm add.wat -o add.wasm
wasmtime compile add.wasm -o add.cwasm
objcopy -O binary -j .text add.cwasm add_text.bin
dd if=add_text.bin of=add_func.bin bs=1 count=12
cp add_func.bin ../src/main/resources/add_func.bin
```

Use `objdump -d add.cwasm` to see the disassembly and determine the function size for the `dd count=` parameter.

## Next steps

### Step 1: Panama-based Machine implementation

Write a `Machine` implementation that uses Panama to execute Cranelift-compiled native code.
This plugs into Chicory's existing dispatch via `Instance.builder(module).withMachineFactory(...)`.

The Machine would:
- At construction time, load pre-compiled native code bytes (from wasmtime compile)
- mmap + mprotect the code
- Create Panama downcall handles for each compiled function
- Set up a VMContext struct with the linear memory base pointer
- On `call(funcId, args)`: dispatch to the Panama downcall handle for that function

This follows the same `Machine` interface as the interpreter and AOT compiler,
so it integrates with the existing `CompilerInterpreterMachine` hybrid dispatch pattern.

### Step 2: Test with linear memory

Test with a function that uses linear memory (sieve, etc.) to validate
VMContext/memory-base-pointer passing via Panama.

### Step 3: Benchmark

Benchmark Panama downcall overhead vs Chicory AOT for compute-heavy functions.

### Step 4: Cranelift-in-Wasm

Compile Cranelift itself to wasm32-wasip1 (Tier 3 supported by wasmtime), wrap it
using Chicory's build-time compiler (following the wabt/wasm-tools pattern), and use
it to compile Wasm functions to native code at runtime — all within the JVM.

Alternatively, wasmtime can be used directly as the native code producer at build time,
with the compiled native code shipped as resources alongside the Wasm module.
