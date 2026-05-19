package ru.nsu.smolin.checker.pipeline;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PipelineTest {
    private static StepContext ctx() {
        return new StepContext(
            new Student("u", "U", "url"),
            new Task("t1", "T", 3, LocalDate.now(), LocalDate.now(), "."),
            Path.of("."), new LinkedHashMap<>());
    }

    @Test
    void runsAllStepsInOrder() {
        List<String> order = new ArrayList<>();
        Step a = step("A", c -> { order.add("A"); return StepResult.ok(); }, false);
        Step b = step("B", c -> { order.add("B"); return StepResult.ok(); }, false);

        Map<String, StepResult> results = new Pipeline(List.of(a, b)).run(ctx());

        assertEquals(List.of("A", "B"), order);
        assertEquals(Status.OK, results.get("A").status());
        assertEquals(Status.OK, results.get("B").status());
    }

    @Test
    void skipsDependentStepWhenPredecessorFails() {
        Step a = step("A", c -> StepResult.fail("boom"), false);
        Step b = step("B", c -> StepResult.ok(), true);

        Map<String, StepResult> results = new Pipeline(List.of(a, b)).run(ctx());

        assertEquals(Status.FAIL, results.get("A").status());
        assertEquals(Status.SKIPPED, results.get("B").status());
    }

    @Test
    void independentStepRunsEvenIfOtherFailed() {
        Step a = step("A", c -> StepResult.fail("x"), false);
        Step b = step("B", c -> StepResult.ok(), false);

        Map<String, StepResult> results = new Pipeline(List.of(a, b)).run(ctx());

        assertEquals(Status.OK, results.get("B").status());
    }

    @Test
    void exceptionInStepBecomesFail() {
        Step a = step("A", c -> { throw new RuntimeException("oops"); }, false);
        Map<String, StepResult> results = new Pipeline(List.of(a)).run(ctx());
        assertEquals(Status.FAIL, results.get("A").status());
        assertTrue(results.get("A").log().contains("oops"));
    }

    private static Step step(String n, java.util.function.Function<StepContext, StepResult> body, boolean skipOnAnyPrevFail) {
        return new Step() {
            @Override public String name() { return n; }
            @Override public StepResult run(StepContext c) { return body.apply(c); }
            @Override public boolean skipIfFailed(String previous) { return skipOnAnyPrevFail; }
        };
    }
}
