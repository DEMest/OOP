package ru.nsu.smolin.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void italicSuccess() {
        Text.Italic italic = new Text.Italic("x");
        assertEquals("*x*", italic.toMarkdown());
    }

    @Test
    void strikeSuccess() {
        Text.Strike strike = new Text.Strike("done");
        assertEquals("~~done~~", strike.toMarkdown());
    }

    @Test
    void inlineCodeSuccess() {
        Text.InlineCode code = new Text.InlineCode("int x");
        assertEquals("`int x`", code.toMarkdown());
    }

    @Test
    void linkSuccess() {
        LinkElements.Link link =
                new LinkElements.Link("GitHub", "https://github.com");
        assertEquals("[GitHub](https://github.com)", link.toMarkdown());
    }

    @Test
    void imageSuccess() {
        LinkElements.Image img =
                new LinkElements.Image("logo", "https://example.com/logo.png");
        assertEquals("![logo](https://example.com/logo.png)", img.toMarkdown());
    }
}