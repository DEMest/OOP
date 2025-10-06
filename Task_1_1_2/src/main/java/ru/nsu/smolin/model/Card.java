package ru.nsu.smolin.model;

import ru.nsu.smolin.util.Config;

/**
 * Base Card class.
 *
 */
public class Card {
    public final Suit suit;
    public final Value value;

    /**
     * Construct that matches char to suit,
     * int to value.
     *
     * @param suit enum class of char value
     * @param value enum class of int value
     */
    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Showing card in string type.
     *
     * @return String of card [{value}{symbol}]
     */
    public String shortString() {
        boolean redSuit = (suit == Suit.HEARTS || suit == Suit.DIAMONDS);
        String color = redSuit ? Config.RED : Config.BLACK;
        return "[" + color + value.display + suit.symbol + Config.RESET + "]";
    }

    @Override
    public String toString() {
        return shortString();
    }
}