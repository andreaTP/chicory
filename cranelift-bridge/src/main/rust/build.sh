#!/bin/bash
set -euo pipefail

# Build the Cranelift bridge Wasm module from Rust source.
# Requires: cargo, rustup target wasm32-wasip1
#
# Output: ../src/main/resources/cranelift-bridge.wasm

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# Ensure the wasm32-wasip1 target is installed
rustup target add wasm32-wasip1 2>/dev/null || true

# Build
cargo build --release --target wasm32-wasip1

echo "Built cranelift-bridge.wasm"
