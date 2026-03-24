package ru.nsu.smolin.integration;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.core.BoundedBuffer;
import ru.nsu.smolin.core.CustomBlockingQueue;
import ru.nsu.smolin.model.Order;
import ru.nsu.smolin.model.OrderState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FullFlowTest {

    @Test
    void orderPassesAllStates() throws InterruptedException {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(10);
        BoundedBuffer warehouse = new BoundedBuffer(5);

        Order order = new Order(1);
        assertEquals(OrderState.PENDING, order.getState());

        queue.put(order);
        Order fromQueue = queue.take();
        assertNotNull(fromQueue);
        fromQueue.setBakerId(1);
        fromQueue.setState(OrderState.COOKING);
        assertEquals(OrderState.COOKING, fromQueue.getState());

        warehouse.put(fromQueue);
        fromQueue.setState(OrderState.WAREHOUSED);
        assertEquals(OrderState.WAREHOUSED, fromQueue.getState());

        List<Order> batch = warehouse.takeUpTo(1);
        assertNotNull(batch);
        Order delivering = batch.get(0);
        delivering.setCourierId(1);
        delivering.setState(OrderState.DELIVERING);
        assertEquals(OrderState.DELIVERING, delivering.getState());

        delivering.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, delivering.getState());
    }
}
