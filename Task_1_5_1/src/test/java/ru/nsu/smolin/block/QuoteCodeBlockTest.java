package ru.nsu.smolin.block;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.table.Text;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuoteCodeBlockTest {

    @Test
    void quoteSuccess() {
        Quote quote = new Quote(List.of(
                new Text.Plain("First"),
                new Text.Plain("Second")
        ));

        assertEquals("> First\n> Second", quote.toMarkdown());
    }

    @Test
    void codeBlockSuccess() {
        CodeBlock block = new CodeBlock("java", "int x = 1;");
        String expected = """
                ```java
                int x = 1;
                ```""";
        assertEquals(expected, block.toMarkdown());
    }
}