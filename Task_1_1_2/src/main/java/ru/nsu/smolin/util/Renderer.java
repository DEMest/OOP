package ru.nsu.smolin.util;

import ru.nsu.smolin.model.Card;
import ru.nsu.smolin.model.Scoring;

import java.util.List;

/**
 * Class wich contains main outputs
 * of program like start screen, hands
 * and round header.
 *
 */
public final class Renderer {
    private Renderer() {}

    /**
     * Main screen when starting the game.
     */
    public static void showSplash() {
        System.out.println(
                "+----------------------------------------------------+\n"
                        + "|                                                    |\n"
                        + "|                   .---------.                      |\n"
                        + "|                   |A        |                      |\n"
                        + "|                   |         |                      |\n"
                        + "|                   |    "
                        + Config.BLACK + "S"
                        + Config.RESET
                        + "    |                      |\n"
                        + "|                   |         |                      |\n"
                        + "|                   |        A|                      |\n"
                        + "|                   '---------'                      |\n"
                        + "|                                                    |\n"
                        + "|             "
                        + Config.RED + "H"
                        + " "
                        + Config.BLACK + "S" + "   BLACKJACK  "
                        + Config.BLACK + "T"
                        + Config.RED + " " + "D"
                        + Config.RESET
                        + "                   |\n"
                        + "|            Press" + " "
                        + Config.RED + " ENTER"
                        + Config.RESET
                        + " to start...                |\n"
                        + "+----------------------------------------------------+"
        );
    }

    /**
     * Show hands and actions.
     *
     * @param player player hand
     * @param dealer dealer hand
     * @param hideDealerHole false - if now is player turn (hide second dealer card)
     *                       true - in settle
     */
    public static void showHands(List<Card> player, List<Card> dealer, boolean hideDealerHole) {
        String playerStr = joinShort(player);
        String dealerStr = hideDealerHole && dealer.size() >= 2
                ? dealer.get(0).shortString() + " [XX]"
                : joinShort(dealer);
        String dealerSum = hideDealerHole ? "" : " (sum: " + Scoring.total(dealer) + ")";
        System.out.println(
                "Your cards:   " + playerStr + "  (sum: " + Scoring.total(player) + ")\n"
                        + "Dealer cards: " + dealerStr + dealerSum + "\n"
                        + "Chose action: Take / Pass / Info   (or Exit)"
        );
    }

    /**
     * Just header.
     *
     * @param n round
     */
    public static void showRoundHeader(int n) {
        System.out.println("\n-------------- ROUND " + n + " --------------\n");
    }

    /**
     * Returns hand in string format,
     * using srting builder to make new stroke
     * from List.
     *
     * @param hand List of cards
     * @return String format of this List
     */
    public static String joinShort(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(hand.get(i).shortString());
        }
        return sb.toString();
    }
}
