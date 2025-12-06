package ru.nsu.smolin;

import java.util.List;

/**
 * <p>Документ, состоящий из последовательности элементов.</p>
 */
public final class Document implements Element {

    private final List<Element> elements;

    public Document(List<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toMarkdown());
            if (i < elements.size() - 1) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }
}