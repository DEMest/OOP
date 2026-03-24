package ru.nsu.smolin.core;

import ru.nsu.smolin.model.Order;
import ru.nsu.smolin.model.OrderState;

import java.util.List;
import java.util.Random;

/**
 * Курьер — рабочий поток, который забирает готовые пиццы со склада
 * (не более объёма багажника) и имитирует доставку заказчикам.
 *
 * <p>Завершает работу когда склад закрыт и пуст ({@code takeUpTo()} вернул {@code null})
 * либо при прерывании потока (FORCE STOP).
 */
public class Courier extends Thread {
    private final int id;
    private final int trunkSize;
    private final BoundedBuffer warehouse;

    public Courier(int id, int trunkSize, BoundedBuffer warehouse) {
        this.id = id;
        this.trunkSize = trunkSize;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        Random rand = new Random();
        System.out.println("Курьер #" + id + " начал работу (багажник: " + trunkSize + ")");

        while (true) {
            try {
                List<Order> orders = warehouse.takeUpTo(trunkSize);
                if (orders == null) break;

                orders.forEach(order -> order.setCourierId(id));
                orders.forEach(order -> {
                    order.setState(OrderState.DELIVERING);
                    System.out.println(order);
                });

                Thread.sleep(2000 + rand.nextInt(3000));

                orders.forEach(order -> {
                    long elapsed = order.stageElapsedMs();
                    order.setState(OrderState.DELIVERED);
                    System.out.println(order.toStringWithTime(elapsed));
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Курьер #" + id + " завершил работу");
    }
}
