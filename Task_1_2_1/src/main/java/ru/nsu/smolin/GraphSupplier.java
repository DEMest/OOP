package ru.nsu.smolin;

import ru.nsu.smolin.Graph;

/**
 * Функциональный интефрейс для выбора реализации ссылкой или лямбда-функцией.
 *
 */
@FunctionalInterface
public interface GraphSupplier {
    Graph get();
}