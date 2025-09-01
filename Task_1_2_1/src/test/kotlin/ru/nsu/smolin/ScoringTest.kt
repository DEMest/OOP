package ru.nsu.smolin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private fun c(v: Value, s: Suit) = Card(s, v)

class ScoringTest {
    @Test
    fun plusWorks() {
        assertEquals(7, 3 + 4)
    }


}