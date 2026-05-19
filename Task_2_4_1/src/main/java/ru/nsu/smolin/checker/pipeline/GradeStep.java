package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.model.Bonus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Финальный шаг пайплайна: вычисляет балл за задачу по формуле курса.
 * <p>База: {@code +1} за успешную сборку, {@code +1} за успешный style guide,
 * {@code +1} если прошёл хотя бы один тест. Множитель дедлайна: до soft — {@code 1.0},
 * между soft и hard — {@code 0.5}, после hard — {@code 0.0}; в качестве даты сдачи берётся
 * {@code submissionDate} из {@link GitCloneStep} (дата автора последнего коммита в папку задачи),
 * либо {@code today} как фоллбек. К результату прибавляются бонусы для этой пары студент+задача,
 * затем сумма ограничивается {@link ru.nsu.smolin.checker.model.Task#maxPoints()}.</p>
 */
public final class GradeStep implements Step {
    public static final String NAME = "grade";

    private final LocalDate today;
    private final List<Bonus> bonuses;

    public GradeStep(LocalDate today, List<Bonus> bonuses) {
        this.today = today;
        this.bonuses = bonuses;
    }

    @Override public String name() { return NAME; }

    @Override public StepResult run(StepContext ctx) {
        int base = 0;

        boolean buildOk = statusOk(ctx, BuildStep.NAME);
        if (buildOk) base++;

        if (statusOk(ctx, StyleStep.NAME)) base++;

        StepResult tests = ctx.previous().get(TestsStep.NAME);
        if (tests != null && tests.status() == Status.OK) {
            int passed = ((Number) tests.data().getOrDefault("passed", 0)).intValue();
            if (passed > 0) base++;
        }

        LocalDate effectiveDate = today;
        StepResult clone = ctx.previous().get(GitCloneStep.NAME);
        if (clone != null) {
            Object submitted = clone.data().get("submissionDate");
            if (submitted instanceof LocalDate) {
                effectiveDate = (LocalDate) submitted;
            }
        }
        double mult = deadlineMultiplier(ctx.task().softDeadline(), ctx.task().hardDeadline(), effectiveDate);
        int adjusted = (int) Math.floor(base * mult);

        int bonus = bonuses.stream()
            .filter(b -> b.studentGithub().equals(ctx.student().github())
                      && b.taskId().equals(ctx.task().id()))
            .mapToInt(Bonus::points).sum();

        int score = Math.min(adjusted + bonus, ctx.task().maxPoints());
        Map<String, Object> data = new HashMap<>();
        data.put("score", Math.min(adjusted, ctx.task().maxPoints()));
        data.put("bonus", bonus);
        data.put("total", score);
        return new StepResult(Status.OK, data, "");
    }

    private boolean statusOk(StepContext ctx, String name) {
        StepResult r = ctx.previous().get(name);
        return r != null && r.status() == Status.OK;
    }

    private double deadlineMultiplier(LocalDate soft, LocalDate hard, LocalDate on) {
        if (!on.isAfter(soft)) return 1.0;
        if (!on.isAfter(hard)) return 0.5;
        return 0.0;
    }
}
