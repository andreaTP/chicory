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

native-poc/                             Original PoC (kept for reference)
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
  Wasm traps. The JVM catches SIGILL and crashes the entire process. Needs a signal
  handler or pre-check approach (check divisor before div instruction). This blocks
  `i32.wast` spec tests that include `assert_trap` for div-by-zero.
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

## Next steps (pick up here next session)

### Immediate: trap handling

The #1 blocker for i32.wast. Cranelift emits `ud2` for Wasm traps (div-by-zero,
integer overflow). Options:
- **Pre-check in generated code**: emit a branch before div/rem to check for zero
  divisor, call back to Java to throw the appropriate WasmRuntimeException
- **Signal handler**: install a SIGILL/SIGFPE handler that converts to Java exception
  (complex, platform-specific, may conflict with JVM's own signal handling)
- **Cranelift trap configuration**: investigate if Cranelift can be configured to
  not emit traps and instead generate branching code

### Then: control flow

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
