/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ChicoryInterruptedException;
import com.dylibso.chicory.runtime.ConstantEvaluators;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.runtime.alloc.MemAllocStrategy;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.UninstantiableException;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.Instruction;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.PassiveDataSegment;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public final class ByteBufferMemory
implements Memory {
    static final Runnable ATOMIC_FENCE_IMPL = ByteBufferMemory.getAtomicFenceImpl();
    private static final int PAGE_SHIFT = 16;
    private static final int PAGE_MASK = 65535;
    private final MemoryLimits limits;
    private DataSegment[] dataSegments;
    private final ByteBuffer[] pages;
    private volatile int nPages;
    private final Object growLock = new Object();
    private final Map<Integer, WaitState> waitStates;

    public ByteBufferMemory(MemoryLimits limits) {
        this.limits = limits;
        int maxPages = Math.min(limits.maximumPages(), Short.MAX_VALUE);
        this.pages = new ByteBuffer[maxPages];
        for (int i = 0; i < limits.initialPages(); ++i) {
            this.pages[i] = ByteBuffer.allocate(65536).order(ByteOrder.LITTLE_ENDIAN);
        }
        this.nPages = limits.initialPages();
        this.waitStates = limits.shared() ? new ConcurrentHashMap<Integer, WaitState>() : null;
    }

    @Deprecated
    public ByteBufferMemory(MemoryLimits limits, MemAllocStrategy allocStrategy) {
        this(limits);
    }

    @Override
    public Object lock(int address) {
        if (!this.shared()) {
            return new Object();
        }
        return this.waitStates.computeIfAbsent(address, k -> new WaitState());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int waitOn(int address, BooleanSupplier condition, long timeout) {
        WaitState state;
        if (!this.shared()) {
            throw new ChicoryException("Attempt to wait on a non-shared memory, not supported.");
        }
        long deadline = timeout < 0L ? Long.MAX_VALUE : System.nanoTime() + timeout;
        WaitState waitState = state = this.waitStates.computeIfAbsent(address, k -> new WaitState());
        synchronized (waitState) {
            if (!condition.getAsBoolean()) {
                return 1;
            }
            ++state.waiterCount;
            try {
                while (state.pendingWakeups == 0) {
                    long remaining = deadline - System.nanoTime();
                    if (remaining <= 0L) {
                        int n = 2;
                        return n;
                    }
                    long millis = Math.max(remaining / 1000000L, 0L);
                    int nanos = Math.max((int)(remaining % 1000000L), 0);
                    try {
                        state.wait(millis, nanos);
                    }
                    catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new ChicoryInterruptedException("Thread interrupted");
                    }
                }
                int n = 0;
                return n;
            }
            finally {
                if (state.pendingWakeups > 0) {
                    --state.pendingWakeups;
                }
                --state.waiterCount;
                assert (0 <= state.pendingWakeups);
                assert (state.pendingWakeups <= state.waiterCount);
            }
        }
    }

    @Override
    public int waitOn(int address, int expected, long timeout) {
        return this.waitOn(address, () -> this.readInt(address) == expected, timeout);
    }

    @Override
    public int waitOn(int address, long expected, long timeout) {
        return this.waitOn(address, () -> this.readLong(address) == expected, timeout);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int notify(int address, int maxThreads) {
        if (!this.shared()) {
            return 0;
        }
        WaitState state = this.waitStates.get(address);
        if (state == null) {
            return 0;
        }
        WaitState waitState = state;
        synchronized (waitState) {
            int actualWaiters = state.waiterCount - state.pendingWakeups;
            if (actualWaiters == 0) {
                return 0;
            }
            int toWake = maxThreads < 0 ? actualWaiters : Math.min(actualWaiters, maxThreads);
            state.pendingWakeups += toWake;
            assert (state.pendingWakeups <= state.waiterCount);
            state.notifyAll();
            return toWake;
        }
    }

    @Override
    public int pages() {
        return this.nPages;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int grow(int size) {
        if (!this.shared()) {
            return this.growImpl(size);
        }
        Object object = this.growLock;
        synchronized (object) {
            return this.growImpl(size);
        }
    }

    private int growImpl(int size) {
        int prevPages = this.nPages;
        int numPages = prevPages + size;
        if (numPages > this.maximumPages() || numPages < prevPages) {
            return -1;
        }
        for (int i = prevPages; i < numPages; ++i) {
            this.pages[i] = ByteBuffer.allocate(65536).order(ByteOrder.LITTLE_ENDIAN);
        }
        this.nPages = numPages;
        return prevPages;
    }

    @Override
    public int initialPages() {
        return this.limits.initialPages();
    }

    @Override
    public int maximumPages() {
        return Math.min(this.limits.maximumPages(), Short.MAX_VALUE);
    }

    @Override
    public boolean shared() {
        return this.limits.shared();
    }

    @Override
    public void initialize(Instance instance, DataSegment[] dataSegments) {
        this.dataSegments = dataSegments;
        if (dataSegments == null) {
            return;
        }
        for (DataSegment s : dataSegments) {
            if (s instanceof ActiveDataSegment) {
                ActiveDataSegment segment = (ActiveDataSegment)s;
                List<Instruction> offsetExpr = segment.offsetInstructions();
                byte[] data = segment.data();
                int offset = (int)ConstantEvaluators.computeConstantValue(instance, offsetExpr)[0];
                ByteBufferMemory.checkBounds(offset, data.length, this.sizeInBytes(), UninstantiableException::new);
                this.write(offset, data, 0, data.length);
                continue;
            }
            if (s instanceof PassiveDataSegment) continue;
            throw new ChicoryException("Data segment should be active or passive: " + String.valueOf(s));
        }
    }

    private static void checkBounds(int addr, int size, int limit, Function<String, ChicoryException> exceptionFactory) {
        if (addr < 0 || size < 0 || addr > limit || size > 0 && addr + size > limit) {
            String errorMsg = "out of bounds memory access: attempted to access address: " + addr + " but limit is: " + limit + " and size: " + size;
            throw exceptionFactory.apply(errorMsg);
        }
    }

    private RuntimeException outOfBoundsException(RuntimeException e, int addr, int size) {
        if (e instanceof IndexOutOfBoundsException || e instanceof BufferOverflowException || e instanceof BufferUnderflowException || e instanceof IllegalArgumentException || e instanceof NullPointerException || e instanceof NegativeArraySizeException) {
            int limit = this.sizeInBytes();
            String errorMsg = "out of bounds memory access: attempted to access address: " + addr + " but limit is: " + limit + " and size: " + size;
            return new WasmRuntimeException(errorMsg);
        }
        return e;
    }

    @Override
    public void initPassiveSegment(int segmentId, int dest, int offset, int size) {
        DataSegment segment = this.dataSegments[segmentId];
        this.write(dest, segment.data(), offset, size);
    }

    private int sizeInBytes() {
        return 65536 * this.nPages;
    }

    @Override
    public void write(int addr, byte[] data, int offset, int size) {
        ByteBufferMemory.checkBounds(offset, size, data.length, WasmRuntimeException::new);
        ByteBufferMemory.checkBounds(addr, size, this.sizeInBytes(), WasmRuntimeException::new);
        while (size > 0) {
            int pageIdx = addr >>> 16;
            int pageOffset = addr & 0xFFFF;
            int chunk = Math.min(size, 65536 - pageOffset);
            this.pages[pageIdx].position(pageOffset);
            this.pages[pageIdx].put(data, offset, chunk);
            addr += chunk;
            offset += chunk;
            size -= chunk;
        }
    }

    @Override
    public byte read(int addr) {
        try {
            return this.pages[addr >>> 16].get(addr & 0xFFFF);
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    @Override
    public byte[] readBytes(int addr, int len) {
        int chunk;
        ByteBufferMemory.checkBounds(addr, len, this.sizeInBytes(), WasmRuntimeException::new);
        byte[] result = new byte[len];
        int destOffset = 0;
        int a = addr;
        for (int remaining = len; remaining > 0; remaining -= chunk) {
            int pageIdx = a >>> 16;
            int pageOffset = a & 0xFFFF;
            chunk = Math.min(remaining, 65536 - pageOffset);
            this.pages[pageIdx].position(pageOffset);
            this.pages[pageIdx].get(result, destOffset, chunk);
            a += chunk;
            destOffset += chunk;
        }
        return result;
    }

    @Override
    public void writeI32(int addr, int data) {
        int off = addr & 0xFFFF;
        if (off + 4 <= 65536) {
            try {
                this.pages[addr >>> 16].putInt(off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        } else {
            this.writeI32Slow(addr, data);
        }
    }

    private void writeI32Slow(int addr, int data) {
        ByteBufferMemory.checkBounds(addr, 4, this.sizeInBytes(), WasmRuntimeException::new);
        this.writeByte(addr, (byte)data);
        this.writeByte(addr + 1, (byte)(data >>> 8));
        this.writeByte(addr + 2, (byte)(data >>> 16));
        this.writeByte(addr + 3, (byte)(data >>> 24));
    }

    @Override
    public int readInt(int addr) {
        int off = addr & 0xFFFF;
        if (off + 4 <= 65536) {
            try {
                return this.pages[addr >>> 16].getInt(off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        }
        return this.readIntSlow(addr);
    }

    private int readIntSlow(int addr) {
        return this.read(addr) & 0xFF | (this.read(addr + 1) & 0xFF) << 8 | (this.read(addr + 2) & 0xFF) << 16 | (this.read(addr + 3) & 0xFF) << 24;
    }

    @Override
    public void writeLong(int addr, long data) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                this.pages[addr >>> 16].putLong(off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        } else {
            this.writeLongSlow(addr, data);
        }
    }

    private void writeLongSlow(int addr, long data) {
        ByteBufferMemory.checkBounds(addr, 8, this.sizeInBytes(), WasmRuntimeException::new);
        this.writeByte(addr, (byte)data);
        this.writeByte(addr + 1, (byte)(data >>> 8));
        this.writeByte(addr + 2, (byte)(data >>> 16));
        this.writeByte(addr + 3, (byte)(data >>> 24));
        this.writeByte(addr + 4, (byte)(data >>> 32));
        this.writeByte(addr + 5, (byte)(data >>> 40));
        this.writeByte(addr + 6, (byte)(data >>> 48));
        this.writeByte(addr + 7, (byte)(data >>> 56));
    }

    @Override
    public long readLong(int addr) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                return this.pages[addr >>> 16].getLong(off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        }
        return this.readLongSlow(addr);
    }

    private long readLongSlow(int addr) {
        return (long)this.read(addr) & 0xFFL | ((long)this.read(addr + 1) & 0xFFL) << 8 | ((long)this.read(addr + 2) & 0xFFL) << 16 | ((long)this.read(addr + 3) & 0xFFL) << 24 | ((long)this.read(addr + 4) & 0xFFL) << 32 | ((long)this.read(addr + 5) & 0xFFL) << 40 | ((long)this.read(addr + 6) & 0xFFL) << 48 | ((long)this.read(addr + 7) & 0xFFL) << 56;
    }

    @Override
    public void writeShort(int addr, short data) {
        int off = addr & 0xFFFF;
        if (off + 2 <= 65536) {
            try {
                this.pages[addr >>> 16].putShort(off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 2);
            }
        } else {
            this.writeShortSlow(addr, data);
        }
    }

    private void writeShortSlow(int addr, short data) {
        ByteBufferMemory.checkBounds(addr, 2, this.sizeInBytes(), WasmRuntimeException::new);
        this.writeByte(addr, (byte)data);
        this.writeByte(addr + 1, (byte)(data >>> 8));
    }

    @Override
    public short readShort(int addr) {
        int off = addr & 0xFFFF;
        if (off + 2 <= 65536) {
            try {
                return this.pages[addr >>> 16].getShort(off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 2);
            }
        }
        return this.readShortSlow(addr);
    }

    private short readShortSlow(int addr) {
        return (short)(this.read(addr) & 0xFF | (this.read(addr + 1) & 0xFF) << 8);
    }

    @Override
    public long readU16(int addr) {
        return (long)this.readShort(addr) & 0xFFFFL;
    }

    @Override
    public void writeByte(int addr, byte data) {
        try {
            this.pages[addr >>> 16].put(addr & 0xFFFF, data);
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    @Override
    public void writeF32(int addr, float data) {
        int off = addr & 0xFFFF;
        if (off + 4 <= 65536) {
            try {
                this.pages[addr >>> 16].putFloat(off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        } else {
            this.writeI32Slow(addr, Float.floatToRawIntBits(data));
        }
    }

    @Override
    public long readF32(int addr) {
        return this.readInt(addr);
    }

    @Override
    public float readFloat(int addr) {
        int off = addr & 0xFFFF;
        if (off + 4 <= 65536) {
            try {
                return this.pages[addr >>> 16].getFloat(off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        }
        return Float.intBitsToFloat(this.readIntSlow(addr));
    }

    @Override
    public void writeF64(int addr, double data) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                this.pages[addr >>> 16].putDouble(off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        } else {
            this.writeLongSlow(addr, Double.doubleToRawLongBits(data));
        }
    }

    @Override
    public double readDouble(int addr) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                return this.pages[addr >>> 16].getDouble(off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        }
        return Double.longBitsToDouble(this.readLongSlow(addr));
    }

    @Override
    public long readF64(int addr) {
        return this.readLong(addr);
    }

    @Override
    public void zero() {
        this.fill((byte)0, 0, this.sizeInBytes());
    }

    @Override
    public void fill(byte value, int fromIndex, int toIndex) {
        int chunk;
        int remaining;
        int addr = fromIndex;
        ByteBufferMemory.checkBounds(addr, remaining, this.sizeInBytes(), WasmRuntimeException::new);
        for (remaining = toIndex - fromIndex; remaining > 0; remaining -= chunk) {
            int pageIdx = addr >>> 16;
            int pageOffset = addr & 0xFFFF;
            chunk = Math.min(remaining, 65536 - pageOffset);
            Arrays.fill(this.pages[pageIdx].array(), pageOffset, pageOffset + chunk, value);
            addr += chunk;
        }
    }

    @Override
    public void drop(int segment) {
        this.dataSegments[segment] = PassiveDataSegment.EMPTY;
    }

    private static Runnable getAtomicFenceImpl() {
        try {
            VarHandle.fullFence();
            return VarHandle::fullFence;
        }
        catch (NoSuchMethodError e) {
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                Object theUnsafe = theUnsafeField.get(null);
                Method fullFence = unsafeClass.getMethod("fullFence", new Class[0]);
                return () -> {
                    try {
                        fullFence.invoke(theUnsafe, new Object[0]);
                    }
                    catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new RuntimeException("ATOMIC_FENCE implementation: Failed to invoke sun.misc.Unsafe", ex);
                    }
                };
            }
            catch (Throwable ex) {
                throw new RuntimeException("ATOMIC_FENCE implementation: Failed to lookup sun.misc.Unsafe", ex);
            }
        }
    }

    private static final class WaitState {
        int waiterCount;
        int pendingWakeups;

        private WaitState() {
        }
    }
}

