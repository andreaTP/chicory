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
22. **25 spec test files, 14023 tests** — 13776 pass, 247 skipped, 0 failures
23. **Full i32/i64/f32/f64 arithmetic/comparison/conversion opcodes** — all ~120 opcodes
24. **Multi-value blocks** — ControlFrame uses FunctionType, block type indices resolved
25. **BR_TABLE** — implemented as if-else chain (Cranelift JumpTable API issues)
26. **Trap pre-checks** — div-by-zero, INT_MIN/-1, unreachable, float trunc NaN check.
    Zero cost on happy path. Cranelift ud2 becomes dead code.
27. **Safety stubs** — per-signature Panama upcall stubs for uncompiled functions
28. **SSE4.1 enabled** — required for Cranelift ceil/floor/trunc/nearest instructions.
    Without it, Cranelift emits libcalls to address 0 (SIGSEGV).
29. **RefNull types** — mapped to i64 for native code
30. **ctxBuffer formalized** — `CtxBuffer.java` constants class, no magic numbers

### Not yet done

- **NativeMemory shortcomings** — leaks, no bounds checking (SIGSEGV on OOB)
- **Float trunc overflow check** — only NaN check implemented, not range check
- **ctxBuffer scalability** — RESOLVED (see P0 section)

### Current opcode support

All i32/i64/f32/f64 opcodes are implemented (~120 total):
- **Constants**: i32/i64/f32/f64.const
- **Arithmetic**: add, sub, mul, div_s/u, rem_s/u (with pre-check traps)
- **Bitwise**: and, or, xor, shl, shr_s/u, rotl, rotr, clz, ctz, popcnt
- **Comparison**: eqz, eq, ne, lt_s/u, gt_s/u, le_s/u, ge_s/u
- **Float arithmetic**: add, sub, mul, div, min, max, copysign, abs, neg,
  ceil, floor, trunc, nearest, sqrt
- **Float comparison**: eq, ne, lt, gt, le, ge
- **Conversions**: all trunc (with NaN pre-check), trunc_sat, convert, promote,
  demote, reinterpret, wrap, extend (signed/unsigned, 8/16/32)
- **Memory**: load/store for all types + all sub-word variants
- **Control flow**: block, loop, if/else, end, br, br_if, br_table, return
- **Variables**: local.get/set/tee, global.get/set
- **Calls**: call (direct native), call_indirect (Java trampoline)
- **Misc**: drop, nop, select/select_t, unreachable, memory.size, memory.grow

### Calling convention (our design)

- Param 0 (rdi): memBase (i64/ADDRESS) — pointer to linear memory
- Param 1 (rsi): ctxPtr (i64/ADDRESS) — pointer to call context buffer
- Param 2+ (rdx, rcx, r8, r9): Wasm function parameters
- Return: Wasm return value in rax/xmm0
- Uses System V ABI

See `CtxBuffer.java` for the full ctxBuffer layout (256 bytes, fixed offsets).

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

**Pre-check approach implemented (2026-03-11).** Instead of relying on Cranelift's
trapping instructions (which emit ud2), we emit explicit checks before each trapping
operation:
- **div/rem**: `icmp(divisor, 0)` + `brif` → trap block. For signed div, also check
  `dividend == INT_MIN && divisor == -1`. Trap block writes trap code to ctxBuffer[16]
  and returns dummy value. Cranelift's ud2 becomes dead code after our branch.
- **unreachable**: write trap code to ctxBuffer[16] + return (no ud2 emitted at all)
- **float trunc**: use `fcvt_to_sint_sat` (non-trapping) + NaN check via `fcmp(NE, x, x)`
- **NativeMachine.call()**: checks ctxBuffer[16] after native return, throws
  ChicoryException with appropriate message

Zero performance cost on happy path (branch predicted not-taken).

**Resolved**: per-signature safety stubs added for uncompiled functions. SSE4.1 enabled
for ceil/floor/trunc/nearest. f32.wast and f64.wast now fully pass (happy path).

**Long-term**: contribute to Cranelift upstream to make ud2 emission configurable
per-opcode, allowing embedders to provide trap handler callbacks instead.

### Previously excluded trap tests (now passing)

