package ru.nsu.smolin.checker.model;

import java.util.List;
import java.util.Map;

public record CheckerConfig(
    Map<String, Task> tasks,
    List<Group> groups,
    List<Assignment> assignments,
    List<Bonus> bonuses,
    List<Checkpoint> checkpoints,
    Settings settings
) {}
