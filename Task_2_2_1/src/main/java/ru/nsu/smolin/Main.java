package ru.nsu.smolin;

import ru.nsu.smolin.core.CustomBlockingQueue;
import ru.nsu.smolin.core.Baker;
import ru.nsu.smolin.core.Courier;
import ru.nsu.smolin.core.BoundedBuffer;
import ru.nsu.smolin.model.Order;
import ru.nsu.smolin.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private final Config config;
    private final CustomBlockingQueue<Order> orderQueue;
    private final BoundedBuffer warehouse;
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private int nextOrderId = 1;

    public Main(Config config) {
        this.config = config;
        this.orderQueue = new CustomBlockingQueue<>(100);
        this.warehouse = new BoundedBuffer(config.warehouseCapacity);
    }

    public void start() throws Exception {
        System.out.println("🚀 ЗАПУСК ПИЦЦЕРИИ 🚀");
        System.out.println("Время работы: " + config.simulationTimeMs + "мс");
        System.out.println("Склад: " + config.warehouseCapacity + " пицц");
        System.out.println("Режим завершения: " + config.shutdownMode);
        System.out.println();

        // Запуск пекарей
        for (var bakerConfig : config.bakers) {
            Baker baker = new Baker(bakerConfig.id, bakerConfig.cookingTimeMs, orderQueue, warehouse);
            bakers.add(baker);
            baker.start();
        }

        // Запуск курьеров
        for (var courierConfig : config.couriers) {
            Courier courier = new Courier(courierConfig.id, courierConfig.trunkSize, warehouse);
            couriers.add(courier);
            courier.start();
        }

        // Генератор заказов
        Thread generator = new Thread(this::generateOrders);
        generator.setDaemon(true);
        generator.start();

        // Таймер симуляции
        Thread timer = new Thread(() -> {
            try {
                Thread.sleep(config.simulationTimeMs);
                stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        timer.setDaemon(true);
        timer.start();

        // Ждём завершения
        for (Baker baker : bakers) baker.join();
        for (Courier courier : couriers) courier.join();
    }

    private void generateOrders() {
        while (running.get()) {
            try {
                Order order = new Order(nextOrderId++);
                System.out.println(order);
                orderQueue.put(order);
                Thread.sleep(config.orderRateMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void stop() {
        running.set(false);
        if ("GRACEFUL".equals(config.shutdownMode)) {
            System.out.println("\n⏸ GRACEFUL STOP: завершаем текущие заказы...");
            bakers.forEach(Baker::stopGracefully);
            couriers.forEach(Courier::stopGracefully);
        } else {
            System.out.println("\n⛔ FORCE STOP: сериализуем незавершённые...");
            // TODO: сериализация остатков из orderQueue.drainAll() и warehouse
        }
        System.out.println("🏪 ПИЦЦЕРИЯ ОСТАНОВЛЕНА 🏪\n");
    }

    public static void main(String[] args) throws Exception {
        Config config = Config.fromFile("config.json");
        new Main(config).start();
    }
}