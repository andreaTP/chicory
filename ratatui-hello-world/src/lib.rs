use std::ptr;
use std::mem;

use ratatui::widgets::{Block, Borders, Paragraph};
use ratatui::layout::{Layout, Constraint, Direction};
use ratatui::Terminal;
use ratatui::backend::TestBackend;

// Use Rust's standard library allocator instead of importing malloc
#[unsafe(no_mangle)]
pub extern "C" fn wasm_malloc(size: usize) -> *mut u8 {
    let mut buf = Vec::with_capacity(size);
    let ptr = buf.as_mut_ptr();
    // Tell Rust not to clean this up - caller will manage it
    mem::forget(buf);
    ptr
}

#[unsafe(no_mangle)]
pub extern "C" fn wasm_free(ptr: *mut u8, len: usize) {
    // Reconstruct the Vec to properly deallocate
    unsafe {
        let _ = Vec::from_raw_parts(ptr, 0, len);
    }
}

#[unsafe(no_mangle)]
pub extern "C" fn demo() -> i64 {
    // Create a test backend with terminal-like dimensions (80x24)
    let backend = TestBackend::new(80, 24);
    
    // Create terminal and render the UI
    let mut terminal = Terminal::new(backend).expect("Failed to create terminal");
    
    terminal.draw(|frame| {
        let area = frame.area();
        
        // Create a simple layout with title and content
        let chunks = Layout::default()
            .direction(Direction::Vertical)
            .constraints([
                Constraint::Length(3),
                Constraint::Min(5),
            ])
            .split(area);
        
        // Title section
        let title_block = Block::default()
            .borders(Borders::ALL)
            .title(" Ratatui + Mousefood ");
        let title_text = Paragraph::new("Hello from Ratatui!")
            .block(title_block)
            .alignment(ratatui::layout::Alignment::Center);
        frame.render_widget(title_text, chunks[0]);
        
        // Content section
        let content_block = Block::default()
            .borders(Borders::ALL)
            .title(" Demo ");
        let content_text = Paragraph::new(
            "This is a static terminal output\ngenerated using Ratatui widgets\nand rendered with Mousefood."
        )
        .block(content_block);
        frame.render_widget(content_text, chunks[1]);
    }).expect("Failed to draw frame");
    
    // Get the buffer from the backend and convert to ANSI
    let backend = terminal.backend();
    let buffer = backend.buffer();
    let ansi_output = buffer_to_ansi(buffer);
    
    pack_string(ansi_output)
}

fn buffer_to_ansi(buffer: &ratatui::buffer::Buffer) -> String {
    let mut output = String::new();
    
    // Clear screen and move cursor to top-left
    output.push_str("\x1b[2J\x1b[H");
    
    let width = buffer.area.width as usize;
    let height = buffer.area.height as usize;
    
    // Render each row
    for y in 0..height {
        let mut current_style: Option<ratatui::style::Style> = None;
        
        for x in 0..width {
            let cell = &buffer[(x as u16, y as u16)];
            let ch = cell.symbol().chars().next().unwrap_or(' ');
            
            // Check if style changed
            if current_style != Some(cell.style()) {
                if current_style.is_some() {
                    output.push_str("\x1b[0m"); // Reset
                }
                current_style = Some(cell.style());
                
                // Apply ANSI color codes
                if let Some(ratatui::style::Color::Rgb(r, g, b)) = cell.style().fg {
                    output.push_str(&format!("\x1b[38;2;{};{};{}m", r, g, b));
                } else if cell.style().fg != Some(ratatui::style::Color::Reset) {
                    // Handle other color types if needed
                    output.push_str("\x1b[0m");
                }
            }
            
            output.push(ch);
        }
        
        // Reset at end of line
        if current_style.is_some() {
            output.push_str("\x1b[0m");
        }
        
        if y < height - 1 {
            output.push('\n');
        }
    }
    
    output
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
        
        result
    }
}
