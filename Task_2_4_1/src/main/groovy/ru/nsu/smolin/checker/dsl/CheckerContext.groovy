package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.*

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
