package ru.nsu.smolin;

import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>(52);

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

    private static final String CLR_LINE = "\u001B[2K";

    public Card draw() {
        return cards.remove(0);
    }

    public void shuffleAnimation(long durationMs, long frameDelayMs) {
        System.out.println("\r" + CLR_LINE + "Shuffling... done!");
    }

    private String preview(int n) {
        StringBuilder sb = new StringBuilder();
        int limit = Math.min(n, cards.size());
        for(int i = 0; i < limit; i++) {
            if(i > 0) sb.append(' ');
            sb.append(cards.get(i).shortString());
        }
        return sb.toString();
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
