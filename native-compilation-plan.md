# Chicory Native Compilation: Cranelift + Panama

## Problem

Large Wasm functions compiled to JVM bytecode by Chicory's AOT compiler may not be
well-optimized by the JVM's C2 JIT. Big methods won't get inlined, and the Java
interpreter is slow for unoptimized code. This creates a performance ceiling for
compute-heavy Wasm modules.

## Architecture

```
Java (compilation logic)                 Rust/Wasm (thin Cranelift bridge)
========================                 =================================

NativeCompiler walks opcodes             cranelift-bridge.wasm exports:
  I32_ADD:
    a = pop()               ──────────►  emit_iadd(a, b) -> value_id
    b = pop()
    result = bridge.iadd(a,b)            internally:
    push(result)                           builder.ins().iadd(a, b)

  I32_STORE:
    bridge.storeI32(...)    ──────────►  emit_store_i32(base, addr, val, offset)

  compile()                 ──────────►  compile() -> code bytes in linear memory
    read native bytes from bridge memory
    mmap + Panama downcall
```

| Chicory JVM Compiler | Cranelift Native Compiler |
|---|---|
| `Emitters` → `asm.visitInsn(IADD)` | `NativeCompiler` → `bridge.emitIadd(a, b)` |
| ASM `InstructionAdapter` | Cranelift `FunctionBuilder` (in Wasm) |
| Output: JVM `.class` files | Output: native x86_64/aarch64 bytes |

## Module structure

```
cranelift-bridge/                       Thin Cranelift FFI wrapper
├── pom.xml
├── README.md
├── rust-src/                           Rust source (builds to Wasm)
│   ├── Cargo.toml
│   ├── build.sh                        ./build.sh to rebuild
│   └── src/lib.rs                      ~200 lines, direct FunctionBuilder calls
└── src/main/
    ├── java/.../CraneliftBridge.java   Java wrapper with typed methods
    └── resources/cranelift-bridge.wasm Pre-built binary (3.5MB)

cranelift-compiler/                     Native compiler + spec tests
├── pom.xml                             test-gen-plugin with i32.wast
└── src/
    ├── main/java/.../compiler/
    │   ├── MachineFactoryNative.java   Public API (mirrors MachineFactoryCompiler)
    │   ├── NativeMachine.java          Machine impl with Panama downcalls
    │   ├── NativeCompiler.java         Walks opcodes, calls bridge
    │   └── PanamaExecutor.java         mmap/mprotect helpers
    └── test/java/.../testing/
        ├── TestModule.java             Injects MachineFactoryNative::compile
        ├── Spectest.java               Standard spectest host module
        └── ArgsAdapter.java            Test helper

```

## Progress

### Done

1. **Panama mmap + execute** — proved native code can be mmapped and called via Panama
2. **Cranelift-in-Chicory** — Cranelift compiled to wasm32-wasip1 (3.5MB), runs inside
   Chicory, produces native x86_64 from FunctionBuilder API calls driven by Java
3. **Direct-call bridge** — no accumulate-and-replay, each export immediately calls
   Cranelift (using raw pointers to keep FunctionBuilder alive across FFI calls)
4. **Explicit value IDs** — Java tracks all value/block/var IDs, Rust side is a pure
   handle-to-object mapping layer
5. **Module infrastructure** — `cranelift-bridge` and `cranelift-compiler` as proper
   Maven modules with test-gen-plugin, `@WasmModuleInterface` annotation for bridge
6. **NativeMemory** — off-heap contiguous memory via Panama `MemorySegment`, integrated
   with Instance via `withMemoryFactory(NativeMemory::new)`
7. **i32 arithmetic opcodes** — all i32 math, bitwise, shift, rotate, comparison opcodes
8. **const.wast fully green** — 778/778 spec tests pass (i32/i64/f32/f64 const + drop)
9. **AddTest + AddAndStoreTest** — hand-written tests pass (pure arithmetic + memory store)
10. **Build with Java 25** — modules target `maven.compiler.release=25`

### Not yet done

- **NativeMemory shortcomings** — `NativeMemory` exists and works for basic store/load,
  but has issues:
  - `Arena.ofShared()` is never closed — leaks off-heap memory when instances are GC'd
  - `grow()` allocates a new segment without freeing the old one (leaks)
  - Multiple NativeMemory instances in the same JVM may interfere if the Arena is shared
  - No lifecycle management (needs `close()` or tie to Instance lifecycle)
  - No bounds checking in native code (out-of-bounds writes will corrupt memory silently)
