package ru.nsu.smolin.checker.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GradleClientTest {
    @Test
    void prefersGradlewWhenPresent(@TempDir Path tmp) throws IOException {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        String wrapperName = win ? "gradlew.bat" : "gradlew";
        Files.writeString(tmp.resolve(wrapperName), "");

        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "BUILD SUCCESSFUL", "", false));
        GradleClient g = new GradleClient(runner, Duration.ofMinutes(1));

        ProcessResult r = g.run(tmp, List.of("test"));

        assertTrue(r.ok());
        String first = runner.calls().get(0).get(0);
        assertTrue(first.endsWith(wrapperName), "expected wrapper, got " + first);
    }

    @Test
    void fallsBackToSystemGradle(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "BUILD SUCCESSFUL", "", false));
        GradleClient g = new GradleClient(runner, Duration.ofMinutes(1));

        g.run(tmp, List.of("build"));

        assertEquals("gradle", runner.calls().get(0).get(0));
    }
}
