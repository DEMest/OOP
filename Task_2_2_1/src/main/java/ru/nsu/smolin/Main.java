package ru.nsu.smolin;

import ru.nsu.smolin.core.CustomBlockingQueue;
import ru.nsu.smolin.core.Baker;
import ru.nsu.smolin.core.Courier;
import ru.nsu.smolin.core.BoundedBuffer;
import ru.nsu.smolin.model.Order;
import ru.nsu.smolin.model.OrderState;
import ru.nsu.smolin.config.Config;

import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Точка входа и координатор работы пиццерии.
 *
 * <p>Запускает потоки пекарей и курьеров, генератор заказов и таймер симуляции.
 * Поток данных: генератор → {@code orderQueue} → {@link Baker} → {@code warehouse}
 * → {@link Courier} → доставлен.
 */
public class Main {
    private final Config config;
    private final CustomBlockingQueue<Order> orderQueue = new CustomBlockingQueue<>(100);
    private final BoundedBuffer warehouse = new BoundedBuffer(config.warehouseCapacity);
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private int nextOrderId = 1;

    public Main(Config config) {
        this.config = config;
    }

    /**
     * Запускает пиццерию и блокируется до полного завершения всех потоков.
     */
    public void start() throws Exception {
        System.out.println("ЗАПУСК ПИЦЦЕРИИ");
        System.out.println("Время работы: " + config.simulationTimeMs + "мс");
        System.out.println("Склад: " + config.warehouseCapacity + " пицц");
        System.out.println("Режим завершения: " + config.shutdownMode);
        System.out.println();

        for (var bakerConfig : config.bakers) {
            Baker baker = new Baker(bakerConfig.id, bakerConfig.cookingTimeMs, orderQueue, warehouse);
            bakers.add(baker);
            baker.start();
        }

        for (var courierConfig : config.couriers) {
            Courier courier = new Courier(courierConfig.id, courierConfig.trunkSize, warehouse);
            couriers.add(courier);
            courier.start();
        }

        Thread generator = new Thread(this::generateOrders);
        generator.setDaemon(true);
        generator.start();

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

        for (Baker baker : bakers) baker.join();

        if ("GRACEFUL".equals(config.shutdownMode)) {
            warehouse.close();
        }

        for (Courier courier : couriers) courier.join();

        if (!"GRACEFUL".equals(config.shutdownMode)) {
            serializeRemaining();
        }

        System.out.println("\nПИЦЦЕРИЯ ЗАВЕРШИЛА РАБОТУ");
    }

    /*
     * Генерирует новые заказы с интервалом orderRateMs пока running=true.
     */
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

    /*
     * GRACEFUL — закрывает очередь; пекари допекают все заказы,
     *            затем склад закрывается и курьеры доставляют остаток.
     * FORCE    — прерывает все потоки; незавершённые заказы сериализуются в JSON.
     */
    private void stop() {
        running.set(false);
        if ("GRACEFUL".equals(config.shutdownMode)) {
            System.out.println("\nGRACEFUL STOP: завершаем текущие заказы...");
            orderQueue.close();
        } else {
            System.out.println("\nFORCE STOP: прерываем работу, сериализуем незавершённые...");
            bakers.forEach(Thread::interrupt);
            couriers.forEach(Thread::interrupt);
            orderQueue.close();
            warehouse.close();
        }
    }

    /*
     * Собирает остатки из очереди и склада, помечает SERIALIZED, пишет в JSON.
     * Вызывается только при FORCE STOP после завершения всех потоков.
     */
    private void serializeRemaining() {
        List<Order> remaining = new ArrayList<>();
        remaining.addAll(orderQueue.drainAll());
        remaining.addAll(warehouse.drainAll());

        remaining.forEach(order -> {
            order.setState(OrderState.SERIALIZED);
            System.out.println(order);
        });

        if (remaining.isEmpty()) return;

        try (FileWriter writer = new FileWriter("serialized_orders.json")) {
            new GsonBuilder().setPrettyPrinting().create().toJson(remaining, writer);
            System.out.println("Сохранено заказов: " + remaining.size() + " -> serialized_orders.json");
        } catch (IOException e) {
            System.err.println("Ошибка сериализации: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Config config = Config.load();
        new Main(config).start();
    }
}
