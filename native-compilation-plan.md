# Chicory Native Compilation: Cranelift + Panama

## Problem

Large Wasm functions compiled to JVM bytecode by Chicory's AOT compiler may not be well-optimized by the JVM's C2 JIT.
Big methods won't get inlined, and the Java interpreter is slow for unoptimized code.
This creates a performance ceiling for compute-heavy Wasm modules.

## Idea

Compile a native Wasm-to-machine-code compiler (Cranelift) to Wasm itself, run it inside Chicory, get native x86_64/aarch64 machine code bytes out, then execute them via Panama/FFM — all with zero shipped native libraries.

## Architecture

```
Wasm module
    |
    +-- Most functions ------> Chicory AOT ------> JVM bytecode (current path)
    |
    +-- Large/hot functions --> Cranelift-in-Wasm -> native x86_64/aarch64 bytes
                                                         |
                                                    Panama mmap
                                                         |
                                                    executable memory
                                                         |
                                                    Panama downcall
```

This creates a tiered compilation system:
- **Tier 1**: Interpreter (fallback, always available)
- **Tier 2**: JVM bytecode via Chicory AOT (current compiler, works everywhere)
- **Tier 3**: Native machine code via Cranelift + Panama (optional, best performance)

## Step 1: Build Cranelift as a Wasm Module

Cranelift is Wasmtime's code generator, written in Rust. Wasmtime officially documents that `cranelift` compiled to `wasm32-wasip1` can **compile** Wasm but cannot **execute** it (Tier 3: "supported but not tested"). This is exactly what we need — a pure code generator.

**Tasks:**
- Compile `cranelift-codegen` targeting `wasm32-wasip1`, stripping the runtime/execution parts
- Expose a minimal API: `compile(wasm_func_bytes, target_arch) -> native_code_bytes`
- The resulting `.wasm` file ships as a resource in a Chicory module
- Cranelift supports x86_64, aarch64, s390x, and riscv64 backends

**Alternative:** WARP (github.com/wasm-ecosystem/wasm-compiler) — a tiny C++14 Wasm-to-native compiler with zero dependencies (187KB binary). Could be compiled to Wasm via wasi-sdk. Simpler but less mature than Cranelift.

**Key question:** How large is the compiled Cranelift Wasm module? Needs experimentation.

## Step 2: Run Cranelift Inside Chicory

- At startup (or lazily on first use), instantiate the Cranelift Wasm module using Chicory's existing interpreter or AOT-to-bytecode compiler
- Feed individual Wasm function bodies to Cranelift, get back native machine code bytes
- This is a **one-time cost per function** — cache the compiled output
- Compilation can happen in a background thread while the function runs via JVM bytecode

## Step 3: Make Native Code Executable via Panama

No native libraries needed — call libc directly via `Linker.nativeLinker()`:

```java
// 1. Allocate writable memory
var addr = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);

// 2. Copy native code bytes
memcpy(addr, codeBytes, size);

// 3. Make executable (W^X safe)
mprotect(addr, size, PROT_READ | PROT_EXEC);

// 4. Create a callable handle to the native function
var funcHandle = Linker.nativeLinker().downcallHandle(addr, functionDescriptor);
```

**Convention:** Pass linear memory base pointer + Wasm function args as parameters, get result back.

**Windows:** Uses `VirtualAlloc` / `VirtualProtect` instead of `mmap` / `mprotect`. Panama can call `kernel32.dll` — needs platform-specific dispatch.

## Step 4: Integrate with Chicory's Function Dispatch

Chicory already supports mixing execution backends per-function via `CompilerInterpreterMachine`. Extend this to a three-tier `HybridMachine`:

```
Machine.call(funcId, args)
    |
    +-- native-compiled?  --> Panama downcall to mmapped code
    +-- AOT-compiled?     --> INVOKESTATIC to FuncGroup (current)
    +-- fallback          --> interpreter
```

- Inter-function calls between tiers go through `Machine.call(funcId, args)` — already works
- Direct calls within the JVM bytecode tier stay fast (INVOKESTATIC)
- The `Machine` interface (`call(int funcId, long[] args) -> long[]`) is the universal dispatch point

## Step 5: Memory Layout Changes

Wasm linear memory must be a **single contiguous off-heap allocation** for native code to access via raw pointers (the current `ByteBuffer[]` page array breaks contiguity).

