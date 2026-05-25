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

class TestsStepTest {
    @Test
    void parsesCountsFromXmlWhenGradleRuns(@TempDir Path tmp) throws IOException {
        Path xml = tmp.resolve("build/test-results/test");
        Files.createDirectories(xml);
        Files.writeString(xml.resolve("a.xml"), """
            <?xml version="1.0"?>
            <testsuite tests="2" failures="1" errors="0" skipped="0">
              <testcase name="ok"/>
              <testcase name="bad"><failure/></testcase>
            </testsuite>
            """);

        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(1, "FAILED", "", false)); // gradle returns non-zero on test failures, that's expected
        TestsStep step = new TestsStep(new GradleClient(runner, Duration.ofMinutes(1)));

        StepResult r = step.run(ctx(tmp));

        assertEquals(1, r.data().get("passed"));
        assertEquals(1, r.data().get("failed"));
        assertEquals(0, r.data().get("skipped"));
        assertEquals(Status.OK, r.status()); // OK because reports parsed; failed-count is what determines grade
    }

    @Test
    void failWhenNoReports(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(1, "no tests defined", "", false));
        TestsStep step = new TestsStep(new GradleClient(runner, Duration.ofMinutes(1)));

        StepResult r = step.run(ctx(tmp));

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
