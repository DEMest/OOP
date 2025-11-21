package ru.nsu.smolin;

import java.util.Iterator;

/**
 * Интерфейс хеш-таблицы.
 * <p>
 * Описывает операции добавления, получения, обновления и удаления пар
 * ключ–значение, а также проверки наличия и обхода элементов.
 *
 * @param <K> Ключи
 * @param <V> Значения
 */
public interface HashTable_<K, V> extends Iterable<MapEntry<K, V>> {

    V put(K key, V value);

    V update(K key, V value);

    V get(K key);

    V remove(K key);

    boolean contains(K key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void clear();

    @Override
    Iterator<MapEntry<K, V>> iterator();
}
