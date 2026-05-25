package ru.nsu.smolin.checker.pipeline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;
import ru.nsu.smolin.checker.tools.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;

class GitCloneStepTest {
    @Test
    void okWhenGitClientSucceeds(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "", "", false));                       // ls-remote
        runner.queue(new ProcessResult(0, "", "", false));                       // clone
        runner.queue(new ProcessResult(0, "2025-09-12T10:30:00+03:00", "", false)); // git log
        GitClient git = new GitClient(runner, Duration.ofSeconds(10));
        GitCloneStep step = new GitCloneStep(git, tmp);

        StepResult r = step.run(ctx());

        assertEquals(Status.OK, r.status());
        assertEquals(LocalDate.of(2025, 9, 12), r.data().get("submissionDate"));
    }

    @Test
    void failWhenGitClientFails(@TempDir Path tmp) {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(128, "", "auth", false));
        GitClient git = new GitClient(runner, Duration.ofSeconds(10));
        GitCloneStep step = new GitCloneStep(git, tmp);

        StepResult r = step.run(ctx());

        assertEquals(Status.FAIL, r.status());
        assertTrue(r.log().contains("auth"));
    }

    private static StepContext ctx() {
        return new StepContext(
            new Student("u", "U", "https://example/repo.git"),
            new Task("t", "T", 3, LocalDate.now(), LocalDate.now(), "."),
            Path.of("ignored"), new LinkedHashMap<>());
    }
}
