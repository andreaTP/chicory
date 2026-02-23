package com.dylibso.chicory.runtime;

/**
 * Runtime representation of a WasmGC struct instance.
 * Fields are stored as raw long values (same encoding as stack values).
 * A parallel Object[] holds strong Java references for ref-typed fields,
 * allowing the JVM GC to trace inter-object reference chains.
 */
public final class WasmStruct implements WasmGcRef {
    private final int typeIdx;
    private final long[] fields;
    private final Object[] refFields;

    public WasmStruct(int typeIdx, long[] fields) {
        this(typeIdx, fields, new Object[fields.length]);
    }

    public WasmStruct(int typeIdx, long[] fields, Object[] refFields) {
        this.typeIdx = typeIdx;
        this.fields = fields;
        this.refFields = refFields;
    }

    @Override
    public int typeIdx() {
        return typeIdx;
    }

    public long field(int idx) {
        return fields[idx];
    }

    public Object fieldRef(int idx) {
        return refFields[idx];
    }

    public void setField(int idx, long value) {
        fields[idx] = value;
        refFields[idx] = null;
    }

    public void setField(int idx, long value, Object ref) {
        fields[idx] = value;
        refFields[idx] = ref;
    }

    public int fieldCount() {
        return fields.length;
    }
}
