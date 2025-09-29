package ru.nsu.smolin;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Deck class.
 */
final class DeckTest {

    @Test
    void newDeckHas52Cards() {
        Deck d = new Deck();
        assertEquals(52, d.size());
    }

    @Test
    void newDeckContainsOnlyUniqueCards() {
        Deck d = new Deck();
        Set<Card> set = new HashSet<>();
        for (int i = 0; i < d.size(); i++) {
            set.add(d.get(i));
        }
        assertEquals(52, set.size());
    }

    @Test
    void drawReducesDeckSize() {
        Deck d = new Deck();
        d.draw();
        assertEquals(51, d.size());
    }
}
