(module
  (func $main (export "main") (result i32)
    i32.const 1
    i32.const 3
    block $outerBlock
      i32.const 5
      i32.const 7
      if
        i32.const 0
        br_table $outerBlock
      end
      drop
    end
    i32.add))
