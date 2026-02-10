/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.UninstantiableException;
import com.dylibso.chicory.wasm.types.Table;
import com.dylibso.chicory.wasm.types.TableLimits;
import com.dylibso.chicory.wasm.types.ValType;
import java.util.Arrays;

public class TableInstance {
    private final Table table;
    private Instance[] instances;
    private int[] refs;

    public TableInstance(Table table, int initialValue) {
        this.table = table;
        this.instances = new Instance[(int)table.limits().min()];
        this.refs = new int[(int)table.limits().min()];
        Arrays.fill(this.refs, initialValue);
    }

    public int size() {
        return this.refs.length;
    }

    public ValType elementType() {
        return this.table.elementType();
    }

    public TableLimits limits() {
        return this.table.limits();
    }

    public int grow(int size, int value, Instance instance) {
        int oldSize = this.refs.length;
        int targetSize = oldSize + size;
        if (size < 0 || (long)targetSize > this.limits().max()) {
            return -1;
        }
        int[] newRefs = Arrays.copyOf(this.refs, targetSize);
        Arrays.fill(newRefs, oldSize, targetSize, value);
        Object[] newInstances = Arrays.copyOf(this.instances, targetSize);
        Arrays.fill(newInstances, oldSize, targetSize, instance);
        this.refs = newRefs;
        this.instances = newInstances;
        this.table.limits().grow(size);
        return oldSize;
    }

    public int ref(int index) {
        if (index < 0 || index >= this.refs.length) {
            throw new ChicoryException("undefined element");
        }
        return this.refs[index];
    }

    public int requiredRef(int index) {
        int ref = this.ref(index);
        if (ref == -1) {
            throw new ChicoryException("uninitialized element " + index);
        }
        return ref;
    }

    public void setRef(int index, int value, Instance instance) {
        if (index < 0 || index >= this.refs.length || index >= this.instances.length) {
            throw new UninstantiableException("out of bounds table access");
        }
        this.refs[index] = value;
        this.instances[index] = instance;
    }

    public Instance instance(int index) {
        return this.instances[index];
    }

    public void reset() {
        for (int i = 0; i < this.refs.length; ++i) {
            this.refs[i] = -1;
        }
    }
}

