package ru.nsu.smolin.core;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.model.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoundedBufferTest {

    @Test
    void putAndTake() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.put(new Order(1));
        assertEquals(1, buffer.getCount());
        List<Order> taken = buffer.takeUpTo(1);
        assertNotNull(taken);
        assertEquals(1, taken.size());
        assertEquals(1, taken.get(0).getId());
        assertEquals(0, buffer.getCount());
    }

    @Test
    void takeUpToRespectsLimit() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(10);
        for (int i = 1; i <= 5; i++) buffer.put(new Order(i));
        List<Order> taken = buffer.takeUpTo(3);
        assertNotNull(taken);
        assertEquals(3, taken.size());
        assertEquals(2, buffer.getCount());
    }

    @Test
    void takeUpToTakesAllWhenLessThanMax() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(10);
        buffer.put(new Order(1));
        buffer.put(new Order(2));
        List<Order> taken = buffer.takeUpTo(5);
        assertNotNull(taken);
        assertEquals(2, taken.size());
        assertEquals(0, buffer.getCount());
    }

    @Test
    void closeEmptyBufferReturnsNull() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.close();
        assertNull(buffer.takeUpTo(3));
    }

    @Test
    void closeAfterPutDrainsBeforeNull() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.put(new Order(1));
        buffer.close();
        List<Order> first = buffer.takeUpTo(5);
        assertNotNull(first);
        assertEquals(1, first.size());
        assertNull(buffer.takeUpTo(5));
    }

    @Test
    void drainAll() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.put(new Order(1));
        buffer.put(new Order(2));
        List<Order> drained = buffer.drainAll();
        assertEquals(2, drained.size());
        assertEquals(0, buffer.getCount());
    }
}
