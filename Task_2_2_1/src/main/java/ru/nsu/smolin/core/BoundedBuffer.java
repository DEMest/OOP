package ru.nsu.smolin.core;

import ru.nsu.smolin.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Склад готовой продукции — потокобезопасный кольцевой буфер ограниченной ёмкости.
 */
public class BoundedBuffer {
    private final Order[] buffer;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    private final int capacity;
    private volatile boolean closed = false;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Order[capacity];
    }

    /**
     * Кладёт пиццу на склад. Блокируется если склад полностью заполнен.
     */
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

    /**
     * Забирает до {@code max} пицц со склада. Блокируется если склад пуст и не закрыт.
     *
     * @return список заказов, или {@code null} если склад закрыт и пуст
     */
    public synchronized List<Order> takeUpTo(int max) throws InterruptedException {
        while (count == 0 && !closed) {
            System.out.println("Склад пуст, курьер ждёт...");
            wait();
        }
        if (count == 0) return null;
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

    /**
     * Закрывает склад. Ожидающие в {@link #takeUpTo(int)} потоки будут разбужены
     * и получат {@code null} когда склад опустеет.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Извлекает все заказы атомарно. Используется при FORCE STOP.
     */
    public synchronized List<Order> drainAll() {
        List<Order> all = new ArrayList<>();
        while (count > 0) {
            Order order = buffer[takeIndex];
            buffer[takeIndex] = null;
            takeIndex = (takeIndex + 1) % capacity;
            count--;
            all.add(order);
        }
        notifyAll();
        return all;
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
