package ru.nsu.smolin.core;

import ru.nsu.smolin.model.Order;
import ru.nsu.smolin.model.OrderState;

import java.util.List;
import java.util.Random;

public class Courier extends Thread {
    private final int id;
    private final int trunkSize;
    private final BoundedBuffer warehouse;
    private volatile boolean running = true;

    public Courier(int id, int trunkSize, BoundedBuffer warehouse) {
        this.id = id;
        this.trunkSize = trunkSize;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        Random rand = new Random();
        System.out.println("🚗 Курьер #" + id + " начал работу (багажник: " + trunkSize + ")");

        while (running) {
            try {
                List<Order> orders = warehouse.takeUpTo(trunkSize);
                if (!orders.isEmpty()) {
                    orders.forEach(order -> order.setCourierId(id));
                    orders.forEach(order -> {
                        order.setState(OrderState.DELIVERING);
                        System.out.println(order);
                    });

                    // Время доставки
                    Thread.sleep(2000 + rand.nextInt(3000));

                    orders.forEach(order -> {
                        order.setState(OrderState.DELIVERED);
                        System.out.println(order);
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("🚗 Курьер #" + id + " завершил работу");
    }

    public void stopGracefully() {
        running = false;
    }
}
