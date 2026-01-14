;; Wrapper module to test the reproducer with wasmtime
;; This provides the malloc import and calls the test functions
(module
  (import "env" "malloc" (func $malloc (param i32) (result i32)))
  
  (import "test" "call_malloc" (func $call_malloc (param i32) (result i32)))
  (import "test" "call_local1" (func $call_local1 (result i32)))
  (import "test" "call_local2" (func $call_local2 (result i32)))
  
  (func $test (export "test")
    ;; Test call_malloc
    i32.const 42
    call $call_malloc
    drop
    
    ;; Test call_local1
    call $call_local1
    drop
    
    ;; Test call_local2
    call $call_local2
    drop
  )
)
