package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;
import java.util.List;

/**
 * ћаркированный, нумерованный или список задач.
 */
public final class ListBlock implements Element {

    public enum Type {
        UNORDERED,
        ORDERED,
        TASKS
    }

    private final Type type;
    private final List<ListItem> items;

    public ListBlock(Type type, List<ListItem> items) {
        this.type = type;
        this.items = List.copyOf(items);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            ListItem item = items.get(i);
            String prefix = switch (type) {
                case ORDERED -> (i + 1) + ". ";
                default -> "- ";
            };
            sb.append(prefix);
            sb.append(item.toMarkdown(type, i + 1));
            if (i < items.size() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
