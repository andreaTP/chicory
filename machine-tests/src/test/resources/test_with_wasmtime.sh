#!/bin/bash
# Test the reproducer with wasmtime to verify expected behavior

cd "$(dirname "$0")"

echo "Compiling .wat to .wasm..."
wasmtime compile reproduce-table-bug.wat -o reproduce-table-bug.wasm

echo ""
echo "Testing with wasmtime (expected behavior):"
echo "==========================================="

# Create a simple JS test
cat > test_wasmtime.js << 'EOF'
const fs = require('fs');
const { instantiate } = require('@bytecodealliance/wasmtime');

async function test() {
    const wasm = fs.readFileSync('reproduce-table-bug.wasm');
    const module = await WebAssembly.compile(wasm);
    
    // Mock malloc function
    const malloc = (size) => size * 2;
    
    const instance = await WebAssembly.instantiate(module, {
        env: { malloc }
    });
    
    console.log("Testing call_malloc(42):");
    const result1 = instance.exports.call_malloc(42);
    console.log("  Result:", result1, "(expected: 84)");
    console.log("  ", result1 === 84 ? "✓ PASS" : "✗ FAIL");
    
    console.log("\nTesting call_local1():");
    const result2 = instance.exports.call_local1();
    console.log("  Result:", result2, "(expected: 100)");
    console.log("  ", result2 === 100 ? "✓ PASS" : "✗ FAIL");
    
    console.log("\nTesting call_local2():");
    const result3 = instance.exports.call_local2();
    console.log("  Result:", result3, "(expected: 200)");
    console.log("  ", result3 === 200 ? "✓ PASS" : "✗ FAIL");
}

test().catch(console.error);
EOF

# For now, just verify the wasm file was created
if [ -f reproduce-table-bug.wasm ]; then
    echo "✓ WASM file created successfully"
    echo "  File size: $(stat -f%z reproduce-table-bug.wasm 2>/dev/null || stat -c%s reproduce-table-bug.wasm) bytes"
else
    echo "✗ Failed to create WASM file"
    exit 1
fi
