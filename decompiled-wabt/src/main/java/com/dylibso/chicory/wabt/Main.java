/*    */ package com.dylibso.chicory.wabt;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Main
/*    */ {
/*    */   public static void main(String[] args) {
/*  8 */     String wat = generateBigWat(500000);
/*    */ 
/*    */     
/* 11 */     Wat2Wasm.parse(wat);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String generateBigWat(int funcCount) {
/* 19 */     StringBuilder wat = new StringBuilder();
/*    */     
/* 21 */     wat.append("(module\n");
/* 22 */     for (int func = 1; func <= funcCount; func++) {
/* 23 */       wat.append("  (func $func_")
/* 24 */         .append(func)
/* 25 */         .append(" (export \"func_")
/* 26 */         .append(func)
/* 27 */         .append("\") (param i32) (result i32)\n");
/* 28 */       wat.append("    local.get 0\n");
/* 29 */       wat.append("    i32.const ").append(func).append('\n');
/* 30 */       wat.append("    i32.add\n");
/*    */       
/* 32 */       if (func != 1) {
/* 33 */         wat.append("    call $func_").append(func - 1).append('\n');
/*    */       }
/*    */       
/* 36 */       wat.append("  )\n");
/*    */     } 
/* 38 */     wat.append(")\n");
/*    */     
/* 40 */     return wat.toString();
/*    */   }
/*    */ }


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Main.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */