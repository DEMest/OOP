package ru.nsu.smolin;

/**
 * Enum class of card suit.
 *
 */
public enum Suit {
    HEARTS('H'),
    SPADES('S'),
    CLUBS('T'),
    DIAMONDS('D');

    public final char symbol;

    Suit(char symbol) {
        this.symbol = symbol;
    }
}