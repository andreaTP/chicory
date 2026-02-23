// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import java.nio.file.FileVisitor;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.Path;

public final class Files
{
    private Files() {
    }
    
    public static void copyDirectory(final Path source, final Path target) throws IOException {
        java.nio.file.Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final String relative = source.relativize(file).toString().replace("\\", "/");
                final Path path = target.resolve(relative);
                java.nio.file.Files.copy(file, path, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
