package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import ru.nsu.smolin.checker.model.Group
import ru.nsu.smolin.checker.model.Student
import static org.junit.jupiter.api.Assertions.*

class AssignmentDslTest {
    @Test
    void groupAssignmentExpandsToAllStudents() {
        CheckerContext ctx = new CheckerContext()
        ctx.groups << new Group('12345', [
            new Student('a', 'A', 'ra'),
            new Student('b', 'B', 'rb')
        ])
        AssignmentDsl.apply(ctx) {
            group '12345', tasks: ['T1', 'T2']
        }
        assertEquals 2, ctx.assignments.size()
        assertEquals(['T1', 'T2'], ctx.assignments[0].taskIds())
        assertEquals 'a', ctx.assignments[0].studentGithub()
        assertEquals 'b', ctx.assignments[1].studentGithub()
    }

    @Test
    void singleStudentAssignment() {
        CheckerContext ctx = new CheckerContext()
        AssignmentDsl.apply(ctx) {
            student 'demest', tasks: ['T3']
        }
        assertEquals 1, ctx.assignments.size()
        assertEquals 'demest', ctx.assignments[0].studentGithub()
        assertEquals(['T3'], ctx.assignments[0].taskIds())
    }
}
