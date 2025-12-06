package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;
import java.util.List;

/**
 * <p>Вспомогательный класс для сериализации {@link Table} в Markdown.</p>
 *
 * <p>Генерирует строку заголовка, строку разделителей с выравниванием
 * колонок и строки с данными.</p>
 */
final class TableRenderer {

    private TableRenderer() { }

    /**
     * <p>Преобразует таблицу в Markdown-строку.</p>
     *
     * <p>Если таблица не содержит строк, возвращается пустая строка.
     * Первая строка списка рассматривается как заголовок.</p>
     *
     * @param table таблица для рендеринга
     * @return текстовое представление таблицы в формате Markdown
     */
    static String render(Table table) {
        List<TableRow> rows = table.rows();
        if (rows.isEmpty()) {
            return "";
        }

        int[] widths = computeColumnWidths(table);

        StringBuilder sb = new StringBuilder();
        TableRow header = rows.get(0);
        sb.append(renderRow(header, widths, table.alignments())).append('\n');
        sb.append(renderAlignmentRow(widths, table.alignments())).append('\n');

        int maxDataRows = Math.min(table.rowLimit(), rows.size() - 1);
        for (int i = 1; i <= maxDataRows; i++) {
            sb.append(renderRow(rows.get(i), widths, table.alignments())).append('\n');
        }
        return sb.toString();
    }

    /**
     * <p>Рендерит одну строку таблицы (заголовок или данные).</p>
     *
     * <p>Каждая ячейка оборачивается в разделители вида
     * <code>| value |</code> и дополняется пробелами в соответствии
     * с шириной колонки и выравниванием.</p>
     *
     * @param row строка таблицы
     * @param widths ширина колонок
     * @param alignments выравнивания по колонкам
     * @return строка в формате Markdown без символа перевода строки
     */
    private static String renderRow(
            TableRow row,
            int[] widths,
            List<Alignment> alignments
    ) {
        StringBuilder sb = new StringBuilder();
        List<Element> cells = row.cells();

        for (int i = 0; i < cells.size(); i++) {
            String text = cells.get(i).toMarkdown();
            int width = widths[i];
            Alignment align = i < alignments.size()
                    ? alignments.get(i)
                    : Alignment.LEFT;

            sb.append('|').append(' ');
            sb.append(pad(text, width, align));
            sb.append(' ');
        }
        sb.append('|');
        return sb.toString();
    }

    /**
     * <p>Дополняет содержимое ячейки пробелами до указанной ширины.</p>
     *
     * @param text   содержимое ячейки
     * @param width  ширина колонки
     * @param align  желаемое выравнивание
     * @return строка, дополненная пробелами до ширины {@code width}
     */
    private static String pad(String text, int width, Alignment align) {
        int spaces = Math.max(0, width - text.length());
        return switch (align) {
            case RIGHT -> " ".repeat(spaces) + text;
            case CENTER -> {
                int left = spaces / 2;
                int right = spaces - left;
                yield " ".repeat(left) + text + " ".repeat(right);
            }
            default -> text + " ".repeat(spaces);
        };
    }

    /**
     * <p>Формирует строку разделителей колонок (вторую строку таблицы).</p>
     *
     * <p>Для каждой колонки генерируется последовательность дефисов
     * и двоеточий в соответствии с её выравниванием:
     * <code>:---</code> для левого, <code>---:</code> для правого,
     * <code>:---:</code> для центрирования.</p>
     *
     * @param widths ширина колонок
     * @param alignments выравнивания по колонкам
     * @return строка разделителей в формате Markdown без перевода строки
     */
    private static String renderAlignmentRow(
            int[] widths,
            List<Alignment> alignments
    ) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            int width = Math.max(3, widths[i]);
            Alignment align = i < alignments.size()
                    ? alignments.get(i)
                    : Alignment.LEFT;

            sb.append('|').append(' ');
            switch (align) {
                case RIGHT ->
                        sb.append("-".repeat(width - 1)).append(':');
                case CENTER ->
                        sb.append(':').append("-".repeat(width - 2)).append(':');
                default ->
                        sb.append(':').append("-".repeat(width - 1));
            }
            sb.append(' ');
        }
        sb.append('|');
        return sb.toString();
    }

    /**
     * <p>Вычисляет минимальные необходимые ширины колонок.</p>
     *
     * @param table таблица, для которой нужно посчитать ширины колонок
     * @return массив ширины колонок по индексу
     */
    private static int[] computeColumnWidths(Table table) {
        List<TableRow> rows = table.rows();
        if (rows.isEmpty()) {
            return new int[0];
        }
        int cols = rows.get(0).cells().size();
        int[] widths = new int[cols];

        for (TableRow row : rows) {
            for (int i = 0; i < cols; i++) {
                String text = row.cells().get(i).toMarkdown();
                widths[i] = Math.max(widths[i], text.length());
            }
        }

        return widths;
    }
}