package ru.nsu.smolin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for making deck from all cards.
 *
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>(52);

    /**
     * Create a new deck by going
     * all the card combinations.
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

    public Card draw() {
        return cards.remove(0);
    }
}