- **Native traps crash the JVM** — Cranelift emits `ud2` for division-by-zero and other
  Wasm traps. The JVM catches SIGILL and crashes the entire process. See "Trap handling
  analysis" section below. Decision: skip trap-asserting tests for now, revisit later.
- **No control flow** — `block`, `loop`, `br`, `if/else` not implemented
- **No function calls** — `call`, `call_indirect` not implemented
- **No inter-function dispatch** — each function is compiled independently, no way for
  native code to call other native functions

### Current opcode support

| Opcode | Status |
|--------|--------|
| `i32.const` | Working |
| `i64.const` | Working |
| `f32.const` | Working |
| `f64.const` | Working |
| `i32.add/sub/mul` | Working |
| `i32.div_s/div_u` | Working (but ud2 trap on div-by-zero crashes JVM) |
| `i32.rem_s/rem_u` | Working (same trap issue) |
| `i32.and/or/xor` | Working |
| `i32.shl/shr_s/shr_u` | Working |
| `i32.rotl/rotr` | Working |
| `i32.clz/ctz/popcnt` | Working |
| `i32.eqz` | Working |
| `i32.eq/ne/lt_s/lt_u/gt_s/gt_u/le_s/le_u/ge_s/ge_u` | Working |
| `i32.extend8_s/extend16_s` | Working |
| `i32.store/load` | Working (with NativeMemory) |
| `local.get/set/tee` | Working |
| `drop` | Working |
| `nop` | Working |
| `end` / return | Working |

### Calling convention (our design)

- First param (rdi): memory base pointer (i64, raw pointer to linear memory)
- Remaining params: Wasm function parameters (mapped to rsi, rdx, rcx, r8, r9)
- Return: Wasm return value in rax/eax
- Uses System V ABI

## Trap handling analysis (2026-03-10)

### The problem

Cranelift's `sdiv`/`udiv`/`srem`/`urem` emit a zero-check followed by `ud2` (x86_64)
before the hardware `div` instruction. When the divisor is zero, `ud2` raises SIGILL.
The JVM catches SIGILL and crashes the entire process — there is no way to recover.

`sdiv(INT_MIN, -1)` also traps with `ud2` (integer overflow).

### Options investigated

**1. Pre-check in generated Cranelift IR** — Emit `icmp` + `brif` before each div/rem
to branch to a trap block that writes a trap code to a `trapInfoPtr` parameter and
returns. Java checks the trap code after each native call.
- Pro: correct, no signal handling, works everywhere
- Pro: negligible overhead (branch predicted not-taken, div is 20-40 cycles)
- Con: double-check — Cranelift's backend still emits its own check + ud2 after ours
  (the ud2 is never reached). Cranelift issue #5908 tracks optimizing this away.
- Con: requires blocks in NativeCompiler (not yet implemented, but needed for control
  flow anyway)

**2. Signal handler via Panama** — Install a SIGILL/SIGFPE handler using `sigaction`
called through Panama, check if the faulting PC is in our mmapped code region, and
recover.
- **JVM conflict**: HotSpot installs its own handlers for SIGILL, SIGFPE, SIGSEGV,
  SIGBUS. HotSpot uses `ud2` + SIGILL for JIT deoptimization (NOP patching). Our
  mmapped code is not in the CodeCache, so HotSpot's handler doesn't recognize it
  and crashes.
- **Handler ordering**: We'd need `libjsig.so` via `LD_PRELOAD` to install our handler
  before HotSpot's — a deployment burden.
- **Can't run Java in signal handler**: A Panama upcall stub in signal context is
  unsafe (GC safepoints, locks, async-signal-safe restrictions).
- **Recovery mechanism**: `longjmp` from signal handler skips JVM frames/destructors,
  may corrupt JVM state. Modifying `ucontext` PC requires native code.
