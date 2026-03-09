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

## Results

### What we proved

The full end-to-end pipeline works:

1. A 30-line Rust wrapper around wasmtime's `Engine::precompile_module()` compiles
   to a **6.8MB Wasm binary** (`cranelift-compiler.wasm`)
2. Chicory runs it as a WASI module — Cranelift compiles `add.wasm` (41 bytes)
   into a **13KB native x86_64 ELF**
3. A minimal ELF parser extracts the `.text` section
4. Panama `mmap` + `mprotect` makes the code executable
5. Panama `downcallHandle` calls into the native code — **result: 42**

All with zero shipped native libraries. The only "native" dependency is libc
(always available), accessed through Panama's `Linker.nativeLinker()`.

### Key numbers

| What | Size |
|------|------|
| cranelift-compiler.wasm | 6.8 MB |
| Input: add.wasm | 41 bytes |
| Output: native ELF | 13,472 bytes |
| Function machine code | 12 bytes |

### Cranelift calling convention

Cranelift (wasmtime) uses a specific register layout:
- `rdi` = VMContext pointer (holds linear memory base at +0x40)
- `rsi` = caller VMContext (unused for simple calls)
- `rdx`, `rcx`, `r8`, `r9` = Wasm function parameters
- Return value in `rax`/`eax`

For the `add` function, Panama bridges this by passing:
`(NULL vmctx, 0L dummy, a, b)` → maps to `rdi, rsi, rdx, rcx` via System V ABI.

## Next steps

### Step 1: Test with a real program using linear memory

The `add` function is trivial — it doesn't use linear memory. The next test
should use a function that reads/writes memory (e.g., sieve of Eratosthenes,
string processing, or a real-world module like SQLite or Prism).

This requires:
- Allocating contiguous off-heap memory for the Wasm linear memory
- Setting up a VMContext struct with the memory base pointer at offset +0x40
- Passing the VMContext pointer to the native function via `rdi`

### Step 2: Panama-based Machine implementation

Write a `Machine` implementation that uses Panama to execute native code.
This plugs into Chicory's existing `withMachineFactory()` API:

```java
public class NativeMachine implements Machine {
    @Override
    public long[] call(int funcId, long[] args) {
        // Dispatch to Panama downcall for this function
    }
}
```

Key challenges:

**VMContext**: Native code accesses linear memory via a VMContext struct pointer
(memory base at offset +0x40). Need to allocate this off-heap and keep it in sync
with Chicory's Memory object.

**CALL**: When a native function calls another Wasm function, Cranelift emits a
direct `call` to a known address. In wasmtime's ELF, these are relocations. We'd
need to either:
- Resolve relocations when loading (patch call targets in the native code)
- Or use a function pointer table: native code calls through a table, and we
  populate the table with addresses of other mmapped functions

**CALL_INDIRECT**: Table-based dispatch. The native code looks up a function
reference from a table and calls it. We'd need to provide the table data in the
VMContext. For functions that are native-compiled, the table entry is the native
address. For functions that are JVM-compiled or interpreted, we'd need a trampoline
that transitions back to Java via Panama upcall.

**Host function callbacks (imports)**: When native code calls a Wasm import
(e.g., WASI fd_write), it needs to call back into Java. This requires Panama
upcall stubs registered in a function pointer table accessible from the VMContext.

**Mixed execution**: Some functions may be native, others JVM bytecode (AOT), others
interpreted. The dispatch in `Machine.call()` already handles this, but direct calls
between native functions need to handle cross-tier transitions.

### Step 3: Benchmark on a real workload

Compare execution time for a compute-heavy module (SQLite, Prism, or a crypto hash)
across all three tiers:
- Chicory interpreter
- Chicory AOT (JVM bytecode)
- Cranelift native via Panama

### Step 4: Wrap Cranelift with Chicory build-time compiler

Follow the wabt/wasm-tools pattern:
- Use `chicory-compiler-maven-plugin` to compile `cranelift-compiler.wasm` to
  Java bytecode at build time
- This makes the Cranelift compilation step itself run as fast as Chicory AOT
  instead of being interpreted
- Ship as a Maven artifact with zero native dependencies
