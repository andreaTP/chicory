// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.TagInstance;
import com.dylibso.chicory.runtime.WasmException;
import com.dylibso.chicory.runtime.ChicoryInterruptedException;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.MemCopyWorkaround;
import com.dylibso.chicory.runtime.OpcodeImpl;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.wasm.types.FunctionType;
import com.dylibso.chicory.runtime.Instance;

final class Wat2WasmModuleMachineShaded
{
    private static final boolean memCopyWorkaround;
    
    private Wat2WasmModuleMachineShaded() {
    }
    
    public static long[] callIndirect(final long[] args, final int typeId, final int funcId, final Instance instance) {
        final FunctionType expectedType = instance.type(typeId);
        final FunctionType actualType = instance.type(instance.functionType(funcId));
        if (!actualType.typesMatch(expectedType)) {
            throw throwIndirectCallTypeMismatch();
        }
        return instance.getMachine().call(funcId, args);
    }
    
    public static long[] callIndirect(final long[] args, final int funcId, final Instance instance) {
        return instance.getMachine().call(funcId, args);
    }
    
    public static long[] callHostFunction(final Instance instance, final int funcId, final long[] args) {
        final ImportFunction imprt = instance.imports().function(funcId);
        return imprt.handle().apply(instance, args);
    }
    
    public static boolean isRefNull(final int ref) {
        return ref == -1;
    }
    
    public static int tableGet(final int index, final int tableIndex, final Instance instance) {
        return OpcodeImpl.TABLE_GET(instance, tableIndex, index);
    }
    
    public static void tableSet(final int index, final int value, final int tableIndex, final Instance instance) {
        instance.table(tableIndex).setRef(index, value, instance);
    }
    
    public static int tableGrow(final int value, final int size, final int tableIndex, final Instance instance) {
        return instance.table(tableIndex).grow(size, value, instance);
    }
    
    public static int tableSize(final int tableIndex, final Instance instance) {
        return instance.table(tableIndex).size();
    }
    
    public static void tableFill(final int offset, final int value, final int size, final int tableIndex, final Instance instance) {
        OpcodeImpl.TABLE_FILL(instance, tableIndex, size, value, offset);
    }
    
    public static void tableCopy(final int d, final int s, final int size, final int dstTableIndex, final int srcTableIndex, final Instance instance) {
        OpcodeImpl.TABLE_COPY(instance, srcTableIndex, dstTableIndex, size, s, d);
    }
    
    public static void tableInit(final int offset, final int elemidx, final int size, final int elementidx, final int tableidx, final Instance instance) {
        OpcodeImpl.TABLE_INIT(instance, tableidx, elementidx, size, elemidx, offset);
    }
    
    public static int i32_ge_u(final int a, final int b) {
        if (Wat2WasmModuleMachineShaded.memCopyWorkaround) {
            return MemCopyWorkaround.i32_ge_u(a, b);
        }
        return OpcodeImpl.I32_GE_U(a, b);
    }
    
    public static void memoryCopy(final int destination, final int offset, final int size, final Memory memory) {
        if (Wat2WasmModuleMachineShaded.memCopyWorkaround) {
            MemCopyWorkaround.memoryCopy(destination, offset, size, memory);
        }
        else {
            memory.copy(destination, offset, size);
        }
    }
    
    public static void memoryFill(final int offset, final byte value, final int size, final Memory memory) {
        final int end = size + offset;
        memory.fill(value, offset, end);
    }
    
    public static void memoryInit(final int destination, final int offset, final int size, final int segmentId, final Memory memory) {
        memory.initPassiveSegment(segmentId, destination, offset, size);
    }
    
    public static int memoryGrow(final int size, final Memory memory) {
        return memory.grow(size);
    }
    
    public static void memoryDrop(final int segment, final Memory memory) {
        memory.drop(segment);
    }
    
    public static int memoryPages(final Memory memory) {
        return memory.pages();
    }
    
    public static byte memoryReadByte(final int base, final int offset, final Memory memory) {
        return memory.read(getAddr(base, offset));
    }
    
    public static short memoryReadShort(final int base, final int offset, final Memory memory) {
        return memory.readShort(getAddr(base, offset));
    }
    
    public static int memoryReadInt(final int base, final int offset, final Memory memory) {
        return memory.readInt(getAddr(base, offset));
    }
    
    public static long memoryReadLong(final int base, final int offset, final Memory memory) {
        return memory.readLong(getAddr(base, offset));
    }
    
    public static float memoryReadFloat(final int base, final int offset, final Memory memory) {
        return memory.readFloat(getAddr(base, offset));
    }
    
    public static double memoryReadDouble(final int base, final int offset, final Memory memory) {
        return memory.readDouble(getAddr(base, offset));
    }
    
    public static void memoryWriteByte(final int base, final byte value, final int offset, final Memory memory) {
        memory.writeByte(getAddr(base, offset), value);
    }
    
    public static void memoryWriteShort(final int base, final short value, final int offset, final Memory memory) {
        memory.writeShort(getAddr(base, offset), value);
    }
    
