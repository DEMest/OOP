package ru.nsu.smolin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class of game logic.
 *
 */
public class Blackjack {
    enum StartOutcome {
        CONTINUE, PLAYER_BJ, BOTH_BJ, DEALER_BJ
    }

    private Deck deck = new Deck();
    private final List<Card> player = new ArrayList<>();
    private final List<Card> dealer = new ArrayList<>();
    private int round = 0;

    /**
     * Main logic and game-loop
     *
     */
    public void run() {
        Scanner in = new Scanner(System.in);
        Renderer.showSplash();
        in.nextLine();

        mainLoop:
        while (true) {
            StartOutcome start = startRound();
            switch (start) {
                case CONTINUE:
                    if (!playerTurn(in)) {
                        dealerTurn();
                        settle();
                    }
                    break;
                case PLAYER_BJ:
                    Renderer.showHands(player, dealer, false);
                    System.out.println("Blackjack! You win.");
                    break;
                case BOTH_BJ:
                    Renderer.showHands(player, dealer, false);
                    System.out.println("Both with blackjack - draw.");
                    break;
                case DEALER_BJ:
                    Renderer.showHands(player, dealer, false);
                    System.out.println("The dealer has blackjack - you lose.");
                    break;
                default:
                    break;
            }
            System.out.println("\nPress ENTER to continue, or print 'exit' for the exit.");
            String ans = in.nextLine().trim().toLowerCase();
            if (ans.equals("exit")) {
                break mainLoop;
            }
        }
        System.out.println("End of the game");
    }

    /**
     * Starting round, showing hands, ask player for action.
     *
     * @return
     */
    private StartOutcome startRound() {
        round++;
        Renderer.showRoundHeader(round);

        if (deck.size() < 15) {
            System.out.println("Not enough cards, create new deck and shuffle...");
            deck = new Deck();// шафл с анимацией (sleep внутри Deck)
        }

        player.clear();
        dealer.clear();
        player.add(deck.draw());
        dealer.add(deck.draw());
        player.add(deck.draw());
        dealer.add(deck.draw());

        int p = Scoring.total(player);
        int d = Scoring.total(dealer);

        if (p == 21 && d == 21) {
            return StartOutcome.BOTH_BJ;
        }
        if (p == 21) {
            return StartOutcome.PLAYER_BJ;
        }
        if (d == 21) {
            return StartOutcome.DEALER_BJ;
        }

        Renderer.showHands(player, dealer, true);
        return StartOutcome.CONTINUE;
    }

    private boolean playerTurn(Scanner in) {
        while (true) {
            Command cmd = Commands.parse(in.nextLine());
            switch (cmd) {
                case TAKE: {
                    Card c = deck.draw();
                    player.add(c);
                    int sum = Scoring.total(player);
                    System.out.println("You took: " + c.shortString() + "  (sum: " + sum + ")");
                    if (sum > 21) {
                        System.out.println("Too much! lost.");
                        return true;
                    }
                    if (sum == 21) {
                        System.out.println("21! Trnsition to dealer.");
                        return false;
                    }
                    Renderer.showHands(player, dealer, true);
                    break;
                }
                case PASS:
                    return false;
                case INFO:
                    Renderer.showHands(player, dealer, true);
                    break;
                case EXIT:
                    System.out.println("Quitting the game.");
                    System.exit(0);
                    return true;
                case UNKNOWN:
                    System.out.println("Undefined command. Available: Take / Pass / Info / Exit");
                default:
                    Renderer.showHands(player, dealer, true);
                    break;
            }
        }
    }

    private void dealerTurn() {
        System.out.println("\nDealer show cards: "
                + joinShort(dealer) + " (sum: " + Scoring.total(dealer) + ")");

        while (Scoring.total(dealer) < 17 || Scoring.total(dealer) < Scoring.total(player)) {
            if (Scoring.total(dealer) > Scoring.total(player)) {
                break;
            }
            Card c = deck.draw();
            dealer.add(c);
            System.out.println("Dealer took: "
                    + c.shortString() + " (sum: " + Scoring.total(dealer) + ")");
        }
    }

    private void settle() {
        int ps = Scoring.total(player);
        int ds = Scoring.total(dealer);
        System.out.println(
                "Your cards:   " + joinShort(player) + "  (sum: " + ps + ")\n"
                        + "Dealer cards: " + joinShort(dealer) + "  (sum: " + ds + ")"
        );
        if (ds > 21){
            System.out.println("Dealer took to much - you win.");
        } else if (ps > ds) {
            System.out.println("Your sum is higher - you win.");
        }
        else if (ps < ds) {
            System.out.println("Your sum is lower - you lost.");
        }
        else {
            System.out.println("Draw.");
        }
    }

    private static String joinShort(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(hand.get(i).shortString());
        }
        return sb.toString();
    }

}
