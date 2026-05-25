package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.tools.CheckstyleRunner;
import ru.nsu.smolin.checker.tools.ProcessResult;

import java.nio.file.Files;
import java.nio.file.Path;

public final class StyleStep implements Step {
    public static final String NAME = "style";

    private final CheckstyleRunner cs;

    public StyleStep(CheckstyleRunner cs) { this.cs = cs; }

    @Override public String name() { return NAME; }

    @Override public boolean skipIfFailed(String previous) {
        return BuildStep.NAME.equals(previous) || GitCloneStep.NAME.equals(previous);
    }

    @Override public StepResult run(StepContext ctx) {
        Path src = BuildStep.projectDir(ctx).resolve("src/main/java");
        if (!Files.isDirectory(src)) {
            return StepResult.fail("no src/main/java");
        }
        ProcessResult r = cs.check(src);
        return r.ok() ? StepResult.ok() : StepResult.fail(tail(r.stdout() + r.stderr()));
    }

    private static String tail(String s) {
        if (s == null) return "";
        return s.length() > 800 ? s.substring(s.length() - 800) : s;
    }
}
