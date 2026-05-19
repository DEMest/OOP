package ru.nsu.smolin.checker.tools;

import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public final class FakeProcessRunner implements ProcessRunner {
    private final Deque<ProcessResult> queued = new ArrayDeque<>();
    private final List<List<String>> calls = new ArrayList<>();

    public void queue(ProcessResult r) { queued.add(r); }
    public List<List<String>> calls() { return calls; }

    @Override
    public ProcessResult run(List<String> command, Path workDir, Duration timeout, Map<String, String> env) {
        calls.add(List.copyOf(command));
        if (queued.isEmpty()) {
            throw new AssertionError("no queued result for: " + command);
        }
        return queued.pollFirst();
    }
}
