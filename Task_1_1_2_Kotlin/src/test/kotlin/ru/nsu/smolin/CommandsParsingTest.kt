package ru.nsu.smolin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CommandsParsingTest {
    @Test
    fun `command take`() {
        assertEquals(Command.TAKE, parseCommand("Take"))
        assertEquals(Command.TAKE, parseCommand("take"))
        assertEquals(Command.TAKE, parseCommand(" take"))
        assertEquals(Command.TAKE, parseCommand(" take "))
    }
    
    @Test
    fun `command pass`() {
        assertEquals(Command.PASS, parseCommand("Pass"))
        assertEquals(Command.PASS, parseCommand("pass"))
        assertEquals(Command.PASS, parseCommand(" pass"))
        assertEquals(Command.PASS, parseCommand(" pass "))
    }
    
    @Test
    fun `command exit`() {
        assertEquals(Command.EXIT, parseCommand("Exit"))
        assertEquals(Command.EXIT, parseCommand("exit"))
        assertEquals(Command.EXIT, parseCommand(" exit"))
        assertEquals(Command.EXIT, parseCommand(" exit "))
    }

    @Test
    fun `command info`() {
        assertEquals(Command.INFO, parseCommand("Info"))
        assertEquals(Command.INFO, parseCommand(""))
    }

    @Test
    fun `command unknown`() {
        assertEquals(Command.UNKNOWN, parseCommand("asdad"))
    }
}