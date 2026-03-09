# Cranelift Bridge

Java wrapper around Cranelift's `FunctionBuilder` API, compiled to WebAssembly and
executed inside Chicory. This enables native code generation from pure Java with zero
shipped native libraries.

## How it works

The Rust source in `rust-src/` is a thin FFI bridge that exposes Cranelift's code
generation API as flat Wasm exports. The Java side (`CraneliftBridge.java`) calls
these exports to build Cranelift IR, then compiles it to native machine code.

```
Java (CraneliftBridge)              Wasm (cranelift-bridge.wasm)
======================              ============================
bridge.emitIadd(a, b)  ──────────► emit_iadd(a, b)
                                       builder.ins().iadd(va, vb)
                                       return value_id

bridge.compile()        ──────────► compile()
                                       builder.finalize()
                                       ctx.compile(&isa)
                                       return code bytes
```

All value/block/variable IDs are explicit u32 handles — Java tracks them and passes
them back. The Rust side is purely a handle-to-Cranelift-object mapping layer.

## Building the Wasm module

Prerequisites: Rust toolchain with `wasm32-wasip1` target.

```bash
cd rust-src
./build.sh
```

This compiles the Rust source to `src/main/resources/cranelift-bridge.wasm` (~3.5MB).

The pre-built `.wasm` file is checked into the repository so that building the Java
module does not require a Rust toolchain.

## Rust source structure

```
rust-src/
├── Cargo.toml          # Dependencies: cranelift-codegen 0.129, cranelift-frontend 0.129
├── build.sh            # Build script (cargo build --target wasm32-wasip1)
└── src/
    └── lib.rs          # Bridge implementation (~200 lines)
```

## Exported Wasm functions

### Setup
- `init(target_ptr, target_len)` — set target architecture (e.g., "x86_64-unknown-linux-gnu")
- `create_function()` — start a new function
- `add_param_type(wasm_type)` — add parameter (0=i32, 1=i64, 2=f32, 3=f64)
- `add_return_type(wasm_type)` — add return type
- `build_function()` — finalize signature, create FunctionBuilder

### Blocks
- `create_block() -> block_id`
- `switch_to_block(block_id)`
- `seal_block(block_id)`
- `seal_all_blocks()`
- `append_block_params_for_func_params(block_id)`

### Variables (Wasm locals)
- `declare_var(wasm_type) -> var_id`
- `def_var(var_id, val_id)`
- `use_var(var_id) -> val_id`

### Values
- `func_param(block_id, index) -> val_id`
- `emit_iconst_32(val) -> val_id`
- `emit_iadd(a, b) -> val_id`
- `emit_isub(a, b) -> val_id`
- `emit_imul(a, b) -> val_id`

### Memory
- `emit_load_i32(base, wasm_addr, offset) -> val_id`
- `emit_store_i32(base, wasm_addr, value, offset)`

### Control flow
- `emit_jump(block_id)`
- `emit_brif(cond, then_block, else_block)`
- `emit_return(val_id)`
- `emit_return_void()`

### Compile
- `compile() -> code_length`
- `get_code_ptr() -> ptr`
- `get_code_len() -> len`

## Adding new opcodes

1. Add a new exported function in `rust-src/src/lib.rs`
2. Rebuild: `cd rust-src && ./build.sh`
3. Add a matching Java method in `CraneliftBridge.java`
4. Add the opcode case in `cranelift-compiler/NativeCompiler.java`
5. Run tests: `mvn test -pl cranelift-compiler -Pdev`
