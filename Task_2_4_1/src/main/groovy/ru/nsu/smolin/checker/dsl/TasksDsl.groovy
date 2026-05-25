package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Task
import java.time.LocalDate

class TasksDsl {
    static void apply(CheckerContext ctx, Closure body) {
        def block = new TasksBlock(ctx: ctx)
        body.delegate = block
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
    }
}

class TasksBlock {
    CheckerContext ctx

    void task(String id, Closure body) {
        def b = new TaskBuilder(id: id)
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        ctx.tasks[id] = b.build()
    }
}

class TaskBuilder {
    String id
    String title
    String path
    Integer maxPoints
    LocalDate softDeadline
    LocalDate hardDeadline

    void title(String value) {
        title = value
    }

    void maxPoints(int value) {
        maxPoints = value
    }

    void path(String value) {
        path = value
    }

    void softDeadline(String date) {
        softDeadline = LocalDate.parse(date)
    }

    void hardDeadline(String date) {
        hardDeadline = LocalDate.parse(date)
    }

    Task build() {
        if (!title || maxPoints == null || !softDeadline || !hardDeadline || !path) {
            throw new IllegalStateException("task '$id' is missing required fields")
        }
        new Task(id, title, maxPoints, softDeadline, hardDeadline, path)
    }
}
