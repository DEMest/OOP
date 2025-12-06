package ru.nsu.smolin.block;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.table.Text;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadingParagraphTest {

    @Test
    void headingSuccess() {
        Heading h = new Heading(2, new Text.Plain("Title"));
        assertEquals("## Title", h.toMarkdown());
    }

    @Test
    void paragraphSuccess() {
        Paragraph p = new Paragraph(List.of(
                new Text.Plain("Hello "),
                new Text.Bold("world")
        ));
        assertEquals("Hello **world**", p.toMarkdown());
    }
}
