package ru.nsu.smolin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DeckTest {
    @Test
    fun `new deck init with 52 card`() {
        val d = Deck()
        assertEquals(52, d.size())
    }

    @Test
    fun `new deck contains only unique cards`() {
        val d = Deck()
        val set = d.toSet()
        assertEquals(52, set.size)
    }

    @Test
    fun `check draw`() {
        val d = Deck()
        d.draw()
        assertEquals(51, d.size())
    }
}