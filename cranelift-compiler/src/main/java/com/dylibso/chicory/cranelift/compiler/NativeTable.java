package com.dylibso.chicory.cranelift.compiler;

import static com.dylibso.chicory.wasm.types.Value.REF_NULL_VALUE;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.TableInstance;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * Off-heap table implementation for native compilation.
 *
 * <p>Layout: [size:i32 @ 0][max:i32 @ 4][refs:i32... @ 8]
 *
 * <p>The refs array is pre-allocated to the table's max capacity so that
 * {@code TABLE.GROW} only bumps the size field without reallocation.
 * Native code reads/writes this buffer directly — no Java trampoline
 * needed for GET/SET/SIZE/GROW/FILL/COPY.
 *
 * <p>Unused slots are filled with {@link com.dylibso.chicory.wasm.types.Value#REF_NULL_VALUE}
 * so that TABLE.GET on uninitialized elements returns the correct sentinel.
 */
final class NativeTable extends TableInstance {

    private static final int MAX_PREALLOC = 1_000_000;

    private final MemorySegment buffer;
    private final int capacity;

    NativeTable(Table table, Arena arena) {
        super(table, REF_NULL_VALUE);
        int initial = (int) table.limits().min();
        int max = (int) table.limits().max();
        // Pre-allocate to max, capped at MAX_PREALLOC
        this.capacity = (max > 0 && max <= MAX_PREALLOC) ? max : Math.max(initial, MAX_PREALLOC);
        long bufferSize = CtxBuffer.TABLE_REFS_OFFSET + (long) capacity * 4;
        this.buffer = arena.allocate(bufferSize, 8);

        // Write header
        buffer.set(ValueLayout.JAVA_INT, CtxBuffer.TABLE_SIZE_OFFSET, initial);
        buffer.set(ValueLayout.JAVA_INT, CtxBuffer.TABLE_MAX_OFFSET, max > 0 ? max : capacity);

        // Fill all refs with REF_NULL_VALUE
        for (int i = 0; i < capacity; i++) {
            buffer.set(
                    ValueLayout.JAVA_INT,
                    CtxBuffer.TABLE_REFS_OFFSET + (long) i * 4,
                    REF_NULL_VALUE);
        }
    }

    /** Get the native address of the table buffer, for passing to native code. */
    MemorySegment nativeBuffer() {
        return buffer;
    }

    @Override
    public int size() {
        return buffer.get(ValueLayout.JAVA_INT, CtxBuffer.TABLE_SIZE_OFFSET);
    }

    @Override
    public ValType elementType() {
        return super.elementType();
    }

    @Override
    public TableLimits limits() {
        return super.limits();
    }

    @Override
    public int ref(int index) {
        if (index < 0 || index >= size()) {
            throw new ChicoryException("undefined element");
        }
        return buffer.get(ValueLayout.JAVA_INT, CtxBuffer.TABLE_REFS_OFFSET + (long) index * 4);
    }

    @Override
    public int requiredRef(int index) {
        int r = ref(index);
        if (r == REF_NULL_VALUE) {
            throw new ChicoryException("uninitialized element " + index);
        }
        return r;
    }

    @Override
    public void setRef(int index, int value, Instance instance) {
        if (index < 0 || index >= size()) {
            throw new ChicoryException("out of bounds table access");
        }
        buffer.set(ValueLayout.JAVA_INT, CtxBuffer.TABLE_REFS_OFFSET + (long) index * 4, value);
    }

    @Override
    public int grow(int delta, int value, Instance instance) {
        int oldSize = size();
        int newSize = oldSize + delta;
        int max = buffer.get(ValueLayout.JAVA_INT, CtxBuffer.TABLE_MAX_OFFSET);
        if (delta < 0 || newSize > max || newSize > capacity) {
            return -1;
        }
        // Fill new slots
        for (int i = oldSize; i < newSize; i++) {
            buffer.set(ValueLayout.JAVA_INT, CtxBuffer.TABLE_REFS_OFFSET + (long) i * 4, value);
        }
        // Update size
        buffer.set(ValueLayout.JAVA_INT, CtxBuffer.TABLE_SIZE_OFFSET, newSize);
        limits().grow(delta);
        return oldSize;
    }

    @Override
    public Instance instance(int index) {
        // Single-module assumption — all entries belong to the same instance
        return null;
    }

    @Override
    public void reset() {
        int sz = size();
        for (int i = 0; i < sz; i++) {
            buffer.set(
                    ValueLayout.JAVA_INT,
                    CtxBuffer.TABLE_REFS_OFFSET + (long) i * 4,
                    REF_NULL_VALUE);
        }
    }
}
