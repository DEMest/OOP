package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Student

class StudentBuilder {
    String github
    String name
    String repo

    void github(String value) {
        github = value
    }

    void name(String value) {
        name = value
    }

    void repo(String value) {
        repo = value
    }

    Student build() {
        if (!github || !name || !repo) {
            throw new IllegalStateException("student missing fields")
        }
        new Student(github, name, repo)
    }
}
