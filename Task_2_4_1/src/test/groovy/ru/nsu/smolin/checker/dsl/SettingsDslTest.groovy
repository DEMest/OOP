package ru.nsu.smolin.checker.dsl

import org.junit.jupiter.api.Test
import java.time.Duration
import static org.junit.jupiter.api.Assertions.*

class SettingsDslTest {
    @Test
    void overridesDefaults() {
        CheckerContext ctx = new CheckerContext()
        SettingsDsl.apply(ctx) {
            grade 'satisfactory', from: 0.4
            grade 'good',         from: 0.7
            grade 'excellent',    from: 0.9
            testTimeout 2, 'minutes'
        }
        assertEquals 0.4d, ctx.settings.gradeThresholds()['satisfactory']
        assertEquals Duration.ofMinutes(2), ctx.settings.testTimeout()
    }
}
