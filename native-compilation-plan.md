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
   Maven modules with test-gen-plugin wired to `i32.wast`
6. **Spec tests running** — 460 tests from i32.wast, ~110 passing with current opcodes

### Not yet done

- **Memory not hooked up** — `NativeMachine.call()` passes `MemorySegment.NULL` as the
  memory base pointer. No off-heap `NativeMemory` implementation exists yet. Functions
  that access linear memory will crash. Needs: contiguous off-heap allocation (Panama
  `MemorySegment`), passed as the first argument to every native function.
- **No control flow** — `block`, `loop`, `br`, `if/else` not implemented
- **No function calls** — `call`, `call_indirect` not implemented
- **No inter-function dispatch** — each function is compiled independently, no way for
  native code to call other native functions

### Current opcode support

| Opcode | Status |
|--------|--------|
| `i32.const` | Working |
| `i32.add` | Working |
| `i32.sub` | Working |
| `i32.mul` | Working |
| `local.get` | Working |
| `local.set` | Working |
| `local.tee` | Working |
| `drop` | Working |
| `end` / return | Working |

### Calling convention (our design)

- First param (rdi): memory base pointer (i64, raw pointer to linear memory)
- Remaining params: Wasm function parameters (mapped to rsi, rdx, rcx, r8, r9)
- Return: Wasm return value in rax/eax
- Uses System V ABI

## Next steps

### Immediate: more i32 opcodes

Add remaining i32 arithmetic and comparison opcodes to pass more i32.wast tests:
- `i32.div_s`, `i32.div_u`, `i32.rem_s`, `i32.rem_u`
- `i32.and`, `i32.or`, `i32.xor`
- `i32.shl`, `i32.shr_s`, `i32.shr_u`
- `i32.clz`, `i32.ctz`, `i32.popcnt`
- `i32.eqz`, `i32.eq`, `i32.ne`, `i32.lt_s`, `i32.gt_s`, etc.

Each requires: add Rust bridge export + Java bridge method + NativeCompiler case.

### Then: control flow

- `block`, `loop`, `br`, `br_if`, `br_table`, `if/else`
- Requires Cranelift block management with proper sealing

### Then: function calls

- `call` (direct) — requires function pointer table or relocation
- `call_indirect` — table-based dispatch
- Host function callbacks (imports) — Panama upcall stubs

### Then: memory access

- `i32.load`, `i32.store` and variants
- Off-heap `NativeMemory` implementation
- Memory base pointer passed as first argument to every native function

### Future

- i64, f32, f64 opcodes
- Benchmark on real workloads (SQLite, Prism)
- Wrap Cranelift bridge with Chicory build-time compiler (wabt/wasm-tools pattern)
- `Machine` implementation with hybrid dispatch (native + interpreter fallback)
