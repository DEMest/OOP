package ru.nsu.smolin.checker.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Duration;
import java.util.List;

public final class CheckstyleRunner {
    private static final String JAR = "checkstyle-10.17.0-all.jar";
    private static final String CFG = "google_checks.xml";

    private final ProcessRunner runner;
    private final Duration timeout;
    private final Path cacheDir;

    public CheckstyleRunner(ProcessRunner runner, Duration timeout, Path cacheDir) {
        this.runner = runner;
        this.timeout = timeout;
        this.cacheDir = cacheDir;
    }

    public ProcessResult check(Path sourceRoot) {
        try {
            extractIfMissing(JAR);
            extractIfMissing(CFG);
        } catch (IOException e) {
            return new ProcessResult(-1, "", "extract failed: " + e.getMessage(), false);
        }

        return runner.run(
            List.of("java", "-jar", cacheDir.resolve(JAR).toString(),
                    "-c", cacheDir.resolve(CFG).toString(),
                    "-f", "xml", sourceRoot.toString()),
            cacheDir, timeout);
    }

    private void extractIfMissing(String name) throws IOException {
        Path dest = cacheDir.resolve(name);
        if (Files.isRegularFile(dest)) return;
        Files.createDirectories(cacheDir);
        try (InputStream in = CheckstyleRunner.class.getResourceAsStream("/tools/" + name)) {
            if (in == null) throw new IOException("missing resource /tools/" + name);
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
