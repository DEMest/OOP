package ru.nsu.smolin.checker;

import ru.nsu.smolin.checker.app.CheckerApp;
import ru.nsu.smolin.checker.cli.Cli;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class Main {
    public static void main(String[] argv) {
        PrintStream utf8Out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.setOut(utf8Out);
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        try {
            Cli.Command cmd = Cli.parse(argv);
            int code;
            if (cmd == Cli.Command.TEST) {
                code = new CheckerApp(Path.of(".").toAbsolutePath().normalize(), utf8Out).test();
            } else {
                throw new IllegalStateException("unhandled command: " + cmd);
            }
            System.exit(code);
        } catch (IllegalArgumentException e) {
            System.err.println("error: " + e.getMessage());
            System.exit(2);
        }
    }
}
