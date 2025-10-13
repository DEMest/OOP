package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.model.Card;
import ru.nsu.smolin.model.Deck;


/**
 * Tests for Deck class.
 */
final class DeckTest {

    @Test
    void newDeckHas52Cards() {
        Deck d = Deck.create();
        assertEquals(52, d.size());
    }

    @Test
    void newDeckContainsOnlyUniqueCards() {
        Deck d = Deck.create();
        Set<Card> set = new HashSet<>();
        for (int i = 0; i < d.size(); i++) {
            set.add(d.get(i));
        }
        assertEquals(52, set.size());
    }

    @Test
    void drawReducesDeckSize() {
        Deck d = Deck.create();
        d.draw();
        assertEquals(51, d.size());
    }
}
