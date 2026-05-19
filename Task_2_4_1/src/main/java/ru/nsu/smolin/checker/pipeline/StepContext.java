package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;

import java.nio.file.Path;
import java.util.Map;

/**
 * Контекст выполнения шага: студент, задача, корень локального клона репозитория
 * и результаты всех предыдущих шагов того же прогона (для извлечения данных).
 */
public record StepContext(
    Student student,
    Task task,
    Path repoRoot,
    Map<String, StepResult> previous
) {}
