# Chicory Native Compilation: Cranelift + Panama

## Problem

Large Wasm functions compiled to JVM bytecode by Chicory's AOT compiler may not be well-optimized by the JVM's C2 JIT.
Big methods won't get inlined, and the Java interpreter is slow for unoptimized code.
This creates a performance ceiling for compute-heavy Wasm modules.

## Idea

Use Cranelift (compiled to Wasm, running inside Chicory) as a thin code generation bridge.
The compilation logic stays in Java — reusing the existing `WasmAnalyzer`/`Emitters` pattern —
and calls into Cranelift's `FunctionBuilder` API via Wasm exports. The native code is
executed through Panama/FFM. Zero shipped native libraries.

## Architecture

```
Java (compilation logic)                 Rust/Wasm (thin Cranelift bridge)
========================                 =================================
WasmAnalyzer
  → CompilerInstruction[]

NativeEmitters                           cranelift-bridge.wasm exports:
  I32_ADD:
    a = pop()               ──────────►  emit_iadd(func_id, a, b) -> value_id
    b = pop()
    result = bridge.iadd(a,b)            internally:
    push(result)                           builder.ins().iadd(a, b)

  I32_LOAD:
    addr = pop()            ──────────►  emit_load_i32(func_id, addr, offset) -> value_id
    result = bridge.load(..)
    push(result)                           builder.ins().load(I32, ...)

  compile()                 ──────────►  compile(func_id) -> writes code to linear memory
    read native bytes from bridge memory
    mmap + Panama downcall
```

This mirrors the existing compiler:

| Chicory JVM Compiler | Cranelift Native Compiler |
|---|---|
| `WasmAnalyzer` walks opcodes | `WasmAnalyzer` walks opcodes (same, reused) |
| `CompilerInstruction` IR | `CompilerInstruction` IR (same, reused) |
| `Emitters` → `asm.visitInsn(IADD)` | `NativeEmitters` → `bridge.iadd(a, b)` |
| ASM `InstructionAdapter` | Cranelift `FunctionBuilder` (in Wasm) |
| Output: JVM `.class` files | Output: native x86_64/aarch64 bytes |

## PoC results so far

### Done: Panama mmap + execute

Validated that Panama can mmap+execute Cranelift-compiled native code (see `native-poc/`):
- `add(17, 25) = 42` works across interpreter, AOT, and Panama native paths
- Cranelift compiled to wasm32-wasip1 (6.8MB) runs inside Chicory and produces native ELF
- Zero native libraries shipped

## Next steps

### Step 1: Cranelift bridge (thin Rust/Wasm layer)

Create `native-poc/cranelift-bridge/` — a ~200-line Rust module that exposes Cranelift's
`FunctionBuilder` API as flat Wasm exports. All handles (func_id, block_id, value_id,
var_id) are opaque `u32`.

**Wasm exports:**
```
// Setup
init(target_ptr, target_len)
create_function() -> func_id
add_param_type(func_id, wasm_type)
add_return_type(func_id, wasm_type)
build_function(func_id)                     // finalize signature, create entry block

// Variables (Wasm locals)
declare_var(func_id, wasm_type) -> var_id
def_var(func_id, var_id, value_id)
use_var(func_id, var_id) -> value_id

// Function params
func_param(func_id, index) -> value_id

// Constants
emit_iconst_32(func_id, val) -> value_id
emit_iconst_64(func_id, val_lo, val_hi) -> value_id

// Arithmetic
emit_iadd(func_id, a, b) -> value_id
emit_isub(func_id, a, b) -> value_id
emit_imul(func_id, a, b) -> value_id
// ... extend as needed

// Memory access
emit_load_i32(func_id, base, wasm_addr, offset) -> value_id
emit_store_i32(func_id, base, wasm_addr, value, offset)

// Control flow
create_block(func_id) -> block_id
switch_to_block(func_id, block_id)
seal_block(func_id, block_id)
emit_jump(func_id, block_id)
emit_brif(func_id, cond, then_block, else_block)
emit_return(func_id, value_id)
emit_return_void(func_id)

// Compile
compile(func_id) -> code_length            // writes native bytes to linear memory
get_code_ptr(func_id) -> ptr               // pointer to code bytes in linear memory
```

**Deps:** `cranelift-codegen 0.129`, `cranelift-frontend 0.129`, `cranelift-control`,
`target-lexicon`. No wasmparser, no wasmtime.

**Calling convention (our design):**
- First param: memory base pointer (i64, raw pointer to linear memory)
- Remaining params: Wasm function parameters
- Return: Wasm return value
- Uses System V ABI

### Step 2: Java bridge wrapper (`CraneliftBridge.java`)

Loads the bridge Wasm module, provides typed Java methods:

```java
class CraneliftBridge {
    int createFunction() { ... }
    void addParamType(int funcId, ValType type) { ... }
    int iadd(int funcId, int a, int b) { ... }
    byte[] compile(int funcId) { ... }  // reads native bytes from bridge memory
}
```

### Step 3: NativeEmitters (minimal, for `add_and_store` example)

Mirrors `Emitters.java`. Uses `WasmAnalyzer` output unchanged. Maintains an explicit
`Deque<Integer>` value stack (Cranelift uses SSA values, not an implicit stack).

Initially handles: `LOCAL_GET`, `LOCAL_SET`, `I32_ADD`, `I32_STORE`, `I32_CONST`, `END`.

### Step 4: Off-heap NativeMemory

Based on old `ByteArrayMemory` — uses Panama `MemorySegment` for contiguous off-heap
buffer. Exposes `address()` for passing to native code as the memory base pointer.

### Step 5: End-to-end test with `add_and_store.wat`

```wat
(module
  (memory (export "memory") 1)
  (func (export "add_and_store") (param $a i32) (param $b i32) (param $addr i32) (result i32)
    (local $sum i32)
    (local.set $sum (i32.add (local.get $a) (local.get $b)))
    (i32.store (local.get $addr) (local.get $sum))
    (local.get $sum)
  )
)
```

Verify: interpreter result = AOT result = Panama native result, and memory write is correct.

### Future steps (not in this PoC)

- Add more opcodes incrementally (i64, f32, f64, comparisons, control flow)
- CALL / CALL_INDIRECT support
- Host function callbacks via Panama upcall stubs
- `Machine` implementation plugging into `withMachineFactory()`
- Benchmark on real workloads (SQLite, Prism)
- Wrap Cranelift bridge with Chicory build-time compiler (wabt/wasm-tools pattern)
