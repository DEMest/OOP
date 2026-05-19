package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Assignment
import ru.nsu.smolin.checker.model.Student

class AssignmentDsl {
    static void apply(CheckerContext ctx, Closure body) {
        def b = new AssignmentBlock(ctx: ctx)
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
    }
}

class AssignmentBlock {
    CheckerContext ctx

    void group(Map<String, ?> opts, String name) {
        List<String> taskIds = (List<String>) opts.tasks
        def grp = ctx.groups.find { it.name() == name }
        if (!grp) {
            throw new IllegalStateException("unknown group: $name")
        }
        for (Student s : grp.students()) {
            ctx.assignments << new Assignment(s.github(), taskIds)
        }
    }

    void student(Map<String, ?> opts, String github) {
        List<String> taskIds = (List<String>) opts.tasks
        ctx.assignments << new Assignment(github, taskIds)
    }
}
