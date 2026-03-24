package ru.nsu.smolin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void initialState() {
        Order order = new Order(1);
        assertEquals(OrderState.PENDING, order.getState());
        assertEquals(1, order.getId());
        assertNull(order.getBakerId());
        assertNull(order.getCourierId());
    }

    @Test
    void stateTransitions() {
        Order order = new Order(1);
        order.setState(OrderState.COOKING);
        assertEquals(OrderState.COOKING, order.getState());
        order.setState(OrderState.WAREHOUSED);
        assertEquals(OrderState.WAREHOUSED, order.getState());
        order.setState(OrderState.DELIVERING);
        assertEquals(OrderState.DELIVERING, order.getState());
        order.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void bakerAndCourierAssignment() {
        Order order = new Order(5);
        order.setBakerId(2);
        order.setCourierId(3);
        assertEquals(2, order.getBakerId());
        assertEquals(3, order.getCourierId());
    }

    @Test
    void stageElapsedMsAfterCooking() throws InterruptedException {
        Order order = new Order(1);
        order.setState(OrderState.COOKING);
        Thread.sleep(50);
        assertTrue(order.stageElapsedMs() >= 50);
    }

    @Test
    void toStringWithTimeContainsIdAndState() {
        Order order = new Order(7);
        order.setState(OrderState.WAREHOUSED);
        String s = order.toStringWithTime(1234);
        assertTrue(s.contains("[7]"));
        assertTrue(s.contains("1234мс"));
    }
}
