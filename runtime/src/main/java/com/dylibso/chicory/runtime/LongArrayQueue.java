package com.dylibso.chicory.runtime;

/*
 * Preserved original copyright:
 * https://github.com/real-logic/agrona/blob/6e15a5c18af85f0d715c8fec06ddcf1e389c8f72/agrona/src/main/java/org/agrona/collections/IntArrayQueue.java
 *
 * Copyright 2014-2024 Real Logic Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// package org.agrona.collections;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Queue of longs which stores the elements without boxing. Null is represented by a special {@link #nullValue()}.
 * <p>
 * The {@link LongIterator} is cached by default to avoid allocation unless directed to do so in the constructor.
 * <p>
 * <b>Note:</b> This class is not threadsafe.
 */
public class LongArrayQueue extends AbstractQueue<Long>
{
    /**
     * Default representation of null for an element.
     */
    public static final long DEFAULT_NULL_VALUE = Long.MIN_VALUE;

    /**
     * Minimum capacity for the queue which must also be a power of 2.
     */
    public static final int MIN_CAPACITY = 8;

    private final boolean shouldAvoidAllocation;
    private int head;
    private int tail;
    private final long nullValue;
    private long[] elements;
    private LongIterator iterator;

    /**
     * Fast method of finding the next power of 2 greater than or equal to the supplied value.
     * <p>
     * If the value is &lt;= 0 then 1 will be returned.
     * <p>
     * This method is not suitable for {@link Integer#MIN_VALUE} or numbers greater than 2^30. When provided
     * then {@link Integer#MIN_VALUE} will be returned.
     * Originally in BitUtil
     *
     * @param value from which to search for next power of 2.
     * @return The next power of 2 or the value itself if it is a power of 2.
     */
    public static int findNextPositivePowerOfTwo(final int value)
    {
        return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(value - 1));
    }

    /**
     * Construct a new queue defaulting to {@link #MIN_CAPACITY} capacity, {@link #DEFAULT_NULL_VALUE}
     * and cached iterators.
     */
    public LongArrayQueue()
    {
        this(MIN_CAPACITY, DEFAULT_NULL_VALUE, true);
    }

    /**
     * Construct a new queue defaulting to {@link #MIN_CAPACITY} capacity and cached iterators.
     *
     * @param nullValue cannot be stored in the queue and used as a sentinel.
     */
    public LongArrayQueue(final long nullValue)
    {
        this(MIN_CAPACITY, nullValue, true);
    }

    /**
     * Construct a new queue default to cached iterators.
     *
     * @param initialCapacity for the queue which will be rounded up to the nearest power of 2.
     * @param nullValue       which cannot be stored in the queue and used as a sentinel.
     */
    public LongArrayQueue(
            final int initialCapacity,
            final long nullValue)
    {
        this(initialCapacity, nullValue, true);
    }

    /**
     * Construct a new queue providing all the config options.
     *
     * @param initialCapacity       for the queue which will be rounded up to the nearest power of 2.
     * @param nullValue             which cannot be stored in the queue and used as a sentinel.
     * @param shouldAvoidAllocation true to cache the iterator otherwise false to allocate a new iterator each time.
     */
    public LongArrayQueue(
            final int initialCapacity,
            final long nullValue,
            final boolean shouldAvoidAllocation)
    {
        this.nullValue = nullValue;
        this.shouldAvoidAllocation = shouldAvoidAllocation;

        if (initialCapacity < MIN_CAPACITY)
        {
            throw new IllegalArgumentException("initial capacity < MIN_INITIAL_CAPACITY : " + initialCapacity);
        }

        final int capacity = findNextPositivePowerOfTwo(initialCapacity);
        if (capacity < MIN_CAPACITY)
        {
            throw new IllegalArgumentException("invalid initial capacity: " + initialCapacity);
        }

        elements = new long[capacity];
        Arrays.fill(elements, nullValue);
    }

    /**
     * The value representing a null element.
     *
     * @return value representing a null element.
     */
    public long nullValue()
    {
        return nullValue;
    }

    /**
     * The current capacity for the collection.
     *
     * @return the current capacity for the collection.
     */
    public int capacity()
    {
        return elements.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return (tail - head) & (elements.length - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return head == tail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        if (head != tail)
        {
            Arrays.fill(elements, nullValue);
            head = 0;
            tail = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean offer(final Long element)
    {
        return offerLong(element);
    }

    /**
     * Offer an element to the tail of the queue without boxing.
     *
     * @param element to be offered to the queue.
     * @return will always be true as long as the underlying array can be expanded.
     */
    public boolean offerLong(final long element)
    {
        if (nullValue == element)
        {
            throw new NullPointerException(); // @DoNotSub
        }

        elements[tail] = element;
        tail = (tail + 1) & (elements.length - 1);

        if (tail == head)
        {
            increaseCapacity();
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(final Long element)
    {
        return offerLong(element);
    }

    /**
     * Offer an element to the tail of the queue without boxing.
     *
     * @param element to be offered to the queue.
     * @return will always be true as long as the underlying array can be expanded.
     */
    public boolean addLong(final long element)
    {
        return offerLong(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long peek()
    {
        final long element = elements[head];

        return element == nullValue ? null : element;
    }

    /**
     * Peek at the element on the head of the queue without boxing.
     *
     * @return the element at the head of the queue without removing it.
     */
    public long peekLong()
    {
        return elements[head];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long poll()
    {
        final long element = pollLong();

        return element == nullValue ? null : element;
    }

    /**
     * Poll the element from the head of the queue without boxing.
     *
     * @return the element at the head of the queue removing it. If empty then {@link #nullValue}.
     */
    public long pollLong()
    {
        final long element = elements[head];
        if (nullValue == element)
        {
            return nullValue;
        }

        elements[head] = nullValue;
        head = (head + 1) & (elements.length - 1);

        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long remove()
    {
        final long element = pollLong();
        if (nullValue == element)
        {
            throw new NoSuchElementException();
        }

        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long element()
    {
        final long element = elements[head];
        if (nullValue == element)
        {
            throw new NoSuchElementException();
        }

        return element;
    }

    /**
     * Peek at the element on the head of the queue without boxing.
     *
     * @return the element at the head of the queue without removing it.
     * @throws NoSuchElementException if the queue is empty.
     */
    public long elementLong()
    {
        final long element = elements[head];
        if (nullValue == element)
        {
            throw new NoSuchElementException();
        }

        return element;
    }

    /**
     * Remove the element at the head of the queue without boxing.
     *
     * @return the element at the head of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public long removeLong()
    {
        final long element = pollLong();
        if (nullValue == element)
        {
            throw new NoSuchElementException();
        }

        return element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = head; i != tail; )
        {
            sb.append(elements[i]).append(", ");
            i = (i + 1) & (elements.length - 1);
        }

        if (sb.length() > 1)
        {
            sb.setLength(sb.length() - 2);
        }

        sb.append(']');

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(final Consumer<? super Long> action)
    {
        for (int i = head; i != tail; )
        {
            action.accept(elements[i]);
            i = (i + 1) & (elements.length - 1);
        }
    }

    /**
     * Iterate over the collection without boxing.
     *
     * @param action to be taken for each element.
     */
    public void forEachLong(final LongConsumer action)
    {
        for (int i = head; i != tail; )
        {
            action.accept(elements[i]);
            i = (i + 1) & (elements.length - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LongIterator iterator()
    {
        LongIterator iterator = this.iterator;
        if (null == iterator)
        {
            iterator = new LongIterator();
            if (shouldAvoidAllocation)
            {
                this.iterator = iterator;
            }
        }

        return iterator.reset();
    }

    private void increaseCapacity()
    {
        final int oldHead = head;
        final int oldCapacity = elements.length;
        final int toEndOfArray = oldCapacity - oldHead;
        final int newCapacity = oldCapacity << 1;

        if (newCapacity < MIN_CAPACITY)
        {
            throw new IllegalStateException("max capacity reached");
        }

        final long[] array = new long[newCapacity];
        Arrays.fill(array, oldCapacity, newCapacity, nullValue);
        System.arraycopy(elements, oldHead, array, 0, toEndOfArray);
        System.arraycopy(elements, 0, array, toEndOfArray, oldHead);

        elements = array;
        head = 0;
        tail = oldCapacity;
    }

    /**
     * Specialised {@link Iterator} from which the value can be retrieved without boxing via {@link #nextValue()}.
     */
    public final class LongIterator implements Iterator<Long>
    {
        private int index;

        /**
         * Create a new instance.
         */
        public LongIterator()
        {
        }

        LongIterator reset()
        {
            index = LongArrayQueue.this.head;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext()
        {
            return index != tail;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Long next()
        {
            return nextValue();
        }

        /**
         * Get the next value from the iterator without boxing.
         *
         * @return the next value from the queue.
         */
        public long nextValue()
        {
            if (index == tail)
            {
                throw new NoSuchElementException();
            }

            final long element = elements[index];
            index = (index + 1) & (elements.length - 1);

            return element;
        }
    }
}
