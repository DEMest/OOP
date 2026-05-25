package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class BonusDslTest {
    @Test
    void addsBonus() {
        CheckerContext ctx = new CheckerContext()
        BonusDsl.apply(ctx) {
            student 'demest'
            task    'T1'
            points  2
            reason  'extra'
        }
        assertEquals 1, ctx.bonuses.size()
        def b = ctx.bonuses[0]
        assertEquals 'demest', b.studentGithub()
        assertEquals 'T1', b.taskId()
        assertEquals 2, b.points()
    }
}
