package ru.nsu.smolin.util;

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
     * @param input input command
     * @return Command from enum class
     */
    public static Command parse(String input) {
        if (input == null) {
            return Command.UNKNOWN;
        }
        String command = input.trim().toLowerCase();
        switch (command) {
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