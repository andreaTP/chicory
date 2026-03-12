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
    │   ├── NativeCompiler.java         Thin orchestrator (analyzer → emitters)
    │   ├── NativeAnalyzer.java         Pre-pass: reachability via exitBlockDepth
    │   ├── NativeValueStack.java       Scope-aware Cranelift value ID stack
    │   ├── NativeEmitters.java         Static opcode emission methods
    │   ├── EmitContext.java            Shared state for emitters
    │   ├── CtxBuffer.java              ctxBuffer layout constants
    │   ├── NativeMemory.java           Off-heap memory via Panama
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
22. **25 spec test files, ~14000 tests** — 13976 pass, 47 skipped, 0 failures
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

## Next steps

### P0: increase test coverage

**Current: 21119 tests, 0 failures, 0 errors, 4 skipped (GlobalTest 76/77, MemoryTest 6/7 — multi-memory).**
76 wast files included. All non-simd, non-linking wasts pass.

Done (2026-03-11 session):
- Two-pass compiler refactoring (NativeAnalyzer + NativeEmitters)
- IF block params (thenBlock/elseBlock with trampoline)
- Multi-return via argsBuffer (single-return fast path, argsBuffer fallback)
- Multi-return in NativeMachine.call() (read from argsBuffer)
- BR_IF targeting function frame (conditional return)
- Dead code verifier errors (unified emitEnd with scopeRestore)
- Memory bounds checking (OOB trap before loads/stores)
- Stack depth guard via get_stack_pointer (wasmtime-style, 512KB reserve)
- SigRef fix for multi-return callees (>2 returns)
- emitZero for RefNull types

Done (2026-03-12 session):
- **NativeTable**: off-heap table buffers `[size:i32][max:i32][refs:i32...]`
- **Fully native CALL_INDIRECT**: bounds check → null check → type check → funcTable
  lookup → direct call. Zero Java trampolines on hot path.
