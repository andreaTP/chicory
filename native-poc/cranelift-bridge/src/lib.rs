//! Thin FFI bridge exposing Cranelift's FunctionBuilder API as flat Wasm exports.
//!
//! All handles (block_id, value_id, var_id) are opaque u32.
//! The Java side tracks them explicitly and passes them to subsequent calls.
//! Each export immediately calls the corresponding Cranelift API — no accumulation.

use cranelift_codegen::ir::types;
use cranelift_codegen::ir::{AbiParam, BlockArg, Function, InstBuilder, MemFlags, Signature, UserFuncName};
use cranelift_codegen::isa::{self, CallConv, TargetIsa};
use cranelift_codegen::settings::{self, Configurable};
use cranelift_codegen::Context;
use cranelift_control::ControlPlane;
use cranelift_frontend::{FunctionBuilder, FunctionBuilderContext, Variable};
use std::sync::Arc;
use target_lexicon::Triple;

// --- Global state (single-threaded Wasm, safe) ---

static mut ISA: Option<Arc<dyn TargetIsa>> = None;
static mut COMPILED_CODE: Vec<u8> = Vec::new();

/// Session holds the FunctionBuilder and its backing data.
/// We use raw pointers to keep FunctionBuilder alive across FFI calls,
/// working around the borrow checker. Safe because Wasm is single-threaded.
struct Session {
    // These are heap-allocated and pinned so their addresses are stable.
    func: Box<Function>,
    builder_ctx: Box<FunctionBuilderContext>,
    // The FunctionBuilder borrows func and builder_ctx.
    // We store it as a raw pointer to avoid lifetime issues.
    builder: *mut FunctionBuilder<'static>,
    // Lookup tables: synthetic u32 IDs -> real Cranelift handles
    blocks: Vec<cranelift_codegen::ir::Block>,
    variables: Vec<Variable>,
    values: Vec<cranelift_codegen::ir::Value>,
}

static mut SESSION: Option<Session> = None;

fn wasm_type_to_clif(ty: u32) -> cranelift_codegen::ir::Type {
    match ty {
        0 => types::I32,
        1 => types::I64,
        2 => types::F32,
        3 => types::F64,
        _ => panic!("Unknown wasm type: {}", ty),
    }
}

/// Get the active FunctionBuilder.
fn b() -> &'static mut FunctionBuilder<'static> {
    unsafe { &mut *SESSION.as_ref().unwrap().builder }
}

/// Get the session.
fn s() -> &'static mut Session {
    unsafe { SESSION.as_mut().unwrap() }
}

// --- Exported functions ---

#[no_mangle]
pub extern "C" fn init(target_ptr: *const u8, target_len: u32) {
    let target_str = unsafe {
        std::str::from_utf8(std::slice::from_raw_parts(target_ptr, target_len as usize)).unwrap()
    };
    let mut flag_builder = settings::builder();
    flag_builder.set("opt_level", "speed").unwrap();
    flag_builder.set("is_pic", "false").unwrap();
    let flags = settings::Flags::new(flag_builder);
    let triple: Triple = target_str.parse().expect("Failed to parse target triple");
    let isa = isa::lookup(triple)
        .expect("Unsupported target")
        .finish(flags)
        .expect("Failed to create ISA");
    unsafe { ISA = Some(isa); }
}

/// Create a new function and its FunctionBuilder. Call add_param_type/add_return_type
/// BEFORE build_function.
#[no_mangle]
pub extern "C" fn create_function() {
    let func = Box::new(Function::with_name_signature(
        UserFuncName::user(0, 0),
        Signature::new(CallConv::SystemV),
    ));
    let builder_ctx = Box::new(FunctionBuilderContext::new());

    unsafe {
        SESSION = Some(Session {
            func,
            builder_ctx,
            builder: std::ptr::null_mut(),
            blocks: Vec::new(),
            variables: Vec::new(),
            values: Vec::new(),
        });
    }
}

#[no_mangle]
pub extern "C" fn add_param_type(wasm_type: u32) {
    s().func.signature.params.push(AbiParam::new(wasm_type_to_clif(wasm_type)));
}

#[no_mangle]
pub extern "C" fn add_return_type(wasm_type: u32) {
    s().func.signature.returns.push(AbiParam::new(wasm_type_to_clif(wasm_type)));
}

/// Finalize the signature and create the FunctionBuilder.
/// Must be called after all add_param_type/add_return_type and before any emit calls.
#[no_mangle]
pub extern "C" fn build_function() {
    let session = s();
    // Create builder from raw pointers to avoid lifetime issues.
    // Safe: single-threaded, session outlives builder, and we only drop in compile().
    let func_ptr: *mut Function = &mut *session.func;
    let ctx_ptr: *mut FunctionBuilderContext = &mut *session.builder_ctx;
    let builder = unsafe {
        FunctionBuilder::new(&mut *func_ptr, &mut *ctx_ptr)
    };
    let builder_box = Box::new(builder);
    session.builder = Box::into_raw(builder_box) as *mut FunctionBuilder<'static>;
}

// --- Blocks ---

#[no_mangle]
pub extern "C" fn create_block() -> u32 {
    let block = b().create_block();
    let session = s();
    let id = session.blocks.len() as u32;
    session.blocks.push(block);
    id
}

#[no_mangle]
pub extern "C" fn switch_to_block(block_id: u32) {
    let block = s().blocks[block_id as usize];
    b().switch_to_block(block);
}

#[no_mangle]
pub extern "C" fn seal_block(block_id: u32) {
    let block = s().blocks[block_id as usize];
    b().seal_block(block);
}

