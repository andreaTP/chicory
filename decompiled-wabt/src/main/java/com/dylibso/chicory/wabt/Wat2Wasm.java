/*    */ package com.dylibso.chicory.wabt;
/*    */ import com.dylibso.chicory.log.Logger;
/*    */ import com.dylibso.chicory.log.SystemLogger;
/*    */ import com.dylibso.chicory.runtime.ImportFunction;
/*    */ import com.dylibso.chicory.runtime.ImportValues;
/*    */ import com.dylibso.chicory.runtime.Instance;
/*    */ import com.dylibso.chicory.wasi.WasiExitException;
/*    */ import com.dylibso.chicory.wasi.WasiOptions;
/*    */ import com.dylibso.chicory.wasi.WasiPreview1;
/*    */ import com.dylibso.chicory.wasm.WasmModule;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class Wat2Wasm {
/* 22 */   private static final Logger logger = (Logger)new SystemLogger();
/* 23 */   private static final WasmModule MODULE = Wat2WasmModule.load();
/*    */ 
/*    */   
/*    */   public static byte[] parse(File file) {
/*    */     
/* 28 */     try { InputStream is = new FileInputStream(file); 
/* 29 */       try { byte[] arrayOfByte = parse(is);
/* 30 */         is.close(); return arrayOfByte; } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 31 */     { throw new UncheckedIOException(e); }
/*    */   
/*    */   }
/*    */   public static byte[] parse(String wat) {
/*    */     
/* 36 */     try { InputStream is = new ByteArrayInputStream(wat.getBytes(StandardCharsets.UTF_8)); 
/* 37 */       try { byte[] arrayOfByte = parse(is);
/* 38 */         is.close(); return arrayOfByte; } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 39 */     { throw new UncheckedIOException(e); }
/*    */   
/*    */   }
/*    */   private static byte[] parse(InputStream is) {
/*    */     
/* 44 */     try { ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream(); 
/* 45 */       try { ByteArrayOutputStream stderrStream = new ByteArrayOutputStream();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 53 */         try { WasiOptions wasiOpts = WasiOptions.builder().withStdin(is).withStdout(stdoutStream).withStderr(stderrStream).withArguments(List.of("wat2wasm", "-", "--output=-")).build();
/*    */ 
/*    */           
/* 56 */           try { WasiPreview1 wasi = WasiPreview1.builder().withLogger(logger).withOptions(wasiOpts).build();
/*    */             
/* 58 */             try { ImportValues imports = ImportValues.builder().addFunction((ImportFunction[])wasi.toHostFunctions()).build();
/* 59 */               Instance.builder(MODULE)
/* 60 */                 .withMachineFactory(Wat2WasmModule::create)
/* 61 */                 .withImportValues(imports)
/* 62 */                 .build();
/* 63 */               if (wasi != null) wasi.close();  } catch (Throwable throwable) { if (wasi != null) try { wasi.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (WasiExitException e)
/* 64 */           { if (e.exitCode() != 0) {
/* 65 */               throw new WatParseException(stdoutStream
/* 66 */                   .toString(StandardCharsets.UTF_8) + stdoutStream.toString(StandardCharsets.UTF_8), 
/* 67 */                   e);
/*    */             } }
/*    */ 
/*    */ 
/*    */           
/* 72 */           byte[] arrayOfByte = stdoutStream.toByteArray();
/* 73 */           stderrStream.close(); stdoutStream.close(); return arrayOfByte; } catch (Throwable throwable) { try { stderrStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { try { stdoutStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 74 */     { throw new UncheckedIOException(e); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Wat2Wasm.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */