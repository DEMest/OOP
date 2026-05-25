package ru.nsu.smolin.checker.tools;

public record ProcessResult(int exitCode, String stdout, String stderr, boolean timedOut) {
    public boolean ok() { return exitCode == 0 && !timedOut; }
}
