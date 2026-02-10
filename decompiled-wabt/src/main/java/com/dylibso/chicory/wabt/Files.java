/*    */ package com.dylibso.chicory.wabt;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Files
/*    */ {
/*    */   public static void copyDirectory(final Path source, final Path target) throws IOException {
/* 18 */     java.nio.file.Files.walkFileTree(source, new SimpleFileVisitor<Path>()
/*    */         {
/*    */ 
/*    */           
/*    */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
/*    */           {
/* 24 */             String relative = source.relativize(file).toString().replace("\\", "/");
/* 25 */             Path path = target.resolve(relative);
/* 26 */             java.nio.file.Files.copy(file, path, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 27 */             return FileVisitResult.CONTINUE;
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/andreatp/workspace/chicory6/wabt/target/original-wabt-999-SNAPSHOT.jar!/com/dylibso/chicory/wabt/Files.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */