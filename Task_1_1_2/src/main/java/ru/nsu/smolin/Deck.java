package ru.nsu.smolin;

import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>(52);

    /**
     * Create a new deck by going all the card combinations
     *
     */
    public Deck() {
        for (Suit s : Suit.values()) {
            for (Value v : Value.values()) {
                cards.add(new Card(s, v));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card draw() {
        return cards.remove(0);
    }
}
