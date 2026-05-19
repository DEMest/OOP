package ru.nsu.smolin.checker.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CliTest {
    @Test
    void parsesTestCommand() {
        Cli.Command cmd = Cli.parse(new String[]{"test"});
        assertEquals(Cli.Command.TEST, cmd);
    }

    @Test
    void unknownCommandThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> Cli.parse(new String[]{"bogus"}));
    }

    @Test
    void noArgsThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> Cli.parse(new String[]{}));
    }
}
