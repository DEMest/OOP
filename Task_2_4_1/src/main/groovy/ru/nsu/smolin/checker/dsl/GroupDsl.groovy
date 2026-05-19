package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Group
import ru.nsu.smolin.checker.model.Student

class GroupDsl {
    static void apply(CheckerContext ctx, String name, Closure body) {
        def block = new GroupBlock(name: name)
        body.delegate = block
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        ctx.groups << new Group(name, block.students)
    }
}

class GroupBlock {
    String name
    List<Student> students = []

    void student(Closure body) {
        def b = new StudentBuilder()
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        students << b.build()
    }
}
