package com.dylibso.chicory.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dylibso.chicory.compiler.MachineFactoryCompiler;
import com.dylibso.chicory.runtime.DebugMapper;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.wasm.Parser;
import org.junit.jupiter.api.Test;

public class WasmModuleTest {

    @Test
    public void shouldThrowRustExceptions() {
        // Target Stack Trace:
        //        wasmtime --invoke count_vowels
        // wasm-corpus/src/main/resources/compiled/count_vowels.rs.wasm 0 0
        //        Error: failed to run main module
        // `wasm-corpus/src/main/resources/compiled/count_vowels.rs.wasm`
        //
        //        Caused by:
        //        0: failed to invoke `count_vowels`
        //        1: error while executing at wasm backtrace:
        //        0:   0x5c49 -
        // count_vowels.wasm!std::panicking::rust_panic_with_hook::hb39abb160cd4038c
        //        1:   0x522e -
        // count_vowels.wasm!std::panicking::begin_panic_handler::{{closure}}::h83b3d84f04c7372b
        //        2:   0x5168 -
        // count_vowels.wasm!std::sys::backtrace::__rust_end_short_backtrace::h8eb99c908c86e40b
        //        3:   0x57d7 - count_vowels.wasm!rust_begin_unwind
        //        4:   0x711a -
        // count_vowels.wasm!core::panicking::panic_nounwind_fmt::h7a87e102e925dda2
        //        5:   0x716e - count_vowels.wasm!core::panicking::panic_nounwind::hc189c31fedd6a605
        //        6:    0xccc -
        // count_vowels.wasm!core::slice::raw::from_raw_parts::precondition_check::h1e15a43dea7fa54e
        //        7:   0x1c04 -
        // count_vowels.wasm!core::slice::raw::from_raw_parts::h506ffe5f5bebefff
        //        8:   0x2d71 - count_vowels.wasm!count_vowels
        //        note: using the `WASMTIME_BACKTRACE_DETAILS=1` environment variable may show more
        // debugging information
        //        2: wasm trap: wasm `unreachable` instruction executed

        var instance =
                Instance.builder(
                                Parser.parse(
                                        WasmModuleTest.class.getResourceAsStream(
                                                "/compiled/count_vowels.rs.wasm")))
                        .withMachineFactory(MachineFactoryCompiler::compile)
                        .withDebugMapper(
                                lineAddress -> {
                                    switch (lineAddress) {
                                        case 23241:
                                            // mapping only the first line
                                            return new DebugMapper.DebugInfo(
                                                    "count_vowels.wasm",
                                                    "std::panicking::rust_panic_with_hook::hb39abb160cd4038c",
                                                    lineAddress);
                                        default:
                                            return null;
                                    }
                                })
                        .build();

        var exception =
                assertThrows(
                        TrapException.class, () -> instance.export("count_vowels").apply(0, 0));
        var stackTraceElem0 = exception.getStackTrace()[0];
        assertEquals(
                "0x005ac9:"
                    + " com.dylibso.chicory.$gen.CompiledMachineFuncGroup_0.func_85.std::panicking::rust_panic_with_hook::hb39abb160cd4038c(count_vowels.wasm:23241)",
                stackTraceElem0.toString());
    }
}
