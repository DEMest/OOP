package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.tools.GradleClient;
import ru.nsu.smolin.checker.tools.ProcessResult;

import java.util.List;

public final class JavadocStep implements Step {
    public static final String NAME = "javadoc";

    private final GradleClient gradle;

    public JavadocStep(GradleClient gradle) { this.gradle = gradle; }

    @Override public String name() { return NAME; }

    @Override public boolean skipIfFailed(String previous) {
        return BuildStep.NAME.equals(previous) || GitCloneStep.NAME.equals(previous);
    }

    @Override public StepResult run(StepContext ctx) {
        ProcessResult r = gradle.run(BuildStep.projectDir(ctx), List.of("javadoc"));
        return r.ok() ? StepResult.ok() : StepResult.fail(tail(r.stderr() + r.stdout()));
    }

    private static String tail(String s) {
        if (s == null) return "";
        return s.length() > 800 ? s.substring(s.length() - 800) : s;
    }
}
