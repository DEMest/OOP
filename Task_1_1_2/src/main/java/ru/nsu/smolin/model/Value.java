package ru.nsu.smolin.model;

/**
 * Enum class of card value.
 *
 */
public enum Value {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11);

    public final String display;
    public final int points;

    Value(String display, int points) {
        this.display = display;
        this.points = points;
    }
}
