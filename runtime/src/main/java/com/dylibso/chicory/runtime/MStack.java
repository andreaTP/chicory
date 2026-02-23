package com.dylibso.chicory.runtime;

public class MStack {
    public static final int MIN_CAPACITY = 8;

    private int count;
    private long[] elements;
    private Object[] refElements;

    public MStack() {
        this.elements = new long[MIN_CAPACITY];
        this.refElements = new Object[MIN_CAPACITY];
    }

    private void increaseCapacity() {
        final int newCapacity = elements.length << 1;

        final long[] array = new long[newCapacity];
        System.arraycopy(elements, 0, array, 0, elements.length);
        elements = array;

        final Object[] refArray = new Object[newCapacity];
        System.arraycopy(refElements, 0, refArray, 0, refElements.length);
        refElements = refArray;
    }

    // internal use only!
    public long[] array() {
        return elements;
    }

    public void push(long v) {
        refElements[count] = null;
        elements[count] = v;
        count++;

        if (count == elements.length) {
            increaseCapacity();
        }
    }

    public void pushRef(long v, Object ref) {
        refElements[count] = ref;
        elements[count] = v;
        count++;

        if (count == elements.length) {
            increaseCapacity();
        }
    }

    public long pop() {
        count--;
        refElements[count] = null;
        return elements[count];
    }

    public long peek() {
        return elements[count - 1];
    }

    public Object peekRef() {
        return refElements[count - 1];
    }

    public int size() {
        return count;
    }
}
