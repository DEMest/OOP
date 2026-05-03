package ru.nsu.smolin.core;

import java.util.LinkedList;
import java.util.List;

/**
 * Потокобезопасная блокирующая очередь ограниченной ёмкости.
 *
 * <p>Реализована на {@code synchronized} / {@code wait} / {@code notifyAll}
 * без классов из {@code java.util.concurrent}.
 *
 * <p>Поддерживает мягкое завершение через {@link #close()}: после закрытия
 * {@link #take()} возвращает {@code null} когда очередь опустеет.
 *
 * @param <T> тип элементов очереди
 */
public class CustomBlockingQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int capacity;
    private volatile boolean closed = false;

    public CustomBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Добавляет элемент в очередь. Блокируется если очередь заполнена.
     */
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.addLast(item);
        notifyAll();
    }

    /**
     * Извлекает первый элемент. Блокируется если очередь пуста и не закрыта.
     *
     * @return следующий элемент, или {@code null} если очередь закрыта и пуста
     */
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }
        if (queue.isEmpty()) return null;
        T item = queue.removeFirst();
        notifyAll();
        return item;
    }

    /**
     * Закрывает очередь. Ожидающие в {@link #take()} потоки будут разбужены
     * и получат {@code null} когда очередь опустеет.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Извлекает все элементы атомарно. Используется при FORCE STOP.
     */
    public synchronized List<T> drainAll() {
        List<T> all = new LinkedList<>(queue);
        queue.clear();
        notifyAll();
        return all;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
