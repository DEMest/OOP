package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import java.time.LocalDate
import static org.junit.jupiter.api.Assertions.*

class CheckpointDslTest {
    @Test
    void addsCheckpoint() {
        CheckerContext ctx = new CheckerContext()
        CheckpointDsl.apply(ctx, 'CP1') { date '2025-04-01' }
        assertEquals 1, ctx.checkpoints.size()
        assertEquals 'CP1', ctx.checkpoints[0].name()
        assertEquals LocalDate.of(2025, 4, 1), ctx.checkpoints[0].date()
    }
}
