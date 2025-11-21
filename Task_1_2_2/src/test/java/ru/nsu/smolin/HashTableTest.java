package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.HashTable;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void putGetUpdateRemove() {
        HashTable<String, Integer> table = new HashTable<>();

        assertEquals(0, table.size());
        assertFalse(table.contains("one"));

        assertNull(table.put("one", 1));
        assertEquals(1, table.size());
        assertTrue(table.contains("one"));
        assertEquals(1, table.get("one"));

        assertEquals(1, table.update("one", 2));
        assertEquals(2, table.get("one"));
        assertEquals(1, table.size());

        assertEquals(2, table.remove("one"));
        assertEquals(0, table.size());
        assertNull(table.get("one"));
        assertFalse(table.contains("one"));
    }

    @Test
    void resizeOnThreshold() {
        HashTable<Integer, Integer> table = new HashTable<>(4);

        int n = 100;
        for (int i = 0; i < n; i++) {
            table.put(i, i);
        }

        assertEquals(n, table.size());
        for (int i = 0; i < n; i++) {
            assertEquals(i, table.get(i));
        }
    }

    @Test
    void iteratorTraversesAllEntries() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        int sum = 0;
        for (var e : table) {
            sum += e.value();
            assertTrue(table.contains(e.key()));
        }
        assertEquals(1 + 2 + 3, sum);
    }
}