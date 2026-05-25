package ru.nsu.smolin.checker.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class GradleClient {
    private final ProcessRunner runner;
    private final Duration timeout;

    public GradleClient(ProcessRunner runner, Duration timeout) {
        this.runner = runner;
        this.timeout = timeout;
    }

    public ProcessResult run(Path projectDir, List<String> tasks) {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        String wrapperName = win ? "gradlew.bat" : "gradlew";
        Path wrapper = projectDir.resolve(wrapperName);

        List<String> cmd = new ArrayList<>();
        cmd.add(Files.isRegularFile(wrapper) ? wrapper.toAbsolutePath().toString() : "gradle");
        cmd.add("--no-daemon");
        cmd.add("--console=plain");
        cmd.addAll(tasks);

        return runner.run(cmd, projectDir, timeout);
    }
}
