package ru.nsu.smolin.checker.pipeline;

import java.util.Map;

/**
 * Результат выполнения одного шага: статус, опциональные структурированные данные и лог.
 * Карта {@code data} используется последующими шагами и репортером для извлечения метрик
 * (например, количество прошедших тестов, дата сдачи задачи и т. п.).
 */
public record StepResult(Status status, Map<String, Object> data, String log) {
    public static StepResult ok() { return new StepResult(Status.OK, Map.of(), ""); }
    public static StepResult ok(Map<String, Object> data) { return new StepResult(Status.OK, data, ""); }
    public static StepResult fail(String log) { return new StepResult(Status.FAIL, Map.of(), log); }
    public static StepResult skipped() { return new StepResult(Status.SKIPPED, Map.of(), ""); }
}
