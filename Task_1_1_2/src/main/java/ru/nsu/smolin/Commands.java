package ru.nsu.smolin;

public final class Commands {
    private Commands() {}

    public static Command parse(String s) {
        if(s == null) return Command.UNKNOWN;
        String t = s.trim().toLowerCase();
        switch(t) {
            case "take": return Command.TAKE;
            case "pass": return Command.PASS;
            case "info":
            case "":     return Command.INFO;
            case "exit": return Command.EXIT;
            default:     return Command.UNKNOWN;
        }
    }
}