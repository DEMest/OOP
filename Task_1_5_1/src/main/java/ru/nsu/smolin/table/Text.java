package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;

/**
 * <p>Вспомогательный класс для представления текстовых элементов.</p>
 *
 * <p>Содержит простые реализации {@link Element} для неформатированного
 * текста и текста с выделением жирным шрифтом.</p>
 */
public class Text {

    /**
     * <p>Простой текст без форматирования</p>
     *
     */
    public static final class Plain implements Element {
        private final String value;

        public Plain(String value) {
            this.value = value;
        }

        @Override public String toMarkdown() {
            return value;
        }
    }

    public static final class Bold implements Element {
        private final String value;

        public Bold(String value) {
            this.value = value;
        }

        @Override
        public String toMarkdown() {
            return "**" + value + "**";
        }
    }

    private Text() {

    }
}
