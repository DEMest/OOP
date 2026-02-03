package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;
import java.util.List;

/**
 * Блочная цитата.
 */
public final class Quote implements Element {

    private final List<Element> lines;

    public Quote(List<Element> lines) {
        this.lines = List.copyOf(lines);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String rendered = lines.get(i).toMarkdown();
            sb.append("> ").append(rendered);
            if (i < lines.size() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
