package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Assignment
import ru.nsu.smolin.checker.model.Bonus
import ru.nsu.smolin.checker.model.CheckerConfig
import ru.nsu.smolin.checker.model.Checkpoint
import ru.nsu.smolin.checker.model.Group
import ru.nsu.smolin.checker.model.Settings
import ru.nsu.smolin.checker.model.Task

class CheckerContext {
    Map<String, Task> tasks = [:]
    List<Group> groups = []
    List<Assignment> assignments = []
    List<Bonus> bonuses = []
    List<Checkpoint> checkpoints = []
    Settings settings = Settings.defaults()

    CheckerConfig toConfig() {
        new CheckerConfig(tasks, groups, assignments, bonuses, checkpoints, settings)
    }
}
