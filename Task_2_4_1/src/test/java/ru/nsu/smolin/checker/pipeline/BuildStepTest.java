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

class BuildStepTest {
    @Test
    void okWhenGradleSucceeds(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "BUILD SUCCESSFUL", "", false));
        GradleClient g = new GradleClient(runner, Duration.ofMinutes(1));

        StepResult r = new BuildStep(g).run(ctx(tmp));

        assertEquals(Status.OK, r.status());
        List<String> cmd = runner.calls().get(0);
        assertTrue(cmd.contains("build"));
        assertTrue(cmd.contains("-x"));
        assertTrue(cmd.contains("test"));
    }

    @Test
    void failWhenGradleNonZero(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(1, "", "compile error", false));
        GradleClient g = new GradleClient(runner, Duration.ofMinutes(1));

        StepResult r = new BuildStep(g).run(ctx(tmp));

        assertEquals(Status.FAIL, r.status());
    }

    static StepContext ctx(Path tmp) {
        Map<String, StepResult> prev = new LinkedHashMap<>();
        prev.put(GitCloneStep.NAME, new StepResult(Status.OK, Map.of("repoPath", tmp.toString()), ""));
        return new StepContext(
            new Student("u", "U", "url"),
            new Task("t", "T", 3, LocalDate.now(), LocalDate.now(), "."),
            tmp, prev);
    }
}