**Options:**
- Panama `MemorySegment` backed by a single `mmap` (preferred)
- Single large `DirectByteBuffer`
- `Unsafe.allocateMemory` (less safe)

**Bounds checking in native code:**
- Option A: Cranelift emits explicit bounds checks (safest)
- Option B: Guard pages — `mmap` extra pages as `PROT_NONE` after valid memory, out-of-bounds access triggers SIGSEGV caught by a signal handler

**The base address** is passed to native functions at call time so they can access linear memory.

## Step 6: Handle Host Function Callbacks (Imports)

When native code calls a Wasm import (host function), it needs to call back into Java. This is the hardest part.

**Option A: Panama upcall stubs (preferred)**
- Pre-register a Panama upcall stub for each import function
- Pass a table of function pointers to native code
- Native code calls the stub, which transitions back into Java via Panama upcall
- Adds some overhead per callback but is clean and correct

**Option B: Shared call buffer**
- Native code writes import funcId + args to a known memory location
- Returns control to Java with a special return code
- Java dispatches the import, writes the result back, re-enters native code
- Simpler but slower and more complex control flow

## Constraints and Trade-offs

### What you get
- **Zero native dependencies** in the distribution — Cranelift ships as `.wasm`, Panama uses system libc
- **Near-native execution speed** for large functions that C2 can't optimize
- **Graceful fallback** — if Panama isn't available (old JDK, restricted env), everything works via JVM bytecode
- **Per-function opt-in** — only compile to native what actually benefits from it

### What you give up
- **Minimum Java version**: Panama/FFM is stable from Java 22. This tier would be optional and only available on 22+
- **GC interaction**: Long-running native functions block JVM safepoints (and thus GC). Cranelift could potentially emit periodic yield points, but this needs investigation
- **Platform coverage**: Need separate code paths for mmap (Unix) vs VirtualAlloc (Windows)
- **Complexity**: Three execution tiers + memory layout changes + host callbacks is significant engineering

### Risks
- Cranelift-to-wasm32 is Tier 3 ("supported but not tested") — may require upstream fixes
- Cranelift Wasm module size could be large — impacts startup and memory
- Host function callbacks via Panama upcalls add latency — matters for import-heavy modules
- W^X enforcement on some platforms (macOS hardened runtime, SELinux) may block mmap+mprotect

## Open Questions

1. How big is Cranelift compiled to Wasm? Is the startup cost acceptable?
2. Can Cranelift emit safepoint-equivalent yield points for long-running functions?
3. What's the performance delta between Panama downcall overhead (~50ns) and the native execution speedup for realistic Wasm workloads?
4. Should this be build-time only (compile Wasm to native at build time, ship the machine code) or also runtime?
5. Is WARP a better starting point than Cranelift for a proof of concept due to its simplicity?

## PoC Progress

### Done: Panama mmap + execute (native-poc/)

Validated that Panama can mmap Cranelift-compiled native code and execute it:

1. Wrote `add(i32, i32) -> i32` in WAT
2. Compiled to native x86_64 via `wasmtime compile` (Cranelift)
3. Extracted raw function bytes from the ELF .text section
4. Used Panama to mmap, mprotect(READ|EXEC), and downcall the native code
5. All three paths (interpreter, AOT, Panama native) produce correct results

See `native-poc/README.md` for details.

### Next: Panama-based Machine implementation

Write a `Machine` implementation that uses Panama to execute native code. This is the
integration point with Chicory's runtime:

```java
public class NativeMachine implements Machine {
    // Pre-loaded native code (mmapped, executable)
    // Panama downcall handles per function
    // VMContext struct with linear memory base pointer

    @Override
    public long[] call(int funcId, long[] args) {
        // Dispatch to Panama downcall for this function
    }
}
```

This plugs into Chicory's existing API:
```java
Instance.builder(module)
    .withMachineFactory(NativeMachine::new)
    .build();
```

### Then: Native code production

Two options for producing native code from Wasm:

**Option A: wasmtime at build time** — Use `wasmtime compile` to produce native code at
build time, ship the compiled code as resources. Simpler, requires wasmtime on the build
machine but not at runtime.

**Option B: Cranelift-in-Wasm** — Compile Cranelift itself to wasm32-wasip1, wrap it using
Chicory's build-time compiler (following the wabt/wasm-tools pattern). This enables runtime
compilation entirely within the JVM. More complex but fully self-contained.
