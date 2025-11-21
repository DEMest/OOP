package ru.nsu.smolin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Узел односвязного списка в бакете хеш-таблицы.
 * Хранит хеш ключа, сам ключ, значение и ссылку на следующий узел.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
@Getter
@RequiredArgsConstructor
public class Node<K, V> {
    private final int hash;
    private final K key;
    public V value;
    public Node<K, V> next;
}
