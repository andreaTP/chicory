/*    */ package com.dylibso.chicory.wabt;
/*    */ 
/*    */ import com.dylibso.chicory.runtime.CompiledModule;
/*    */ import com.dylibso.chicory.runtime.Instance;
/*    */ import com.dylibso.chicory.runtime.Machine;
/*    */ import com.dylibso.chicory.wasm.Parser;
/*    */ import com.dylibso.chicory.wasm.WasmModule;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Wat2WasmModule
/*    */   implements CompiledModule
/*    */ {
/*    */   public static Machine create(Instance instance) {
/* 19 */     return new Wat2WasmModuleMachine(instance);
/*    */   }
/*    */   
/*    */   private static class WasmModuleHolder {
/*    */     static final WasmModule INSTANCE;
/*    */     
/*    */     static {
/*    */       
/* 27 */       try { InputStream in = Wat2WasmModule.class.getResourceAsStream("Wat2WasmModule.meta"); 
/* 28 */         try { INSTANCE = Parser.parse(in);
/* 29 */           if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 30 */       { throw new UncheckedIOException("Failed to load .meta WASM module", e); }
/*    */     
/*    */     }
/*    */   }
/*    */   
/*    */   public static WasmModule load() {
/* 36 */     return WasmModuleHolder.INSTANCE;
/*    */   }
/*    */   
/*    */   public Function<Instance, Machine> machineFactory() {
/* 40 */     return Wat2WasmModule::create;
/*    */   }
/*    */   
/*    */   public WasmModule wasmModule() {
/* 44 */     return load();
/*    */   }
/*    */ }


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Wat2WasmModule.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */