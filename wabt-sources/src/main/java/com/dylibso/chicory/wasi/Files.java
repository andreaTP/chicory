/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public final class Files {
    private Files() {
    }

    public static void copyDirectory(final Path source, final Path target) throws IOException {
        java.nio.file.Files.walkFileTree(source, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (java.nio.file.Files.isSymbolicLink(dir)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                Path directory = target.resolve(source.relativize(dir).toString());
                if (!directory.toString().equals("/")) {
                    FileAttribute[] attributes = new FileAttribute[]{};
                    PosixFileAttributeView attributeView = java.nio.file.Files.getFileAttributeView(dir, PosixFileAttributeView.class, new LinkOption[0]);
                    if (attributeView != null) {
                        Set<PosixFilePermission> permissions = attributeView.readAttributes().permissions();
                        FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(permissions);
                        attributes = new FileAttribute[]{attribute};
                    }
                    java.nio.file.Files.createDirectory(directory, attributes);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String relative = source.relativize(file).toString().replace("\\", "/");
                Path path = target.resolve(relative);
                java.nio.file.Files.copy(file, path, StandardCopyOption.COPY_ATTRIBUTES);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

