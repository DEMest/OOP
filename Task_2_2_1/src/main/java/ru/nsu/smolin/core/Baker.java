package ru.nsu.smolin.core;

import ru.nsu.smolin.model.OrderState;
import ru.nsu.smolin.model.Order;

import java.util.Random;

public class Baker extends Thread {
    private final int id;
    private final long cookingTimeMs;
    private final CustomBlockingQueue<Order> orderQueue;
    private final BoundedBuffer warehouse;
    private volatile boolean running = true;

    public Baker(int id, long cookingTimeMs, CustomBlockingQueue<Order> orderQueue, BoundedBuffer warehouse) {
        this.id = id;
        this.cookingTimeMs = cookingTimeMs;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        Random rand = new Random();
        System.out.println("🍕 Пекарь #" + id + " начал работу (время готовки: " + cookingTimeMs + "мс)");

        while (running || !orderQueue.isEmpty()) {
            try {
                Order order = orderQueue.take();
                order.setBakerId(id);
                order.setState(OrderState.COOKING);
                System.out.println(order);

                Thread.sleep(cookingTimeMs + rand.nextInt(1000)); // случайность

                warehouse.put(order);
                order.setState(OrderState.WAREHOUSED);
                System.out.println(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("🍕 Пекарь #" + id + " завершил работу");
    }

    public void stopGracefully() {
        running = false;
    }
}