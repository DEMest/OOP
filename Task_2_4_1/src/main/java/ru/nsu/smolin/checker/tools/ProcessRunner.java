package ru.nsu.smolin.checker.tools;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface ProcessRunner {
    ProcessResult run(List<String> command, Path workDir, Duration timeout, Map<String, String> env);

    default ProcessResult run(List<String> command, Path workDir, Duration timeout) {
        return run(command, workDir, timeout, Map.of());
    }
}
