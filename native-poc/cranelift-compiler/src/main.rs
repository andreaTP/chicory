use std::io::{self, Read, Write};
use wasmtime::{Config, Engine, OptLevel};

/// Minimal Wasm-to-native compiler using wasmtime/Cranelift.
///
/// Reads a .wasm module from stdin, compiles it to native code,
/// and writes the serialized compiled module to stdout.
///
/// The output is wasmtime's serialized format (ELF with .text section
/// containing Cranelift-compiled native code).
///
/// Usage: cranelift-compiler [target-triple] < input.wasm > output.cwasm
fn main() {
    let mut wasm_bytes = Vec::new();
    io::stdin().read_to_end(&mut wasm_bytes).expect("Failed to read stdin");

    let target = std::env::args().nth(1);

    let mut config = Config::new();
    config.cranelift_opt_level(OptLevel::Speed);

    if let Some(ref target) = target {
        config.target(target).expect("Failed to set target");
    }

    let engine = Engine::new(&config).expect("Failed to create engine");

    let serialized = engine
        .precompile_module(&wasm_bytes)
        .expect("Failed to compile module");

    io::stdout()
        .write_all(&serialized)
        .expect("Failed to write output");

    eprintln!(
        "Compiled {} bytes of wasm to {} bytes of native code",
        wasm_bytes.len(),
        serialized.len()
    );
}
