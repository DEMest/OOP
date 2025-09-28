package ru.nsu.smolin;

import java.util.List;

public final class Renderer {
    private Renderer() {}

    public static void showSplash() {

        System.out.println(
                "+----------------------------------------------------+\n" +
                        "|                                                    |\n" +
                        "|                   .---------.                      |\n" +
                        "|                   |A        |                      |\n" +
                        "|                   |         |                      |\n" +
                        "|                   |    " + Config.BLACK + "S" + Config.RESET + "    |                      |\n" +
                        "|                   |         |                      |\n" +
                        "|                   |        A|                      |\n" +
                        "|                   '---------'                      |\n" +
                        "|                                                    |\n" +
                        "|             " + Config.RED + "H" + " " + Config.BLACK + "S" + "   BLACKJACK  " + Config.BLACK + "T" + Config.RED + " " + "D" + Config.RESET +
                        "                   |\n" +
                        "|            Press" + " " + Config.RED + " ENTER" + Config.RESET + " to start...                |\n" +
                        "+----------------------------------------------------+"
        );
    }


    public static void showHands(List<Card> player, List<Card> dealer, boolean hideDealerHole) {
        String playerStr = joinShort(player);
        String dealerStr = hideDealerHole && dealer.size() >= 2
                ? dealer.get(0).shortString() + " [XX]"
                : joinShort(dealer);
        String dealerSum = hideDealerHole ? "" : " (sum: " + Scoring.total(dealer) + ")";
        System.out.println(
                "Your cards:   " + playerStr + "  (sum: " + Scoring.total(player) + ")\n" +
                        "Dealer cards: " + dealerStr + dealerSum + "\n" +
                        "Chose action: Take / Pass / Info   (or Exit)"
        );
    }

    public static void showRoundHeader(int n) {
        System.out.println("\n-------------- ROUND " + n + " --------------\n");
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
