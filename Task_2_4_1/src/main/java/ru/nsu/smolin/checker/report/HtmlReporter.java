package ru.nsu.smolin.checker.report;

import ru.nsu.smolin.checker.model.*;
import ru.nsu.smolin.checker.pipeline.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Рендерер итогового HTML-отчёта.
 * Без шаблонизатора: собирается {@link StringBuilder}'ом с inline-CSS.
 * Для каждой группы выводит: таблицы по отдельным задачам, общую статистику и таблицы по контрольным точкам.
 */
public final class HtmlReporter {

    /**
     * Формирует полную HTML-страницу отчёта.
     *
     * @param cfg     загруженная конфигурация (определяет состав групп, задач, контрольных точек)
     * @param reports список результатов пайплайна по парам студент+задача
     * @return строка с готовым HTML-документом
     */
    public String render(CheckerConfig cfg, List<TaskReport> reports) {
        StringBuilder b = new StringBuilder();
        b.append("<!doctype html>\n<html><head><meta charset=\"utf-8\">\n")
         .append("<style>\n")
         .append("body{font-family:sans-serif;margin:24px}\n")
         .append("h2{margin-top:32px}\n")
         .append("table{border-collapse:collapse;margin-bottom:16px}\n")
         .append("th,td{border:1px solid #999;padding:4px 10px;text-align:center}\n")
         .append("th{background:#eee}\n")
         .append(".ok{color:#070}.bad{color:#a00}\n")
         .append("</style>\n")
         .append("<title>Отчёт чекера</title>\n</head><body>\n")
         .append("<h1>Отчёт чекера от ")
         .append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
         .append("</h1>\n");

        for (Group g : cfg.groups()) {
            b.append("<h2>Группа ").append(esc(g.name())).append("</h2>\n");

            Set<String> taskIds = new LinkedHashSet<>();
            Set<String> ghs = new HashSet<>();
            for (Student s : g.students()) {
                ghs.add(s.github());
            }
            for (Assignment a : cfg.assignments()) {
                if (ghs.contains(a.studentGithub())) {
                    taskIds.addAll(a.taskIds());
                }
            }

            for (String tid : taskIds) {
                Task t = cfg.tasks().get(tid);
                if (t == null) continue;
                b.append("<h3>Лабораторная ").append(esc(t.id()))
                 .append(" (").append(esc(t.title())).append(")</h3>\n");
                b.append("<table><tr>")
                 .append("<th>Студент</th><th>Сборка</th>")
                 .append("<th>Style guide</th><th>Тесты</th><th>Сдано</th><th>Доп. балл</th><th>Общий балл</th>")
                 .append("</tr>\n");
                for (Student s : g.students()) {
                    TaskReport r = find(reports, s.github(), tid);
                    b.append("<tr>")
                     .append("<td>").append(esc(s.github())).append(" ").append(esc(s.name())).append("</td>")
                     .append(statusCell(r, BuildStep.NAME))
                     .append(statusCell(r, StyleStep.NAME))
                     .append("<td>").append(testsText(r)).append("</td>")
                     .append("<td>").append(submissionText(r)).append("</td>")
                     .append("<td>").append(numberField(r, GradeStep.NAME, "bonus")).append("</td>")
                     .append("<td>").append(numberField(r, GradeStep.NAME, "total")).append("</td>")
                     .append("</tr>\n");
                }
                b.append("</table>\n");
            }

            b.append("<h3>Общая статистика группы ").append(esc(g.name())).append("</h3>\n");
            b.append("<table><tr><th>Студент</th>");
            for (String tid : taskIds) b.append("<th>").append(esc(tid)).append("</th>");
            b.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>\n");
            int max = totalMax(cfg, taskIds);
            for (Student s : g.students()) {
                int sum = 0;
                b.append("<tr><td>").append(esc(s.github())).append(" ").append(esc(s.name())).append("</td>");
                for (String tid : taskIds) {
                    TaskReport r = find(reports, s.github(), tid);
                    int val = totalOf(r);
                    sum += val;
                    b.append("<td>").append(val).append("</td>");
                }
                b.append("<td>").append(sum).append("</td>")
                 .append("<td>—</td>")
                 .append("<td>").append(grade(sum, max, cfg.settings())).append("</td>")
                 .append("</tr>\n");
            }
            b.append("</table>\n");

            for (Checkpoint cp : cfg.checkpoints()) {
                renderCheckpoint(b, cp, g, taskIds, cfg, reports);
            }
        }

        b.append("</body></html>\n");
        return b.toString();
    }

