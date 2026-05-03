package ru.nsu.smolin.core;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.model.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomBlockingQueueTest {

    @Test
    void putAndTake() throws InterruptedException {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(10);
        Order order = new Order(1);
        queue.put(order);
        assertFalse(queue.isEmpty());
        Order taken = queue.take();
        assertNotNull(taken);
        assertEquals(1, taken.getId());
        assertTrue(queue.isEmpty());
    }

    @Test
    void closeEmptyQueueReturnsNull() throws InterruptedException {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(10);
        queue.close();
        assertNull(queue.take());
    }

    @Test
    void closeAfterPutDrainsBeforeNull() throws InterruptedException {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(10);
        queue.put(new Order(1));
        queue.close();
        assertNotNull(queue.take());
        assertNull(queue.take());
    }

    @Test
    void drainAll() throws InterruptedException {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(10);
        queue.put(new Order(1));
        queue.put(new Order(2));
        List<Order> drained = queue.drainAll();
        assertEquals(2, drained.size());
        assertTrue(queue.isEmpty());
    }
}
