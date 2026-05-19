package ru.nsu.smolin.checker.pipeline;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.checker.model.Bonus;
import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GradeStepTest {
    @Test
    void allOkBeforeSoftDeadline_givesThreePlusBonusCappedAtMax() {
        StepContext ctx = ctx(allOk(), today().plusDays(7), today().plusDays(14));
        GradeStep step = new GradeStep(today(), List.of(
            new Bonus("u", "t", 5, "x")
        ));

        StepResult r = step.run(ctx);

        assertEquals(Status.OK, r.status());
        assertEquals(3, r.data().get("score"));
        assertEquals(5, r.data().get("bonus"));
        // base 3 + bonus 5 = 8, capped at maxPoints=3 → score field stays 3 (we cap after sum)
    }

    @Test
    void betweenDeadlines_halvesBaseScore() {
        StepContext ctx = ctx(allOk(), today().minusDays(2), today().plusDays(2));
        GradeStep step = new GradeStep(today(), List.of());
        StepResult r = step.run(ctx);
        // base = 3 * 0.5 = 1.5 → floor → 1
        assertEquals(1, r.data().get("score"));
    }

    @Test
    void afterHardDeadline_zeroBaseButBonusStillApplied() {
        StepContext ctx = ctx(allOk(), today().minusDays(10), today().minusDays(5));
        GradeStep step = new GradeStep(today(), List.of(new Bonus("u", "t", 1, "x")));
        StepResult r = step.run(ctx);
        // base 0, bonus 1, capped at 3 → 1
        assertEquals(0, r.data().get("score"));
        assertEquals(1, r.data().get("bonus"));
    }

    @Test
    void buildFailGivesZeroBase() {
        Map<String, StepResult> prev = base();
        prev.put(BuildStep.NAME, StepResult.fail("x"));
        prev.put(StyleStep.NAME, StepResult.skipped());
        prev.put(JavadocStep.NAME, StepResult.skipped());
        prev.put(TestsStep.NAME, StepResult.skipped());
        StepContext ctx = ctxFromPrev(prev, today().plusDays(1), today().plusDays(2));
        StepResult r = new GradeStep(today(), List.of()).run(ctx);
        assertEquals(0, r.data().get("score"));
    }

    @Test
    void javadocResultIsIgnoredByFormula() {
        Map<String, StepResult> prev = base();
        prev.put(BuildStep.NAME, StepResult.ok());
        prev.put(StyleStep.NAME, StepResult.ok());
        prev.put(JavadocStep.NAME, StepResult.fail("x"));
        prev.put(TestsStep.NAME, new StepResult(Status.OK,
            Map.of("passed", 5, "failed", 0, "skipped", 0), ""));
        StepContext ctx = ctxFromPrev(prev, today().plusDays(1), today().plusDays(2));
        StepResult r = new GradeStep(today(), List.of()).run(ctx);
        // build(+1) + style(+1) + tests(+1) = 3, javadoc fail must NOT subtract
        assertEquals(3, r.data().get("score"));
    }

    @Test
    void zeroPassedTests_noTestsBonus() {
        Map<String, StepResult> prev = base();
        prev.put(BuildStep.NAME, StepResult.ok());
        prev.put(StyleStep.NAME, StepResult.ok());
        prev.put(TestsStep.NAME, new StepResult(Status.OK,
            Map.of("passed", 0, "failed", 3, "skipped", 0), ""));
        StepContext ctx = ctxFromPrev(prev, today().plusDays(1), today().plusDays(2));
        StepResult r = new GradeStep(today(), List.of()).run(ctx);
        // build(+1) + style(+1) + tests(0) = 2
        assertEquals(2, r.data().get("score"));
    }

    private static LocalDate today() { return LocalDate.of(2026, 5, 3); }

    private static Map<String, StepResult> base() {
        Map<String, StepResult> m = new LinkedHashMap<>();
        m.put(GitCloneStep.NAME, new StepResult(Status.OK, Map.of("repoPath", "/x"), ""));
        return m;
    }

    private static Map<String, StepResult> allOk() {
        Map<String, StepResult> m = base();
        m.put(BuildStep.NAME, StepResult.ok());
        m.put(StyleStep.NAME, StepResult.ok());
        m.put(JavadocStep.NAME, StepResult.ok());
        m.put(TestsStep.NAME, new StepResult(Status.OK,
            Map.of("passed", 5, "failed", 0, "skipped", 0), ""));
        return m;
    }

    private static StepContext ctx(Map<String, StepResult> prev, LocalDate soft, LocalDate hard) {
        return ctxFromPrev(prev, soft, hard);
    }

    private static StepContext ctxFromPrev(Map<String, StepResult> prev, LocalDate soft, LocalDate hard) {
        return new StepContext(
            new Student("u", "U", "url"),
            new Task("t", "T", 3, soft, hard, "."),
            Path.of("/x"), prev);
    }
}
