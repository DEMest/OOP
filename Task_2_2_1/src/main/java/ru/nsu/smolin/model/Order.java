package ru.nsu.smolin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель заказа на пиццу. Хранит идентификатор, текущее состояние,
 * а также кто из пекарей и курьеров обрабатывает заказ.
 *
 * <p>При смене состояния на COOKING или DELIVERING автоматически фиксируется
 * метка времени, доступная через {@link #stageElapsedMs()}.
 */
@Getter
public class Order {
    private final int id;
    private volatile OrderState state = OrderState.PENDING;
    @Setter private Integer bakerId;
    @Setter private Integer courierId;
    private long stageStartMs;

    public Order(int id) {
        this.id = id;
    }

    /**
     * Меняет состояние заказа. При переходе в COOKING или DELIVERING
     * сохраняет текущее время для последующего замера длительности этапа.
     */
    public void setState(OrderState state) {
        this.state = state;
        if (state == OrderState.COOKING || state == OrderState.DELIVERING) {
            this.stageStartMs = System.currentTimeMillis();
        }
    }

    /**
     * Возвращает миллисекунды, прошедшие с начала текущего этапа (COOKING / DELIVERING).
     * Вызывать после завершения этапа, до setState следующего.
     */
    public long stageElapsedMs() {
        return System.currentTimeMillis() - stageStartMs;
    }

    @Override
    public String toString() {
        return state.getColor() + "[" + id + "] " + state.getDisplay() + state.getReset();
    }

    /**
     * Строка с временем выполнения завершённого этапа: {@code [n] Состояние (1234мс)}.
     */
    public String toStringWithTime(long elapsedMs) {
        return state.getColor() + "[" + id + "] " + state.getDisplay()
                + " (" + elapsedMs + "мс)" + state.getReset();
    }
}
