package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.model.Card;
import ru.nsu.smolin.model.Suit;
import ru.nsu.smolin.model.Value;
import ru.nsu.smolin.model.Scoring;


/**
 * Tests for Scoring.
 */
final class ScoringTest {

    private static Card createCard(Value v, Suit s) {
        return new Card(s, v);
    }

    @Test
    void checkSum1() {
        List<Card> hand = List.of(createCard(Value.EIGHT, Suit.SPADES),
                createCard(Value.TWO, Suit.SPADES));
        assertEquals(10, Scoring.total(hand));
    }

    @Test
    void checkSum2() {
        List<Card> hand = List.of(createCard(Value.TEN, Suit.SPADES),
                createCard(Value.JACK, Suit.SPADES));
        assertEquals(20, Scoring.total(hand));
    }

    @Test
    void checkSumWithOneAce() {
        List<Card> hand = List.of(createCard(Value.ACE, Suit.SPADES),
                createCard(Value.JACK, Suit.SPADES));
        assertEquals(21, Scoring.total(hand));
    }

    @Test
    void checkSumWithAceAndTotalAbove21() {
        List<Card> hand = List.of(
                createCard(Value.TEN, Suit.SPADES),
                createCard(Value.ACE, Suit.SPADES),
                createCard(Value.TWO, Suit.HEARTS)
        );
        assertEquals(13, Scoring.total(hand));
    }

    @Test
    void checkSumWithMultipleAces() {
        List<Card> hand = List.of(createCard(Value.ACE, Suit.SPADES),
                createCard(Value.ACE, Suit.HEARTS));
        assertEquals(12, Scoring.total(hand));
    }
}