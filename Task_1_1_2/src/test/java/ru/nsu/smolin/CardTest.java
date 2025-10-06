package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.model.Card;
import ru.nsu.smolin.model.Suit;
import ru.nsu.smolin.model.Value;
import ru.nsu.smolin.util.Config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Card class.
 *
 */
final class CardTest {

    @Test
    void compareExpectedFields() {
        Card c = new Card(Suit.SPADES, Value.ACE);
        assertEquals(Suit.SPADES, c.suit);
        assertEquals(Value.ACE, c.value);
    }

    @Test
    void shortStringUsesRedForHearts() {
        Card c = new Card(Suit.HEARTS, Value.ACE);
        String s = c.shortString();
        assertTrue(s.contains(Config.RED));
        assertTrue(s.contains(Config.RESET));
    }

    @Test
    void shortStringUsesBlackForSpades() {
        Card c = new Card(Suit.SPADES, Value.KING);
        String s = c.shortString();
        assertTrue(s.contains(Config.BLACK));
        assertTrue(s.contains(Config.RESET));
    }

    @Test
    void toStringDelegatesToShortString() {
        Card c = new Card(Suit.CLUBS, Value.TEN);
        assertEquals(c.shortString(), c.toString());
    }
}