/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

public class MStack {
    public static final int MIN_CAPACITY = 8;
    private int count;
    private long[] elements = new long[8];

    private void increaseCapacity() {
        int newCapacity = this.elements.length << 1;
        long[] array = new long[newCapacity];
        System.arraycopy(this.elements, 0, array, 0, this.elements.length);
        this.elements = array;
    }

    public long[] array() {
        return this.elements;
    }

    public void push(long v) {
        this.elements[this.count] = v;
        ++this.count;
        if (this.count == this.elements.length) {
            this.increaseCapacity();
        }
    }

    public long pop() {
        --this.count;
        return this.elements[this.count];
    }

    public long peek() {
        return this.elements[this.count - 1];
    }

    public int size() {
        return this.count;
    }
}

