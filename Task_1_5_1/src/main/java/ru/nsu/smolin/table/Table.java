package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;

import java.util.ArrayList;
import java.util.List;

public class Table implements Element {
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;

    private final List<Integer> alignments;
    private final List<Row> rows;
    private final int rowLimit;

    private Table(List<Integer> alignments, List<Row> rows, int rowLimit) {
        this.alignments = List.copyOf(alignments);
        this.rows = List.copyOf(rows);
        this.rowLimit = rowLimit;
    }

    @Override
    public String toMarkdown() {
        if (rows.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int[] widths = computeColumnWidths();

        Row header = rows.get(0);
        sb.append(renderRow(header, widths)).append('\n');
        sb.append(renderAlignmentRow(widths)).append('\n');

        int maxDataRows = Math.min(rowLimit, rows.size() - 1);
        for (int i = 1; i <= maxDataRows; i++) {
            sb.append(renderRow(rows.get(i), widths)).append('\n');
        }
        return sb.toString();
    }

    private String renderRow(Row row, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.cells.size(); i++) {
            String text = row.cells.get(i).toMarkdown();
            int width = widths[i];
            int align = i < alignments.size() ? alignments.get(i) : ALIGN_LEFT;

            sb.append('|').append(' ');
            sb.append(pad(text, width, align));
            sb.append(' ');
        }
        sb.append('|');
        return sb.toString();
    }

    private String pad(String text, int width, int align) {
        int spaces = Math.max(0, width - text.length());

        return switch (align) {
            case ALIGN_RIGHT -> " ".repeat(spaces) + text;
            case ALIGN_CENTER -> {
                int left = spaces / 2;
                int right = spaces - left;
                yield " ".repeat(left) + text + " ".repeat(right);
            }
            default -> text + " ".repeat(spaces);
        };
    }

    private String renderAlignmentRow(int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            int width = Math.max(3, widths[i]);
            int align = i < alignments.size() ? alignments.get(i) : ALIGN_LEFT;
            sb.append('|').append(' ');
            switch (align) {
                case ALIGN_RIGHT -> sb.append("-"
                        .repeat(width - 1))
                        .append(':');
                case ALIGN_CENTER -> sb.append(':')
                        .append("-".repeat(width - 2))
                        .append(':');
                default -> sb.append(':')
                        .append("-"
                                .repeat(width - 1));
            }
            sb.append(' ');
        }
        sb.append('|');
        return sb.toString();
    }

    private int[] computeColumnWidths() {
        if (rows.isEmpty()) return new int[0];
        int cols = rows.get(0).cells.size();
        int[] widths = new int[cols];

        for (Row row : rows) {
            for (int i = 0; i < cols; i++) {
                String text = row.cells.get(i).toMarkdown();
                int len = text.length();           // БЕЗ visibleLength
                widths[i] = Math.max(widths[i], len);
            }
        }

        // небольшой запас, чтобы центр/право были заметны даже когда текст короче
        for (int i = 0; i < cols; i++) {
            int align = i < alignments.size() ? alignments.get(i) : ALIGN_LEFT;
            if (align == ALIGN_CENTER) {
                widths[i] += 2; // один символ слева и справа
            } else if (align == ALIGN_RIGHT) {
                widths[i] += 1; // минимум один слева
            }
        }

        return widths;
    }

    public static final class Builder {
        private final List<Integer> alignments = new ArrayList<>();
        private final List<Row> rows = new ArrayList<>();
        private int rowLimit = Integer.MAX_VALUE;

        public Builder withAlignments(int... aligns) {
            alignments.clear();
            for (int a : aligns) {
                alignments.add(a);
            }
            return this;
        }

        public Builder withRowLimit(int limit) {
            this.rowLimit = limit;
            return this;
        }

        public Builder addRow(Object... cells) {
            if (rows.size() >= rowLimit) return this;
            List<Element> elems = new ArrayList<>();
            for (Object c : cells) {
                elems.add(asElement(c));
            }
            rows.add(new Row(List.copyOf(elems)));
            return this;
        }

        public Table build() {
            return new Table(alignments, rows, rowLimit);
        }

        private Element asElement(Object value) {
            if (value instanceof Element e) {
                return e;
            }
            return new Text.Plain(String.valueOf(value));
        }
    }

    private record Row(List<Element> cells) { }

    @Override
    public String toString() {
        return toMarkdown();
    }
}
