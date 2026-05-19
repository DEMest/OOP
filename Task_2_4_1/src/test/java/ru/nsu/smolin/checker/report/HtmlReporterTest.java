package ru.nsu.smolin.checker.report;

import org.junit.jupiter.api.Test;
import ru.nsu.smolin.checker.model.*;
import ru.nsu.smolin.checker.pipeline.*;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class HtmlReporterTest {
    @Test
    void containsHeaderAndPerTaskTable() {
        CheckerConfig cfg = sampleConfig();
        List<TaskReport> reports = sampleReports(cfg);

        String html = new HtmlReporter().render(cfg, reports);

        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(html.contains("Группа G1"));
        assertTrue(html.contains("Лабораторная T1 (Первая)"));
        assertTrue(html.contains("DEMest"));
        assertTrue(html.contains("Сборка"));
        assertTrue(html.contains("Тесты"));
    }

    @Test
    void escapesHtmlInStudentNames() {
        CheckerConfig cfg = sampleConfigWithName("<script>alert(1)</script>");
        String html = new HtmlReporter().render(cfg, List.of());
        assertFalse(html.contains("<script>alert"));
        assertTrue(html.contains("&lt;script&gt;"));
    }

    @Test
    void rendersCheckpointTablesWithCumulativeScores() {
        // Two tasks: T1 due 2025-09-22, T2 due 2025-10-06.
        // Two checkpoints: CP1=2025-10-01 (only T1 counts), CP2=2025-11-01 (both count).
        // Student submitted T1 on 2025-09-20 (score 1) and T2 on 2025-10-05 (score 1).
        Map<String, Task> tasks = new LinkedHashMap<>();
        tasks.put("T1", new Task("T1", "Первая", 1,
            LocalDate.of(2025, 9, 15), LocalDate.of(2025, 9, 22), "T1"));
        tasks.put("T2", new Task("T2", "Вторая", 1,
            LocalDate.of(2025, 9, 29), LocalDate.of(2025, 10, 6), "T2"));
        Student s = new Student("DEMest", "Daniel", "url");
        Group g = new Group("G1", List.of(s));
        List<Checkpoint> cps = List.of(
            new Checkpoint("CP1", LocalDate.of(2025, 10, 1)),
            new Checkpoint("CP2", LocalDate.of(2025, 11, 1)));
        CheckerConfig cfg = new CheckerConfig(
            tasks, List.of(g),
            List.of(new Assignment("DEMest", List.of("T1", "T2"))),
            List.of(), cps, Settings.defaults());

        List<TaskReport> reports = List.of(
            reportFor(s, tasks.get("T1"), LocalDate.of(2025, 9, 20), 1),
            reportFor(s, tasks.get("T2"), LocalDate.of(2025, 10, 5), 1));

        String html = new HtmlReporter().render(cfg, reports);

        assertTrue(html.contains("Контрольная точка CP1"), "CP1 header missing");
        assertTrue(html.contains("Контрольная точка CP2"), "CP2 header missing");
        // CP1: only T1 due → student has 1 / 1
        // CP2: both T1 and T2 due → student has 2 / 2
        int cp1 = html.indexOf("CP1");
        int cp2 = html.indexOf("CP2");
        String cp1Section = html.substring(cp1, cp2);
        String cp2Section = html.substring(cp2);
        assertTrue(cp1Section.contains(">1<"), "CP1 cumulative score should be 1");
        assertTrue(cp2Section.contains(">2<"), "CP2 cumulative score should be 2");
    }

    private static TaskReport reportFor(Student s, Task t, LocalDate submittedOn, int total) {
        Map<String, StepResult> steps = new LinkedHashMap<>();
        Map<String, Object> cloneData = new LinkedHashMap<>();
        cloneData.put("repoPath", "/x");
        cloneData.put("submissionDate", submittedOn);
        steps.put(GitCloneStep.NAME, new StepResult(Status.OK, cloneData, ""));
        steps.put(BuildStep.NAME, StepResult.ok());
        steps.put(StyleStep.NAME, StepResult.ok());
        steps.put(TestsStep.NAME, new StepResult(Status.OK,
            Map.of("passed", 1, "failed", 0, "skipped", 0), ""));
        steps.put(GradeStep.NAME, new StepResult(Status.OK,
            Map.of("score", total, "bonus", 0, "total", total), ""));
        return new TaskReport(s, t, steps, total, 0);
    }

    private static CheckerConfig sampleConfig() {
        return sampleConfigWithName("Daniel Smolin");
    }

    private static CheckerConfig sampleConfigWithName(String name) {
        Map<String, Task> tasks = new LinkedHashMap<>();
        tasks.put("T1", new Task("T1", "Первая", 3,
            LocalDate.now().plusDays(7), LocalDate.now().plusDays(14), "."));
        Group g = new Group("G1", List.of(new Student("DEMest", name, "url")));
        return new CheckerConfig(
            tasks, List.of(g),
            List.of(new Assignment("DEMest", List.of("T1"))),
            List.of(), List.of(),
            Settings.defaults());
    }

    private static List<TaskReport> sampleReports(CheckerConfig cfg) {
        Task t = cfg.tasks().get("T1");
        Student s = cfg.groups().get(0).students().get(0);
        Map<String, StepResult> steps = new LinkedHashMap<>();
        steps.put(GitCloneStep.NAME, StepResult.ok());
        steps.put(BuildStep.NAME, StepResult.ok());
        steps.put(StyleStep.NAME, StepResult.ok());
        steps.put(JavadocStep.NAME, StepResult.ok());
        steps.put(TestsStep.NAME, new StepResult(Status.OK,
            Map.of("passed", 4, "failed", 0, "skipped", 0), ""));
        steps.put(GradeStep.NAME, new StepResult(Status.OK,
            Map.of("score", 3, "bonus", 0, "total", 3), ""));
        return List.of(new TaskReport(s, t, steps, 3, 0));
    }
}
