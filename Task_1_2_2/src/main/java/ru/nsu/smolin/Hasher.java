package ru.nsu.smolin;

/**
 * Стратегия вычисления хеша для ключей.
 *
 * @param <K> тип ключей, для которых считается хеш
 */
@FunctionalInterface
public interface Hasher<K> {
    /**
     * Возвращает хеш указанного ключа.
     *
     * @param key ключ, для которого нужно посчитать хеш (может быть {@code null})
     * @return целочисленный хеш-код ключа
     */
    int hash(K key);
}