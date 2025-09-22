package ru.nsu.smolin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private fun c(v: Value, s: Suit) = Card(s, v)

class ScoringTest {
    @Test
    fun `check sum 1`() {
        val hand = listOf(c(Value.EIGHT, Suit.SPADES), c(Value.TWO, Suit.SPADES))
        assertEquals(10, Scoring.total(hand))
    }

    @Test
    fun `check sum 2`() {
        val hand = listOf(c(Value.TEN, Suit.SPADES), c(Value.JACK, Suit.SPADES))
        assertEquals(20, Scoring.total(hand))
    }

    @Test
    fun `check sum with one ace`() {
        val hand = listOf(c(Value.ACE, Suit.SPADES), c(Value.JACK, Suit.SPADES))
        assertEquals(21, Scoring.total(hand))
    }
    @Test
    fun `check sum with ace and total above 21`() {
        val hand = listOf(c(Value.TEN, Suit.SPADES), c(Value.ACE, Suit.SPADES), c(Value.TWO, Suit.HEARTS))
        assertEquals(13, Scoring.total(hand))
    }

    @Test
    fun `check sum with multiple aces`() {
        val hand = listOf(c(Value.ACE, Suit.SPADES), c(Value.ACE, Suit.HEARTS))
        assertEquals(12, Scoring.total(hand))
    }
}