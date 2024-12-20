package com.dylibso.chicory.runtime;

/*
 * WebAssembly.Exception in the JS API
 * represents a WebAssembly Exception
 * https://github.com/WebAssembly/exception-handling/blob/main/proposals/exception-handling/Exceptions.md
 */
public class WasmException extends RuntimeException {
    private final TagInstance tag;

    public WasmException(TagInstance tag) {
        this.tag = tag;
    }

    public WasmException(TagInstance tag, String msg) {
        super(msg);
        this.tag = tag;
    }

    public WasmException(TagInstance tag, Throwable cause) {
        super(cause);
        this.tag = tag;
    }

    public WasmException(TagInstance tag, String msg, Throwable cause) {
        super(msg, cause);
        this.tag = tag;
    }

    public TagInstance tag() {
        return tag;
    }
}
