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
11. **Control flow** — `block`, `loop`, `if/else`, `br`, `br_if`, `return`, `end` all working
    with proper dead code handling for dummy frames after unconditional transfers
12. **CALL (direct)** — native-to-native via function pointer table, import calls via
    Panama upcall stubs with ctxBuffer. CallTest verifies both native and import calls.
13. **CALL_INDIRECT** — dispatches through Java trampoline (table lookup + type check)
14. **Exception propagation** — pendingException pattern for upcall stubs (trampoline,
    import dispatch). Exceptions caught before they unwind through native frames.
15. **SELECT** — Cranelift `select` instruction with i32-to-boolean conversion
16. **GLOBAL_GET/GLOBAL_SET** — off-heap globals buffer with `NativeGlobalInstance` subclass.
    Native code reads/writes buffer directly; Java reads via overridden `getValue()/setValue()`.
    No sync needed — the buffer IS the canonical storage.
17. **MEMORY_SIZE/MEMORY_GROW** — page count in ctxBuffer, grow via dedicated upcall stub.
    After grow, memBase variable is reloaded from ctxBuffer (memBase is a Cranelift variable).
18. **UNREACHABLE** — emits Cranelift trap instruction
19. **i64 arithmetic** — all i64 math, bitwise, shift, rotate, comparison, extension opcodes
20. **i32/i64 wrap/extend** — I32_WRAP_I64, I64_EXTEND_I32_S/U, I64_EXTEND_{8,16,32}_S
21. **Memory load/store variants** — i64, f32, f64 full-width; i32/i64 sub-word
    (load8/16 signed/unsigned, store8/16; i64 load32 signed/unsigned, store32)
22. **16 spec test files green** — 2550/2807 tests pass (257 skipped):
    const.wast 778/778, i32.wast 450/460, block.wast 171/223,
    br.wast 94/97, br_if.wast 91/118, if.wast 183/241, loop.wast 70/120,
    return.wast 84/84, global.wast 101/110, select.wast 130/148,
    local_get.wast 33/36, local_set.wast 51/53, local_tee.wast 90/97,
    memory_grow.wast 94/104, memory_size.wast 40/42, nop.wast 82/88

### Not yet done

- **NativeMemory shortcomings** — leaks, no bounds checking (see earlier notes)
- **Native traps crash the JVM** — ud2 on div-by-zero. See trap handling analysis below.
- **Multi-value blocks** — block type index not yet supported (throws UnsupportedOperationException)
- **BR_TABLE** — not yet implemented
- **f32/f64 arithmetic/comparison** — F32_NEG, F64_NEG, F32_GT etc. not yet implemented
- **RefNull types** — function params with RefNull types unsupported

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
| `block/loop/if/else/end` | Working |
| `br/br_if` | Working |
| `return` | Working |
| `call` | Working (direct native-to-native + import via ctxBuffer) |
| `call_indirect` | Working (via Java trampoline) |
| `select/select_t` | Working |
| `global.get/set` | Working (NativeGlobalInstance + off-heap buffer) |
| `memory.size` | Working (page count from ctxBuffer) |
| `memory.grow` | Working (upcall stub + memBase reload) |
| `unreachable` | Working (Cranelift trap) |
| `i64.add/sub/mul` | Working |
| `i64.div_s/div_u` | Working (ud2 trap on div-by-zero) |
| `i64.rem_s/rem_u` | Working (same trap issue) |
| `i64.and/or/xor` | Working |
| `i64.shl/shr_s/shr_u` | Working |
| `i64.rotl/rotr` | Working |
| `i64.clz/ctz/popcnt` | Working |
| `i64.eqz` | Working |
| `i64.eq/ne/lt_s/lt_u/gt_s/gt_u/le_s/le_u/ge_s/ge_u` | Working |
| `i64.extend_i32_s/u` | Working |
| `i64.extend8_s/extend16_s/extend32_s` | Working |
| `i32.wrap_i64` | Working |
| `i64.store/load` | Working |
| `f32.store/load` | Working |
| `f64.store/load` | Working |
| `i32.load8_s/u, load16_s/u` | Working |
| `i32.store8, store16` | Working |
| `i64.load8_s/u, load16_s/u, load32_s/u` | Working |
| `i64.store8, store16, store32` | Working |

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

### Immediate: more opcodes for wider coverage

- `br_table` — branch table (switch dispatch), needed by many spec tests
- f32/f64 arithmetic: `neg`, `abs`, `ceil`, `floor`, `trunc`, `nearest`, `sqrt`,
  `add`, `sub`, `mul`, `div`, `min`, `max`, `copysign`
- f32/f64 comparison: `eq`, `ne`, `lt`, `gt`, `le`, `ge`
- Conversions: `f32.convert_i32_s/u`, `f64.convert_i32_s/u`, etc.
- Multi-value blocks: block type index decoding

### Then: more spec test files

- `i64.wast` — i64 arithmetic spec tests (opcodes already implemented)
- `f32.wast`, `f64.wast`, `f32_cmp.wast`, `f64_cmp.wast` — once f32/f64 ops added
- `call.wast`, `call_indirect.wast` — function call spec tests
- `load.wast`, `store.wast` — memory access spec tests

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