    private static void renderCheckpoint(StringBuilder b, Checkpoint cp, Group g,
                                         Set<String> taskIds, CheckerConfig cfg,
                                         List<TaskReport> reports) {
        b.append("<h3>Контрольная точка ").append(esc(cp.name()))
         .append(" (").append(cp.date()).append(")</h3>\n");
        b.append("<table><tr><th>Студент</th><th>Сумма</th><th>Максимум к дате</th><th>Оценка</th></tr>\n");

        int maxByDate = 0;
        for (String tid : taskIds) {
            Task t = cfg.tasks().get(tid);
            if (t == null) continue;
            if (!t.hardDeadline().isAfter(cp.date())) {
                maxByDate += t.maxPoints();
            }
        }

        for (Student s : g.students()) {
            int sum = 0;
            for (String tid : taskIds) {
                TaskReport r = find(reports, s.github(), tid);
                LocalDate submitted = submissionDateOf(r);
                if (submitted != null && !submitted.isAfter(cp.date())) {
                    sum += totalOf(r);
                }
            }
            b.append("<tr><td>").append(esc(s.github())).append(" ").append(esc(s.name())).append("</td>")
             .append("<td>").append(sum).append("</td>")
             .append("<td>").append(maxByDate).append("</td>")
             .append("<td>").append(grade(sum, maxByDate, cfg.settings())).append("</td>")
             .append("</tr>\n");
        }
        b.append("</table>\n");
    }

    private static LocalDate submissionDateOf(TaskReport r) {
        if (r == null) return null;
        StepResult sr = r.steps().get(GitCloneStep.NAME);
        if (sr == null) return null;
        Object v = sr.data().get("submissionDate");
        if (v instanceof LocalDate) {
            return (LocalDate) v;
        }
        return null;
    }

    private static String statusCell(TaskReport r, String stepName) {
        if (r == null) return "<td>—</td>";
        StepResult sr = r.steps().get(stepName);
        if (sr == null) return "<td>—</td>";
        switch (sr.status()) {
            case OK:
                return "<td class=\"ok\">+</td>";
            case FAIL:
                return "<td class=\"bad\">-</td>";
            case SKIPPED:
            default:
                return "<td>—</td>";
        }
    }

    private static String submissionText(TaskReport r) {
        if (r == null) return "—";
        StepResult sr = r.steps().get(GitCloneStep.NAME);
        if (sr == null) return "—";
        Object v = sr.data().get("submissionDate");
        if (v == null) return "—";
        return v.toString();
    }

    private static String testsText(TaskReport r) {
        if (r == null) return "—";
        StepResult sr = r.steps().get(TestsStep.NAME);
        if (sr == null || sr.data().isEmpty()) return "0/0/0";
        int p = ((Number) sr.data().getOrDefault("passed", 0)).intValue();
        int f = ((Number) sr.data().getOrDefault("failed", 0)).intValue();
        int s = ((Number) sr.data().getOrDefault("skipped", 0)).intValue();
        return p + "/" + f + "/" + s;
    }

    private static String numberField(TaskReport r, String stepName, String key) {
        if (r == null) return "0";
        StepResult sr = r.steps().get(stepName);
        if (sr == null) return "0";
        Object v = sr.data().get(key);
        if (v == null) return "0";
        return v.toString();
    }

    private static int totalOf(TaskReport r) {
        if (r == null) return 0;
        StepResult sr = r.steps().get(GradeStep.NAME);
        if (sr == null) return 0;
        return ((Number) sr.data().getOrDefault("total", 0)).intValue();
    }

    private static int totalMax(CheckerConfig cfg, Set<String> taskIds) {
        int sum = 0;
        for (String tid : taskIds) {
            Task t = cfg.tasks().get(tid);
            if (t != null) sum += t.maxPoints();
        }
        return sum;
    }

    private static String grade(int sum, int max, Settings s) {
        if (max == 0) return "—";
        double frac = (double) sum / max;
        String chosen = "—";
        for (Map.Entry<String, Double> e : s.gradeThresholds().entrySet()) {
            if (frac >= e.getValue()) chosen = e.getKey();
        }
        return chosen;
    }

    private static TaskReport find(List<TaskReport> reports, String github, String taskId) {
        for (TaskReport r : reports) {
            if (r.student().github().equals(github) && r.task().id().equals(taskId)) {
                return r;
            }
        }
        return null;
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
