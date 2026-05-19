package ru.nsu.smolin.checker.cli;

public final class Cli {
    public enum Command { TEST }

    private Cli() {}

    public static Command parse(String[] argv) {
        if (argv.length == 0) {
            throw new IllegalArgumentException("usage: oop-checker <command>");
        }
        String name = argv[0];
        if (name.equals("test")) {
            return Command.TEST;
        }
        throw new IllegalArgumentException("unknown command: " + name);
    }
}
