package ru.nsu.smolin.core;

import ru.nsu.smolin.model.Order;

import java.util.ArrayList;
import java.util.List;

public class BoundedBuffer {
    private final Order[] buffer;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    private final int capacity;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Order[capacity];
    }

    public synchronized void put(Order order) throws InterruptedException {
        while (count == capacity) {
            System.out.println("Склад полон (" + capacity + "/" + capacity + "), пекарь ждёт...");
            wait();
        }
        buffer[putIndex] = order;
        putIndex = (putIndex + 1) % capacity;
        count++;
        notifyAll();
    }

    public synchronized List<Order> takeUpTo(int max) throws InterruptedException {
        while (count == 0) {
            System.out.println("Склад пуст, курьер ждёт...");
            wait();
        }
        List<Order> taken = new ArrayList<>(Math.min(max, count));
        int takenCount = 0;
        while (takenCount < max && count > 0) {
            Order order = buffer[takeIndex];
            buffer[takeIndex] = null;
            takeIndex = (takeIndex + 1) % capacity;
            count--;
            taken.add(order);
            takenCount++;
        }
        notifyAll();
        return taken;
    }

    public synchronized int getCount() { return count; }
    public synchronized List<Integer> getOrderIds() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (buffer[i] != null) ids.add(buffer[i].getId());
        }
        return ids;
    }
}