package ru.nsu.smolin.model;

import lombok.Getter;

@Getter
public enum OrderState {
    PENDING("⏳  Ожидание"),
    COOKING("🔥  Готовится"),
    WAREHOUSED("📦  На складе"),
    DELIVERING("🚗  Доставка"),
    DELIVERED("✅  Доставлен"),
    SERIALIZED("💾  Сохранён");

    private final String display;
    OrderState(String display) { this.display = display; }
}
