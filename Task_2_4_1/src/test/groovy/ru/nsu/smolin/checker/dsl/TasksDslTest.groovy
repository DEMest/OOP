package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import java.time.LocalDate
import static org.junit.jupiter.api.Assertions.*

class TasksDslTest {
    @Test
    void buildsTasksFromClosure() {
        CheckerContext ctx = new CheckerContext()
        TasksDsl.apply(ctx) {
            task('Task_2_1_1') {
                title 'Простые числа'
                maxPoints 1
                softDeadline '2025-03-15'
                hardDeadline '2025-03-22'
                path 'Task_2_1_1'
            }
        }
        def t = ctx.tasks['Task_2_1_1']
        assertNotNull t
        assertEquals 'Простые числа', t.title()
        assertEquals 1, t.maxPoints()
        assertEquals LocalDate.of(2025, 3, 15), t.softDeadline()
        assertEquals 'Task_2_1_1', t.path()
    }

    @Test
    void rejectsMissingFields() {
        CheckerContext ctx = new CheckerContext()
        assertThrows(IllegalStateException) {
            TasksDsl.apply(ctx) { task('X') { title 'x' } }
        }
    }
}