#[no_mangle]
pub extern "C" fn seal_all_blocks() {
    b().seal_all_blocks();
}

#[no_mangle]
pub extern "C" fn append_block_params_for_func_params(block_id: u32) {
    let block = s().blocks[block_id as usize];
    b().append_block_params_for_function_params(block);
}

// --- Variables ---

#[no_mangle]
pub extern "C" fn declare_var(wasm_type: u32) -> u32 {
    let var = b().declare_var(wasm_type_to_clif(wasm_type));
    let session = s();
    let id = session.variables.len() as u32;
    session.variables.push(var);
    id
}

#[no_mangle]
pub extern "C" fn def_var(var_id: u32, val_id: u32) {
    let var = s().variables[var_id as usize];
    let val = s().values[val_id as usize];
    b().def_var(var, val);
}

#[no_mangle]
pub extern "C" fn use_var(var_id: u32) -> u32 {
    let var = s().variables[var_id as usize];
    let val = b().use_var(var);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(val);
    id
}

// --- Params ---

#[no_mangle]
pub extern "C" fn func_param(block_id: u32, index: u32) -> u32 {
    let block = s().blocks[block_id as usize];
    let params = b().block_params(block);
    let val = params[index as usize];
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(val);
    id
}

// --- Constants ---

#[no_mangle]
pub extern "C" fn emit_iconst_32(val: i32) -> u32 {
    let v = b().ins().iconst(types::I32, val as i64);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(v);
    id
}

#[no_mangle]
pub extern "C" fn emit_iconst_64(val_lo: u32, val_hi: u32) -> u32 {
    let val = (val_lo as i64) | ((val_hi as i64) << 32);
    let v = b().ins().iconst(types::I64, val);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(v);
    id
}

// --- Arithmetic ---

#[no_mangle]
pub extern "C" fn emit_iadd(a: u32, b_id: u32) -> u32 {
    let va = s().values[a as usize];
    let vb = s().values[b_id as usize];
    let r = b().ins().iadd(va, vb);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(r);
    id
}

#[no_mangle]
pub extern "C" fn emit_isub(a: u32, b_id: u32) -> u32 {
    let va = s().values[a as usize];
    let vb = s().values[b_id as usize];
    let r = b().ins().isub(va, vb);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(r);
    id
}

#[no_mangle]
pub extern "C" fn emit_imul(a: u32, b_id: u32) -> u32 {
    let va = s().values[a as usize];
    let vb = s().values[b_id as usize];
    let r = b().ins().imul(va, vb);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(r);
    id
}

// --- Memory ---

#[no_mangle]
pub extern "C" fn emit_load_i32(base: u32, wasm_addr: u32, offset: i32) -> u32 {
    let vbase = s().values[base as usize];
    let vaddr = s().values[wasm_addr as usize];
    let extended = b().ins().uextend(types::I64, vaddr);
    let effective = b().ins().iadd(vbase, extended);
    let val = b().ins().load(types::I32, MemFlags::new(), effective, offset);
    let session = s();
    let id = session.values.len() as u32;
    session.values.push(val);
    id
}

#[no_mangle]
pub extern "C" fn emit_store_i32(base: u32, wasm_addr: u32, value: u32, offset: i32) {
    let vbase = s().values[base as usize];
    let vaddr = s().values[wasm_addr as usize];
    let vvalue = s().values[value as usize];
    let extended = b().ins().uextend(types::I64, vaddr);
    let effective = b().ins().iadd(vbase, extended);
    b().ins().store(MemFlags::new(), vvalue, effective, offset);
}

// --- Control flow ---

#[no_mangle]
pub extern "C" fn emit_jump(block_id: u32) {
    let block = s().blocks[block_id as usize];
    let no_args: &[BlockArg] = &[];
    b().ins().jump(block, no_args);
}

#[no_mangle]
pub extern "C" fn emit_brif(cond: u32, then_block: u32, else_block: u32) {
    let vcond = s().values[cond as usize];
    let bt = s().blocks[then_block as usize];
    let be = s().blocks[else_block as usize];
    let no_args: &[BlockArg] = &[];
    b().ins().brif(vcond, bt, no_args, be, no_args);
}

#[no_mangle]
pub extern "C" fn emit_return(val_id: u32) {
    let val = s().values[val_id as usize];
    b().ins().return_(&[val]);
}

#[no_mangle]
pub extern "C" fn emit_return_void() {
    b().ins().return_(&[]);
}

// --- Compile ---

/// Finalize the builder, compile to native code, return code length.
/// Code bytes are stored internally; read with get_code_ptr/get_code_len.
#[no_mangle]
pub extern "C" fn compile() -> u32 {
    let isa = unsafe { ISA.as_ref().expect("ISA not initialized") };
    let session = s();

    // Take ownership of the builder and finalize it
    let builder = unsafe { Box::from_raw(session.builder) };
    builder.finalize();
    session.builder = std::ptr::null_mut();

    // Compile
    let mut ctx = Context::for_function((*session.func).clone());
    let compiled = ctx
        .compile(isa.as_ref(), &mut ControlPlane::default())
        .expect("Compilation failed");

    let code = compiled.code_buffer();
    unsafe {
        COMPILED_CODE = code.to_vec();
        COMPILED_CODE.len() as u32
    }
}

#[no_mangle]
pub extern "C" fn get_code_ptr() -> *const u8 {
    unsafe { COMPILED_CODE.as_ptr() }
}

#[no_mangle]
pub extern "C" fn get_code_len() -> u32 {
    unsafe { COMPILED_CODE.len() as u32 }
}