    public static void memoryWriteInt(final int base, final int value, final int offset, final Memory memory) {
        memory.writeI32(getAddr(base, offset), value);
    }
    
    public static void memoryWriteLong(final int base, final long value, final int offset, final Memory memory) {
        memory.writeLong(getAddr(base, offset), value);
    }
    
    public static void memoryWriteFloat(final int base, final float value, final int offset, final Memory memory) {
        memory.writeF32(getAddr(base, offset), value);
    }
    
    public static void memoryWriteDouble(final int base, final double value, final int offset, final Memory memory) {
        memory.writeF64(getAddr(base, offset), value);
    }
    
    public static int memoryAtomicIntByteRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicReadByte(ptr));
    }
    
    public static int memoryAtomicIntShortRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicReadShort(ptr));
    }
    
    public static int memoryAtomicIntRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicReadInt(ptr);
    }
    
    public static long memoryAtomicLongRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicReadLong(ptr);
    }
    
    public static long memoryAtomicLongByteRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicReadByte(ptr));
    }
    
    public static long memoryAtomicLongShortRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicReadShort(ptr));
    }
    
    public static long memoryAtomicLongIntRead(final int base, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicReadInt(ptr));
    }
    
    public static void memoryAtomicIntWrite(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteInt(ptr, value);
    }
    
    public static void memoryAtomicIntByteWrite(final int base, final byte value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        memory.atomicWriteByte(ptr, value);
    }
    
    public static void memoryAtomicIntShortWrite(final int base, final short value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteShort(ptr, value);
    }
    
    public static void memoryAtomicLongWrite(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteLong(ptr, value);
    }
    
    public static void memoryAtomicLongByteWrite(final int base, final byte value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        memory.atomicWriteByte(ptr, value);
    }
    
    public static void memoryAtomicLongShortWrite(final int base, final short value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteShort(ptr, value);
    }
    
    public static void memoryAtomicLongIntWrite(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteInt(ptr, value);
    }
    
    public static int getAddr(final int base, final int offset) {
        return (base < 0) ? base : (base + offset);
    }
    
    public static RuntimeException throwCallStackExhausted(final StackOverflowError e) {
        throw new ChicoryException("call stack exhausted", (Throwable)e);
    }
    
    public static RuntimeException throwIndirectCallTypeMismatch() {
        return (RuntimeException)new ChicoryException("indirect call type mismatch");
    }
    
    public static RuntimeException throwOutOfBoundsMemoryAccess() {
        throw new WasmRuntimeException("out of bounds memory access");
    }
    
    public static RuntimeException throwTrapException() {
        throw new TrapException("Trapped on unreachable instruction");
    }
    
    public static RuntimeException throwUnknownFunction(final int index) {
        throw new InvalidException(String.format("unknown function %d", index));
    }
    
    public static void checkInterruption() {
        if (Thread.currentThread().isInterrupted()) {
            throw new ChicoryInterruptedException("Thread interrupted");
        }
    }
    
    public static long readGlobal(final int index, final Instance instance) {
        return instance.global(index).getValue();
    }
    
    public static void writeGlobal(final long value, final int index, final Instance instance) {
        instance.global(index).setValue(value);
    }
    
    public static WasmException createWasmException(long[] args, final int tagNumber, final Instance instance) {
        if (args == null) {
            args = new long[0];
        }
        final WasmException e = new WasmException(instance, tagNumber, args);
        instance.registerException(e);
        return e;
    }
    
    public static boolean exceptionMatches(final WasmException exception, final int tag, final Instance instance) {
        if (exception.instance() == instance && exception.tagIdx() == tag) {
            return true;
        }
        final TagInstance currentCatchTag = instance.tag(tag);
        final TagInstance exceptionTag = exception.instance().tag(exception.tagIdx());
        return tag < instance.imports().tagCount() && currentCatchTag.type().typesMatch(exceptionTag.type()) && currentCatchTag.type().returnsMatch(exceptionTag.type());
    }
    
    public static int memoryAtomicIntRmwAdd(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddInt(ptr, value);
    }
    
    public static int memoryAtomicIntRmwSub(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddInt(ptr, -value);
    }
    
    public static int memoryAtomicIntRmwAnd(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAndInt(ptr, value);
    }
    
    public static int memoryAtomicIntRmwOr(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicOrInt(ptr, value);
    }
    
    public static int memoryAtomicIntRmwXor(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXorInt(ptr, value);
    }
    
    public static int memoryAtomicIntRmwXchg(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXchgInt(ptr, value);
    }
    
    public static int memoryAtomicIntRmwCmpxchg(final int base, final int expected, final int replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicCmpxchgInt(ptr, expected, replacement);
    }
    
    public static int memoryAtomicIntRmw8AddU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte)value));
    }
    
    public static int memoryAtomicIntRmw8SubU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte)(-value)));
    }
    
    public static int memoryAtomicIntRmw8AndU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAndByte(ptr, (byte)value));
    }
    
    public static int memoryAtomicIntRmw8OrU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicOrByte(ptr, (byte)value));
    }
    
    public static int memoryAtomicIntRmw8XorU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicXorByte(ptr, (byte)value));
    }
    
    public static int memoryAtomicIntRmw8XchgU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicXchgByte(ptr, (byte)value));
    }
    
    public static int memoryAtomicIntRmw8CmpxchgU(final int base, final int expected, final int replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicCmpxchgByte(ptr, (byte)expected, (byte)replacement));
    }
    
    public static int memoryAtomicIntRmw16AddU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddShort(ptr, (short)value) & 0xFFFF;
    }
    
    public static int memoryAtomicIntRmw16SubU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicAddShort(ptr, (short)(-value)));
    }
    
    public static int memoryAtomicIntRmw16AndU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicAndShort(ptr, (short)value));
    }
    
    public static int memoryAtomicIntRmw16OrU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicOrShort(ptr, (short)value));
    }
    
    public static int memoryAtomicIntRmw16XorU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicXorShort(ptr, (short)value));
    }
    
    public static int memoryAtomicIntRmw16XchgU(final int base, final int value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicXchgShort(ptr, (short)value));
    }
    
    public static int memoryAtomicIntRmw16CmpxchgU(final int base, final int expected, final int replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicCmpxchgShort(ptr, (short)expected, (short)replacement));
    }
    
    public static long memoryAtomicLongRmw8AddU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte)value));
    }
    
    public static long memoryAtomicLongRmw8SubU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte)(-value)));
    }
    
    public static long memoryAtomicLongRmw8AndU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAndByte(ptr, (byte)value));
    }
    
    public static long memoryAtomicLongRmw8OrU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicOrByte(ptr, (byte)value));
    }
    
    public static long memoryAtomicLongRmw8XorU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicXorByte(ptr, (byte)value));
    }
    
    public static long memoryAtomicLongRmw8XchgU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicXchgByte(ptr, (byte)value));
    }
    
    public static long memoryAtomicLongRmw8CmpxchgU(final int base, final long expected, final long replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicCmpxchgByte(ptr, (byte)expected, (byte)replacement));
    }
    
    public static long memoryAtomicLongRmw16AddU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short)value));
    }
    
    public static long memoryAtomicLongRmw16SubU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short)(-value)));
    }
    
    public static long memoryAtomicLongRmw16AndU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAndShort(ptr, (short)value));
    }
    
    public static long memoryAtomicLongRmw16OrU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicOrShort(ptr, (short)value));
    }
    
    public static long memoryAtomicLongRmw16XorU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicXorShort(ptr, (short)value));
    }
    
    public static long memoryAtomicLongRmw16XchgU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicXchgShort(ptr, (short)value));
    }
    
    public static long memoryAtomicLongRmw16CmpxchgU(final int base, final long expected, final long replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicCmpxchgShort(ptr, (short)expected, (short)replacement));
    }
    
    public static long memoryAtomicLongRmw32AddU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int)value));
    }
    
    public static long memoryAtomicLongRmw32SubU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int)(-value)));
    }
    
    public static long memoryAtomicLongRmw32AndU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAndInt(ptr, (int)value));
    }
    
    public static long memoryAtomicLongRmw32OrU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicOrInt(ptr, (int)value));
    }
    
    public static long memoryAtomicLongRmw32XorU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicXorInt(ptr, (int)value));
    }
    
    public static long memoryAtomicLongRmw32XchgU(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicXchgInt(ptr, (int)value));
    }
    
    public static long memoryAtomicLongRmw32CmpxchgU(final int base, final long expected, final long replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicCmpxchgInt(ptr, (int)expected, (int)replacement));
    }
    
    public static long memoryAtomicLongRmwAdd(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddLong(ptr, value);
    }
    
    public static long memoryAtomicLongRmwSub(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddLong(ptr, -value);
    }
    
    public static long memoryAtomicLongRmwAnd(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAndLong(ptr, value);
    }
    
    public static long memoryAtomicLongRmwOr(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicOrLong(ptr, value);
    }
    
    public static long memoryAtomicLongRmwXor(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXorLong(ptr, value);
    }
    
    public static long memoryAtomicLongRmwXchg(final int base, final long value, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXchgLong(ptr, value);
    }
    
    public static long memoryAtomicLongRmwCmpxchg(final int base, final long expected, final long replacement, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicCmpxchgLong(ptr, expected, replacement);
    }
    
    public static int memoryAtomicWait32(final int base, final int expected, final long timeout, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicWait(ptr, expected, timeout);
    }
    
    public static int memoryAtomicWait64(final int base, final long expected, final long timeout, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicWait(ptr, expected, timeout);
    }
    
    public static int memoryAtomicNotify(final int base, final int count, final int offset, final Memory memory) {
        final int ptr = getAddr(base, offset);
        return memory.atomicNotify(ptr, count);
    }
    
    public static void memoryAtomicFence(final Memory memory) {
        memory.atomicFence();
    }
    
    static {
        final String prop = System.getProperty("chicory.memCopyWorkaround");
        if (prop != null) {
            memCopyWorkaround = Boolean.valueOf(prop);
        }
        else {
            memCopyWorkaround = MemCopyWorkaround.shouldUseMemWorkaround();
        }
    }
}
