/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.dylibso.chicory.annotations.Buffer
 *  com.dylibso.chicory.annotations.HostModule
 *  com.dylibso.chicory.annotations.WasmExport
 */
package com.dylibso.chicory.wasi;

import com.dylibso.chicory.annotations.Buffer;
import com.dylibso.chicory.annotations.HostModule;
import com.dylibso.chicory.annotations.WasmExport;
import com.dylibso.chicory.log.BasicLogger;
import com.dylibso.chicory.log.Logger;
import com.dylibso.chicory.log.SystemLogger;
import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.wasi.Descriptors;
import com.dylibso.chicory.wasi.WasiErrno;
import com.dylibso.chicory.wasi.WasiExitException;
import com.dylibso.chicory.wasi.WasiFdFlags;
import com.dylibso.chicory.wasi.WasiFileType;
import com.dylibso.chicory.wasi.WasiFstFlags;
import com.dylibso.chicory.wasi.WasiOpenFlags;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1_ModuleFactory;
import com.dylibso.chicory.wasi.WasiRights;
import com.dylibso.chicory.wasm.ChicoryException;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NotLinkException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@HostModule(value="wasi_snapshot_preview1")
public final class WasiPreview1
implements Closeable {
    private final Logger logger;
    private final Random random;
    private final Clock clock;
    private final List<byte[]> arguments;
    private final List<Map.Entry<byte[], byte[]>> environment;
    private final Descriptors descriptors = new Descriptors();

    private WasiPreview1(Logger logger, WasiOptions opts) {
        this.logger = Objects.requireNonNull(logger);
        this.random = opts.random();
        this.clock = opts.clock();
        this.arguments = opts.arguments().stream().map(value -> value.getBytes(StandardCharsets.UTF_8)).collect(Collectors.toList());
        this.environment = opts.environment().entrySet().stream().map(x -> Map.entry(((String)x.getKey()).getBytes(StandardCharsets.UTF_8), ((String)x.getValue()).getBytes(StandardCharsets.UTF_8))).collect(Collectors.toList());
        this.descriptors.allocate(new Descriptors.InStream(opts.stdin()));
        this.descriptors.allocate(new Descriptors.OutStream(opts.stdout()));
        this.descriptors.allocate(new Descriptors.OutStream(opts.stderr()));
        for (Map.Entry<String, Path> entry : opts.directories().entrySet()) {
            byte[] name = entry.getKey().getBytes(StandardCharsets.UTF_8);
            this.descriptors.allocate(new Descriptors.PreopenedDirectory(name, entry.getValue()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void close() {
        this.descriptors.closeAll();
    }

    @WasmExport
    public int adapterCloseBadfd(int fd) {
        this.logger.tracef("adapter_close_badfd: [%s]", fd);
        throw new WasmRuntimeException("We don't yet support this WASI call: adapter_close_badfd");
    }

    @WasmExport
    public int adapterOpenBadfd(int fd) {
        this.logger.tracef("adapter_open_badfd: [%s]", fd);
        throw new WasmRuntimeException("We don't yet support this WASI call: adapter_open_badfd");
    }

    @WasmExport
    public int argsGet(Memory memory, int argv, int argvBuf) {
        this.logger.tracef("args_get: [%s, %s]", argv, argvBuf);
        for (byte[] argument : this.arguments) {
            memory.writeI32(argv, argvBuf);
            argv += 4;
            memory.write(argvBuf, argument);
            memory.writeByte(argvBuf += argument.length, (byte)0);
            ++argvBuf;
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int argsSizesGet(Memory memory, int argc, int argvBufSize) {
        this.logger.tracef("args_sizes_get: [%s, %s]", argc, argvBufSize);
        int bufSize = this.arguments.stream().mapToInt(x -> ((byte[])x).length + 1).sum();
        memory.writeI32(argc, this.arguments.size());
        memory.writeI32(argvBufSize, bufSize);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int clockResGet(Memory memory, int clockId, int resultPtr) {
        this.logger.tracef("clock_res_get: [%s, %s]", clockId, resultPtr);
        switch (clockId) {
            case 0: 
            case 1: {
                memory.writeLong(resultPtr, 1L);
                return this.wasiResult(WasiErrno.ESUCCESS);
            }
            case 2: 
            case 3: {
                return this.wasiResult(WasiErrno.ENOTSUP);
            }
        }
        return this.wasiResult(WasiErrno.EINVAL);
    }

    @WasmExport
    public int clockTimeGet(Memory memory, int clockId, long precision, int resultPtr) {
        this.logger.tracef("clock_time_get: [%s, %s, %s]", clockId, precision, resultPtr);
        switch (clockId) {
            case 0: 
            case 1: {
                memory.writeLong(resultPtr, this.clockTime(clockId));
                return this.wasiResult(WasiErrno.ESUCCESS);
            }
            case 2: 
            case 3: {
                return this.wasiResult(WasiErrno.ENOTSUP);
            }
        }
        return this.wasiResult(WasiErrno.EINVAL);
    }

    @WasmExport
    public int environGet(Memory memory, int environ, int environBuf) {
        this.logger.tracef("environ_get: [%s, %s]", environ, environBuf);
        for (Map.Entry<byte[], byte[]> entry : this.environment) {
            byte[] name = entry.getKey();
            byte[] value = entry.getValue();
            byte[] data = new byte[name.length + value.length + 2];
            System.arraycopy(name, 0, data, 0, name.length);
            data[name.length] = 61;
            System.arraycopy(value, 0, data, name.length + 1, value.length);
            data[data.length - 1] = 0;
            memory.writeI32(environ, environBuf);
            environ += 4;
            memory.write(environBuf, data);
            environBuf += data.length;
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int environSizesGet(Memory memory, int environCount, int environBufSize) {
        this.logger.tracef("environ_sizes_get: [%s, %s]", environCount, environBufSize);
        int bufSize = this.environment.stream().mapToInt(x -> ((byte[])x.getKey()).length + ((byte[])x.getValue()).length + 2).sum();
        memory.writeI32(environCount, this.environment.size());
        memory.writeI32(environBufSize, bufSize);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdAdvise(int fd, long offset, long len, int advice) {
        this.logger.tracef("fd_advise: [%s, %s, %s, %s]", fd, offset, len, advice);
        if (len < 0L || offset < 0L) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.ESPIPE);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @WasmExport
    public int fdAllocate(int fd, long offset, long len) {
        this.logger.tracef("fd_allocate: [%s, %s, %s]", fd, offset, len);
        if (len <= 0L) return this.wasiResult(WasiErrno.EINVAL);
        if (offset < 0L) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) return this.wasiResult(WasiErrno.EINVAL);
        if (descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        FileChannel channel = ((Descriptors.OpenFile)descriptor).channel();
        try {
            long size = offset + len;
            if (size <= channel.size()) return this.wasiResult(WasiErrno.ESUCCESS);
            long position = channel.position();
            try {
                channel.position(size - 1L);
                if (channel.write(ByteBuffer.wrap(new byte[1])) == 1) return this.wasiResult(WasiErrno.ESUCCESS);
                int n = this.wasiResult(WasiErrno.EIO);
                return n;
            }
            finally {
                channel.position(position);
            }
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
    }

    @WasmExport
    public int fdClose(int fd) {
        this.logger.tracef("fd_close: [%s]", fd);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        this.descriptors.free(fd);
        try {
            if (descriptor instanceof Closeable) {
                ((Closeable)((Object)descriptor)).close();
            }
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdDatasync(int fd) {
        this.logger.tracef("fd_datasync: [%s]", fd);
        return this.wasiResult(this.fileSync(fd, false));
    }

    @WasmExport
    public int fdFdstatGet(Memory memory, int fd, int buf) {
        long rightsBase;
        WasiFileType fileType;
        this.logger.tracef("fd_fdstat_get: [%s, %s]", fd, buf);
        int flags = 0;
        long rightsInheriting = 0L;
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) {
            fileType = WasiFileType.CHARACTER_DEVICE;
            rightsBase = WasiRights.FD_READ;
        } else if (descriptor instanceof Descriptors.OutStream) {
            fileType = WasiFileType.CHARACTER_DEVICE;
            rightsBase = WasiRights.FD_WRITE;
        } else if (descriptor instanceof Descriptors.Directory) {
            fileType = WasiFileType.DIRECTORY;
            rightsBase = WasiRights.DIRECTORY_RIGHTS_BASE;
            rightsInheriting = rightsBase | (long)WasiRights.FILE_RIGHTS_BASE;
        } else if (descriptor instanceof Descriptors.OpenFile) {
            Descriptors.OpenFile file = (Descriptors.OpenFile)descriptor;
            fileType = WasiFileType.REGULAR_FILE;
            rightsBase = file.rights() & (long)WasiRights.FILE_RIGHTS_BASE;
            flags = file.fdFlags();
        } else {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        memory.write(buf, new byte[8]);
        memory.writeByte(buf, (byte)fileType.value());
        memory.writeShort(buf + 2, (short)flags);
        memory.writeLong(buf + 8, rightsBase);
        memory.writeLong(buf + 16, rightsInheriting);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdFdstatSetFlags(int fd, int flags) {
        this.logger.tracef("fd_fdstat_set_flags: [%s, %s]", fd, flags);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        if (descriptor instanceof Descriptors.OpenDirectory || descriptor instanceof Descriptors.PreopenedDirectory) {
            return this.wasiResult(WasiErrno.ESUCCESS);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        if (flags != ((Descriptors.OpenFile)descriptor).fdFlags()) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdFdstatSetRights(int fd, long rightsBase, long rightsInheriting) {
        this.logger.errorf("fd_fdstat_set_rights operation not supported: [%s, %s, %s]", fd, rightsBase, rightsInheriting);
        return this.wasiResult(WasiErrno.ENOTSUP);
    }

    @WasmExport
    public int fdFilestatGet(Memory memory, int fd, int buf) {
        Map<String, Object> attributes;
        Path path;
        this.logger.tracef("fd_filestat_get: [%s, %s]", fd, buf);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            Map<String, Object> attributes2 = Map.of("dev", 0L, "ino", 0L, "nlink", 1L, "size", 0L, "lastAccessTime", FileTime.from(Instant.EPOCH), "lastModifiedTime", FileTime.from(Instant.EPOCH), "ctime", FileTime.from(Instant.EPOCH));
            WasiPreview1.writeFileStat(memory, buf, attributes2, WasiFileType.CHARACTER_DEVICE);
            return this.wasiResult(WasiErrno.ESUCCESS);
        }
        if (descriptor instanceof Descriptors.OpenFile) {
            path = ((Descriptors.OpenFile)descriptor).path();
        } else if (descriptor instanceof Descriptors.OpenDirectory) {
            path = ((Descriptors.OpenDirectory)descriptor).path();
        } else {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        try {
            attributes = Files.readAttributes(path, "unix:*", new LinkOption[0]);
        }
        catch (UnsupportedOperationException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        WasiPreview1.writeFileStat(memory, buf, attributes, WasiPreview1.getFileType(attributes));
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @WasmExport
    public int fdFilestatSetSize(int fd, long size) {
        this.logger.tracef("fd_filestat_set_size: [%s, %s]", fd, size);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) return this.wasiResult(WasiErrno.EINVAL);
        if (descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        FileChannel channel = ((Descriptors.OpenFile)descriptor).channel();
        try {
            long position = channel.position();
            try {
                if (size <= channel.size()) {
                    channel.truncate(size);
                    return this.wasiResult(WasiErrno.ESUCCESS);
                }
                channel.position(size - 1L);
                if (channel.write(ByteBuffer.wrap(new byte[1])) == 1) return this.wasiResult(WasiErrno.ESUCCESS);
                int n = this.wasiResult(WasiErrno.EIO);
                return n;
            }
            finally {
                channel.position(position);
            }
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
    }

    @WasmExport
    public int fdFilestatSetTimes(int fd, long accessTime, long modifiedTime, int fstFlags) {
        Path path;
        this.logger.tracef("fd_filestat_set_times: [%s, %s, %s, %s]", fd, accessTime, modifiedTime, fstFlags);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        if (descriptor instanceof Descriptors.OpenFile) {
            path = ((Descriptors.OpenFile)descriptor).path();
        } else if (descriptor instanceof Descriptors.Directory) {
            path = ((Descriptors.Directory)((Object)descriptor)).path();
        } else {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        return this.wasiResult(this.setFileTimes(path, modifiedTime, accessTime, fstFlags));
    }

    @WasmExport
    public int fdPread(Memory memory, int fd, int iovs, int iovsLen, long offset, int nreadPtr) {
        this.logger.tracef("fd_pread: [%s, %s, %s, %s, %s]", fd, iovs, iovsLen, offset, nreadPtr);
        if (offset < 0L) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) {
            return this.wasiResult(WasiErrno.ESPIPE);
        }
        if (descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        Descriptors.OpenFile file = (Descriptors.OpenFile)descriptor;
        int totalRead = 0;
        for (int i = 0; i < iovsLen; ++i) {
            int base = iovs + i * 8;
            int iovBase = memory.readInt(base);
            int iovLen = memory.readInt(base + 4);
            try {
                byte[] data = new byte[iovLen];
                int read = file.read(data, offset);
                if (read < 0) break;
                memory.write(iovBase, data, 0, read);
                offset += (long)read;
                totalRead += read;
                if (read >= iovLen) continue;
                break;
            }
            catch (NonReadableChannelException e) {
                return this.wasiResult(WasiErrno.ENOTCAPABLE);
            }
            catch (IOException e) {
                return this.wasiResult(WasiErrno.EIO);
            }
        }
        memory.writeI32(nreadPtr, totalRead);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdPrestatDirName(Memory memory, int fd, int path, int pathLen) {
        this.logger.tracef("fd_prestat_dir_name: [%s, %s, %s]", fd, path, pathLen);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.PreopenedDirectory)) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        byte[] name = ((Descriptors.PreopenedDirectory)descriptor).name();
        if (pathLen < name.length) {
            return this.wasiResult(WasiErrno.ENAMETOOLONG);
        }
        memory.write(path, name);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdPrestatGet(Memory memory, int fd, int buf) {
        this.logger.tracef("fd_prestat_get: [%s, %s]", fd, buf);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.PreopenedDirectory)) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        int length = ((Descriptors.PreopenedDirectory)descriptor).name().length;
        memory.writeI32(buf, 0);
        memory.writeI32(buf + 4, length);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdPwrite(Memory memory, int fd, int iovs, int iovsLen, long offset, int nwrittenPtr) {
        this.logger.tracef("fd_pwrite: [%s, %s, %s, %s, %s]", fd, iovs, iovsLen, offset, nwrittenPtr);
        if (offset < 0L) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.ESPIPE);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        Descriptors.OpenFile file = (Descriptors.OpenFile)descriptor;
        if (WasiPreview1.flagSet(file.fdFlags(), WasiFdFlags.APPEND)) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        int totalWritten = 0;
        for (int i = 0; i < iovsLen; ++i) {
            int base = iovs + i * 8;
            int iovBase = memory.readInt(base);
            int iovLen = memory.readInt(base + 4);
            byte[] data = memory.readBytes(iovBase, iovLen);
            try {
                int written = file.write(data, offset);
                offset += (long)written;
                totalWritten += written;
                if (written >= iovLen) continue;
                break;
            }
            catch (NonWritableChannelException e) {
                return this.wasiResult(WasiErrno.ENOTCAPABLE);
            }
            catch (IOException e) {
                return this.wasiResult(WasiErrno.EIO);
            }
        }
        memory.writeI32(nwrittenPtr, totalWritten);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdRead(Memory memory, int fd, int iovs, int iovsLen, int nreadPtr) {
        this.logger.tracef("fd_read: [%s, %s, %s, %s]", fd, iovs, iovsLen, nreadPtr);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.DataReader)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        Descriptors.DataReader reader = (Descriptors.DataReader)((Object)descriptor);
        int totalRead = 0;
        for (int i = 0; i < iovsLen; ++i) {
            int base = iovs + i * 8;
            int iovBase = memory.readInt(base);
            int iovLen = memory.readInt(base + 4);
            try {
                byte[] data = new byte[iovLen];
                int read = reader.read(data);
                if (read < 0) break;
                memory.write(iovBase, data, 0, read);
                totalRead += read;
                if (read >= iovLen) continue;
                break;
            }
            catch (NonReadableChannelException e) {
                return this.wasiResult(WasiErrno.ENOTCAPABLE);
            }
            catch (IOException e) {
                return this.wasiResult(WasiErrno.EIO);
            }
        }
        memory.writeI32(nreadPtr, totalRead);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @WasmExport
    public int fdReaddir(Memory memory, int dirFd, int buf, int bufLen, long cookie, int bufUsedPtr) {
        int used;
        block19: {
            this.logger.tracef("fd_readdir: [%s, %s, %s, %s, %s]", dirFd, buf, bufLen, cookie, bufUsedPtr);
            if (cookie < 0L) {
                return this.wasiResult(WasiErrno.EINVAL);
            }
            Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
            if (descriptor == null) {
                return this.wasiResult(WasiErrno.EBADF);
            }
            if (!(descriptor instanceof Descriptors.Directory)) {
                return this.wasiResult(WasiErrno.ENOTDIR);
            }
            Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
            used = 0;
            try {
                ByteBuffer entry;
                Map<String, Object> attributes;
                byte[] name;
                Stream<Path> stream = Files.list(directory);
                Stream<Path> special = Stream.of(directory.resolve("."), directory.resolve(".."));
                Iterator iterator = Stream.concat(special, stream).skip(cookie).iterator();
                while (iterator.hasNext()) {
                    Path entryPath = (Path)iterator.next();
                    name = entryPath.getFileName().toString().getBytes(StandardCharsets.UTF_8);
                    ++cookie;
                    try {
                        attributes = Files.readAttributes(entryPath, "unix:*", new LinkOption[0]);
                    }
                    catch (UnsupportedOperationException e) {
                        int n = this.wasiResult(WasiErrno.ENOTSUP);
                        if (stream != null) {
                            stream.close();
                        }
                        return n;
                    }
                    catch (NoSuchFileException e) {
                        continue;
                    }
                    entry = ByteBuffer.allocate(24 + name.length).order(ByteOrder.LITTLE_ENDIAN);
                    entry.putLong(0, cookie);
                }
                break block19;
                {
                    entry.putLong(8, ((Number)attributes.get("ino")).longValue());
                    entry.putInt(16, name.length);
                    entry.put(20, (byte)WasiPreview1.getFileType(attributes).value());
                    entry.position(24);
                    entry.put(name);
                    int writeSize = Math.min(entry.capacity(), bufLen - used);
                    memory.write(buf + used, entry.array(), 0, writeSize);
                    if ((used += writeSize) != bufLen) continue;
                    break block19;
                    break;
                }
                finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
            }
            catch (NotDirectoryException e) {
                return this.wasiResult(WasiErrno.ENOTDIR);
            }
            catch (NoSuchFileException e) {
                return this.wasiResult(WasiErrno.ENOENT);
            }
            catch (IOException e) {
                return this.wasiResult(WasiErrno.EIO);
            }
        }
        memory.writeI32(bufUsedPtr, used);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdRenumber(int from, int to) {
        this.logger.tracef("fd_renumber: [%s, %s]", from, to);
        Descriptors.Descriptor fromDescriptor = this.descriptors.get(from);
        if (fromDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (from == to) {
            return this.wasiResult(WasiErrno.ESUCCESS);
        }
        Descriptors.Descriptor toDescriptor = this.descriptors.get(to);
        if (toDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        try {
            if (toDescriptor instanceof Closeable) {
                ((Closeable)((Object)toDescriptor)).close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.descriptors.free(from);
        this.descriptors.set(to, fromDescriptor);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdSeek(Memory memory, int fd, long offset, int whence, int newOffsetPtr) {
        long newOffset;
        this.logger.tracef("fd_seek: [%s, %s, %s, %s]", fd, offset, whence, newOffsetPtr);
        if (whence < 0 || whence > 2) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.ESPIPE);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        FileChannel channel = ((Descriptors.OpenFile)descriptor).channel();
        try {
            switch (whence) {
                case 0: {
                    channel.position(offset);
                    break;
                }
                case 1: {
                    channel.position(channel.position() + offset);
                    break;
                }
                case 2: {
                    channel.position(channel.size() + offset);
                }
            }
            newOffset = channel.position();
        }
        catch (IllegalArgumentException e) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        memory.writeLong(newOffsetPtr, newOffset);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdSync(int fd) {
        this.logger.tracef("fd_sync: [%s]", fd);
        return this.wasiResult(this.fileSync(fd, true));
    }

    @WasmExport
    public int fdTell(Memory memory, int fd, int offsetPtr) {
        long offset;
        this.logger.tracef("fd_tell: [%s, %s]", fd, offsetPtr);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream) {
            return this.wasiResult(WasiErrno.ESPIPE);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        FileChannel channel = ((Descriptors.OpenFile)descriptor).channel();
        try {
            offset = channel.position();
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        memory.writeLong(offsetPtr, offset);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int fdWrite(Memory memory, int fd, int iovs, int iovsLen, int nwrittenPtr) {
        this.logger.tracef("fd_write: [%s, %s, %s, %s]", fd, iovs, iovsLen, nwrittenPtr);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.InStream) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (descriptor instanceof Descriptors.Directory) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        if (!(descriptor instanceof Descriptors.DataWriter)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        Descriptors.DataWriter writer = (Descriptors.DataWriter)((Object)descriptor);
        int totalWritten = 0;
        for (int i = 0; i < iovsLen; ++i) {
            int base = iovs + i * 8;
            int iovBase = memory.readInt(base);
            int iovLen = memory.readInt(base + 4);
            byte[] data = memory.readBytes(iovBase, iovLen);
            try {
                int written = writer.write(data);
                totalWritten += written;
                if (written >= iovLen) continue;
                break;
            }
            catch (NonWritableChannelException e) {
                return this.wasiResult(WasiErrno.ENOTCAPABLE);
            }
            catch (IOException e) {
                return this.wasiResult(WasiErrno.EIO);
            }
        }
        memory.writeI32(nwrittenPtr, totalWritten);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathCreateDirectory(int dirFd, @Buffer String rawPath) {
        this.logger.tracef("path_create_directory: [%s, \"%s\"]", dirFd, rawPath);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        try {
            Files.createDirectory(path, new FileAttribute[0]);
        }
        catch (FileAlreadyExistsException e) {
            return this.wasiResult(WasiErrno.EEXIST);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathFilestatGet(Memory memory, int dirFd, int lookupFlags, @Buffer String rawPath, int buf) {
        Map<String, Object> attributes;
        this.logger.tracef("path_filestat_get: [%s, %s, \"%s\", %s]", dirFd, lookupFlags, rawPath, buf);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        LinkOption[] linkOptions = WasiPreview1.toLinkOptions(lookupFlags);
        try {
            if (WasiPreview1.flagSet(lookupFlags, 1L)) {
                ResolvedSymlink resolved = this.resolveSymlinks(path);
                if (resolved.isError()) {
                    return resolved.error();
                }
                path = resolved.resolved();
            }
            attributes = Files.readAttributes(path, "unix:*", linkOptions);
        }
        catch (UnsupportedOperationException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        WasiPreview1.writeFileStat(memory, buf, attributes, WasiPreview1.getFileType(attributes));
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathFilestatSetTimes(int fd, int lookupFlags, @Buffer String rawPath, long accessTime, long modifiedTime, int fstFlags) {
        this.logger.tracef("path_filestat_set_times: [%s, %s, \"%s\", %s, %s, %s]", fd, lookupFlags, rawPath, accessTime, modifiedTime, fstFlags);
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        return this.wasiResult(this.setFileTimes(path, modifiedTime, accessTime, fstFlags));
    }

    private ResolvedSymlink resolveSymlinks(Path path) {
        ArrayList<Path> visited = new ArrayList<Path>();
        while (Files.isSymbolicLink(path)) {
            if (visited.contains(path)) {
                return new ResolvedSymlink(this.wasiResult(WasiErrno.ELOOP));
            }
            visited.add(path);
            try {
                path = Files.readSymbolicLink(path);
            }
            catch (IOException e) {
                return new ResolvedSymlink(this.wasiResult(WasiErrno.EIO));
            }
        }
        return new ResolvedSymlink(path);
    }

    @WasmExport
    public int pathLink(int oldFd, int oldFlags, @Buffer String rawOldPath, int newFd, @Buffer String rawNewPath) {
        this.logger.tracef("path_link: [%s, %s, \"%s\", %s, \"%s\"]", oldFd, oldFlags, rawOldPath, newFd, rawNewPath);
        Descriptors.Descriptor oldDescriptor = this.descriptors.get(oldFd);
        if (oldDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(oldDescriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path oldDirectory = ((Descriptors.Directory)((Object)oldDescriptor)).path();
        Descriptors.Descriptor newDescriptor = this.descriptors.get(newFd);
        if (newDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(newDescriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path newDirectory = ((Descriptors.Directory)((Object)newDescriptor)).path();
        Path oldPath = WasiPreview1.resolvePath(oldDirectory, rawOldPath);
        if (oldPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (rawNewPath.endsWith("/")) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        Path newPath = WasiPreview1.resolvePath(newDirectory, rawNewPath);
        if (newPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (Files.exists(newPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EEXIST);
        }
        if (Files.isDirectory(oldPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (Files.isDirectory(oldPath, new LinkOption[0]) && Files.isRegularFile(newPath, LinkOption.NOFOLLOW_LINKS)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        if (Files.isRegularFile(oldPath, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(newPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        try {
            if (WasiPreview1.flagSet(oldFlags, 1L)) {
                ResolvedSymlink resolved = this.resolveSymlinks(oldPath);
                if (resolved.isError()) {
                    return resolved.error();
                }
                oldPath = resolved.resolved();
            }
            Files.createLink(newPath, oldPath);
        }
        catch (UnsupportedOperationException | AtomicMoveNotSupportedException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (DirectoryNotEmptyException e) {
            return this.wasiResult(WasiErrno.ENOTEMPTY);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathOpen(Memory memory, int dirFd, int lookupFlags, @Buffer String rawPath, int openFlags, long rightsBase, long rightsInheriting, int fdFlags, int fdPtr) {
        int fd;
        this.logger.tracef("path_open: [%s, %s, \"%s\", %s, %s, %s, %s, %s]", dirFd, lookupFlags, rawPath, openFlags, rightsBase, rightsInheriting, fdFlags, fdPtr);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        if (rawPath.endsWith("\u0000")) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EPERM);
        }
        if (WasiPreview1.flagSet(openFlags, WasiOpenFlags.DIRECTORY) && !WasiPreview1.flagSet(lookupFlags, 1L) && Files.isSymbolicLink(path)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        if (!WasiPreview1.flagSet(lookupFlags, 1L) && Files.isSymbolicLink(path)) {
            return this.wasiResult(WasiErrno.ELOOP);
        }
        if (WasiPreview1.flagSet(lookupFlags, 1L)) {
            ResolvedSymlink resolved = this.resolveSymlinks(path);
            if (resolved.isError()) {
                return resolved.error();
            }
            path = resolved.resolved();
        }
        if (Files.isDirectory(path, new LinkOption[0])) {
            if (WasiPreview1.flagSet(rightsBase, WasiRights.FD_WRITE)) {
                return this.wasiResult(WasiErrno.EISDIR);
            }
            int fd2 = this.descriptors.allocate(new Descriptors.OpenDirectory(path));
            memory.writeI32(fdPtr, fd2);
            return this.wasiResult(WasiErrno.ESUCCESS);
        }
        if (rawPath.endsWith("/")) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        if (WasiPreview1.flagSet(openFlags, WasiOpenFlags.DIRECTORY) && Files.exists(path, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        HashSet<StandardOpenOption> openOptions = new HashSet<StandardOpenOption>(List.of());
        boolean append = WasiPreview1.flagSet(fdFlags, WasiFdFlags.APPEND);
        boolean truncate = WasiPreview1.flagSet(openFlags, WasiOpenFlags.TRUNC);
        if (append && truncate) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        if (!append && WasiPreview1.flagSet(rightsBase, WasiRights.FD_READ)) {
            openOptions.add(StandardOpenOption.READ);
        }
        if (WasiPreview1.flagSet(rightsBase, WasiRights.FD_WRITE)) {
            openOptions.add(StandardOpenOption.WRITE);
        }
        if (WasiPreview1.flagSet(openFlags, WasiOpenFlags.CREAT)) {
            if (WasiPreview1.flagSet(openFlags, WasiOpenFlags.EXCL)) {
                openOptions.add(StandardOpenOption.CREATE_NEW);
            } else {
                openOptions.add(StandardOpenOption.CREATE);
            }
            openOptions.add(StandardOpenOption.WRITE);
        }
        if (truncate) {
            openOptions.add(StandardOpenOption.TRUNCATE_EXISTING);
        }
        if (append) {
            openOptions.add(StandardOpenOption.APPEND);
        }
        if (WasiPreview1.flagSet(fdFlags, WasiFdFlags.SYNC)) {
            openOptions.add(StandardOpenOption.SYNC);
        }
        if (WasiPreview1.flagSet(fdFlags, WasiFdFlags.DSYNC)) {
            openOptions.add(StandardOpenOption.DSYNC);
        }
        try {
            FileChannel channel = FileChannel.open(path, openOptions, new FileAttribute[0]);
            fd = this.descriptors.allocate(new Descriptors.OpenFile(path, channel, fdFlags, rightsBase));
        }
        catch (FileAlreadyExistsException e) {
            return this.wasiResult(WasiErrno.EEXIST);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        memory.writeI32(fdPtr, fd);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathReadlink(Memory memory, int dirFd, @Buffer String rawPath, int buf, int bufLen, int bufUsedPtr) {
        Path link;
        this.logger.tracef("path_readlink: [%s, \"%s\", %s, %s, %s]", dirFd, rawPath, buf, bufLen, bufUsedPtr);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        try {
            link = Files.readSymbolicLink(path);
        }
        catch (UnsupportedOperationException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (NotLinkException e) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        byte[] name = link.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        int used = Math.min(name.length, bufLen);
        memory.write(buf, name, 0, used);
        memory.writeI32(bufUsedPtr, used);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathRemoveDirectory(int dirFd, @Buffer String rawPath) {
        this.logger.tracef("path_remove_directory: [%s, \"%s\"]", dirFd, rawPath);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            if (!attributes.isDirectory()) {
                return this.wasiResult(WasiErrno.ENOTDIR);
            }
            Files.delete(path);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (DirectoryNotEmptyException e) {
            return this.wasiResult(WasiErrno.ENOTEMPTY);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathRename(int oldFd, @Buffer String oldRawPath, int newFd, @Buffer String newRawPath) {
        this.logger.tracef("path_rename: [%s, \"%s\", %s, \"%s\"]", oldFd, oldRawPath, newFd, newRawPath);
        Descriptors.Descriptor oldDescriptor = this.descriptors.get(oldFd);
        if (oldDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(oldDescriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path oldDirectory = ((Descriptors.Directory)((Object)oldDescriptor)).path();
        Descriptors.Descriptor newDescriptor = this.descriptors.get(newFd);
        if (newDescriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(newDescriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path newDirectory = ((Descriptors.Directory)((Object)newDescriptor)).path();
        Path oldPath = WasiPreview1.resolvePath(oldDirectory, oldRawPath);
        if (oldPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        Path newPath = WasiPreview1.resolvePath(newDirectory, newRawPath);
        if (newPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (Files.isDirectory(oldPath, new LinkOption[0]) && Files.isRegularFile(newPath, LinkOption.NOFOLLOW_LINKS)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        if (Files.isRegularFile(oldPath, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(newPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        try {
            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.COPY_ATTRIBUTES);
        }
        catch (UnsupportedOperationException | AtomicMoveNotSupportedException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (DirectoryNotEmptyException e) {
            return this.wasiResult(WasiErrno.ENOTEMPTY);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathSymlink(@Buffer String oldRawPath, int dirFd, @Buffer String newRawPath) {
        this.logger.tracef("path_symlink: [\"%s\", %s, \"%s\"]", oldRawPath, dirFd, newRawPath);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path oldPath = WasiPreview1.resolvePath(directory, oldRawPath);
        if (oldPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (newRawPath.endsWith("/")) {
            return this.wasiResult(WasiErrno.EEXIST);
        }
        Path newPath = WasiPreview1.resolvePath(directory, newRawPath);
        if (newPath == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        if (Files.exists(newPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EEXIST);
        }
        if (Files.isDirectory(oldPath, new LinkOption[0]) && Files.isRegularFile(newPath, LinkOption.NOFOLLOW_LINKS)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        if (Files.isRegularFile(oldPath, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(newPath, new LinkOption[0])) {
            return this.wasiResult(WasiErrno.EISDIR);
        }
        try {
            Files.createSymbolicLink(newPath, oldPath, new FileAttribute[0]);
        }
        catch (UnsupportedOperationException | AtomicMoveNotSupportedException e) {
            return this.wasiResult(WasiErrno.ENOTSUP);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (DirectoryNotEmptyException e) {
            return this.wasiResult(WasiErrno.ENOTEMPTY);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pathUnlinkFile(int dirFd, @Buffer String rawPath) {
        this.logger.tracef("path_unlink_file: [%s, \"%s\"]", dirFd, rawPath);
        Descriptors.Descriptor descriptor = this.descriptors.get(dirFd);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        if (!(descriptor instanceof Descriptors.Directory)) {
            return this.wasiResult(WasiErrno.ENOTDIR);
        }
        Path directory = ((Descriptors.Directory)((Object)descriptor)).path();
        Path path = WasiPreview1.resolvePath(directory, rawPath);
        if (path == null) {
            return this.wasiResult(WasiErrno.EACCES);
        }
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            if (attributes.isDirectory()) {
                return this.wasiResult(WasiErrno.EISDIR);
            }
            if (rawPath.endsWith("/")) {
                return this.wasiResult(WasiErrno.ENOTDIR);
            }
            Files.delete(path);
        }
        catch (NoSuchFileException e) {
            return this.wasiResult(WasiErrno.ENOENT);
        }
        catch (IOException e) {
            return this.wasiResult(WasiErrno.EIO);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int pollOneoff(Memory memory, int inPtr, int outPtr, int nsubscriptions, int neventsPtr) {
        this.logger.tracef("poll_oneoff: [%s, %s, %s, %s]", inPtr, outPtr, nsubscriptions, neventsPtr);
        if (nsubscriptions <= 0) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        int nevents = 0;
        ArrayList<Map.Entry<Long, Long>> clockSubs = new ArrayList<Map.Entry<Long, Long>>();
        ArrayList<Map.Entry<Descriptors.InStream, Long>> readSubs = new ArrayList<Map.Entry<Descriptors.InStream, Long>>();
        for (int i = 0; i < nsubscriptions; ++i) {
            long userData = memory.readLong(inPtr);
            byte eventType = memory.read(inPtr + 8);
            inPtr += 16;
            switch (eventType) {
                case 0: {
                    int clockId = memory.readInt(inPtr);
                    long l = memory.readLong(inPtr + 8);
                    short s = memory.readShort(inPtr + 24);
                    if (clockId != 0 && clockId != 1) {
                        return this.wasiResult(WasiErrno.EINVAL);
                    }
                    if (WasiPreview1.flagSet(s, 1L)) {
                        l -= this.clockTime(clockId);
                    }
                    clockSubs.add(Map.entry(l, userData));
                    break;
                }
                case 1: 
                case 2: {
                    int fd = memory.readInt(inPtr);
                    if (fd < 0) {
                        return this.wasiResult(WasiErrno.EBADF);
                    }
                    Descriptors.Descriptor descriptor = this.descriptors.get(fd);
                    if (descriptor instanceof Descriptors.InStream && eventType == 1) {
                        readSubs.add(Map.entry((Descriptors.InStream)descriptor, userData));
                        break;
                    }
                    if (descriptor instanceof Descriptors.OutStream && eventType == 2) {
                        WasiPreview1.writeEvent(memory, outPtr, userData, eventType, WasiErrno.ESUCCESS);
                        outPtr += 32;
                        ++nevents;
                        break;
                    }
                    WasiErrno errno = descriptor == null ? WasiErrno.EBADF : (descriptor instanceof Descriptors.OpenFile ? WasiErrno.ESUCCESS : WasiErrno.ENOTSUP);
                    WasiPreview1.writeEvent(memory, outPtr, userData, eventType, errno);
                    outPtr += 32;
                    ++nevents;
                    break;
                }
                default: {
                    return this.wasiResult(WasiErrno.EINVAL);
                }
            }
            inPtr += 32;
        }
        long minTimeout = clockSubs.stream().mapToLong(Map.Entry::getKey).min().orElse(Long.MAX_VALUE);
        long start = System.nanoTime();
        do {
            for (Map.Entry entry : readSubs) {
                Descriptors.InStream stream = (Descriptors.InStream)entry.getKey();
                long l = (Long)entry.getValue();
                try {
                    int available = stream.available();
                    if (available <= 0) continue;
                    WasiPreview1.writeEvent(memory, outPtr, l, (byte)1, WasiErrno.ESUCCESS);
                    memory.writeLong(outPtr + 16, available);
                    outPtr += 32;
                    ++nevents;
                }
                catch (IOException e) {
                    WasiPreview1.writeEvent(memory, outPtr, l, (byte)1, WasiErrno.EIO);
                    outPtr += 32;
                    ++nevents;
                }
            }
            long elapsed = System.nanoTime() - start;
            if (nevents == 0) {
                long duration = Math.max(minTimeout, 0L) - elapsed;
                if (!readSubs.isEmpty()) {
                    duration = Math.min(duration, TimeUnit.MILLISECONDS.toNanos(100L));
                }
                try {
                    TimeUnit.NANOSECONDS.sleep(duration);
                }
                catch (InterruptedException e) {
                    throw new ChicoryException("Thread interrupted", e);
                }
            }
            for (Map.Entry entry : clockSubs) {
                long timeout = (Long)entry.getKey();
                long userData = (Long)entry.getValue();
                if (timeout > elapsed) continue;
                WasiPreview1.writeEvent(memory, outPtr, userData, (byte)0, WasiErrno.ESUCCESS);
                outPtr += 32;
                ++nevents;
            }
        } while (nevents == 0);
        memory.writeI32(neventsPtr, nevents);
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public void procExit(int code) {
        this.logger.tracef("proc_exit: [%s]", code);
        throw new WasiExitException(code);
    }

    @WasmExport
    public int procRaise(int sig) {
        this.logger.tracef("proc_raise: [%s]", sig);
        throw new WasmRuntimeException("We don't yet support this WASI call: proc_raise");
    }

    @WasmExport
    public int randomGet(Memory memory, int buf, int bufLen) {
        int size;
        this.logger.tracef("random_get: [%s, %s]", buf, bufLen);
        if (bufLen < 0) {
            return this.wasiResult(WasiErrno.EINVAL);
        }
        byte[] data = new byte[Math.min(bufLen, 4096)];
        for (int written = 0; written < bufLen; written += size) {
            if (Thread.currentThread().isInterrupted()) {
                throw new ChicoryException("Thread interrupted");
            }
            size = Math.min(data.length, bufLen - written);
            if (size < data.length) {
                data = new byte[size];
            }
            this.random.nextBytes(data);
            memory.write(buf + written, data, 0, size);
        }
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int schedYield() {
        this.logger.trace("sched_yield");
        return this.wasiResult(WasiErrno.ESUCCESS);
    }

    @WasmExport
    public int sockAccept(int sock, int fdFlags, int roFdPtr) {
        this.logger.tracef("sock_accept: [%s, %s, %s]", sock, fdFlags, roFdPtr);
        throw new WasmRuntimeException("We don't yet support this WASI call: sock_accept");
    }

    @WasmExport
    public int sockRecv(int sock, int riDataPtr, int riDataLen, int riFlags, int roDataLenPtr, int roFlagsPtr) {
        this.logger.tracef("sock_recv: [%s, %s, %s, %s, %s, %s]", sock, riDataPtr, riDataLen, riFlags, roDataLenPtr, roFlagsPtr);
        throw new WasmRuntimeException("We don't yet support this WASI call: sock_recv");
    }

    @WasmExport
    public int sockSend(int sock, int siDataPtr, int siDataLen, int siFlags, int retDataLenPtr) {
        this.logger.tracef("sock_send: [%s, %s, %s, %s, %s]", sock, siDataPtr, siDataLen, siFlags, retDataLenPtr);
        throw new WasmRuntimeException("We don't yet support this WASI call: sock_send");
    }

    @WasmExport
    public int sockShutdown(int sock, int how) {
        this.logger.tracef("sock_shutdown: [%s, %s]", sock, how);
        Descriptors.Descriptor descriptor = this.descriptors.get(sock);
        if (descriptor == null) {
            return this.wasiResult(WasiErrno.EBADF);
        }
        return this.wasiResult(WasiErrno.ENOTSOCK);
    }

    public HostFunction[] toHostFunctions() {
        return WasiPreview1_ModuleFactory.toHostFunctions(this);
    }

    private int wasiResult(WasiErrno errno) {
        if (errno != WasiErrno.ESUCCESS) {
            this.logger.tracef("result = %s", errno.name());
        }
        return errno.value();
    }

    private static void writeEvent(Memory memory, int index, long userData, byte eventType, WasiErrno errno) {
        memory.fill((byte)0, index, index + 32);
        memory.writeLong(index, userData);
        memory.writeShort(index + 8, (short)errno.value());
        memory.writeByte(index + 10, eventType);
    }

    private long clockTime(int clockId) {
        switch (clockId) {
            case 0: {
                Instant now = this.clock.instant();
                return TimeUnit.SECONDS.toNanos(now.getEpochSecond()) + (long)now.getNano();
            }
            case 1: {
                return System.nanoTime();
            }
        }
        throw new IllegalArgumentException("Invalid clockId: " + clockId);
    }

    private WasiErrno setFileTimes(Path path, long modifiedTime, long accessTime, int flags) {
        boolean modifiedSet = WasiPreview1.flagSet(flags, WasiFstFlags.MTIM);
        boolean modifiedNow = WasiPreview1.flagSet(flags, WasiFstFlags.MTIM_NOW);
        boolean accessSet = WasiPreview1.flagSet(flags, WasiFstFlags.ATIM);
        boolean accessNow = WasiPreview1.flagSet(flags, WasiFstFlags.ATIM_NOW);
        if (modifiedSet && modifiedNow || accessSet && accessNow) {
            return WasiErrno.EINVAL;
        }
        FileTime lastModifiedTime = this.toFileTime(modifiedTime, modifiedSet, modifiedNow);
        FileTime lastAccessTime = this.toFileTime(accessTime, accessSet, accessNow);
        try {
            Files.getFileAttributeView(path, BasicFileAttributeView.class, new LinkOption[0]).setTimes(lastModifiedTime, lastAccessTime, null);
        }
        catch (IOException e) {
            return WasiErrno.EIO;
        }
        return WasiErrno.ESUCCESS;
    }

    private FileTime toFileTime(long time, boolean set, boolean now) {
        if (set) {
            return FileTime.from(time, TimeUnit.NANOSECONDS);
        }
        if (now) {
            return FileTime.from(this.clock.instant());
        }
        return null;
    }

    private WasiErrno fileSync(int fd, boolean metadata) {
        Descriptors.Descriptor descriptor = this.descriptors.get(fd);
        if (descriptor == null) {
            return WasiErrno.EBADF;
        }
        if (descriptor instanceof Descriptors.InStream || descriptor instanceof Descriptors.OutStream || descriptor instanceof Descriptors.Directory) {
            return WasiErrno.EINVAL;
        }
        if (!(descriptor instanceof Descriptors.OpenFile)) {
            throw WasiPreview1.unhandledDescriptor(descriptor);
        }
        FileChannel channel = ((Descriptors.OpenFile)descriptor).channel();
        try {
            channel.force(metadata);
        }
        catch (IOException e) {
            return WasiErrno.EIO;
        }
        return WasiErrno.ESUCCESS;
    }

    private static Path resolvePath(Path directory, String rawPathString) {
        Path rawPath;
        try {
            rawPath = directory.getFileSystem().getPath(rawPathString, new String[0]);
        }
        catch (InvalidPathException e) {
            return null;
        }
        if (rawPath.isAbsolute()) {
            return null;
        }
        String normalized = rawPath.normalize().toString();
        if (normalized.equals("..") || normalized.startsWith("../")) {
            return null;
        }
        return directory.resolve(normalized);
    }

    private static void writeFileStat(Memory memory, int buf, Map<String, Object> attributes, WasiFileType fileType) {
        memory.writeLong(buf, (Long)attributes.get("dev"));
        memory.writeLong(buf + 8, ((Number)attributes.get("ino")).longValue());
        memory.write(buf + 16, new byte[8]);
        memory.writeByte(buf + 16, (byte)fileType.value());
        memory.writeLong(buf + 24, ((Number)attributes.get("nlink")).longValue());
        memory.writeLong(buf + 32, (Long)attributes.get("size"));
        memory.writeLong(buf + 40, WasiPreview1.fileTimeToNanos(attributes, "lastAccessTime"));
        memory.writeLong(buf + 48, WasiPreview1.fileTimeToNanos(attributes, "lastModifiedTime"));
        memory.writeLong(buf + 56, WasiPreview1.fileTimeToNanos(attributes, "ctime"));
    }

    private static long fileTimeToNanos(Map<String, Object> attributes, String name) {
        return ((FileTime)attributes.get(name)).to(TimeUnit.NANOSECONDS);
    }

    private static WasiFileType getFileType(Map<String, Object> attributes) {
        if (((Boolean)attributes.get("isSymbolicLink")).booleanValue()) {
            return WasiFileType.SYMBOLIC_LINK;
        }
        if (((Boolean)attributes.get("isDirectory")).booleanValue()) {
            return WasiFileType.DIRECTORY;
        }
        if (((Boolean)attributes.get("isRegularFile")).booleanValue()) {
            return WasiFileType.REGULAR_FILE;
        }
        return WasiFileType.UNKNOWN;
    }

    private static LinkOption[] toLinkOptions(int lookupFlags) {
        LinkOption[] linkOptionArray;
        if (WasiPreview1.flagSet(lookupFlags, 1L)) {
            linkOptionArray = new LinkOption[]{};
        } else {
            LinkOption[] linkOptionArray2 = new LinkOption[1];
            linkOptionArray = linkOptionArray2;
            linkOptionArray2[0] = LinkOption.NOFOLLOW_LINKS;
        }
        return linkOptionArray;
    }

    private static boolean flagSet(long flags, long mask) {
        if (Long.bitCount(mask) != 1) {
            throw new IllegalArgumentException("mask must be a single bit");
        }
        return (flags & mask) != 0L;
    }

    private static RuntimeException unhandledDescriptor(Descriptors.Descriptor descriptor) {
        return new WasmRuntimeException("Unhandled descriptor: " + descriptor.getClass().getName());
    }

    public static final class Builder {
        private Logger logger;
        private WasiOptions opts;

        private Builder() {
        }

        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder withOptions(WasiOptions opts) {
            this.opts = opts;
            return this;
        }

        private static boolean isAndroid() {
            try {
                Class.forName("android.os.Build");
                return true;
            }
            catch (ClassNotFoundException e) {
                String runtime = System.getProperty("java.runtime.name");
                return runtime != null && runtime.toLowerCase(Locale.ENGLISH).contains("android");
            }
        }

        public WasiPreview1 build() {
            if (this.logger == null && Builder.isAndroid()) {
                this.logger = new BasicLogger();
            } else if (this.logger == null) {
                this.logger = new SystemLogger();
            }
            if (this.opts == null) {
                this.opts = WasiOptions.builder().build();
            }
            return new WasiPreview1(this.logger, this.opts);
        }
    }

    private static final class ResolvedSymlink {
        private final Path resolved;
        private final int errorcode;

        ResolvedSymlink(Path resolved) {
            this.resolved = resolved;
            this.errorcode = 0;
        }

        ResolvedSymlink(int errcode) {
            this.resolved = null;
            this.errorcode = errcode;
        }

        boolean isError() {
            return this.resolved == null;
        }

        int error() {
            return this.errorcode;
        }

        Path resolved() {
            return this.resolved;
        }
    }
}

