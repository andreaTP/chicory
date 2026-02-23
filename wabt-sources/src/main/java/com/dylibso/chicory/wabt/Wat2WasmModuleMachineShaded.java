/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.ChicoryInterruptedException;
import com.dylibso.chicory.runtime.ImportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.MemCopyWorkaround;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.OpcodeImpl;
import com.dylibso.chicory.runtime.TagInstance;
import com.dylibso.chicory.runtime.TrapException;
import com.dylibso.chicory.runtime.WasmException;
import com.dylibso.chicory.runtime.WasmRuntimeException;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.InvalidException;
import com.dylibso.chicory.wasm.types.FunctionType;

final class Wat2WasmModuleMachineShaded {
    private static final boolean memCopyWorkaround;

    private Wat2WasmModuleMachineShaded() {}

    public static long[] callIndirect(long[] args, int typeId, int funcId, Instance instance) {
        FunctionType expectedType = instance.type(typeId);
        FunctionType actualType = instance.type(instance.functionType(funcId));
        if (!actualType.typesMatch(expectedType)) {
            throw Wat2WasmModuleMachineShaded.throwIndirectCallTypeMismatch();
        }
        return instance.getMachine().call(funcId, args);
    }

    public static long[] callIndirect(long[] args, int funcId, Instance instance) {
        return instance.getMachine().call(funcId, args);
    }

    public static long[] callHostFunction(Instance instance, int funcId, long[] args) {
        ImportFunction imprt = instance.imports().function(funcId);
        return imprt.handle().apply(instance, args);
    }

    public static boolean isRefNull(int ref) {
        return ref == -1;
    }

    public static int tableGet(int index, int tableIndex, Instance instance) {
        return OpcodeImpl.TABLE_GET(instance, tableIndex, index);
    }

    public static void tableSet(int index, int value, int tableIndex, Instance instance) {
        instance.table(tableIndex).setRef(index, value, instance);
    }

    public static int tableGrow(int value, int size, int tableIndex, Instance instance) {
        return instance.table(tableIndex).grow(size, value, instance);
    }

    public static int tableSize(int tableIndex, Instance instance) {
        return instance.table(tableIndex).size();
    }

    public static void tableFill(
            int offset, int value, int size, int tableIndex, Instance instance) {
        OpcodeImpl.TABLE_FILL(instance, tableIndex, size, value, offset);
    }

    public static void tableCopy(
            int d, int s, int size, int dstTableIndex, int srcTableIndex, Instance instance) {
        OpcodeImpl.TABLE_COPY(instance, srcTableIndex, dstTableIndex, size, s, d);
    }

    public static void tableInit(
            int offset, int elemidx, int size, int elementidx, int tableidx, Instance instance) {
        OpcodeImpl.TABLE_INIT(instance, tableidx, elementidx, size, elemidx, offset);
    }

    public static int i32_ge_u(int a, int b) {
        if (memCopyWorkaround) {
            return MemCopyWorkaround.i32_ge_u(a, b);
        }
        return OpcodeImpl.I32_GE_U(a, b);
    }

    public static void memoryCopy(int destination, int offset, int size, Memory memory) {
        if (memCopyWorkaround) {
            MemCopyWorkaround.memoryCopy(destination, offset, size, memory);
        } else {
            memory.copy(destination, offset, size);
        }
    }

    public static void memoryFill(int offset, byte value, int size, Memory memory) {
        int end = size + offset;
        memory.fill(value, offset, end);
    }

    public static void memoryInit(
            int destination, int offset, int size, int segmentId, Memory memory) {
        memory.initPassiveSegment(segmentId, destination, offset, size);
    }

    public static int memoryGrow(int size, Memory memory) {
        return memory.grow(size);
    }

    public static void memoryDrop(int segment, Memory memory) {
        memory.drop(segment);
    }

    public static int memoryPages(Memory memory) {
        return memory.pages();
    }

