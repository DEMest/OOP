package ru.nsu.smolin.checker.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CheckstyleRunnerTest {
    @Test
    void invokesJavaWithJarAndConfig(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "Audit done.", "", false));
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(2), tmp);

        Path src = tmp.resolve("src/main/java");
        ProcessResult r = cs.check(src);

        assertTrue(r.ok());
        List<String> cmd = runner.calls().get(0);
        assertEquals("java", cmd.get(0));
        assertTrue(cmd.contains("-jar"));
        assertTrue(cmd.stream().anyMatch(s -> s.endsWith("checkstyle-10.17.0-all.jar")));
        assertTrue(cmd.stream().anyMatch(s -> s.endsWith("google_checks.xml")));
        assertTrue(cmd.contains(src.toString()));
    }

    @Test
    void extractsResourcesOnFirstUse(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "", "", false));
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(2), tmp);

        cs.check(tmp);

        assertTrue(java.nio.file.Files.isRegularFile(tmp.resolve("checkstyle-10.17.0-all.jar")));
        assertTrue(java.nio.file.Files.isRegularFile(tmp.resolve("google_checks.xml")));
    }
}
