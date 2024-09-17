package com.dylibso.chicory.runtime;

/**
 * A temporary class that gives us a little more control over the interface.
 * It allows us to assert non-nulls as well as throw stack under and overflow exceptions
 * We should replace with something more idiomatic and performant.
 */
public class MStack {
    private final LongArrayQueue stack;

    public MStack() {
        this.stack = new LongArrayQueue();
    }

    public void push(long v) {
        this.stack.offerLong(v);
    }

    public long pop() {
        return this.stack.pollLong();
    }

    public long peek() {
        return this.stack.peekLong();
    }

    public int size() {
        return this.stack.size();
    }

    @Override
    public String toString() {
        return this.stack.toString();
    }
}
