package com.dylibso.chicory.runtime;

/**
 * Runtime representation of a WasmGC array instance.
 * Elements are stored as raw long values (same encoding as stack values).
 * Packed types (i8, i16) are stored as full long slots for simplicity.
 * A parallel Object[] holds strong Java references for ref-typed elements,
 * allowing the JVM GC to trace inter-object reference chains.
 */
public final class WasmArray implements WasmGcRef {
    private final int typeIdx;
    private final long[] elements;
    private final Object[] refElements;

    public WasmArray(int typeIdx, long[] elements) {
        this(typeIdx, elements, new Object[elements.length]);
    }

    public WasmArray(int typeIdx, long[] elements, Object[] refElements) {
        this.typeIdx = typeIdx;
        this.elements = elements;
        this.refElements = refElements;
    }

    @Override
    public int typeIdx() {
        return typeIdx;
    }

    public long get(int idx) {
        return elements[idx];
    }

    public Object getRef(int idx) {
        return refElements[idx];
    }

    public void set(int idx, long value) {
        elements[idx] = value;
        refElements[idx] = null;
    }

    public void set(int idx, long value, Object ref) {
        elements[idx] = value;
        refElements[idx] = ref;
    }

    public int length() {
        return elements.length;
    }

    public long[] elements() {
        return elements;
    }
}
