package ru.nsu.smolin;

public class Card {
    public final Suit suit;
    public final Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

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