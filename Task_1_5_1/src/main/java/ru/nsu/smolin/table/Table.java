package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Markdown-таблица.</p>
 *
 * <p>Таблица создаётся через {@link Table.Builder} и может содержать
 * произвольные элементы, реализующие {@link Element}, в качестве ячеек.</p>
 *
 * <p>Первая добавленная строка считается заголовком, вторая строка при
 * сериализации содержит разделители колонок и информацию о выравнивании.</p>
 */
public final class Table implements Element {

    private final List<Alignment> alignments;
    private final List<TableRow> rows;
    private final int rowLimit;

    /**
     * <p>Создаёт неизменяемый объект таблицы.</p>
     *
     * <p>Конструктор пакетной видимости, используется только {@link Builder}.</p>
     */
    Table(List<Alignment> alignments, List<TableRow> rows, int rowLimit) {
        this.alignments = List.copyOf(alignments);
        this.rows = List.copyOf(rows);
        this.rowLimit = rowLimit;
    }

    /**
     * <p>Возвращает список выравниваний колонок в порядке слева направо.
     * Используется рендерером при вычислении паддингов и строки разделителей.</p>
     */
    List<Alignment> alignments() {
        return alignments;
    }

    /**
     * <p>Возвращает все строки таблицы, включая заголовок.</p>
     *
     * <p>Первая строка интерпретируется как строка заголовков.</p>
     */
    List<TableRow> rows() {
        return rows;
    }

    int rowLimit() {
        return rowLimit;
    }

    /**
     * <p>Сериализует таблицу в строку в формате Markdown.</p>
     *
     * <p>Строка разделителей содержит <code>-</code> и <code>:</code> в
     * соответствии с выравниванием колонок.</p>
     */
    @Override
    public String toMarkdown() {
        return TableRenderer.render(this);
    }

    @Override
    public String toString() {
        return toMarkdown();
    }

    /**
     * <p>Билдер для пошаговой сборки {@link Table}.</p>
     *
     * <p>Позволяет задать выравнивания колонок, лимит строк и
     * последовательно добавлять строки с произвольными значениями.</p>
     */
    public static final class Builder {

        private final List<Alignment> alignments = new ArrayList<>();
        private final List<TableRow> rows = new ArrayList<>();
        private int rowLimit = Integer.MAX_VALUE;

        /**
         * <p>Задаёт выравнивание для колонок.</p>
         *
         * <p>Порядок значений соответствует порядку колонок в таблице.
         * Если колонок больше, чем элементов в массиве, остальные
         * считаются выровненными по левому краю.</p>
         *
         * @param aligns выравнивания по колонкам
         * @return текущий билдер
         */
        public Builder withAlignments(Alignment... aligns) {
            alignments.clear();
            alignments.addAll(List.of(aligns));
            return this;
        }

        /**
         * <p>Задаёт максимальное количество строк данных.</p>
         *
         * <p>Заголовок не входит в этот лимит: он всегда присутствует
         * в итоговой таблице.</p>
         *
         * @param limit максимальное число строк данных
         * @return текущий билдер
         */
        public Builder withRowLimit(int limit) {
            this.rowLimit = limit;
            return this;
        }

        /**
         * <p>Добавляет строку таблицы.</p>
         *
         * <p>Каждое значение приводится к {@link Element}: если объект уже
         * реализует этот интерфейс, он используется как есть, в противном
         * случае значение оборачивается в {@link Text.Plain}.</p>
         *
         * <p>Если достигнут {@code rowLimit}, новые строки игнорируются.</p>
         *
         * @param cells значения ячеек строки
         * @return текущий билдер
         */
        public Builder addRow(Object... cells) {
            if (rows.size() >= rowLimit) {
                return this;
            }

            List<Element> elems = new ArrayList<>();
            for (Object value : cells) {
                elems.add(asElement(value));
            }

            rows.add(new TableRow(List.copyOf(elems)));
            return this;
        }

        public Table build() {
            return new Table(alignments, rows, rowLimit);
        }

        /**
         * <p>Преобразует произвольное значение в {@link Element}.</p>
         *
         * <p>Если значение уже является элементом, оно возвращается
         * без изменений; иначе используется текстовое представление
         * через {@link Object#toString()} и создаётся {@link Text.Plain}.</p>
         */
        private Element asElement(Object value) {
            if (value instanceof Element e) {
                return e;
            }
            return new Text.Plain(String.valueOf(value));
        }
    }
}
