package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.model.Student;
import ru.nsu.smolin.checker.model.Task;

import java.util.Map;

public record TaskReport(
    Student student,
    Task task,
    Map<String, StepResult> steps,
    int score,
    int bonus
) {}
