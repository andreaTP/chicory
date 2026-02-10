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
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public final class ByteArrayMemory
implements Memory {
    private static final VarHandle BYTE_ARR_HANDLE = MethodHandles.arrayElementVarHandle(byte[].class);
    private static final VarHandle SHORT_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle INT_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle FLOAT_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(float[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LONG_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle DOUBLE_ARR_HANDLE = MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.LITTLE_ENDIAN);
    private static final boolean HAS_BYTE_ATOMICS = ByteArrayMemory.hasFullAtomicSupport(BYTE_ARR_HANDLE);
    private static final boolean HAS_SHORT_ATOMICS = ByteArrayMemory.hasFullAtomicSupport(SHORT_ARR_HANDLE);
    private static final boolean HAS_INT_ATOMICS = ByteArrayMemory.hasFullAtomicSupport(INT_ARR_HANDLE);
    private static final boolean HAS_LONG_ATOMICS = ByteArrayMemory.hasFullAtomicSupport(LONG_ARR_HANDLE);
    private static final int PAGE_SHIFT = 16;
    private static final int PAGE_MASK = 65535;
    private final MemoryLimits limits;
    private DataSegment[] dataSegments;
    private final byte[][] pages;
    private volatile int nPages;
    private final Object growLock = new Object();
    private final Map<Integer, WaitState> waitStates;

    private static boolean hasFullAtomicSupport(VarHandle varHandle) {
        return varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE) && varHandle.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE) && varHandle.isAccessModeSupported(VarHandle.AccessMode.COMPARE_AND_EXCHANGE) && varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_AND_SET) && varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_AND_ADD) && varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_AND) && varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_OR) && varHandle.isAccessModeSupported(VarHandle.AccessMode.GET_AND_BITWISE_XOR);
    }

    public ByteArrayMemory(MemoryLimits limits) {
        this.limits = limits;
        int maxPages = Math.min(limits.maximumPages(), Short.MAX_VALUE);
        this.pages = new byte[maxPages][];
        for (int i = 0; i < limits.initialPages(); ++i) {
            this.pages[i] = new byte[65536];
        }
        this.nPages = limits.initialPages();
        this.waitStates = limits.shared() ? new ConcurrentHashMap<Integer, WaitState>() : null;
    }

    @Deprecated
    public ByteArrayMemory(MemoryLimits limits, MemAllocStrategy allocStrategy) {
        this(limits);
    }

    @Override
    public Object lock(int address) {
        throw new UnsupportedOperationException();
    }

    private Object monitor(int address) {
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
        return this.waitOn(address, () -> this.atomicReadInt(address) == expected, timeout);
    }

    @Override
    public int waitOn(int address, long expected, long timeout) {
        return this.waitOn(address, () -> this.atomicReadLong(address) == expected, timeout);
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
            this.pages[i] = new byte[65536];
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
                ByteArrayMemory.checkBounds(offset, data.length, this.sizeInBytes(), UninstantiableException::new);
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
        if (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException || e instanceof NullPointerException || e instanceof NegativeArraySizeException) {
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
        ByteArrayMemory.checkBounds(offset, size, data.length, WasmRuntimeException::new);
        ByteArrayMemory.checkBounds(addr, size, this.sizeInBytes(), WasmRuntimeException::new);
        while (size > 0) {
            int pageIdx = addr >>> 16;
            int pageOffset = addr & 0xFFFF;
            int chunk = Math.min(size, 65536 - pageOffset);
            System.arraycopy(data, offset, this.pages[pageIdx], pageOffset, chunk);
            addr += chunk;
            offset += chunk;
            size -= chunk;
        }
    }

    @Override
    public byte read(int addr) {
        try {
            return this.pages[addr >>> 16][addr & 0xFFFF];
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    @Override
    public byte[] readBytes(int addr, int len) {
        int chunk;
        ByteArrayMemory.checkBounds(addr, len, this.sizeInBytes(), WasmRuntimeException::new);
        byte[] result = new byte[len];
        int destOffset = 0;
        int a = addr;
        for (int remaining = len; remaining > 0; remaining -= chunk) {
            int pageIdx = a >>> 16;
            int pageOffset = a & 0xFFFF;
            chunk = Math.min(remaining, 65536 - pageOffset);
            System.arraycopy(this.pages[pageIdx], pageOffset, result, destOffset, chunk);
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
                INT_ARR_HANDLE.set(this.pages[addr >>> 16], off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        } else {
            this.writeI32Slow(addr, data);
        }
    }

    private void writeI32Slow(int addr, int data) {
        ByteArrayMemory.checkBounds(addr, 4, this.sizeInBytes(), WasmRuntimeException::new);
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
                return INT_ARR_HANDLE.get(this.pages[addr >>> 16], off);
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
                LONG_ARR_HANDLE.set(this.pages[addr >>> 16], off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        } else {
            this.writeLongSlow(addr, data);
        }
    }

    private void writeLongSlow(int addr, long data) {
        ByteArrayMemory.checkBounds(addr, 8, this.sizeInBytes(), WasmRuntimeException::new);
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
                return LONG_ARR_HANDLE.get(this.pages[addr >>> 16], off);
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
                SHORT_ARR_HANDLE.set(this.pages[addr >>> 16], off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 2);
            }
        } else {
            this.writeShortSlow(addr, data);
        }
    }

    private void writeShortSlow(int addr, short data) {
        ByteArrayMemory.checkBounds(addr, 2, this.sizeInBytes(), WasmRuntimeException::new);
        this.writeByte(addr, (byte)data);
        this.writeByte(addr + 1, (byte)(data >>> 8));
    }

    @Override
    public short readShort(int addr) {
        int off = addr & 0xFFFF;
        if (off + 2 <= 65536) {
            try {
                return SHORT_ARR_HANDLE.get(this.pages[addr >>> 16], off);
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
            this.pages[addr >>> 16][addr & 0xFFFF] = data;
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
                FLOAT_ARR_HANDLE.set(this.pages[addr >>> 16], off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        } else {
            this.writeI32(addr, Float.floatToRawIntBits(data));
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
                return FLOAT_ARR_HANDLE.get(this.pages[addr >>> 16], off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 4);
            }
        }
        return Float.intBitsToFloat(this.readInt(addr));
    }

    @Override
    public void writeF64(int addr, double data) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                DOUBLE_ARR_HANDLE.set(this.pages[addr >>> 16], off, data);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        } else {
            this.writeLong(addr, Double.doubleToRawLongBits(data));
        }
    }

    @Override
    public double readDouble(int addr) {
        int off = addr & 0xFFFF;
        if (off + 8 <= 65536) {
            try {
                return DOUBLE_ARR_HANDLE.get(this.pages[addr >>> 16], off);
            }
            catch (RuntimeException e) {
                throw this.outOfBoundsException(e, addr, 8);
            }
        }
        return Double.longBitsToDouble(this.readLong(addr));
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
        ByteArrayMemory.checkBounds(addr, remaining, this.sizeInBytes(), WasmRuntimeException::new);
        for (remaining = toIndex - fromIndex; remaining > 0; remaining -= chunk) {
            int pageIdx = addr >>> 16;
            int pageOffset = addr & 0xFFFF;
            chunk = Math.min(remaining, 65536 - pageOffset);
            Arrays.fill(this.pages[pageIdx], pageOffset, pageOffset + chunk, value);
            addr += chunk;
        }
    }

    @Override
    public void copy(int dest, int src, int size) {
        int limit = this.sizeInBytes();
        ByteArrayMemory.checkBounds(dest, size, limit, WasmRuntimeException::new);
        ByteArrayMemory.checkBounds(src, size, limit, WasmRuntimeException::new);
        while (size > 0) {
            int destOffset = dest & 0xFFFF;
            int srcOffset = src & 0xFFFF;
            int chunk = Math.min(size, 65536 - Math.max(destOffset, srcOffset));
            System.arraycopy(this.pages[src >>> 16], srcOffset, this.pages[dest >>> 16], destOffset, chunk);
            dest += chunk;
            src += chunk;
            size -= chunk;
        }
    }

    @Override
    public void drop(int segment) {
        this.dataSegments[segment] = PassiveDataSegment.EMPTY;
    }

    @Override
    public void atomicFence() {
        VarHandle.fullFence();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicAddByte(int addr, byte delta) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.getAndAdd(page, off, delta);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte value = BYTE_ARR_HANDLE.get(page, off);
                BYTE_ARR_HANDLE.set(page, off, (byte)(value + delta));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicAddInt(int addr, int delta) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.getAndAdd(page, off, delta);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int value = INT_ARR_HANDLE.get(page, off);
                INT_ARR_HANDLE.set(page, off, value + delta);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicAddLong(int addr, long delta) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.getAndAdd(page, off, delta);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long value = LONG_ARR_HANDLE.get(page, off);
                LONG_ARR_HANDLE.set(page, off, value + delta);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicAddShort(int addr, short delta) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.getAndAdd(page, off, delta);
            }
            if (HAS_INT_ATOMICS) {
                short oldShort;
                short newShort;
                int newInt;
                int oldInt;
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int mask = 65535 << shift;
                while (!INT_ARR_HANDLE.compareAndSet(page, alignedOff, oldInt = INT_ARR_HANDLE.getVolatile(page, alignedOff), newInt = oldInt & ~mask | ((newShort = (short)((oldShort = (short)(oldInt >>> shift & 0xFFFF)) + delta)) & 0xFFFF) << shift)) {
                }
                return oldShort;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short value = SHORT_ARR_HANDLE.get(page, off);
                SHORT_ARR_HANDLE.set(page, off, (short)(value + delta));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicAndByte(int addr, byte mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.getAndBitwiseAnd(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte value = BYTE_ARR_HANDLE.get(page, off);
                BYTE_ARR_HANDLE.set(page, off, (byte)(value & mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicAndInt(int addr, int mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.getAndBitwiseAnd(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int value = INT_ARR_HANDLE.get(page, off);
                INT_ARR_HANDLE.set(page, off, value & mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicAndLong(int addr, long mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.getAndBitwiseAnd(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long value = LONG_ARR_HANDLE.get(page, off);
                LONG_ARR_HANDLE.set(page, off, value & mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicAndShort(int addr, short mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.getAndBitwiseAnd(page, off, mask);
            }
            if (HAS_INT_ATOMICS) {
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int intMask = (mask & 0xFFFF) << shift | ~(65535 << shift);
                int intValue = INT_ARR_HANDLE.getAndBitwiseAnd(page, alignedOff, intMask);
                return (short)(intValue >>> shift & 0xFFFF);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short value = SHORT_ARR_HANDLE.get(page, off);
                SHORT_ARR_HANDLE.set(page, off, (short)(value & mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicCmpxchgByte(int addr, byte expected, byte replacement) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.compareAndExchange(page, off, expected, replacement);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte value = BYTE_ARR_HANDLE.get(page, off);
                if (value == expected) {
                    BYTE_ARR_HANDLE.set(page, off, replacement);
                }
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicCmpxchgInt(int addr, int expected, int replacement) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.compareAndExchange(page, off, expected, replacement);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int value = INT_ARR_HANDLE.get(page, off);
                if (value == expected) {
                    INT_ARR_HANDLE.set(page, off, replacement);
                }
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicCmpxchgLong(int addr, long expected, long replacement) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.compareAndExchange(page, off, expected, replacement);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long value = LONG_ARR_HANDLE.get(page, off);
                if (value == expected) {
                    LONG_ARR_HANDLE.set(page, off, replacement);
                }
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicCmpxchgShort(int addr, short expected, short replacement) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.compareAndExchange(page, off, expected, replacement);
            }
            if (HAS_INT_ATOMICS) {
                short oldShort;
                int newInt;
                int oldInt;
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int mask = 65535 << shift;
                do {
                    if ((oldShort = (short)((oldInt = INT_ARR_HANDLE.getVolatile(page, alignedOff)) >>> shift & 0xFFFF)) == expected) continue;
                    return oldShort;
                } while (!INT_ARR_HANDLE.compareAndSet(page, alignedOff, oldInt, newInt = oldInt & ~mask | (replacement & 0xFFFF) << shift));
                return oldShort;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short value = SHORT_ARR_HANDLE.get(page, off);
                if (value == expected) {
                    SHORT_ARR_HANDLE.set(page, off, replacement);
                }
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicOrByte(int addr, byte mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.getAndBitwiseOr(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte value = BYTE_ARR_HANDLE.get(page, off);
                BYTE_ARR_HANDLE.set(page, off, (byte)(value | mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicOrInt(int addr, int mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.getAndBitwiseOr(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int value = INT_ARR_HANDLE.get(page, off);
                INT_ARR_HANDLE.set(page, off, value | mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicOrLong(int addr, long mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.getAndBitwiseOr(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long value = LONG_ARR_HANDLE.get(page, off);
                LONG_ARR_HANDLE.set(page, off, value | mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicOrShort(int addr, short mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.getAndBitwiseOr(page, off, mask);
            }
            if (HAS_INT_ATOMICS) {
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int intMask = (mask & 0xFFFF) << shift;
                int intValue = INT_ARR_HANDLE.getAndBitwiseOr(page, alignedOff, intMask);
                return (short)(intValue >>> shift & 0xFFFF);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short value = SHORT_ARR_HANDLE.get(page, off);
                SHORT_ARR_HANDLE.set(page, off, (short)(value | mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicReadByte(int addr) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (BYTE_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE)) {
                return BYTE_ARR_HANDLE.getVolatile(page, off);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                return BYTE_ARR_HANDLE.get(page, off);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicReadInt(int addr) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (INT_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE)) {
                return INT_ARR_HANDLE.getVolatile(page, off);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                return INT_ARR_HANDLE.get(page, off);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicReadLong(int addr) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (LONG_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE)) {
                return LONG_ARR_HANDLE.getVolatile(page, off);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                return LONG_ARR_HANDLE.get(page, off);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicReadShort(int addr) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (SHORT_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.GET_VOLATILE)) {
                return SHORT_ARR_HANDLE.getVolatile(page, off);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                return SHORT_ARR_HANDLE.get(page, off);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void atomicWriteByte(int addr, byte value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (BYTE_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE)) {
                BYTE_ARR_HANDLE.setVolatile(page, off, value);
                return;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                BYTE_ARR_HANDLE.set(page, off, value);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void atomicWriteInt(int addr, int value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (INT_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE)) {
                INT_ARR_HANDLE.setVolatile(page, off, value);
                return;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                INT_ARR_HANDLE.set(page, off, value);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void atomicWriteLong(int addr, long value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (LONG_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE)) {
                LONG_ARR_HANDLE.setVolatile(page, off, value);
                return;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                LONG_ARR_HANDLE.set(page, off, value);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void atomicWriteShort(int addr, short value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (SHORT_ARR_HANDLE.isAccessModeSupported(VarHandle.AccessMode.SET_VOLATILE)) {
                SHORT_ARR_HANDLE.setVolatile(page, off, value);
                return;
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                SHORT_ARR_HANDLE.set(page, off, value);
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicXchgByte(int addr, byte value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.getAndSet(page, off, value);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte oldValue = BYTE_ARR_HANDLE.get(page, off);
                BYTE_ARR_HANDLE.set(page, off, value);
                return oldValue;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicXchgInt(int addr, int value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.getAndSet(page, off, value);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int oldValue = INT_ARR_HANDLE.get(page, off);
                INT_ARR_HANDLE.set(page, off, value);
                return oldValue;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicXchgLong(int addr, long value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.getAndSet(page, off, value);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long oldValue = LONG_ARR_HANDLE.get(page, off);
                LONG_ARR_HANDLE.set(page, off, value);
                return oldValue;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicXchgShort(int addr, short value) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.getAndSet(page, off, value);
            }
            if (HAS_INT_ATOMICS) {
                int newInt;
                int oldInt;
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int mask = 65535 << shift;
                while (!INT_ARR_HANDLE.compareAndSet(page, alignedOff, oldInt = INT_ARR_HANDLE.getVolatile(page, alignedOff), newInt = oldInt & ~mask | (value & 0xFFFF) << shift)) {
                }
                return (short)(oldInt >>> shift & 0xFFFF);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short oldValue = SHORT_ARR_HANDLE.get(page, off);
                SHORT_ARR_HANDLE.set(page, off, value);
                return oldValue;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte atomicXorByte(int addr, byte mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_BYTE_ATOMICS) {
                return BYTE_ARR_HANDLE.getAndBitwiseXor(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                byte value = BYTE_ARR_HANDLE.get(page, off);
                BYTE_ARR_HANDLE.set(page, off, (byte)(value ^ mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 1);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int atomicXorInt(int addr, int mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_INT_ATOMICS) {
                return INT_ARR_HANDLE.getAndBitwiseXor(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                int value = INT_ARR_HANDLE.get(page, off);
                INT_ARR_HANDLE.set(page, off, value ^ mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 4);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long atomicXorLong(int addr, long mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_LONG_ATOMICS) {
                return LONG_ARR_HANDLE.getAndBitwiseXor(page, off, mask);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                long value = LONG_ARR_HANDLE.get(page, off);
                LONG_ARR_HANDLE.set(page, off, value ^ mask);
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 8);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public short atomicXorShort(int addr, short mask) {
        try {
            byte[] page = this.pages[addr >>> 16];
            int off = addr & 0xFFFF;
            if (HAS_SHORT_ATOMICS) {
                return SHORT_ARR_HANDLE.getAndBitwiseXor(page, off, mask);
            }
            if (HAS_INT_ATOMICS) {
                int alignedOff = off & 0xFFFFFFFC;
                int shift = (off & 2) * 8;
                int intMask = (mask & 0xFFFF) << shift;
                int intValue = INT_ARR_HANDLE.getAndBitwiseXor(page, alignedOff, intMask);
                return (short)(intValue >>> shift & 0xFFFF);
            }
            Object object = this.monitor(addr);
            synchronized (object) {
                short value = SHORT_ARR_HANDLE.get(page, off);
                SHORT_ARR_HANDLE.set(page, off, (short)(value ^ mask));
                return value;
            }
        }
        catch (RuntimeException e) {
            throw this.outOfBoundsException(e, addr, 2);
        }
    }

    private static final class WaitState {
        int waiterCount;
        int pendingWakeups;

        private WaitState() {
        }
    }
}

