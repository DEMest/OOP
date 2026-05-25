package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.tools.GradleClient;
import ru.nsu.smolin.checker.tools.JunitXmlParser;
import ru.nsu.smolin.checker.tools.ProcessResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class TestsStep implements Step {
    public static final String NAME = "tests";

    private final GradleClient gradle;

    public TestsStep(GradleClient gradle) { this.gradle = gradle; }

    @Override public String name() { return NAME; }

    @Override public boolean skipIfFailed(String previous) {
        return BuildStep.NAME.equals(previous) || GitCloneStep.NAME.equals(previous);
    }

    @Override public StepResult run(StepContext ctx) {
        Path proj = BuildStep.projectDir(ctx);
        ProcessResult pr = gradle.run(proj, List.of("test"));

        Path reports = proj.resolve("build/test-results/test");
        if (!Files.isDirectory(reports)) {
            return StepResult.fail("no test reports: " + tail(pr.stderr() + pr.stdout()));
        }
        JunitXmlParser.Counts c = JunitXmlParser.parseDir(reports);
        return new StepResult(Status.OK, Map.of(
            "passed", c.passed(), "failed", c.failed(), "skipped", c.skipped()), "");
    }

    private static String tail(String s) {
        if (s == null) return "";
        return s.length() > 800 ? s.substring(s.length() - 800) : s;
    }
}
