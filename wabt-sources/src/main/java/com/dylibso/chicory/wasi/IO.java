/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

final class IO {
    private IO() {
    }

    public static OutputStream nullOutputStream() {
        return new OutputStream(){
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            @Override
            public void write(int b) throws IOException {
                this.ensureOpen();
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, b.length);
                this.ensureOpen();
            }

            @Override
            public void close() {
                this.closed = true;
            }
        };
    }

    public static InputStream nullInputStream() {
        return new InputStream(){
            private volatile boolean closed;

            private void ensureOpen() throws IOException {
                if (this.closed) {
                    throw new IOException("Stream closed");
                }
            }

            @Override
            public int available() throws IOException {
                this.ensureOpen();
                return 0;
            }

            @Override
            public int read() throws IOException {
                this.ensureOpen();
                return -1;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, b.length);
                if (len == 0) {
                    return 0;
                }
                this.ensureOpen();
                return -1;
            }

            @Override
            public byte[] readAllBytes() throws IOException {
                this.ensureOpen();
                return new byte[0];
            }

            @Override
            public int readNBytes(byte[] b, int off, int len) throws IOException {
                Objects.checkFromIndexSize(off, len, b.length);
                this.ensureOpen();
                return 0;
            }

            @Override
            public byte[] readNBytes(int len) throws IOException {
                if (len < 0) {
                    throw new IllegalArgumentException("len < 0");
                }
                this.ensureOpen();
                return new byte[0];
            }

            @Override
            public long skip(long n) throws IOException {
                this.ensureOpen();
                return 0L;
            }

            @Override
            public long transferTo(OutputStream out) throws IOException {
                Objects.requireNonNull(out);
                this.ensureOpen();
                return 0L;
            }

            @Override
            public void close() throws IOException {
                this.closed = true;
            }
        };
    }
}

