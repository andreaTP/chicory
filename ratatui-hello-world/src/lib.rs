use std::ptr;
use std::mem;
use std::sync::Mutex;

use ratatui::widgets::{Block, Borders, Paragraph, List, ListItem, ListState, Gauge};
use ratatui::layout::{Layout, Constraint, Direction};
use ratatui::Terminal;
use ratatui::backend::TestBackend;
use ratatui::style::{Style, Color};

// Application state
struct AppState {
    counter: i32,
    selected_item: usize,
    items: Vec<String>,
    quit: bool,
    terminal_width: u16,
    terminal_height: u16,
}

impl AppState {
    fn new() -> Self {
        Self {
            counter: 0,
            selected_item: 0,
            items: vec![
                "Item 1".to_string(),
                "Item 2".to_string(),
                "Item 3".to_string(),
                "Item 4".to_string(),
                "Item 5".to_string(),
            ],
            quit: false,
            terminal_width: 80,
            terminal_height: 24,
        }
    }
}

// Global state (using Mutex for thread safety, though WASM is single-threaded)
static STATE: Mutex<Option<AppState>> = Mutex::new(None);

fn get_state() -> std::sync::MutexGuard<'static, Option<AppState>> {
    STATE.lock().unwrap()
}

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

// Set terminal size
#[unsafe(no_mangle)]
pub extern "C" fn set_terminal_size(width: i32, height: i32) {
    let mut state = get_state();
    if let Some(ref mut app) = *state {
        app.terminal_width = width as u16;
        app.terminal_height = height as u16;
    } else {
        let mut app = AppState::new();
        app.terminal_width = width as u16;
        app.terminal_height = height as u16;
        *state = Some(app);
    }
}

// Initialize the application state
#[unsafe(no_mangle)]
pub extern "C" fn init() {
    let mut state = get_state();
    if state.is_none() {
        *state = Some(AppState::new());
    }
}

// Handle keyboard input
// Returns: 0 = continue, 1 = quit
#[unsafe(no_mangle)]
pub extern "C" fn handle_key(key: u8) -> i32 {
    let mut state = get_state();
    if let Some(ref mut app) = *state {
        match key {
            b'q' | b'Q' => {
                app.quit = true;
                return 1;
            }
            b'j' | 0x1B => { // 'j' or arrow down (ESC sequence start, simplified)
                if app.selected_item < app.items.len().saturating_sub(1) {
                    app.selected_item += 1;
                }
            }
            b'k' => { // 'k' or arrow up (simplified)
                if app.selected_item > 0 {
                    app.selected_item -= 1;
                }
            }
            b'+' | b'=' => {
                app.counter += 1;
            }
            b'-' | b'_' => {
                app.counter -= 1;
            }
            b'r' | b'R' => {
                app.counter = 0;
            }
            _ => {}
        }
    }
    0
}

// Render the current state and return ANSI output
#[unsafe(no_mangle)]
pub extern "C" fn render() -> i64 {
    let state = get_state();
    let default_state = AppState::new();
    let app = state.as_ref().unwrap_or(&default_state);
    
    // Use the stored terminal dimensions
    let width = app.terminal_width;
    let height = app.terminal_height;
    
    // Create a test backend with actual terminal dimensions
    let backend = TestBackend::new(width, height);
    
    // Create terminal and render the UI
    let mut terminal = Terminal::new(backend).expect("Failed to create terminal");
    
    terminal.draw(|frame| {
        let area = frame.area();
        
        // Create a layout with title, content, and help
        let chunks = Layout::default()
            .direction(Direction::Vertical)
            .constraints([
                Constraint::Length(3),
                Constraint::Min(10),
                Constraint::Length(3),
            ])
            .split(area);
        
        // Title section
        let title_block = Block::default()
            .borders(Borders::ALL)
            .title(" Interactive Ratatui Demo ");
        let title_text = Paragraph::new("Use keys to interact!")
            .block(title_block)
            .alignment(ratatui::layout::Alignment::Center);
        frame.render_widget(title_text, chunks[0]);
        
        // Main content area
        let content_chunks = Layout::default()
            .direction(Direction::Horizontal)
            .constraints([Constraint::Percentage(50), Constraint::Percentage(50)])
            .split(chunks[1]);
        
        // Left: List (switched from right)
        let list_block = Block::default()
            .borders(Borders::ALL)
            .title(" Items ");
        let items: Vec<ListItem> = app.items
            .iter()
            .enumerate()
            .map(|(i, item)| {
                let style = if i == app.selected_item {
                    Style::default().fg(Color::Yellow).bg(Color::DarkGray)
                } else {
                    Style::default()
                };
                ListItem::new(item.clone()).style(style)
            })
            .collect();
        let mut list_state = ListState::default();
        list_state.select(Some(app.selected_item));
        let list = List::new(items)
            .block(list_block)
            .highlight_style(Style::default().fg(Color::Yellow).bg(Color::DarkGray));
        frame.render_stateful_widget(list, content_chunks[0], &mut list_state);
        
        // Right: Counter with Gauge (switched from left, added Gauge)
        let right_chunks = Layout::default()
            .direction(Direction::Vertical)
            .constraints([
                Constraint::Length(3),
                Constraint::Min(3),
                Constraint::Length(3),
            ])
            .split(content_chunks[1]);
        
        // Counter text
        let counter_block = Block::default()
            .borders(Borders::ALL)
            .title(" Counter ");
        let counter_text = Paragraph::new(format!("Value: {}", app.counter))
            .block(counter_block)
            .alignment(ratatui::layout::Alignment::Center)
            .style(Style::default().fg(Color::Cyan));
        frame.render_widget(counter_text, right_chunks[0]);
        
        // Gauge showing counter value (cool visual widget!)
        let gauge_block = Block::default()
            .borders(Borders::ALL)
            .title(" Progress ");
        // Normalize counter to 0-100 range for gauge (using absolute value)
        let counter_abs = app.counter.abs() as u16;
        let gauge_value = (counter_abs % 101) as u16; // Keep it in 0-100 range
        let gauge = Gauge::default()
            .block(gauge_block)
            .gauge_style(Style::default()
                .fg(match gauge_value {
                    0..=33 => Color::Red,
                    34..=66 => Color::Yellow,
                    _ => Color::Green,
                })
                .bg(Color::DarkGray))
            .percent(gauge_value)
            .label(format!("{}%", gauge_value));
        frame.render_widget(gauge, right_chunks[1]);
        
        // Status info
        let status_block = Block::default()
            .borders(Borders::ALL)
            .title(" Status ");
        let status_text = Paragraph::new(format!(
            "Selected: Item {}\nCounter: {}",
            app.selected_item + 1,
            app.counter
        ))
        .block(status_block);
        frame.render_widget(status_text, right_chunks[2]);
        
        // Footer with help
        let help_block = Block::default()
            .borders(Borders::ALL)
            .title(" Help ");
        let help_text = Paragraph::new(
            "j/k: navigate | +/-: counter | r: reset | q: quit"
        )
        .block(help_block)
        .alignment(ratatui::layout::Alignment::Center);
        frame.render_widget(help_text, chunks[2]);
    }).expect("Failed to draw frame");
    
    // Get the buffer from the backend and convert to ANSI
    let backend = terminal.backend();
    let buffer = backend.buffer();
    let ansi_output = buffer_to_ansi(buffer);
    
    pack_string(ansi_output)
}

