package ru.nsu.smolin.model;

/**
 * Жизненный цикл заказа с текстовым описанием и ANSI-цветом для терминала.
 *
 * <p>Порядок переходов: PENDING → COOKING → WAREHOUSED → DELIVERING → DELIVERED.
 * SERIALIZED — конечное состояние при FORCE STOP.
 */
public enum OrderState {
    PENDING   ("Ожидание", "\u001B[37m"),
    COOKING   ("Готовится", "\u001B[33m"),
    WAREHOUSED("На складе", "\u001B[34m"),
    DELIVERING("В доставке", "\u001B[36m"),
    DELIVERED ("Доставлен", "\u001B[32m"),
    SERIALIZED("Сохранен", "\u001B[35m");

    static final String RESET = "\u001B[0m";

    private final String display;
    private final String color;

    OrderState(String display, String color) {
        this.display = display;
        this.color = color;
    }

    public String getDisplay() {
        return display;
    }
    public String getColor() {
        return color;
    }
    public String getReset() {
        return RESET;
    }
}
