package ru.nsu.smolin.table;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    void plainSuccess() {
        Text.Plain text = new Text.Plain("hello");
        assertEquals("hello", text.toMarkdown());
    }

    @Test
    void boldSuccess() {
        Text.Bold bold = new Text.Bold("42");
        assertEquals("**42**", bold.toMarkdown());
    }
}