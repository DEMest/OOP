package ru.nsu.smolin;

/**
 * Base class of available user commands.
 *
 */
public final class Commands {
    private Commands() {}

    /**
     * Command parser
     * Take - take next card
     * Pass - Transition to dealer turn
     * Info - Show info (hands, available actions)
     * Exit - Instant exit out of game
     * Empty stroke - equals to Info.
     *
     * @param s input command
     * @return Command from enum class
     */
    public static Command parse(String s) {
        if (s == null) {
            return Command.UNKNOWN;
        }
        String t = s.trim().toLowerCase();
        switch (t) {
            case "take":
                return Command.TAKE;
            case "pass":
                return Command.PASS;
            case "info":
            case "":
                return Command.INFO;
            case "exit":
                return Command.EXIT;
            default:
                return Command.UNKNOWN;
        }
    }
}