With pre-check traps implemented, i32.wast and i64.wast div/rem trap tests now pass
(integer divide by zero, integer overflow). 247 tests still excluded across 25 wast
files — these are assert_trap tests for other trapping conditions (uncompiled functions,
call_indirect type mismatch, validation errors, etc.)

### References

- [Cranelift IR docs — traps](https://github.com/bytecodealliance/wasmtime/blob/main/cranelift/docs/ir.md)
- [Cranelift interpreter trap handling](https://github.com/bytecodealliance/wasmtime/blob/1b59b579856569d1a6ddca82429bf94f6fbef5e3/cranelift/interpreter/src/interpreter.rs#L595)
- [Wasmtime PR #11592 — Replace setjmp/longjmp](https://github.com/bytecodealliance/wasmtime/pull/11592)
- [Wasmtime Unix signal handler](https://docs.rs/wasmtime/latest/src/wasmtime/runtime/vm/sys/unix/signals.rs.html)
- [HotSpot signals_posix.cpp](https://github.com/openjdk/jdk/blob/master/src/hotspot/os/posix/signals_posix.cpp)
- [Oracle Signal Chaining docs](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/signal-chaining.html)
- [Cranelift issue #5908 — trapping arithmetic optimization](https://github.com/bytecodealliance/wasmtime/issues/5908)

## Next steps (pick up here next session)

### P0: ctxBuffer scalability issues — RESOLVED

All three ctxBuffer scalability issues have been fixed:

1. **Re-entrancy safety** — Verified safe by design. All Java-side readers
   (`callIndirectTrampoline`, `importDispatchDirect`) copy ctxBuffer/argsBuffer
   values into Java locals before dispatching. Re-entrant calls overwrite the
   buffers, but outer readers have already captured what they need. Native-to-native
   calls pass args via CPU registers; ctxBuffer writes are only for import stubs.
   Full analysis documented in `CtxBuffer.java` javadoc.

2. **Args moved to separate buffer** — Args are now in a dedicated `argsBuffer`
   MemorySegment (1024 × 8 bytes = 8KB), pointed to by `ctxBuffer[ARGS_PTR]`.
   No more hard cap of 20 args. Native code loads argsPtr from ctxBuffer, then
   reads/writes args at `argsPtr + i*8`.

3. **ARG_COUNT no longer overloaded** — Dedicated `MEM_GROW_DELTA` field at
   offset 36 for `memory.grow` page delta. `ARG_COUNT` at offset 32 is now
   exclusively for call argument count.

### P1: increase test coverage

- Remove all excludedTests, re-run, re-exclude only genuine failures
- Full float trunc range check (currently NaN-only, not overflow)
- Enable more wast files: conversions, call, call_indirect, load, store, etc.
- Memory bounds checking (currently no bounds checks = SIGSEGV on OOB)

### P2: future work

- Benchmark on real workloads (SQLite, Prism)
- Wrap Cranelift bridge with Chicory build-time compiler (wabt/wasm-tools pattern)
- `Machine` implementation with hybrid dispatch (native + interpreter fallback)
- NativeMemory lifecycle management (Arena cleanup)
- Contribute ud2 configurability to Cranelift upstream

## How to build and test

```bash
# Switch to Java 25
sdk use java 25-tem

# 1. Rebuild Rust bridge (only when lib.rs changes)
cranelift-bridge/src/main/rust/build.sh

# 2. ALWAYS run clean install after Rust rebuild (regenerates @WasmModuleInterface exports)
mvn clean install -f cranelift-bridge/pom.xml

# 3. Build and run cranelift-compiler tests
mvn clean install -f cranelift-compiler/pom.xml

# Run specific tests
mvn install -f cranelift-compiler/pom.xml -Dtest=AddTest
mvn install -f cranelift-compiler/pom.xml -Dtest=SpecV1ConstTest
```

**Important**: After rebuilding Rust, you MUST run `mvn clean install -f cranelift-bridge/pom.xml`
before building cranelift-compiler. The annotation processor reads the .wasm file at compile
time — if the .wasm changed but Java sources didn't, incremental compilation may skip
regeneration. Always use `clean install` to avoid stale state.
