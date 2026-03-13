# Chicory Native Compilation: Cranelift + Panama

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

  compile()                 ──────────►  compile() -> code bytes in linear memory
    read native bytes from bridge memory
    mmap + Panama downcall
```

Two-pass compilation: NativeAnalyzer (reachability via exitBlockDepth) produces
parallel boolean arrays, NativeEmitters (static opcode handlers) emit Cranelift IR
through EmitContext. NativeCompiler is a thin orchestrator.

## Module structure

```
cranelift-bridge/                       Thin Cranelift FFI wrapper
├── rust-src/src/lib.rs                 ~200 lines, direct FunctionBuilder calls
├── src/main/java/.../CraneliftBridge.java  Java wrapper (@WasmModuleInterface)
└── src/main/resources/cranelift-bridge.wasm  Pre-built binary (3.5MB)

cranelift-compiler/                     Native compiler + spec tests
└── src/main/java/.../compiler/
    ├── NativeMachineFactory.java       Public API (shared Arena + globals buffer)
    ├── NativeMachine.java              Machine impl with Panama downcalls
    ├── NativeCompiler.java             Thin orchestrator (analyzer → emitters)
    ├── NativeAnalyzer.java             Pre-pass: reachability via exitBlockDepth
    ├── NativeValueStack.java           Scope-aware Cranelift value ID stack
    ├── NativeEmitters.java             Static opcode emission methods
    ├── EmitContext.java                Shared state for emitters
    ├── CtxBuffer.java                  ctxBuffer layout constants (256 bytes)
    ├── NativeTable.java                Off-heap 16-byte anyfunc table entries
    ├── NativeMemory.java               Off-heap memory via Panama
    ├── NativeGlobalInstance.java        Off-heap globals buffer
    └── PanamaExecutor.java             mmap/mprotect helpers
```

## Current state

**25667 tests, 0 failures, 0 errors, 9 skipped** (78 wast files).
Requires Java 25.

Key features:
- All i32/i64/f32/f64 arithmetic/comparison/conversion opcodes (~120 total)
- Full control flow: block, loop, if/else, br, br_if, br_table, return
- Memory load/store with all widths + OOB bounds checking
- Direct calls (native-to-native via funcTable) + import upcall stubs
- CALL_INDIRECT: funcPtr+typeIdx loaded directly from 16-byte table entry
- Multi-return via argsBuffer (single-return fast path in register)
- NativeTable: 16-byte anyfunc entries with cross-module resolution
- Bulk memory: memory.copy, memory.fill (via trampoline)
- Table ops: GET/SET/SIZE/GROW/FILL/COPY/INIT, ELEM.DROP (native + trampoline)
- Trap pre-checks: div-by-zero, INT_MIN/-1, unreachable, float trunc NaN
- Stack depth guard via get_stack_pointer (512KB reserve)
- Off-heap globals, tables, memory — no sync between Java and native

## Excluded wasts

### Happy-path failures (need new opcodes or fixes)
- **bulk.wast** — needs memory.init, data.drop
- **memory_fill.wast** — memory.fill implemented, needs enabling + testing
- **memory_init.wast** — needs memory.init, data.drop
- **conversions.wast** — float trunc overflow (NaN-only check, not range)
- **data.wast** — needs data segment operations
- **address.wast, align.wast** — memory access patterns (likely OOB-related)
- **start.wast** — start function execution

### Multi-module / linking
- **imports.wast, linking.wast** — cross-module linking
- **table_grow.wast** — has linking tests (register/instantiate)

### Validation / parse-only
- **binary.wast, binary-leb128.wast** — binary format validation
- **obsolete-keywords.wast** — parse-level rejection
- **br_table.wast** — compilation too heavy for large tables

### Known issues
- Float trunc overflow check (NaN-only, not range — 35 conversions failures)
- br_table compilation too heavy for large tables (excluded)
- Global validation (GlobalTest 76/77 — skipped, also in runtime-tests)

## Next priorities

### DONE: Fix native resource leak (JVM crash after ~25K tests)

**Fixed.** `java.lang.ref.Cleaner` on NativeMachine closes Arena + munmaps code region
when GC'd (safety net). Future: add `AutoCloseable` on `NativeMachineFactory` for
explicit deterministic cleanup.

### P1: Fix remaining excluded tests (103 skipped across 92 wast files)

Error-path only — all happy-path tests pass. Categories:
- **30 address.wast**: large-offset OOB loads don't trap (bounds check overflow)
- **35 conversions.wast**: float trunc overflow (NaN-only check, not range)
- **17 binary/align/data/imports/linking/start**: parser/validation exception types
- **21 misc**: elem, global, memory, bulk message mismatches

### P1: Native memory.copy/fill (optimization)
Current memory.copy/fill go through Java trampoline (native → upcall → Java).
Emit as native `memmove`/`memset` with inline OOB checks — no trampoline needed.
For large copies the memcpy dominates; for small copies the upcall overhead matters.

### P2: Future work
- Benchmark on real workloads (SQLite, Prism)
- Wrap Cranelift bridge with Chicory build-time compiler (wabt/wasm-tools pattern)
- `Machine` implementation with hybrid dispatch (native + interpreter fallback)
- Contribute ud2 configurability to Cranelift upstream
- Float trunc range check (not just NaN)

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
