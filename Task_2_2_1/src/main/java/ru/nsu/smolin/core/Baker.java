package ru.nsu.smolin.core;

import ru.nsu.smolin.model.OrderState;
import ru.nsu.smolin.model.Order;

import java.util.Random;

/**
 * Пекарь — рабочий поток, который забирает заказы из общей очереди,
 * имитирует приготовление и кладёт готовый заказ на склад.
 *
 * <p>Завершает работу когда очередь закрыта и пуста ({@code take()} вернул {@code null})
 * либо при прерывании потока (FORCE STOP).
 */
public class Baker extends Thread {
    private final int id;
    private final long cookingTimeMs;
    private final CustomBlockingQueue<Order> orderQueue;
    private final BoundedBuffer warehouse;

    public Baker(int id, long cookingTimeMs, CustomBlockingQueue<Order> orderQueue, BoundedBuffer warehouse) {
        this.id = id;
        this.cookingTimeMs = cookingTimeMs;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        Random rand = new Random();
        System.out.println("Пекарь #" + id + " начал работу (время готовки: " + cookingTimeMs + "мс)");

        while (true) {
            try {
                Order order = orderQueue.take();
                if (order == null) break;

                order.setBakerId(id);
                order.setState(OrderState.COOKING);
                System.out.println(order);

                Thread.sleep(cookingTimeMs + rand.nextInt(1000));
                long elapsed = order.stageElapsedMs();

                warehouse.put(order);
                order.setState(OrderState.WAREHOUSED);
                System.out.println(order.toStringWithTime(elapsed));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Пекарь #" + id + " завершил работу");
    }
}
