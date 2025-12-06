package ru.nsu.smolin.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>Узел односвязного списка в бакете хеш-таблицы.
 * Хранит хеш ключа, сам ключ, значение и ссылку на следующий узел.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
@Data
@RequiredArgsConstructor
class Node<K, V> {
    private final int hash;
    private final K key;
    private V value;
    private Node<K, V> next;
}