- **Canonical type map**: structurally equal FunctionTypes get the same canonical index
  in funcTypesArray. Fixes type mismatch for modules with duplicate type definitions
  (same approach as AOT compiler's `isFuncTypeMatch`).
- **Instance.Builder factories**: `withTableFactory()`, `withGlobalFactory()` — removed
  reflection hack for globals and sync hack for tables. Single source of truth.
- **NativeMachineFactory**: instance class holding shared Arena + globals buffer
- **Table/ref opcodes**: TABLE.GET/SET/SIZE/GROW/FILL/COPY, REF_NULL/REF_IS_NULL/REF_FUNC,
  TABLE.INIT/ELEM.DROP (trampoline for init/drop)
- **funcTypesArray**: off-heap i32 array with canonical type indices per function
- call_indirect.wast enabled (172 tests, all pass)

**Adding wast files**: add ONE at a time, verify test count = previous+N.
If count drops, the JVM crashed — find which function and fix.

Known issues:
- Float trunc overflow check (NaN-only, not range — 35 conversions failures)
- br_table compilation too heavy for large tables (excluded)
- Global validation (GlobalTest 76/77 — skipped, also in runtime-tests)

### Next priority: bulk memory + elem + conversions

Excluded wasts with happy-path failures (not linking/validation-only):
- **bulk.wast** — bulk memory ops (memory.copy/fill/init, data.drop)
- **elem.wast** — element segment operations
- **memory_copy.wast, memory_fill.wast, memory_init.wast** — bulk memory
- **conversions.wast** — float trunc overflow (NaN-only check, not range)
- **data.wast** — data segment operations
- **address.wast, align.wast** — memory access patterns (likely OOB-related)
- **start.wast** — start function execution

Excluded wasts requiring multi-module/linking support:
- **imports.wast, linking.wast** — cross-module linking
- **table_grow.wast** — has linking tests (register/instantiate)

Excluded wasts that are validation/parse-only:
- **binary.wast, binary-leb128.wast** — binary format validation
- **obsolete-keywords.wast** — parse-level rejection
- **br_table.wast** — compilation too heavy for large tables
- Enable more wast files: conversions, call, call_indirect, load, store, etc.

### P1.5: NativeTable — fully native table operations

#### Goal

Eliminate Java trampolines for table operations. The current `CALL_INDIRECT` bounces
through Java for every indirect call (table lookup + type check + dispatch). With
`NativeTable`, the entire `CALL_INDIRECT` hot path runs in native code — zero FFI
overhead.

#### Architecture

```
Off-heap layout (all in Arena-managed MemorySegment):

  tableBuffer (per table):
  ┌──────────────────────────────────────────────────────┐
  │  [0..4)    i32  size          current element count  │
  │  [4..8)    i32  maxSize       max capacity (or 0)    │
  │  [8..N)    i32[]  refs        funcId per element      │
  └──────────────────────────────────────────────────────┘
  Pre-allocated to maxSize (table limits max). If max is unset or huge,
  use a reasonable cap. grow() only bumps the size field, no reallocation.

  funcTypesArray (one per module, one i32 per function):
  ┌──────────────────────────────────────────────────────┐
  │  [0]  typeIdx for func 0                             │
  │  [1]  typeIdx for func 1                             │
  │  ...                                                 │
  │  [N]  typeIdx for func N                             │
  └──────────────────────────────────────────────────────┘
  Used by CALL_INDIRECT for type checking without Java.

  ctxBuffer additions:
    TABLE_PTRS    i64    Pointer to array of table buffer pointers
    FUNC_TYPES    i64    Pointer to funcTypesArray
```

#### Operation-by-operation design

**Fully native (no trampoline):**

| Operation | Native implementation |
|---|---|
| `CALL_INDIRECT` | 1. Read tablePtr from TABLE_PTRS[tableIdx] |
|                 | 2. Bounds check: elemIdx < table.size → trap OOB |
|                 | 3. Read funcId = table.refs[elemIdx] → trap if REF_NULL |
|                 | 4. Type check: funcTypes[funcId] == expectedTypeId → trap mismatch |
|                 | 5. Read funcPtr = funcTable[funcId] |
|                 | 6. Call funcPtr directly (System V ABI) |
| `TABLE.GET`    | Bounds check + read refs[index] |
| `TABLE.SET`    | Bounds check + write refs[index] |
| `TABLE.SIZE`   | Read size field from table header |
| `TABLE.GROW`   | If pre-allocated to max: just bump size field + fill new slots. Return -1 if over max. |
| `TABLE.FILL`   | Loop writing value to refs[offset..offset+size], bounds check first |
| `TABLE.COPY`   | Memmove on refs arrays (handles overlapping src/dst), bounds check first |
| `REF_NULL`     | Push REF_NULL_VALUE sentinel (0xFFFFFFFF = -1) |
| `REF_IS_NULL`  | Compare value against REF_NULL_VALUE sentinel |
| `REF_FUNC`     | Push funcId as i32 constant |

**Java trampoline (rare operations):**

| Operation | Why trampoline needed |
|---|---|
| `TABLE.INIT` | Reads from elem segments (Java-managed `Element[]` with expression initializers) |
| `ELEM.DROP`  | Sets Java-side `Element` to empty (passive segment lifecycle) |
| `TABLE.GROW` (fallback) | Only if not pre-allocated to max (unbounded tables) |

#### NativeTable class

```java
// Extends or replaces TableInstance for native compilation.
// refs[] live off-heap; native code reads/writes directly.
// No Instance[] array — single-module assumption (all entries same instance).
final class NativeTable {
    MemorySegment buffer;    // [size:i32][max:i32][refs:i32...]
    // Java-side accessors read/write the same off-heap memory
    int ref(int index)       // bounds check + read from buffer
    void setRef(int index, int value)  // bounds check + write to buffer
    int size()               // read size from buffer header
    int grow(int delta, int fillValue) // bump size, fill new slots
}
```

#### CALL_INDIRECT: before vs after

**Before (current — Java trampoline):**
```
native code:
  store typeId, tableIdx, elemIdx, args to ctxBuffer
  call trampolinePtr (upcall to Java)
Java callIndirectTrampoline():
  read typeId, tableIdx, elemIdx from ctxBuffer
  funcId = instance.table(tableIdx).requiredRef(elemIdx)  // Java table
  typeCheck(funcId, typeId)                                // Java
  result = this.call(funcId, args)                         // re-enter native
  return result
```

**After (fully native):**
```
native code:
  tablePtr = load TABLE_PTRS[tableIdx]
  tableSize = load tablePtr[0]            // i32 size field
  brif elemIdx >= tableSize → trap OOB
  funcId = load tablePtr[8 + elemIdx*4]   // i32 refs array
  brif funcId == REF_NULL → trap null
  expectedType = typeId (immediate)
  actualType = load FUNC_TYPES[funcId]    // i32 type index
  brif actualType != expectedType → trap type mismatch
  funcPtr = load FUNC_TABLE[funcId*8]     // i64 function pointer
  call_indirect funcPtr(memBase, ctxPtr, args...)
```

No FFI boundary crossing. No upcall stub. The common case is ~6 loads + 3 branches,
all predicted not-taken on the happy path.

#### Pre-allocation strategy

Wasm tables usually have a declared max size (required for `table.grow`). Pre-allocate
the refs array to max size so `TABLE.GROW` is just incrementing the size field:

- If table max is declared and ≤ 1M entries: pre-allocate to max (4 bytes × 1M = 4MB)
- If table max is undeclared or > 1M: pre-allocate to 64K entries, trampoline for grow
  beyond that (reallocate + update pointer in ctxBuffer)

Fill unused slots with REF_NULL_VALUE so TABLE.GET on uninitialized elements returns
the correct sentinel.

#### Implementation steps

1. **Add ctxBuffer fields**: `TABLE_PTRS`, `FUNC_TYPES_PTR` offsets in `CtxBuffer.java`
2. **Create `NativeTable`**: off-heap buffer with header + refs array
3. **Create `funcTypesArray`**: off-heap i32 array, one entry per function, filled at init
4. **Wire up in `NativeMachine`**: allocate tables, fill from elem segments, store pointers
5. **Emit native `CALL_INDIRECT`**: replace trampoline with inline IR (bounds + null + type + call)
6. **Emit `TABLE.GET/SET/SIZE/GROW/FILL/COPY`**: inline IR in NativeEmitters
7. **Emit `REF_NULL/REF_IS_NULL/REF_FUNC`**: trivial constant/compare ops
8. **Emit `TABLE.INIT/ELEM.DROP`**: trampoline to Java (rare)
9. **Enable test files**: table.wast, call_indirect.wast, elem.wast, ref_*.wast, func_ptrs.wast

#### Cross-instance note

`TableInstance` tracks an `Instance[]` parallel to `refs[]` for cross-module linking
(different table entries may belong to different instances). `NativeTable` drops this:
all entries are assumed to be in the same instance. This is correct for single-module
native compilation. Multi-module support (linking.wast, imports.wast) would need
a different strategy — likely keeping the trampoline for cross-instance calls.

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

## Refactor plan: separate control flow analysis from code emission

### Problem

`NativeCompiler.emitInstruction()` mixes two concerns in one 2300-line method:
1. **Control flow analysis** — unreachable propagation, value stack management,
   block type resolution, dead code skipping
2. **Code emission** — calling bridge exports to emit Cranelift IR

This causes the remaining 45 verifier failures: when a block body is entirely
unreachable and no `br`/`br_if` targets its merge block, the value stack has
stale entries with wrong types. The FUNCTION END pops these and emits a return
with mismatched types, which Cranelift's verifier rejects.

### Root cause

The Wasm spec defines **polymorphic stack behavior** after unreachable code
(§3.3.8.3): at a merge point, the declared block return types are available
regardless of whether the path was reachable. Chicory's `compiler/` module
implements this via `TypeStack.scopeRestore()` — at END, replace the actual
stack with a pre-computed "normalized" stack (params removed, returns added).

Our `NativeCompiler` doesn't do this. It just pushes merge block params onto
the value stack at END, but doesn't propagate unreachability to the parent
frame. Code after a fully-dead block continues to emit IR in a zombie merge
block with stale values.

### Architecture: two-pass compilation (follow `compiler/` pattern)

```
compiler/ module                    cranelift-compiler/ (new)
===============                     ====================

WasmAnalyzer                        NativeAnalyzer
  - walks instructions                - pre-pass over AnnotatedInstruction[]
  - tracks exitBlockDepth             - tracks exitBlockDepth via ins.depth()
  - manages TypeStack                 - produces boolean[] skip, boolean[] scopeRestore
  - produces CompilerInstruction[]    - NO type stack needed (just depth comparison)

TypeStack                           NativeValueStack
  - tracks ValTypes                   - tracks Cranelift value IDs (int)
  - enterScope / scopeRestore         - enterScope(paramCount, mergeParamIds)
  - polymorphic pop                   - scopeRestore() at END when analyzer says so
  - lives in analyzer                 - lives in emission pass (needs bridge IDs)

Emitters (static methods)           NativeEmitters (static methods)
  - one method per opcode             - grouped by category (arithmetic, memory, etc.)
  - no control flow logic             - takes EmitContext, calls bridge.emitXxx()
  - takes JVM MethodVisitor           - pops/pushes NativeValueStack

AotCompiler (orchestrator)          NativeCompiler (thin orchestrator)
  - loops over CompilerInstruction[]  - runs NativeAnalyzer, gets annotations
  - switches on opcode                - loops over instructions + annotations
  - calls Emitters                    - control flow: manages ControlFrame + blocks
  - trusts WasmAnalyzer for CF        - opcodes: delegates to NativeEmitters
```

### Key design decisions

1. **Analyzer produces parallel arrays, not a new instruction stream.**
   Unlike WasmAnalyzer which produces `CompilerInstruction[]` (a transformed
   stream with DROP_KEEP, GOTO, LABEL), NativeAnalyzer produces `boolean[]`
   arrays parallel to the original `AnnotatedInstruction[]` list. This is
   simpler because Cranelift handles its own block/jump semantics — we don't
   need to translate to GOTO/LABEL.

2. **Type stack lives in analyzer, value stack lives in emitter.**
   The analyzer only needs `exitBlockDepth` + `ins.depth()` to decide
   reachability — no type stack required. The NativeValueStack tracks
   Cranelift value IDs and is populated during emission when bridge calls
   produce value IDs. `enterScope()` is called during emission (needs
   merge block param IDs from bridge), `scopeRestore()` is triggered by
   analyzer annotations.

3. **EmitContext bundles shared state for emitters.**
   Instead of passing 8+ parameters, emitters receive an `EmitContext`
   holding: bridge, valueStack, memBaseVar, ctxPtrVar, localVars,
   funcType, module, numImports, sigRefCache.

4. **NativeEmitters are pure opcode handlers.**
   Static methods grouped by category. No control flow logic — they just
   pop operands from value stack, call bridge, push results. The compiler
   orchestrator handles BLOCK/LOOP/IF/ELSE/END/BR/BR_IF/BR_TABLE/RETURN.

### Implementation steps

1. **`NativeAnalyzer`** — pre-pass that walks `AnnotatedInstruction[]`:
   - Uses `exitBlockDepth` (same pattern as `WasmAnalyzer`)
   - `ins.depth() > exitBlockDepth` → mark skip
   - END at exit depth → mark scopeRestore
   - ELSE at exit depth → reset exitBlockDepth (not scopeRestore)
   - BLOCK/LOOP/IF in dead code → increment depth tracking (implicit)
   - Output: `boolean[] skip`, `boolean[] scopeRestore` per instruction index

2. **`NativeValueStack`** — scope-aware value stack (DONE):
   - `enterScope(paramCount, mergeParamIds)` — snapshot for restore
   - `scopeRestore()` — replace stack with snapshot
   - `exitScope()` — pop restore entry
   - `push/pop/peek/size/trimTo`

3. **`EmitContext`** — shared state class:
   - bridge, valueStack, memBaseVar, ctxPtrVar, localVars
   - funcType, module, numImports, sigRefCache
   - Helper methods: emitZero, valTypeToBridgeType, resolveGlobalType, etc.

4. **`NativeEmitters`** — static methods extracted from emitInstruction:
   - `emitArithmetic(ctx, opcode)` — i32/i64/f32/f64 add/sub/mul/...
   - `emitComparison(ctx, opcode)` — icmp/fcmp/eqz
   - `emitMemory(ctx, ins)` — load/store with all widths
   - `emitConversion(ctx, opcode)` — trunc/extend/convert/reinterpret
   - `emitLocal(ctx, ins)` — local.get/set/tee
   - `emitGlobal(ctx, ins)` — global.get/set
   - `emitCall(ctx, ins)` — call/call_indirect
   - `emitMisc(ctx, ins)` — select, drop, nop, memory.size/grow, unreachable
   - `emitSafeDiv(ctx, ...)` — division with trap pre-checks
   - `emitSafeTrunc(ctx, ...)` — float-to-int with NaN check

5. **`NativeCompiler`** — rewritten as thin orchestrator:
   - `compileFunction()`: setup (create function, entry block, locals)
   - Run `NativeAnalyzer.analyze()` to get skip/scopeRestore arrays
   - Loop over instructions:
     - If `skip[i]`: continue (but still track dummy frames for BLOCK/LOOP/IF)
     - Control flow (BLOCK/LOOP/IF/ELSE/END): manage ControlFrame stack,
       NativeValueStack scopes, Cranelift blocks. At END, if `scopeRestore[i]`,
       call `valueStack.scopeRestore()`
     - BR/BR_IF/BR_TABLE/RETURN: emit branches (stays in compiler)
     - All other opcodes: delegate to `NativeEmitters`

6. **Verify** — remove all 45 excludedTests, run `mvn clean install`, 0 failures
