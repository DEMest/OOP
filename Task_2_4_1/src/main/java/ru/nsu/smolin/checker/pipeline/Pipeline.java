package ru.nsu.smolin.checker.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Последовательный исполнитель шагов проверки.
 * Шаги выполняются в порядке передачи; если шаг падает, последующие шаги, отметившие зависимость
 * через {@link Step#skipIfFailed}, получают статус SKIPPED.
 * Любое исключение из шага ловится и превращается в результат FAIL — один студент не должен ронять весь прогон.
 */
public final class Pipeline {
    private static final Logger log = LoggerFactory.getLogger(Pipeline.class);

    private final List<Step> steps;

    public Pipeline(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * Выполняет все шаги пайплайна для одной пары (студент, задача).
     *
     * @param base начальный контекст (с пустой картой previous)
     * @return карта name → результат для каждого шага в порядке выполнения
     */
    public Map<String, StepResult> run(StepContext base) {
        Map<String, StepResult> results = new LinkedHashMap<>();
        for (Step s : steps) {
            boolean shouldSkip = false;
            for (Map.Entry<String, StepResult> e : results.entrySet()) {
                if (e.getValue().status() == Status.FAIL && s.skipIfFailed(e.getKey())) {
                    shouldSkip = true;
                    break;
                }
            }
            if (shouldSkip) {
                results.put(s.name(), StepResult.skipped());
                continue;
            }
            StepContext ctx = new StepContext(base.student(), base.task(), base.repoRoot(), results);
            StepResult r;
            try {
                r = s.run(ctx);
            } catch (Throwable t) {
                StringWriter w = new StringWriter();
                t.printStackTrace(new PrintWriter(w));
                log.error("step {} threw", s.name(), t);
                r = StepResult.fail(w.toString());
            }
            results.put(s.name(), r);
        }
        return results;
    }
}
