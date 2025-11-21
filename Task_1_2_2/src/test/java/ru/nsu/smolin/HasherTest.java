package ru.nsu.smolin;

import ru.nsu.smolin.impl.Hasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {

    @Test
    void returnsZeroForNullKey() {
        Hasher<Object> hasher = new Hasher<>();
        assertEquals(0, hasher.hash(null));
    }

    @Test
    void delegatesToHashCodeForNonNullKey() {
        Hasher<String> hasher = new Hasher<>();
        String key = "hello";

        int expected = key.hashCode();
        int actual = hasher.hash(key);

        assertEquals(expected, actual);
    }

    @Test
    void producesSameHashForEqualObjects() {
        Hasher<String> hasher = new Hasher<>();

        String a = "abc";
        String b = "abc";

        assertEquals(a.hashCode(), hasher.hash(a));
        assertEquals(b.hashCode(), hasher.hash(b));
        assertEquals(hasher.hash(a), hasher.hash(b));
    }
}