package ru.nsu.smolin.checker.pipeline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;
import ru.nsu.smolin.checker.tools.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class StyleStepTest {
    @Test
    void okWhenCheckstyleExitZero(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("src/main/java");
        Files.createDirectories(src);

        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "Audit done.", "", false));
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(1), tmp.resolve(".cache"));

        StepResult r = new StyleStep(cs).run(ctx(tmp));

        assertEquals(Status.OK, r.status());
    }

    @Test
    void failWhenCheckstyleNonZero(@TempDir Path tmp) throws IOException {
        Path src = tmp.resolve("src/main/java");
        Files.createDirectories(src);

        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(1, "violations", "", false));
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(1), tmp.resolve(".cache"));

        StepResult r = new StyleStep(cs).run(ctx(tmp));

        assertEquals(Status.FAIL, r.status());
    }

    @Test
    void skippedWhenNoSourceDir(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(1), tmp.resolve(".cache"));
        StepResult r = new StyleStep(cs).run(ctx(tmp));
        assertEquals(Status.FAIL, r.status());
    }

    static StepContext ctx(Path tmp) {
        Map<String, StepResult> prev = new LinkedHashMap<>();
        prev.put(GitCloneStep.NAME, new StepResult(Status.OK, Map.of("repoPath", tmp.toString()), ""));
        prev.put(BuildStep.NAME, StepResult.ok());
        return new StepContext(
            new Student("u", "U", "url"),
            new Task("t", "T", 3, LocalDate.now(), LocalDate.now(), "."),
            tmp, prev);
    }
}
