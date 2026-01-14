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
