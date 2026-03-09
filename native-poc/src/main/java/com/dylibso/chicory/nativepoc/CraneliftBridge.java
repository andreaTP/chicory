package com.dylibso.chicory.nativepoc;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasm.Parser;

/**
 * Java wrapper around the cranelift-bridge.wasm module.
 * Provides typed methods that map to Cranelift's FunctionBuilder API.
 * All IDs (block, var, value) are explicit — Java tracks them.
 */
final class CraneliftBridge {

    private final Instance instance;
    private final Memory memory;

    // Exports
    private final ExportFunction fnInit;
    private final ExportFunction fnCreateFunction;
    private final ExportFunction fnAddParamType;
    private final ExportFunction fnAddReturnType;
    private final ExportFunction fnCreateBlock;
    private final ExportFunction fnSwitchToBlock;
    private final ExportFunction fnSealBlock;
    private final ExportFunction fnSealAllBlocks;
    private final ExportFunction fnAppendBlockParamsForFuncParams;
    private final ExportFunction fnDeclareVar;
    private final ExportFunction fnDefVar;
    private final ExportFunction fnUseVar;
    private final ExportFunction fnFuncParam;
    private final ExportFunction fnEmitIconst32;
    private final ExportFunction fnEmitIadd;
    private final ExportFunction fnEmitIsub;
    private final ExportFunction fnEmitImul;
    private final ExportFunction fnBuildFunction;
    private final ExportFunction fnEmitLoadI32;
    private final ExportFunction fnEmitStoreI32;
    private final ExportFunction fnEmitJump;
    private final ExportFunction fnEmitBrif;
    private final ExportFunction fnEmitReturn;
    private final ExportFunction fnEmitReturnVoid;
    private final ExportFunction fnCompile;
    private final ExportFunction fnGetCodePtr;
    private final ExportFunction fnGetCodeLen;

    CraneliftBridge() {
        byte[] wasmBytes;
        try (var is = CraneliftBridge.class.getResourceAsStream("/cranelift-bridge.wasm")) {
            wasmBytes = is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load cranelift-bridge.wasm", e);
        }

        var module = Parser.parse(wasmBytes);

        // The bridge Wasm module needs WASI (Rust std uses random_get for HashMap)
        var wasiOpts = com.dylibso.chicory.wasi.WasiOptions.builder().inheritSystem().build();
        var wasi = com.dylibso.chicory.wasi.WasiPreview1.builder().withOptions(wasiOpts).build();
        var imports =
                com.dylibso.chicory.runtime.ImportValues.builder()
                        .addFunction(wasi.toHostFunctions())
                        .build();

        instance = Instance.builder(module).withImportValues(imports).build();
        memory = instance.memory();

        fnInit = instance.export("init");
        fnCreateFunction = instance.export("create_function");
        fnAddParamType = instance.export("add_param_type");
        fnAddReturnType = instance.export("add_return_type");
        fnCreateBlock = instance.export("create_block");
        fnSwitchToBlock = instance.export("switch_to_block");
        fnSealBlock = instance.export("seal_block");
        fnSealAllBlocks = instance.export("seal_all_blocks");
        fnAppendBlockParamsForFuncParams = instance.export("append_block_params_for_func_params");
        fnDeclareVar = instance.export("declare_var");
        fnDefVar = instance.export("def_var");
        fnUseVar = instance.export("use_var");
        fnFuncParam = instance.export("func_param");
        fnEmitIconst32 = instance.export("emit_iconst_32");
        fnEmitIadd = instance.export("emit_iadd");
        fnEmitIsub = instance.export("emit_isub");
        fnEmitImul = instance.export("emit_imul");
        fnBuildFunction = instance.export("build_function");
        fnEmitLoadI32 = instance.export("emit_load_i32");
        fnEmitStoreI32 = instance.export("emit_store_i32");
        fnEmitJump = instance.export("emit_jump");
        fnEmitBrif = instance.export("emit_brif");
        fnEmitReturn = instance.export("emit_return");
        fnEmitReturnVoid = instance.export("emit_return_void");
        fnCompile = instance.export("compile");
        fnGetCodePtr = instance.export("get_code_ptr");
        fnGetCodeLen = instance.export("get_code_len");
    }

    /** Initialize ISA for target triple (e.g., "x86_64-unknown-linux-gnu"). */
    void init(String target) {
        byte[] bytes = target.getBytes();
        // Write target string to bridge's linear memory at a known offset
        int ptr = 1024; // safe offset in memory
        for (int i = 0; i < bytes.length; i++) {
            memory.writeByte(ptr + i, bytes[i]);
        }
        fnInit.apply(ptr, bytes.length);
    }

    // Wasm type constants matching the bridge
    static final int TYPE_I32 = 0;
    static final int TYPE_I64 = 1;
    static final int TYPE_F32 = 2;
    static final int TYPE_F64 = 3;

    void createFunction() {
        fnCreateFunction.apply();
    }

    /** Finalize signature and create the FunctionBuilder. Call after add*Type, before emit*. */
    void buildFunction() {
        fnBuildFunction.apply();
    }

    void addParamType(int wasmType) {
        fnAddParamType.apply(wasmType);
    }

    void addReturnType(int wasmType) {
        fnAddReturnType.apply(wasmType);
    }

    int createBlock() {
        return (int) fnCreateBlock.apply()[0];
    }

    void switchToBlock(int blockId) {
        fnSwitchToBlock.apply(blockId);
    }

    void sealBlock(int blockId) {
        fnSealBlock.apply(blockId);
    }

    void sealAllBlocks() {
        fnSealAllBlocks.apply();
    }

    void appendBlockParamsForFuncParams(int blockId) {
        fnAppendBlockParamsForFuncParams.apply(blockId);
    }

    int declareVar(int wasmType) {
        return (int) fnDeclareVar.apply(wasmType)[0];
    }

    void defVar(int varId, int valId) {
        fnDefVar.apply(varId, valId);
    }

    int useVar(int varId) {
        return (int) fnUseVar.apply(varId)[0];
    }

    int funcParam(int blockId, int index) {
        return (int) fnFuncParam.apply(blockId, index)[0];
    }

    int emitIconst32(int val) {
        return (int) fnEmitIconst32.apply(val)[0];
    }

    int emitIadd(int a, int b) {
        return (int) fnEmitIadd.apply(a, b)[0];
    }

    int emitIsub(int a, int b) {
        return (int) fnEmitIsub.apply(a, b)[0];
    }

    int emitImul(int a, int b) {
        return (int) fnEmitImul.apply(a, b)[0];
    }

    int emitLoadI32(int base, int wasmAddr, int offset) {
        return (int) fnEmitLoadI32.apply(base, wasmAddr, offset)[0];
    }

    void emitStoreI32(int base, int wasmAddr, int value, int offset) {
        fnEmitStoreI32.apply(base, wasmAddr, value, offset);
    }

    void emitJump(int blockId) {
        fnEmitJump.apply(blockId);
    }

    void emitBrif(int cond, int thenBlock, int elseBlock) {
        fnEmitBrif.apply(cond, thenBlock, elseBlock);
    }

    void emitReturn(int valId) {
        fnEmitReturn.apply(valId);
    }

    void emitReturnVoid() {
        fnEmitReturnVoid.apply();
    }

    /** Compile and return the native code bytes. */
    byte[] compile() {
        fnCompile.apply();
        int codePtr = (int) fnGetCodePtr.apply()[0];
        int codeLen = (int) fnGetCodeLen.apply()[0];
        return memory.readBytes(codePtr, codeLen);
    }
}
