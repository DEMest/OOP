package ru.nsu.smolin.table;

import ru.nsu.smolin.Element;

/**
 * Простые ссылочные элементы: ссылка и изображение.
 */
public final class LinkElements {

    /**
     * <p>Гиперссылка в формате <code>[текст](url)</code>.</p>
     */
    public static final class Link implements Element {
        private final String label;
        private final String url;
        private final String title;

        public Link(String label, String url) {
            this(label, url, null);
        }

        public Link(String label, String url, String title) {
            this.label = label;
            this.url = url;
            this.title = title;
        }

        @Override
        public String toMarkdown() {
            if (title == null || title.isEmpty()) {
                return "[" + label + "](" + url + ")";
            }
            return "[" + label + "](" + url + " \"" + title + "\")";
        }
    }

    /**
     * <p>Изображение в формате de>![alt](url)</code>.</p>
     */
    public static final class Image implements Element {
        private final String alt;
        private final String url;

        public Image(String alt, String url) {
            this.alt = alt;
            this.url = url;
        }

        @Override
        public String toMarkdown() {
            return "![" + alt + "](" + url + ")";
        }
    }

    private LinkElements() {
    }
}