package ru.nsu.smolin.impl;

/**
 * Стандартная реализация Hasher: для ключа возвращает его hashCode,
 * а для null — 0. Подходит как хешер по умолчанию.
 * @param <K> тип ключей, для которых вычисляется хеш
 */
public class Hasher<K> implements ru.nsu.smolin.Hasher<K> {

    @Override
    public int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }
}