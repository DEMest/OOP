package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;
import java.util.List;

/**
 * <p>Параграф текста.</p>
 *
 * <p>Содержит последовательность инлайновых элементов и при сериализации
 * конкатенирует их без перевода строки.</p>
 */
public final class Paragraph implements Element {

    private final List<Element> inlines;

    public Paragraph(List<Element> inlines) {
        this.inlines = List.copyOf(inlines);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (Element inline : inlines) {
            sb.append(inline.toMarkdown());
        }
        return sb.toString();
    }
}
