package ru.nsu.smolin;

/**
 * Пара "ключ–значение", которую возвращает хеш-таблица и её итераторы.
 *
 * @param key ключ записи
 * @param value значение, связанное с ключом
 * @param <K> тип ключа
 * @param <V> ип значения
 */
public record HashTableEntry<K, V>(K key, V value) {
}
