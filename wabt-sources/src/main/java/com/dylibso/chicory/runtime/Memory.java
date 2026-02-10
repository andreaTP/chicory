/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.ByteBufferMemory;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.types.DataSegment;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Memory {
    public static final int PAGE_SIZE = 65536;
    public static final int RUNTIME_MAX_PAGES = Short.MAX_VALUE;

    public static int bytes(int pages) {
        return 65536 * Math.min(pages, Short.MAX_VALUE);
    }

    public int pages();

    public int grow(int var1);

    public int initialPages();

    public int maximumPages();

    public boolean shared();

    @Deprecated(forRemoval=true)
    public Object lock(int var1);

    @Deprecated(forRemoval=true)
    public int waitOn(int var1, int var2, long var3);

    @Deprecated(forRemoval=true)
    public int waitOn(int var1, long var2, long var4);

    @Deprecated(forRemoval=true)
    public int notify(int var1, int var2);

    default public void atomicFence() {
        ByteBufferMemory.ATOMIC_FENCE_IMPL.run();
    }

    default public int atomicWait(int addr, int expected, long timeout) {
        return this.waitOn(addr, expected, timeout);
    }

    default public int atomicWait(int addr, long expected, long timeout) {
        return this.waitOn(addr, expected, timeout);
    }

    default public int atomicNotify(int addr, int maxThreads) {
        return this.notify(addr, maxThreads);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicReadInt(int addr) {
        Object object = this.lock(addr);
        synchronized (object) {
            return this.readInt(addr);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicReadLong(int addr) {
        Object object = this.lock(addr);
        synchronized (object) {
            return this.readLong(addr);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicReadShort(int addr) {
        Object object = this.lock(addr);
        synchronized (object) {
            return this.readShort(addr);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicReadByte(int addr) {
        Object object = this.lock(addr);
        synchronized (object) {
            return this.read(addr);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public void atomicWriteInt(int addr, int value) {
        Object object = this.lock(addr);
        synchronized (object) {
            this.writeI32(addr, value);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public void atomicWriteLong(int addr, long value) {
        Object object = this.lock(addr);
        synchronized (object) {
            this.writeLong(addr, value);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public void atomicWriteShort(int addr, short value) {
        Object object = this.lock(addr);
        synchronized (object) {
            this.writeShort(addr, value);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public void atomicWriteByte(int addr, byte value) {
        Object object = this.lock(addr);
        synchronized (object) {
            this.writeByte(addr, value);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicAddInt(int addr, int delta) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            this.writeI32(addr, val + delta);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicAndInt(int addr, int mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            this.writeI32(addr, val & mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicOrInt(int addr, int mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            this.writeI32(addr, val | mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicXorInt(int addr, int mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            this.writeI32(addr, val ^ mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicXchgInt(int addr, int value) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            this.writeI32(addr, value);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public int atomicCmpxchgInt(int addr, int expected, int replacement) {
        Object object = this.lock(addr);
        synchronized (object) {
            int val = this.readInt(addr);
            if (val == expected) {
                this.writeI32(addr, replacement);
            }
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicAddLong(int addr, long delta) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            this.writeLong(addr, val + delta);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicAndLong(int addr, long mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            this.writeLong(addr, val & mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicOrLong(int addr, long mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            this.writeLong(addr, val | mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicXorLong(int addr, long mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            this.writeLong(addr, val ^ mask);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicXchgLong(int addr, long value) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            this.writeLong(addr, value);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public long atomicCmpxchgLong(int addr, long expected, long replacement) {
        Object object = this.lock(addr);
        synchronized (object) {
            long val = this.readLong(addr);
            if (val == expected) {
                this.writeLong(addr, replacement);
            }
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicAddShort(int addr, short delta) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            this.writeShort(addr, (short)(val + delta));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicAndShort(int addr, short mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            this.writeShort(addr, (short)(val & mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicOrShort(int addr, short mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            this.writeShort(addr, (short)(val | mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicXorShort(int addr, short mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            this.writeShort(addr, (short)(val ^ mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicXchgShort(int addr, short value) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            this.writeShort(addr, value);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public short atomicCmpxchgShort(int addr, short expected, short replacement) {
        Object object = this.lock(addr);
        synchronized (object) {
            short val = this.readShort(addr);
            if (val == expected) {
                this.writeShort(addr, replacement);
            }
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicAddByte(int addr, byte delta) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            this.writeByte(addr, (byte)(val + delta));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicAndByte(int addr, byte mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            this.writeByte(addr, (byte)(val & mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicOrByte(int addr, byte mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            this.writeByte(addr, (byte)(val | mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicXorByte(int addr, byte mask) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            this.writeByte(addr, (byte)(val ^ mask));
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicXchgByte(int addr, byte value) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            this.writeByte(addr, value);
            return val;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    default public byte atomicCmpxchgByte(int addr, byte expected, byte replacement) {
        Object object = this.lock(addr);
        synchronized (object) {
            byte val = this.read(addr);
            if (val == expected) {
                this.writeByte(addr, replacement);
            }
            return val;
        }
    }

    public void initialize(Instance var1, DataSegment[] var2);

    public void initPassiveSegment(int var1, int var2, int var3, int var4);

    default public void writeString(int offset, String data, Charset charSet) {
        this.write(offset, data.getBytes(charSet));
    }

    default public void writeString(int offset, String data) {
        this.writeString(offset, data, StandardCharsets.UTF_8);
    }

    default public String readString(int addr, int len) {
        return this.readString(addr, len, StandardCharsets.UTF_8);
    }

    default public String readString(int addr, int len, Charset charSet) {
        return new String(this.readBytes(addr, len), charSet);
    }

    default public void writeCString(int offset, String str) {
        this.writeCString(offset, str, StandardCharsets.UTF_8);
    }

    default public void writeCString(int offset, String str, Charset charSet) {
        this.writeString(offset, str + "\u0000", charSet);
    }

    default public String readCString(int addr, Charset charSet) {
        int c = addr;
        while (this.read(c) != 0) {
            ++c;
        }
        return new String(this.readBytes(addr, c - addr), charSet);
    }

    default public String readCString(int addr) {
        return this.readCString(addr, StandardCharsets.UTF_8);
    }

    default public void write(int addr, byte[] data) {
        this.write(addr, data, 0, data.length);
    }

    public void write(int var1, byte[] var2, int var3, int var4);

    public byte read(int var1);

    public byte[] readBytes(int var1, int var2);

    public void writeI32(int var1, int var2);

    public int readInt(int var1);

    default public long readI32(int addr) {
        return this.readInt(addr);
    }

    default public long readU32(int addr) {
        return Integer.toUnsignedLong(this.readInt(addr));
    }

    public void writeLong(int var1, long var2);

    public long readLong(int var1);

    default public long readI64(int addr) {
        return this.readLong(addr);
    }

    public void writeShort(int var1, short var2);

    public short readShort(int var1);

    default public long readI16(int addr) {
        return this.readShort(addr);
    }

    public long readU16(int var1);

    public void writeByte(int var1, byte var2);

    default public long readU8(int addr) {
        return this.read(addr) & 0xFF;
    }

    default public long readI8(int addr) {
        return this.read(addr);
    }

    public void writeF32(int var1, float var2);

    public long readF32(int var1);

    public float readFloat(int var1);

    public void writeF64(int var1, double var2);

    public double readDouble(int var1);

    public long readF64(int var1);

    public void zero();

    public void fill(byte var1, int var2, int var3);

    default public void copy(int dest, int src, int size) {
        this.write(dest, this.readBytes(src, size));
    }

    public void drop(int var1);
}

