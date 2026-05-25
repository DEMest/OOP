package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Bonus

class BonusDsl {
    static void apply(CheckerContext ctx, Closure body) {
        def b = new BonusBuilder()
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        ctx.bonuses << b.build()
    }
}

class BonusBuilder {
    String studentGithub
    String taskId
    Integer points
    String reason = ''

    void student(String github) {
        studentGithub = github
    }

    void task(String id) {
        taskId = id
    }

    void points(int value) {
        points = value
    }

    void reason(String text) {
        reason = text
    }

    Bonus build() {
        if (!studentGithub || !taskId || points == null) {
            throw new IllegalStateException("bonus missing fields")
        }
        new Bonus(studentGithub, taskId, points, reason)
    }
}
