package ru.nsu.smolin.checker.model;

import java.time.LocalDate;

public record Task(
    String id,
    String title,
    int maxPoints,
    LocalDate softDeadline,
    LocalDate hardDeadline,
    String path
) {}
