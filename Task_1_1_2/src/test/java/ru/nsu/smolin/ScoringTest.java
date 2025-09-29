package ru.nsu.smolin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Scoring.
 */
final class ScoringTest {

    private static Card c(Value v, Suit s) {
        return new Card(s, v);
    }

    @Test
    void checkSum1() {
        List<Card> hand = List.of(c(Value.EIGHT, Suit.SPADES), c(Value.TWO, Suit.SPADES));
        assertEquals(10, Scoring.total(hand));
    }

    @Test
    void checkSum2() {
        List<Card> hand = List.of(c(Value.TEN, Suit.SPADES), c(Value.JACK, Suit.SPADES));
        assertEquals(20, Scoring.total(hand));
    }

    @Test
    void checkSumWithOneAce() {
        List<Card> hand = List.of(c(Value.ACE, Suit.SPADES), c(Value.JACK, Suit.SPADES));
        assertEquals(21, Scoring.total(hand));
    }

    @Test
    void checkSumWithAceAndTotalAbove21() {
        List<Card> hand = List.of(
                c(Value.TEN, Suit.SPADES),
                c(Value.ACE, Suit.SPADES),
                c(Value.TWO, Suit.HEARTS)
        );
        assertEquals(13, Scoring.total(hand));
    }

    @Test
    void checkSumWithMultipleAces() {
        List<Card> hand = List.of(c(Value.ACE, Suit.SPADES), c(Value.ACE, Suit.HEARTS));
        assertEquals(12, Scoring.total(hand));
    }
}