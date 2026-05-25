package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.*
import static org.junit.jupiter.api.Assertions.*

class ConfigLoaderTest {
    @Test
    void loadsSingleFile(@TempDir Path tmp) {
        Files.writeString(tmp.resolve('checker.groovy'), '''
            tasks {
                task('T1') {
                    title 'X'
                    maxPoints 1
                    softDeadline '2025-01-01'
                    hardDeadline '2025-01-15'
                    path 'T1'
                }
            }
        ''')
        def cfg = ConfigLoader.load(tmp.resolve('checker.groovy'))
        assertEquals 1, cfg.tasks().size()
        assertEquals 'X', cfg.tasks()['T1'].title()
    }

    @Test
    void resolvesApplyFrom(@TempDir Path tmp) {
        Files.writeString(tmp.resolve('tasks.groovy'), '''
            tasks {
                task('T1') {
                    title 'A'; maxPoints 1; softDeadline '2025-01-01'
                    hardDeadline '2025-01-10'; path '.'
                }
            }
        ''')
        Files.writeString(tmp.resolve('groups.groovy'), '''
            group('G1') {
                student { github 'u'; name 'U'; repo 'r' }
            }
        ''')
        Files.writeString(tmp.resolve('checker.groovy'), '''
            apply from: 'tasks.groovy'
            apply from: 'groups.groovy'
            assignment {
                group 'G1', tasks: ['T1']
            }
        ''')
        def cfg = ConfigLoader.load(tmp.resolve('checker.groovy'))
        assertEquals 1, cfg.tasks().size()
        assertEquals 1, cfg.groups().size()
        assertEquals 1, cfg.assignments().size()
    }
}