    public static byte memoryReadByte(int base, int offset, Memory memory) {
        return memory.read(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static short memoryReadShort(int base, int offset, Memory memory) {
        return memory.readShort(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static int memoryReadInt(int base, int offset, Memory memory) {
        return memory.readInt(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static long memoryReadLong(int base, int offset, Memory memory) {
        return memory.readLong(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static float memoryReadFloat(int base, int offset, Memory memory) {
        return memory.readFloat(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static double memoryReadDouble(int base, int offset, Memory memory) {
        return memory.readDouble(Wat2WasmModuleMachineShaded.getAddr(base, offset));
    }

    public static void memoryWriteByte(int base, byte value, int offset, Memory memory) {
        memory.writeByte(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static void memoryWriteShort(int base, short value, int offset, Memory memory) {
        memory.writeShort(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static void memoryWriteInt(int base, int value, int offset, Memory memory) {
        memory.writeI32(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static void memoryWriteLong(int base, long value, int offset, Memory memory) {
        memory.writeLong(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static void memoryWriteFloat(int base, float value, int offset, Memory memory) {
        memory.writeF32(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static void memoryWriteDouble(int base, double value, int offset, Memory memory) {
        memory.writeF64(Wat2WasmModuleMachineShaded.getAddr(base, offset), value);
    }

    public static int memoryAtomicIntByteRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicReadByte(ptr));
    }

    public static int memoryAtomicIntShortRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicReadShort(ptr));
    }

    public static int memoryAtomicIntRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicReadInt(ptr);
    }

    public static long memoryAtomicLongRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicReadLong(ptr);
    }

    public static long memoryAtomicLongByteRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicReadByte(ptr));
    }

    public static long memoryAtomicLongShortRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicReadShort(ptr));
    }

    public static long memoryAtomicLongIntRead(int base, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicReadInt(ptr));
    }

    public static void memoryAtomicIntWrite(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteInt(ptr, value);
    }

    public static void memoryAtomicIntByteWrite(int base, byte value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        memory.atomicWriteByte(ptr, value);
    }

    public static void memoryAtomicIntShortWrite(int base, short value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteShort(ptr, value);
    }

    public static void memoryAtomicLongWrite(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteLong(ptr, value);
    }

    public static void memoryAtomicLongByteWrite(int base, byte value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        memory.atomicWriteByte(ptr, value);
    }

    public static void memoryAtomicLongShortWrite(
            int base, short value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteShort(ptr, value);
    }

    public static void memoryAtomicLongIntWrite(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        memory.atomicWriteInt(ptr, value);
    }

    public static int getAddr(int base, int offset) {
        return base < 0 ? base : base + offset;
    }

    public static RuntimeException throwCallStackExhausted(StackOverflowError e) {
        throw new ChicoryException("call stack exhausted", e);
    }

    public static RuntimeException throwIndirectCallTypeMismatch() {
        return new ChicoryException("indirect call type mismatch");
    }

    public static RuntimeException throwOutOfBoundsMemoryAccess() {
        throw new WasmRuntimeException("out of bounds memory access");
    }

    public static RuntimeException throwTrapException() {
        throw new TrapException("Trapped on unreachable instruction");
    }

    public static RuntimeException throwUnknownFunction(int index) {
        throw new InvalidException(String.format("unknown function %d", index));
    }

    public static void checkInterruption() {
        if (Thread.currentThread().isInterrupted()) {
            throw new ChicoryInterruptedException("Thread interrupted");
        }
    }

    public static long readGlobal(int index, Instance instance) {
        return instance.global(index).getValue();
    }

    public static void writeGlobal(long value, int index, Instance instance) {
        instance.global(index).setValue(value);
    }

    public static WasmException createWasmException(long[] args, int tagNumber, Instance instance) {
        if (args == null) {
            args = new long[] {};
        }
        WasmException e = new WasmException(instance, tagNumber, args);
        instance.registerException(e);
        return e;
    }

    public static boolean exceptionMatches(WasmException exception, int tag, Instance instance) {
        if (exception.instance() == instance && exception.tagIdx() == tag) {
            return true;
        }
        TagInstance currentCatchTag = instance.tag(tag);
        TagInstance exceptionTag = exception.instance().tag(exception.tagIdx());
        return tag < instance.imports().tagCount()
                && currentCatchTag.type().typesMatch(exceptionTag.type())
                && currentCatchTag.type().returnsMatch(exceptionTag.type());
    }

    public static int memoryAtomicIntRmwAdd(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddInt(ptr, value);
    }

    public static int memoryAtomicIntRmwSub(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddInt(ptr, -value);
    }

    public static int memoryAtomicIntRmwAnd(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAndInt(ptr, value);
    }

    public static int memoryAtomicIntRmwOr(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicOrInt(ptr, value);
    }

    public static int memoryAtomicIntRmwXor(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXorInt(ptr, value);
    }

    public static int memoryAtomicIntRmwXchg(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXchgInt(ptr, value);
    }

    public static int memoryAtomicIntRmwCmpxchg(
            int base, int expected, int replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicCmpxchgInt(ptr, expected, replacement);
    }

    public static int memoryAtomicIntRmw8AddU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte) value));
    }

    public static int memoryAtomicIntRmw8SubU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAddByte(ptr, (byte) (-value)));
    }

    public static int memoryAtomicIntRmw8AndU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicAndByte(ptr, (byte) value));
    }

    public static int memoryAtomicIntRmw8OrU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicOrByte(ptr, (byte) value));
    }

    public static int memoryAtomicIntRmw8XorU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicXorByte(ptr, (byte) value));
    }

    public static int memoryAtomicIntRmw8XchgU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(memory.atomicXchgByte(ptr, (byte) value));
    }

    public static int memoryAtomicIntRmw8CmpxchgU(
            int base, int expected, int replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedInt(
                memory.atomicCmpxchgByte(ptr, (byte) expected, (byte) replacement));
    }

    public static int memoryAtomicIntRmw16AddU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddShort(ptr, (short) value) & 0xFFFF;
    }

    public static int memoryAtomicIntRmw16SubU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicAddShort(ptr, (short) (-value)));
    }

    public static int memoryAtomicIntRmw16AndU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicAndShort(ptr, (short) value));
    }

    public static int memoryAtomicIntRmw16OrU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicOrShort(ptr, (short) value));
    }

    public static int memoryAtomicIntRmw16XorU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicXorShort(ptr, (short) value));
    }

    public static int memoryAtomicIntRmw16XchgU(int base, int value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(memory.atomicXchgShort(ptr, (short) value));
    }

    public static int memoryAtomicIntRmw16CmpxchgU(
            int base, int expected, int replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedInt(
                memory.atomicCmpxchgShort(ptr, (short) expected, (short) replacement));
    }

    public static long memoryAtomicLongRmw8AddU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte) value));
    }

    public static long memoryAtomicLongRmw8SubU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAddByte(ptr, (byte) (-value)));
    }

    public static long memoryAtomicLongRmw8AndU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicAndByte(ptr, (byte) value));
    }

    public static long memoryAtomicLongRmw8OrU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicOrByte(ptr, (byte) value));
    }

    public static long memoryAtomicLongRmw8XorU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicXorByte(ptr, (byte) value));
    }

    public static long memoryAtomicLongRmw8XchgU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(memory.atomicXchgByte(ptr, (byte) value));
    }

    public static long memoryAtomicLongRmw8CmpxchgU(
            int base, long expected, long replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return Byte.toUnsignedLong(
                memory.atomicCmpxchgByte(ptr, (byte) expected, (byte) replacement));
    }

    public static long memoryAtomicLongRmw16AddU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short) value));
    }

    public static long memoryAtomicLongRmw16SubU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAddShort(ptr, (short) (-value)));
    }

    public static long memoryAtomicLongRmw16AndU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicAndShort(ptr, (short) value));
    }

    public static long memoryAtomicLongRmw16OrU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicOrShort(ptr, (short) value));
    }

    public static long memoryAtomicLongRmw16XorU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicXorShort(ptr, (short) value));
    }

    public static long memoryAtomicLongRmw16XchgU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(memory.atomicXchgShort(ptr, (short) value));
    }

    public static long memoryAtomicLongRmw16CmpxchgU(
            int base, long expected, long replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 2 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Short.toUnsignedLong(
                memory.atomicCmpxchgShort(ptr, (short) expected, (short) replacement));
    }

    public static long memoryAtomicLongRmw32AddU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int) value));
    }

    public static long memoryAtomicLongRmw32SubU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAddInt(ptr, (int) (-value)));
    }

    public static long memoryAtomicLongRmw32AndU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicAndInt(ptr, (int) value));
    }

    public static long memoryAtomicLongRmw32OrU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicOrInt(ptr, (int) value));
    }

    public static long memoryAtomicLongRmw32XorU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicXorInt(ptr, (int) value));
    }

    public static long memoryAtomicLongRmw32XchgU(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(memory.atomicXchgInt(ptr, (int) value));
    }

    public static long memoryAtomicLongRmw32CmpxchgU(
            int base, long expected, long replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return Integer.toUnsignedLong(
                memory.atomicCmpxchgInt(ptr, (int) expected, (int) replacement));
    }

    public static long memoryAtomicLongRmwAdd(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddLong(ptr, value);
    }

    public static long memoryAtomicLongRmwSub(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAddLong(ptr, -value);
    }

    public static long memoryAtomicLongRmwAnd(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicAndLong(ptr, value);
    }

    public static long memoryAtomicLongRmwOr(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicOrLong(ptr, value);
    }

    public static long memoryAtomicLongRmwXor(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXorLong(ptr, value);
    }

    public static long memoryAtomicLongRmwXchg(int base, long value, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicXchgLong(ptr, value);
    }

    public static long memoryAtomicLongRmwCmpxchg(
            int base, long expected, long replacement, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicCmpxchgLong(ptr, expected, replacement);
    }

    public static int memoryAtomicWait32(
            int base, int expected, long timeout, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 4 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicWait(ptr, expected, timeout);
    }

    public static int memoryAtomicWait64(
            int base, long expected, long timeout, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        if (ptr % 8 != 0) {
            throw new InvalidException("unaligned atomic");
        }
        return memory.atomicWait(ptr, expected, timeout);
    }

    public static int memoryAtomicNotify(int base, int count, int offset, Memory memory) {
        int ptr = Wat2WasmModuleMachineShaded.getAddr(base, offset);
        return memory.atomicNotify(ptr, count);
    }

    public static void memoryAtomicFence(Memory memory) {
        memory.atomicFence();
    }

    static {
        String prop = System.getProperty("chicory.memCopyWorkaround");
        memCopyWorkaround =
                prop != null ? Boolean.valueOf(prop) : MemCopyWorkaround.shouldUseMemWorkaround();
    }
}
