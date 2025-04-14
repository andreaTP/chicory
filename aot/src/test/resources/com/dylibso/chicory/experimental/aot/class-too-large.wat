(module
{{#each functions}}
  (func $func_{{.}} (export "func_{{.}}") (param i32) (result i32)
    local.get 0
    i32.const {{.}}
    i32.add

    {{#each ../instructions~}}
    i32.const 1
    i32.add
    i32.const 1
    i32.sub
    {{/each}}
    {{#neq . 1 ~}}
    call $func_{{minus . 1}}
    {{/neq ~}}
)
{{/each ~}}
)