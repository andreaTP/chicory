package com.dylibso.chicory.cranelift;

import com.dylibso.chicory.annotations.WasmModuleInterface;
import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;

/**
 * Java wrapper around the cranelift-bridge.wasm module.
 * Provides typed methods that map to Cranelift's FunctionBuilder API.
 * All IDs (block, var, value) are explicit — Java tracks them.
 */
@WasmModuleInterface(WasmResource.absoluteFile)
public final class CraneliftBridge {

    private final Instance instance;
    private final Memory memory;

    private final ExportFunction fnInit;
    private final ExportFunction fnCreateFunction;
    private final ExportFunction fnAddParamType;
    private final ExportFunction fnAddReturnType;
    private final ExportFunction fnBuildFunction;
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
    private final ExportFunction fnEmitLoadI32;
    private final ExportFunction fnEmitStoreI32;
    private final ExportFunction fnEmitJump;
    private final ExportFunction fnEmitBrif;
    private final ExportFunction fnEmitReturn;
    private final ExportFunction fnEmitReturnVoid;
    private final ExportFunction fnCompile;
    private final ExportFunction fnGetCodePtr;
    private final ExportFunction fnGetCodeLen;

    public CraneliftBridge() {
        var module = Cranelift.load();
        var wasiOpts = WasiOptions.builder().inheritSystem().build();
        var wasi = WasiPreview1.builder().withOptions(wasiOpts).build();
        var imports = ImportValues.builder().addFunction(wasi.toHostFunctions()).build();

        instance =
                Instance.builder(module)
                        .withImportValues(imports)
                        .withMachineFactory(Cranelift::create)
                        .build();
        memory = instance.memory();

        fnInit = instance.export("init");
        fnCreateFunction = instance.export("create_function");
        fnAddParamType = instance.export("add_param_type");
        fnAddReturnType = instance.export("add_return_type");
        fnBuildFunction = instance.export("build_function");
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

    public static final int TYPE_I32 = 0;
    public static final int TYPE_I64 = 1;
    public static final int TYPE_F32 = 2;
    public static final int TYPE_F64 = 3;

    public void init(String target) {
        byte[] bytes = target.getBytes();
        int ptr = 1024;
        for (int i = 0; i < bytes.length; i++) {
            memory.writeByte(ptr + i, bytes[i]);
        }
        fnInit.apply(ptr, bytes.length);
    }

    public void createFunction() {
        fnCreateFunction.apply();
    }

    public void addParamType(int wasmType) {
        fnAddParamType.apply(wasmType);
    }

    public void addReturnType(int wasmType) {
        fnAddReturnType.apply(wasmType);
    }

    public void buildFunction() {
        fnBuildFunction.apply();
    }

    public int createBlock() {
        return (int) fnCreateBlock.apply()[0];
    }

    public void switchToBlock(int blockId) {
        fnSwitchToBlock.apply(blockId);
    }

    public void sealBlock(int blockId) {
        fnSealBlock.apply(blockId);
    }

    public void sealAllBlocks() {
        fnSealAllBlocks.apply();
    }

    public void appendBlockParamsForFuncParams(int blockId) {
        fnAppendBlockParamsForFuncParams.apply(blockId);
    }

    public int declareVar(int wasmType) {
        return (int) fnDeclareVar.apply(wasmType)[0];
    }

    public void defVar(int varId, int valId) {
        fnDefVar.apply(varId, valId);
    }

    public int useVar(int varId) {
        return (int) fnUseVar.apply(varId)[0];
    }

    public int funcParam(int blockId, int index) {
        return (int) fnFuncParam.apply(blockId, index)[0];
    }

    public int emitIconst32(int val) {
        return (int) fnEmitIconst32.apply(val)[0];
    }

    public int emitIadd(int a, int b) {
        return (int) fnEmitIadd.apply(a, b)[0];
    }

    public int emitIsub(int a, int b) {
        return (int) fnEmitIsub.apply(a, b)[0];
    }

    public int emitImul(int a, int b) {
        return (int) fnEmitImul.apply(a, b)[0];
    }

    public int emitLoadI32(int base, int wasmAddr, int offset) {
        return (int) fnEmitLoadI32.apply(base, wasmAddr, offset)[0];
    }

    public void emitStoreI32(int base, int wasmAddr, int value, int offset) {
        fnEmitStoreI32.apply(base, wasmAddr, value, offset);
    }

    public void emitJump(int blockId) {
        fnEmitJump.apply(blockId);
    }

    public void emitBrif(int cond, int thenBlock, int elseBlock) {
        fnEmitBrif.apply(cond, thenBlock, elseBlock);
    }

    public void emitReturn(int valId) {
        fnEmitReturn.apply(valId);
    }

    public void emitReturnVoid() {
        fnEmitReturnVoid.apply();
    }

    public byte[] compile() {
        fnCompile.apply();
        int codePtr = (int) fnGetCodePtr.apply()[0];
        int codeLen = (int) fnGetCodeLen.apply()[0];
        return memory.readBytes(codePtr, codeLen);
    }
}
