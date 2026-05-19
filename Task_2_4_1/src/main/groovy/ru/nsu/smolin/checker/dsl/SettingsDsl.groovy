package ru.nsu.smolin.checker.dsl

import ru.nsu.smolin.checker.model.Settings
import java.time.Duration

class SettingsDsl {
    static void apply(CheckerContext ctx, Closure body) {
        def b = new SettingsBlock(
            thresholds: new LinkedHashMap<>(ctx.settings.gradeThresholds()),
            timeout: ctx.settings.testTimeout())
        body.delegate = b
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body()
        ctx.settings = new Settings(b.thresholds, b.timeout)
    }
}

class SettingsBlock {
    Map<String, Double> thresholds
    Duration timeout

    void grade(Map<String, Object> opts, String label) {
        thresholds[label] = ((Number) opts.from).doubleValue()
    }

    void testTimeout(int amount, String unit) {
        if (unit == 'seconds') {
            timeout = Duration.ofSeconds(amount)
        } else if (unit == 'minutes') {
            timeout = Duration.ofMinutes(amount)
        } else if (unit == 'hours') {
            timeout = Duration.ofHours(amount)
        } else {
            throw new IllegalArgumentException("unit: $unit")
        }
    }
}
