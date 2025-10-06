package ru.nsu.smolin.model;

import java.util.List;

/**
 * Class to calculate total sum
 * of hand.
 *
 */
public final class Scoring {
    private Scoring() {}

    /**
     * Input of List of cards transforms
     * into it sum by it values.
     *
     * @param hand input hand (List of cards)
     * @return sum of card values
     */
    public static int total(List<Card> hand) {
        int sum = 0;
        int aces = 0;
        for (Card c : hand) {
            sum += c.value.points;
            if (c.value == Value.ACE) {
                aces++;
            }
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }
}
