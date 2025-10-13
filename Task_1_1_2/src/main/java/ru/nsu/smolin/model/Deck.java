package ru.nsu.smolin.model;

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
    public static Deck create() {
        Deck deck = new Deck();
        for (Suit s : Suit.values()) {
            for (Value v : Value.values()) {
                deck.cards.add(new Card(s, v));
            }
        }
        deck.shuffle();
        return deck;
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        return cards.remove(0);
    }

    public Card get(int index) {
        return cards.get(index);
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }
}
