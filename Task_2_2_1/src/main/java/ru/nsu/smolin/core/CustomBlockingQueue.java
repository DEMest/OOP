package ru.nsu.smolin.core;

import java.util.LinkedList;
import java.util.List;

public class CustomBlockingQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int capacity;

    public CustomBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println("Очередь заказов полная, пекарь ждёт...");
            wait();
        }
        queue.addLast(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.removeFirst();
        notifyAll();
        return item;
    }

    public synchronized List<T> drainAll() {
        List<T> all = new LinkedList<>(queue);
        queue.clear();
        return all;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}