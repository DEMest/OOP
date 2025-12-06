package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;

public class Text {
    public static final class Plain implements Element {
        private final String value;
        public Plain(String value) {
            this.value = value;
        }
        @Override public String toMarkdown() {
            return escape(value);
        }
    }

    public static final class Bold implements Element {
        private final String value;
        public Bold(String value) { this.value = value; }
        @Override public String toMarkdown() { return "**" + value + "**"; }
    }

    private static String escape(String s) {
        return s;
    }

    private Text() { }
}
