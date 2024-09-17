package com.dylibso.chicory.runtime;

/*
 * Preserved original copyright:
 * https://github.com/real-logic/agrona/blob/6e15a5c18af85f0d715c8fec06ddcf1e389c8f72/agrona/src/test/java/org/agrona/collections/LongArrayQueueTest.java
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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LongArrayQueueTest
{
    @Test
    void shouldDefaultInitialise()
    {
        final LongArrayQueue queue = new LongArrayQueue();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertEquals(LongArrayQueue.MIN_CAPACITY, queue.capacity());
    }

    @Test
    void shouldOfferThenPoll()
    {
        final LongArrayQueue queue = new LongArrayQueue();
        final Long element = 7L;

        assertTrue(queue.offer(element));
        assertEquals(1, queue.size());

        assertEquals(element, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void shouldForEachWithoutBoxing()
    {
        final LongArrayQueue queue = new LongArrayQueue();
        final ArrayList<Long> expected = new ArrayList();

        for (long i = 0; i < 20; i++)
        {
            assertTrue(queue.offerLong(i));
            expected.add(i);
        }

        final ArrayList<Long> actual = new ArrayList();
        queue.forEachLong(actual::add);

        assertEquals(expected, actual);
    }

    @Test
    void shouldClear()
    {
        final LongArrayQueue queue = new LongArrayQueue();

        for (long i = 0; i < 7; i++)
        {
            queue.offerLong(i);
        }
        queue.removeLong();

        queue.clear();
        assertEquals(0, queue.size());
    }

    @Test
    void shouldOfferThenPollWithoutBoxing()
    {
        final LongArrayQueue queue = new LongArrayQueue();
        final int count = 20;

        for (long i = 0; i < count; i++)
        {
            assertTrue(queue.offerLong(i));
        }

        assertFalse(queue.isEmpty());
        assertEquals(count, queue.size());

        for (int i = 0; i < count; i++)
        {
            assertEquals(i, queue.pollLong());
        }

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void shouldPeek()
    {
        final long nullValue = -1L;
        final LongArrayQueue queue = new LongArrayQueue(nullValue);
        assertEquals(nullValue, queue.nullValue());
        assertNull(queue.peek());

        final Long element = 7L;
        assertTrue(queue.offer(element));
        assertTrue(queue.offer(element));
        assertEquals(element, queue.peek());
    }

    @Test
    void shouldPeekWithoutBoxing()
    {
        final long nullValue = -1L;
        final LongArrayQueue queue = new LongArrayQueue(nullValue);
        assertEquals(nullValue, queue.peekLong());

        final long element = 7L;
        assertTrue(queue.offerLong(element));
        assertEquals(element, queue.peekLong());
    }

    @Test
    void shouldIterate()
    {
        final LongArrayQueue queue = new LongArrayQueue();
        final int count = 20;

        for (long i = 0; i < count; i++)
        {
            assertTrue(queue.offerLong(i));
        }

        final LongArrayQueue.LongIterator iterator = queue.iterator();
        for (long i = 0; i < count; i++)
        {
            assertTrue(iterator.hasNext());
            assertEquals(Long.valueOf(i), iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void shouldIterateWithoutBoxing()
    {
        final LongArrayQueue queue = new LongArrayQueue();
        final int count = 20;

        for (long i = 0; i < count; i++)
        {
            assertTrue(queue.offerLong(i));
        }

        final LongArrayQueue.LongIterator iterator = queue.iterator();
        for (int i = 0; i < count; i++)
        {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.nextValue());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void shouldIterateEmptyQueue()
    {
        final LongArrayQueue queue = new LongArrayQueue();

        final LongArrayQueue.LongIterator iteratorOne = queue.iterator();
        assertFalse(iteratorOne.hasNext());

        final int count = 20;
        for (int i = 0; i < count; i++)
        {
            assertTrue(queue.offerLong(i));
            assertEquals(i, queue.removeLong());
        }

        final LongArrayQueue.LongIterator iteratorTwo = queue.iterator();
        assertFalse(iteratorTwo.hasNext());
    }
}
