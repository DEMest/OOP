package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class GroupDslTest {
    @Test
    void buildsGroupWithStudents() {
        CheckerContext ctx = new CheckerContext()
        GroupDsl.apply(ctx, '12345') {
            student {
                github 'DEMest'
                name 'Смолин Даниил'
                repo 'https://github.com/DEMest/oop'
            }
            student {
                github 'foo'
                name 'Foo Bar'
                repo 'https://github.com/foo/oop'
            }
        }
        assertEquals 1, ctx.groups.size()
        def g = ctx.groups[0]
        assertEquals '12345', g.name()
        assertEquals 2, g.students().size()
        assertEquals 'DEMest', g.students()[0].github()
    }
}
