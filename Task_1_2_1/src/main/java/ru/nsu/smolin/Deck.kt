package ru.nsu.smolin

enum class Suit {
    HEARTS, SPADES, CLUBS, DIAMONDS
}

enum class Value(val values: List<Int>) {
    TWO(listOf(2)),
    THREE(listOf(3)),
    FOUR(listOf(4)),
    FIVE(listOf(5)),
    SIX(listOf(6)),
    SEVEN(listOf(7)),
    EIGHT(listOf(8)),
    NINE(listOf(9)),
    TEN(listOf(10)),
    JACK(listOf(10)),
    QUEEN(listOf(10)),
    KING(listOf(10)),
    ACE(listOf(1, 11));
}

data class Card (val suit: Suit, val value: Value)

class Deck : Iterable<Card> {
    private val cards = mutableListOf<Card>()
    init {
        for (suit in Suit.values()) {
            for(value in Value.values()) {
                cards.add(Card(suit, value))
            }
        }
        cards.shuffle()
    }
    fun draw(): Card? {
        return if (cards.isNotEmpty()) cards.removeAt(0) else null
    }

    override fun iterator(): Iterator<Card> {
        return cards.iterator()
    }
}