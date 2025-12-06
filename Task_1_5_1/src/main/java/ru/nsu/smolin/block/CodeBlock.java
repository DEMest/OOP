package ru.nsu.smolin.block;

import ru.nsu.smolin.Element;

/**
 * Многострочный блок кода.
 */
public final class CodeBlock implements Element {

    private final String language;
    private final String code;

    public CodeBlock(String language, String code) {
        this.language = language;
        this.code = code;
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("```");
        if (language != null && !language.isEmpty()) {
            sb.append(language);
        }
        sb.append('\n');
        sb.append(code);
        sb.append('\n');
        sb.append("```");
        return sb.toString();
    }
}
