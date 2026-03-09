(module
  (memory (export "memory") 1)
  (func (export "add_and_store") (param $a i32) (param $b i32) (param $addr i32) (result i32)
    (local $sum i32)
    (local.set $sum (i32.add (local.get $a) (local.get $b)))
    (i32.store (local.get $addr) (local.get $sum))
    (local.get $sum)
  )
)
