package ru.nsu.smolin.checker.model;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public record Settings(Map<String, Double> gradeThresholds, Duration testTimeout) {
    public static Settings defaults() {
        Map<String, Double> g = new LinkedHashMap<>();
        g.put("satisfactory", 0.5);
        g.put("good", 0.7);
        g.put("excellent", 0.9);
        return new Settings(g, Duration.ofMinutes(5));
    }
}
