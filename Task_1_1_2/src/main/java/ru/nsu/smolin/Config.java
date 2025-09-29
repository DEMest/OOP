package ru.nsu.smolin;

/**
 * Config to colorise text.
 *
 */
public final class Config {
    /**
     * Base constructor, not need input
     * behave like data class.
     *
     */
    private Config() {}
    public static final String RESET = "\u001B[0m";
    public static final String RED   = "\u001B[31m";
    public static final String BLACK = "\u001B[30m";
}
