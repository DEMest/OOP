package ru.nsu.smolin

object Config {
    const val useColors: Boolean = true
}

/**
 * Enumerating suit of card
 *
 */
enum class Suit(val symbol: Char) {
    HEARTS('♥'), SPADES('♠'), CLUBS('♣'), DIAMONDS('♦')
}

/**
 * Displaying and evaluating cards by their rank
 *
 */
enum class Value(val display: String, val points: Int) {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5),
    SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
    TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10),
    ACE("A", 11)
}

/**
 * Simple description of Card and simple print
 *
 */
data class Card (val suit: Suit, val value: Value) {
    fun short(): String {
        val reset = "\u001B[0m"
        val red = "\u001B[31m"
        val black = "\u001B[30m"
        val color = if (suit == Suit.HEARTS || suit == Suit.DIAMONDS) red else black
        // val plain = "[${value.display}${suit.symbol}"
        return if (Config.useColors) "[${color}${value.display}${suit.symbol}$reset]" else "[${value.display}${suit.symbol}]"
    }
}

/**
 * Object for calculate total sum of player or dealer hand
 *
 */
object Scoring {
    fun total(hand: List<Card>): Int {
        var sum = 0
        var aces = 0
        for(c in hand) {
            sum += c.value.points
            if(c.value == Value.ACE) aces++
        }
        while(sum > 21 && aces > 0) {
            sum -= 10
            aces--
        }
        return sum
    }
}

class Deck : Iterable<Card> {
    /**
     * Create a deck by trying all combinations and transform
     * it into a mutableList of card
     *
     */
    private val cards: MutableList<Card> = buildList {
        for (s in Suit.entries) for (v in Value.entries) add(Card(s, v))
    }.toMutableList()

    init { shuffle() }

    private fun shuffle() = cards.shuffle()

    fun size(): Int = cards.size

    /**
     * Print error if Deck is empty,
     * else remove and return first card in deck
     *
     */
    fun draw(): Card {
        if (cards.isEmpty()) error("Deck is empty")
        return cards.removeFirst()
    }

    override fun iterator(): Iterator<Card> = cards.iterator()

    /**
     * Simple animation of shuffling deck
     *
     */
    fun shuffleAnimation(times: Int = 50, delayMs: Long = 30) {
        repeat(times) {
            shuffle()
            print("\r${this.take(10).joinToString(" ") { it.short() }}")
            Thread.sleep(delayMs)
        }
        print("\rShuffling... done!\n")
    }

}
