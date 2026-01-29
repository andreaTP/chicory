extern crate syntastica;
extern crate syntastica_parsers_git;

use std::mem;
use std::ptr;

use syntastica::renderer::TerminalRenderer;
use syntastica_parsers_git::{Lang, LanguageSetImpl};

#[no_mangle]
pub extern "C" fn demo() -> i64 {
    let html = syntastica::highlight(
            "fn main() {\n    println!(\"Hello, World!\");\n}",
            Lang::Rust,
            &LanguageSetImpl::new(),
            &mut TerminalRenderer::new(None),
            syntastica_themes::one::dark(),
        ).unwrap();

    pack_string(html.to_string())
}

fn pack_string(s: String) -> i64 {
    let bytes = s.as_bytes();
    let len = bytes.len();
    
    let mut buf = Vec::with_capacity(len);
    unsafe {
        ptr::copy_nonoverlapping(bytes.as_ptr(), buf.as_mut_ptr(), len);
        buf.set_len(len);
        
        let ptr = buf.as_mut_ptr() as u32;
        mem::forget(buf);
        
        let result = (((ptr as u64) << 32) | (len as u32 as u64)) as i64;

        result
    }
}
