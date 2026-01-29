extern crate lumis;

use std::ptr;

use lumis::{highlight, languages::Language, themes, TerminalBuilder};

// Import malloc and free from WASI-SDK C library
extern "C" {
    fn malloc(size: usize) -> *mut u8;
    fn free(ptr: *mut u8);
}

// Expose WASI malloc as wasm_malloc
#[no_mangle]
pub extern "C" fn wasm_malloc(size: usize) -> *mut u8 {
    unsafe { malloc(size) }
}

// Expose WASI free as wasm_free
#[no_mangle]
pub extern "C" fn wasm_free(ptr: *mut u8) {
    unsafe { free(ptr) }
}

#[no_mangle]
pub extern "C" fn demo(code_ptr: i32, code_len: i32) -> i64 {
    // Read the input string from WASM memory
    let code = unsafe {
        let bytes = std::slice::from_raw_parts(code_ptr as *const u8, code_len as usize);
        std::str::from_utf8(bytes).expect("Invalid UTF-8")
    };

    let theme = themes::get("github_dark").expect("github_dark theme should be available");

    let formatter = TerminalBuilder::new()
        .lang(Language::JavaScript)
        .theme(Some(theme))
        .build()
        .expect("Failed to build formatter");

    let ansi_output = highlight(code, formatter);

    // println!("FROM RUST");
    // println!("{}", ansi_output);
    // println!("END FROM RUST");

    pack_string(ansi_output.to_string())
}

fn pack_string(s: String) -> i64 {
    let bytes = s.as_bytes();
    let len = bytes.len();
    
    unsafe {
        let ptr = wasm_malloc(len);
        
        if ptr.is_null() {
            return pack_string("Allocation failed".to_string());
        }
        
        ptr::copy_nonoverlapping(bytes.as_ptr(), ptr, len);
        
        let result = (((ptr as u32 as u64) << 32) | (len as u32 as u64)) as i64;
        
        // println!("RESULT: {}", result);

        result
    }
}
