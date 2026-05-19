package ru.nsu.smolin.checker.pipeline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;
import ru.nsu.smolin.checker.tools.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class JavadocStepTest {
    @Test
    void okWhenJavadocSucceeds(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "BUILD SUCCESSFUL", "", false));
        JavadocStep step = new JavadocStep(new GradleClient(runner, Duration.ofMinutes(1)));

        StepResult r = step.run(ctx(tmp));

        assertEquals(Status.OK, r.status());
        assertTrue(runner.calls().get(0).contains("javadoc"));
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
