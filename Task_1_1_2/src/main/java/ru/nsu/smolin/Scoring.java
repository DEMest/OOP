package ru.nsu.smolin;

import java.util.List;

public final class Scoring {
    private Scoring() {}

    public static int total(List<Card> hand) {
        int sum = 0;
        int aces = 0;
        for(Card c: hand) {
            sum += c.value.points;
            if(c.value == Value.ACE) aces++;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }
}
