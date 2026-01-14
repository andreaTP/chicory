# Function Table Index Bug Reproducer

## Problem
During `palloc` in PGLite, when attempting to invoke "malloc" via `call_indirect`, the function index stored in the table is completely wrong.

## Hypothesis
The issue likely occurs when:
1. Element segments initialize function tables with function references
2. The function indices in element segments might not account for imported functions correctly
3. When `call_indirect` retrieves the function index from the table, it gets the wrong value

## Minimal Test Case
The file `reproduce-table-bug.wat` contains a minimal test case that:
- Imports a function (`malloc`)
- Defines local functions
- Uses element segments to store function references in a table
- Calls functions via `call_indirect`

## Testing
To test this:
1. Compile the .wat file to .wasm (using wasmtime or wat2wasm)
2. Run the test in Chicory
3. Compare with wasmtime behavior

## Next Steps
1. Run the test and observe the failure
2. Add debug logging to see what function indices are stored/retrieved
3. Bisect further to isolate the exact issue
