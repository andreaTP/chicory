package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.types.RawSection;

@FunctionalInterface
public interface RawParserListener {

    void onSection(RawSection section);
}
