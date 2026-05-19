package ru.nsu.smolin.checker.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.smolin.checker.dsl.ConfigLoader;
import ru.nsu.smolin.checker.model.*;
import ru.nsu.smolin.checker.pipeline.*;
import ru.nsu.smolin.checker.report.HtmlReporter;
import ru.nsu.smolin.checker.tools.*;

import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Оркестратор приложения: загружает DSL-конфигурацию, формирует пайплайн,
 * прогоняет его для каждой пары (студент, задача) из блока {@code assignment}
 * и рендерит итоговый HTML-отчёт в указанный поток.
 */
public final class CheckerApp {
    private static final Logger log = LoggerFactory.getLogger(CheckerApp.class);

    private final Path workDir;
    private final PrintStream out;

    public CheckerApp(Path workDir, PrintStream out) {
        this.workDir = workDir;
        this.out = out;
    }

    /**
     * Выполняет команду {@code test}: читает {@code checker.groovy} из рабочей директории,
     * прогоняет проверки и пишет HTML-отчёт в {@code out}.
     *
     * @return код возврата процесса: 0 — успех, 2 — ошибка загрузки конфигурации.
     */
    public int test() {
        Path script = workDir.resolve("checker.groovy");
        CheckerConfig cfg;
        try {
            cfg = ConfigLoader.load(script);
        } catch (Exception e) {
            System.err.println("error: failed to load " + script + ": " + e.getMessage());
            return 2;
        }

        Path reposRoot = workDir.resolve(".oop-checker/repos");
        Path csCache   = workDir.resolve(".oop-checker/cache");

        ProcessRunner runner = new RealProcessRunner();
        GitClient git = new GitClient(runner, Duration.ofMinutes(2));
        GradleClient gradle = new GradleClient(runner, cfg.settings().testTimeout());
        CheckstyleRunner cs = new CheckstyleRunner(runner, Duration.ofMinutes(2), csCache);

        List<Step> steps = List.of(
            new GitCloneStep(git, reposRoot),
            new BuildStep(gradle),
            new StyleStep(cs),
            new TestsStep(gradle),
            new GradeStep(LocalDate.now(), cfg.bonuses())
        );
        Pipeline pipe = new Pipeline(steps);

        List<TaskReport> reports = new ArrayList<>();
        for (Assignment a : cfg.assignments()) {
            Student student = findStudent(cfg, a.studentGithub());
            if (student == null) {
                log.warn("unknown student in assignment: {}", a.studentGithub());
                continue;
            }
            for (String taskId : a.taskIds()) {
                Task task = cfg.tasks().get(taskId);
                if (task == null) {
                    log.warn("unknown task in assignment: {}", taskId);
                    continue;
                }
                log.info("checking {} on {}", student.github(), taskId);
                StepContext base = new StepContext(student, task, reposRoot.resolve(student.github()), java.util.Map.of());
                var results = pipe.run(base);
                int total = ((Number) results.get(GradeStep.NAME).data().getOrDefault("total", 0)).intValue();
                int bonus = ((Number) results.get(GradeStep.NAME).data().getOrDefault("bonus", 0)).intValue();
                reports.add(new TaskReport(student, task, results, total - bonus, bonus));
            }
        }

        out.print(new HtmlReporter().render(cfg, reports));
        return 0;
    }

    private static Student findStudent(CheckerConfig cfg, String github) {
        for (Group g : cfg.groups()) {
            for (Student s : g.students()) {
                if (s.github().equals(github)) return s;
            }
        }
        return null;
    }
}
