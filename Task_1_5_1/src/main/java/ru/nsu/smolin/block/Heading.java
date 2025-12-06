package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;

/**
 * Заголовок уровня 1–6 в стиле ATX.
 * Примеры: "# Заголовок", "## Заголовок" и т.д.
 */
public final class Heading implements Element {

    private final int level;
    private final Element text;

    public Heading(int level, Element text) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be in range 1..6");
        }
        this.level = level;
        this.text = text;
    }

    @Override
    public String toMarkdown() {
        String hashes = "#".repeat(level);
        return hashes + " " + text.toMarkdown();
    }
}