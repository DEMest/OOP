package ru.nsu.smolin.impl;

import ru.nsu.smolin.HashTable_;
import ru.nsu.smolin.MapEntry;
import ru.nsu.smolin.Node;


import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Реализация хеш‑таблицы с separate chaining.
 * <p>
 * Хранит пары ключ–значение во внутреннем массиве бакетов {@code table},
 * где каждый бакет представляет собой односвязный список узлов {@link Node}.
 */
@SuppressWarnings("unchecked")
public class HashTable<K, V> implements HashTable_<K, V> {

    private static final int INIT_CAP = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private final ru.nsu.smolin.Hasher<? super K> hasher;
    private int modCount;

    public HashTable() {
        this.loadFactor = LOAD_FACTOR;
        this.hasher = new Hasher<>();
        int capacity = INIT_CAP;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.threshold = (int) (capacity * loadFactor);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, LOAD_FACTOR, new Hasher<>());
    }

    public HashTable(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, new Hasher<>());
    }

    public HashTable(int initialCapacity, float loadFactor, ru.nsu.smolin.Hasher<? super K> hasher) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.hasher = Objects.requireNonNull(hasher);

        int capacity = tableSizeFor(initialCapacity);
        this.table = (Node<K, V>[]) new Node[capacity];
        this.threshold = (int) (capacity * loadFactor);
    }

    /**
     * Вычисляет ближайшую степень двойки, не меньшую заданной ёмкости.
     * <p>
     * Используется для выбора размера массива бакетов {@code table},
     * чтобы можно было быстро считать индекс по маске {@code hash & (length - 1)}.
     *
     * @param cap желаемая минимальная ёмкость
     * @return размер массива — степень двойки, не меньше {@code cap}
     */
    private int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= (1 << 30)) ? (1 << 30) : n + 1;
    }

    /**
     * Считает внутренний хеш для ключа.
     * Берёт хеш из Hasher и перемешивает биты,
     * чтобы ключи равномернее распределялись по бакетам.
     *
     * @param key ключ, для которого нужно получить хеш
     * @return перемешанный целочисленный хеш
     */
    private int hash(Object key) {
        K k = (K) key;
        int h = hasher.hash(k);
        return h ^ (h >>> 16);
    }

    /**
     * Преобразует хеш в индекс бакета во внутреннем массиве.
     * <p>
     * Предполагается, что длина массива {@code length} — степень двойки,
     * поэтому операция {@code hash & (length - 1)} эквивалентна
     * {@code hash % length}, но работает быстрее.
     *
     * @param hash перемешанный хеш
     * @param length длина массива бакетов
     * @return индекс бакета
     */
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public V put(K key, V value) {
        return putInternal(key, value, true);
    }

    @Override
    public V update(K key, V value) {
        return putInternal(key, value, false);
    }

    /**
     * putInternal: если allowCreate == true, создаёт новую запись при отсутствии ключа,
     * иначе только обновляет существующую.
     */
    private V putInternal(K key, V value, boolean allowCreate) {
        int hash = hash(key);
        Node<K, V>[] tab = table;
        int index = indexFor(hash, tab.length);

        Node<K, V> node = tab[index];
        for (Node<K, V> e = node; e != null; e = e.getNext()) {
            if (e.getHash() == hash && Objects.equals(e.getKey(), key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        if (!allowCreate) {
            throw new NoSuchElementException("Key not found: " + key);
        }

        Node<K, V> newNode = new Node<>(hash, key);
        newNode.value = value;
        newNode.next = node;
        tab[index] = newNode;
        size++;
        modCount++;

        if (size > threshold) {
            resize();
        }

        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean contains(K key) {
        return findNode(key) != null;
    }

    private Node<K, V> findNode(K key) {
        int hash = hash(key);
        Node<K, V>[] tab = table;
        int index = indexFor(hash, tab.length);

        for (Node<K, V> e = tab[index]; e != null; e = e.getNext()) {
            if (e.getHash() == hash && Objects.equals(e.getKey(), key)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int hash = hash(key);
        Node<K, V>[] tab = table;
        int index = indexFor(hash, tab.length);

        Node<K, V> prev = null;
        Node<K, V> e = tab[index];

        while (e != null) {
            Node<K, V> next = e.getNext();
            if (e.getHash() == hash && Objects.equals(e.getKey(), key)) {
                if (prev == null) {
                    tab[index] = next;
                } else {
                    prev.next = next;
                }
                size--;
                modCount++;
                return e.value;
            }
            prev = e;
            e = next;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }
        Arrays.fill(table, null);
        size = 0;
        modCount++;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = oldCap << 1;
        if (newCap <= 0) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];

        for (Node<K, V> e : oldTab) {
            while (e != null) {
                Node<K, V> next = e.getNext();
                int index = indexFor(e.getHash(), newCap);
                e.next = newTab[index];
                newTab[index] = e;
                e = next;
            }
        }

        table = newTab;
        threshold = (int) (newCap * loadFactor);
        modCount++;
    }

    @Override
    public Iterator<MapEntry<K, V>> iterator() {
        return new EntryIterator();
    }

    /**
     * Итератор по элементам хеш-таблицы.
     * Обходит все бакеты и связанные списки узлов и работает по принципу fail‑fast:
     * при структурном изменении таблицы во время обхода кидает ConcurrentModificationException.
     */
    private final class EntryIterator implements Iterator<MapEntry<K, V>> {

        private int expectedModCount = modCount;
        private int index;
        private Node<K, V> current;
        private Node<K, V> next;

        /**
         * Находит первый непустой бакет и ставит указатель next на его первый узел.
         */
        EntryIterator() {
            Node<K, V>[] tab = table;
            int i = 0;
            Node<K, V> n = null;
            if (size > 0) {
                while (i < tab.length && (n = tab[i]) == null) {
                    i++;
                }
            }
            this.index = i;
            this.next = n;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Возвращает следующую пару ключ–значение в таблице.
         * Если элементов больше нет, кидает NoSuchElementException.
         *
         * @return следующая MapEntry в порядке обхода
         */
        @Override
        public MapEntry<K, V> next() {
            checkForModification();
            Node<K, V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }

            Node<K, V>[] tab = table;
            Node<K, V> n = e.getNext();
            int i = index;

            while (n == null && ++i < tab.length) {
                n = tab[i];
            }

            index = i;
            next = n;
            current = e;

            return new MapEntry<>(e.getKey(), e.value);
        }

        /**
         * Проверяет, не была ли таблица структурно изменена после создания итератора.
         * Если modCount изменился — бросаем ConcurrentModificationException (fail‑fast).
         */
        private void checkForModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            checkForModification();
            HashTable.this.remove(current.getKey());
            current = null;
            expectedModCount = modCount;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Iterator<MapEntry<K, V>> it = iterator();
        while (it.hasNext()) {
            MapEntry<K, V> e = it.next();
            sb.append(e.key()).append('=').append(e.value());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTable<?, ?> other)) return false;
        if (this.size != other.size) return false;

        HashTable<K, V> that = (HashTable<K, V>) other;

        for (MapEntry<K, V> entry : this) {
            V otherVal = that.get(entry.key());
            if (!Objects.equals(entry.value(), otherVal)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (MapEntry<K, V> entry : this) {
            h += Objects.hashCode(entry.key()) ^ Objects.hashCode(entry.value());
        }
        return h;
    }
}

