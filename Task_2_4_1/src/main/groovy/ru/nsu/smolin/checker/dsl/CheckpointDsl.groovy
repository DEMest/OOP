package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Checkpoint
import java.time.LocalDate

class CheckpointDsl {
    static void apply(CheckerContext ctx, String name, Closure body) {
        def b = new CheckpointBuilder()
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        ctx.checkpoints << new Checkpoint(name, b.date)
    }
}

class CheckpointBuilder {
    LocalDate date

    void date(String value) {
        date = LocalDate.parse(value)
    }
}
