package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;
import java.util.List;

/**
 * Ёлемент списка, опционально Ч задача.
 */
public final class ListItem {

    private final List<Element> content;
    private final Boolean checked;

    public ListItem(List<Element> content) {
        this(content, null);
    }

    public ListItem(List<Element> content, Boolean checked) {
        this.content = List.copyOf(content);
        this.checked = checked;
    }

    String toMarkdown(ListBlock.Type type, int index) {
        StringBuilder sb = new StringBuilder();

        if (type == ListBlock.Type.TASKS) {
            String box = (checked != null && checked) ? "[x] " : "[ ] ";
            sb.append(box);
        }

        for (Element e : content) {
            sb.append(e.toMarkdown());
        }

        return sb.toString();
    }
}
