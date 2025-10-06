package ru.nsu.smolin;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.util.Command;
import ru.nsu.smolin.util.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Commands.
 */
final class CommandsTest {

    @Test
    void commandTake() {
        assertEquals(Command.TAKE, Commands.parse("Take"));
        assertEquals(Command.TAKE, Commands.parse("take"));
        assertEquals(Command.TAKE, Commands.parse(" take"));
        assertEquals(Command.TAKE, Commands.parse(" take "));
    }

    @Test
    void commandPass() {
        assertEquals(Command.PASS, Commands.parse("Pass"));
        assertEquals(Command.PASS, Commands.parse("pass"));
        assertEquals(Command.PASS, Commands.parse(" pass"));
        assertEquals(Command.PASS, Commands.parse(" pass "));
    }

    @Test
    void commandExit() {
        assertEquals(Command.EXIT, Commands.parse("Exit"));
        assertEquals(Command.EXIT, Commands.parse("exit"));
        assertEquals(Command.EXIT, Commands.parse(" exit"));
        assertEquals(Command.EXIT, Commands.parse(" exit "));
    }

    @Test
    void commandInfo() {
        assertEquals(Command.INFO, Commands.parse("Info"));
        assertEquals(Command.INFO, Commands.parse(""));
    }

    @Test
    void commandUnknown() {
        assertEquals(Command.UNKNOWN, Commands.parse("asdad"));
    }
}