// Main demo function - initializes and renders
#[unsafe(no_mangle)]
pub extern "C" fn demo() -> i64 {
    // Initialize if not already done
    {
        let mut state = get_state();
        if state.is_none() {
            *state = Some(AppState::new());
        }
    }
    
    render()
}

fn buffer_to_ansi(buffer: &ratatui::buffer::Buffer) -> String {
    let mut output = String::new();
    
    // Hide cursor during rendering
    output.push_str("\x1b[?25l");
    
    // Move cursor to top-left without clearing (faster, less flicker)
    output.push_str("\x1b[H");
    
    let width = buffer.area.width as usize;
    let height = buffer.area.height as usize;
    
    // Render each row with optimized ANSI codes
    for y in 0..height {
        let mut current_fg: Option<ratatui::style::Color> = None;
        let mut current_bg: Option<ratatui::style::Color> = None;
        
        for x in 0..width {
            let cell = &buffer[(x as u16, y as u16)];
            let ch = cell.symbol().chars().next().unwrap_or(' ');
            let style = cell.style();
            
            // Check if colors changed
            let fg_changed = current_fg != style.fg;
            let bg_changed = current_bg != style.bg;
            
            if fg_changed || bg_changed {
                if current_fg.is_some() || current_bg.is_some() {
                    output.push_str("\x1b[0m"); // Reset
                }
                
                // Apply foreground color
                if fg_changed {
                    current_fg = style.fg;
                    if let Some(color) = style.fg {
                        match color {
                            ratatui::style::Color::Rgb(r, g, b) => {
                                output.push_str(&format!("\x1b[38;2;{};{};{}m", r, g, b));
                            }
                            ratatui::style::Color::Cyan => {
                                output.push_str("\x1b[36m");
                            }
                            ratatui::style::Color::Yellow => {
                                output.push_str("\x1b[33m");
                            }
                            ratatui::style::Color::Green => {
                                output.push_str("\x1b[32m");
                            }
                            ratatui::style::Color::Red => {
                                output.push_str("\x1b[31m");
                            }
                            ratatui::style::Color::Blue => {
                                output.push_str("\x1b[34m");
                            }
                            ratatui::style::Color::Magenta => {
                                output.push_str("\x1b[35m");
                            }
                            ratatui::style::Color::White => {
                                output.push_str("\x1b[37m");
                            }
                            ratatui::style::Color::Black => {
                                output.push_str("\x1b[30m");
                            }
                            _ => {}
                        }
                    }
                }
                
                // Apply background color
                if bg_changed {
                    current_bg = style.bg;
                    if let Some(color) = style.bg {
                        match color {
                            ratatui::style::Color::Rgb(r, g, b) => {
                                output.push_str(&format!("\x1b[48;2;{};{};{}m", r, g, b));
                            }
                            ratatui::style::Color::DarkGray => {
                                output.push_str("\x1b[100m");
                            }
                            ratatui::style::Color::Gray => {
                                output.push_str("\x1b[47m");
                            }
                            ratatui::style::Color::Black => {
                                output.push_str("\x1b[40m");
                            }
                            _ => {}
                        }
                    }
                }
            }
            
            output.push(ch);
        }
        
        // Reset at end of line
        if current_fg.is_some() || current_bg.is_some() {
            output.push_str("\x1b[0m");
            current_fg = None;
            current_bg = None;
        }
        
        if y < height - 1 {
            output.push('\n');
        }
    }
    
    // Show cursor at the end
    output.push_str("\x1b[?25h");
    
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