- **Wasmtime's experience**: Even wasmtime replaced `setjmp`/`longjmp` with Cranelift
  exceptions (PR #11592) due to unsoundness and 7.5% performance overhead.
- **Breaks zero-native-libs goal**: Would require shipping a C library or generating
  machine code stubs for the handler.
- **Platform-specific**: `ucontext_t` layout, register names, `sigaltstack` behavior
  all differ across Linux/macOS/aarch64/x86_64.

**3. Cranelift configuration** — No flag exists to replace traps with branches.
Cranelift issue #5908 discusses related optimization but doesn't change semantics.

### Decision

**Skip trap-asserting tests for now.** The `i32.wast` spec tests are enabled with
`assert_trap` tests excluded (10 tests). This is **unsafe** — division by zero or
`sdiv(INT_MIN, -1)` in user code will crash the JVM. This is acceptable for the
current experimental phase.

Proper trap handling requires deeper analysis and experimentation with signal handlers.
The pre-check approach (option 1) is the likely solution once control flow (blocks)
is implemented in NativeCompiler.

### Excluded i32.wast trap tests

| Test | Function | Trap | Args |
|------|----------|------|------|
| test25 | div_s | integer divide by zero | (1, 0) |
| test26 | div_s | integer divide by zero | (0, 0) |
| test27 | div_s | integer overflow | (0x80000000, -1) |
| test28 | div_s | integer divide by zero | (0x80000000, 0) |
| test45 | div_u | integer divide by zero | (1, 0) |
| test46 | div_u | integer divide by zero | (0, 0) |
| test61 | rem_s | integer divide by zero | (1, 0) |
| test62 | rem_s | integer divide by zero | (0, 0) |
| test81 | rem_u | integer divide by zero | (1, 0) |
| test82 | rem_u | integer divide by zero | (0, 0) |

### References

- [Cranelift IR docs — traps](https://github.com/bytecodealliance/wasmtime/blob/main/cranelift/docs/ir.md)
- [Cranelift interpreter trap handling](https://github.com/bytecodealliance/wasmtime/blob/1b59b579856569d1a6ddca82429bf94f6fbef5e3/cranelift/interpreter/src/interpreter.rs#L595)
- [Wasmtime PR #11592 — Replace setjmp/longjmp](https://github.com/bytecodealliance/wasmtime/pull/11592)
- [Wasmtime Unix signal handler](https://docs.rs/wasmtime/latest/src/wasmtime/runtime/vm/sys/unix/signals.rs.html)
- [HotSpot signals_posix.cpp](https://github.com/openjdk/jdk/blob/master/src/hotspot/os/posix/signals_posix.cpp)
- [Oracle Signal Chaining docs](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/signal-chaining.html)
- [Cranelift issue #5908 — trapping arithmetic optimization](https://github.com/bytecodealliance/wasmtime/issues/5908)

## Next steps (pick up here next session)

### Immediate: control flow

- `block`, `loop`, `br`, `br_if`, `br_table`, `if/else`
- Requires Cranelift block management with proper sealing
- This unlocks most wast files (nop.wast, local_get.wast, etc.)

### Then: function calls

- `call` (direct) — requires function pointer table or relocation
- `call_indirect` — table-based dispatch
- Host function callbacks (imports) — Panama upcall stubs

### Then: more types

- i64 arithmetic opcodes (same pattern as i32, bridge already has i64 support)
- f32/f64 arithmetic, comparison, conversion opcodes
- Memory load/store variants (i64.load, f32.load, etc.)

### Future

- Benchmark on real workloads (SQLite, Prism)
- Wrap Cranelift bridge with Chicory build-time compiler (wabt/wasm-tools pattern)
- `Machine` implementation with hybrid dispatch (native + interpreter fallback)
- NativeMemory lifecycle management (Arena cleanup)

## How to build and test

```bash
# Switch to Java 25
sdk use java 25-tem

# Rebuild Rust bridge (only when lib.rs changes)
cd cranelift-bridge/src/main/rust && cargo build --release --target wasm32-wasip1

# Rebuild cranelift-bridge Java (only when Rust or bridge Java changes)
mvn install -f cranelift-bridge/pom.xml -q

# Build and run cranelift-compiler tests
mvn install -f cranelift-compiler/pom.xml

# Run specific tests
mvn install -f cranelift-compiler/pom.xml -Dtest=AddTest
mvn install -f cranelift-compiler/pom.xml -Dtest=AddAndStoreTest
mvn install -f cranelift-compiler/pom.xml -Dtest=SpecV1ConstTest
```
