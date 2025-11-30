package ru.nsu.smolin.impl;

import ru.nsu.smolin.Hasher;
import java.util.Objects;

/**
 * Стандартная реализация Hasher: для ключа возвращает его hashCode,
 * а для null — 0. Подходит как хешер по умолчанию.
 * @param <K> тип ключей, для которых вычисляется хеш
 */
public class HasherImpl<K> implements Hasher<K> {

    @Override
    public int hash(K key) {
        return Objects.hashCode(key);
    }
}