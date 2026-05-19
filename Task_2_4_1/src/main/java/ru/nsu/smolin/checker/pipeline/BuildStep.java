package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.tools.GradleClient;
import ru.nsu.smolin.checker.tools.ProcessResult;

import java.nio.file.Path;
import java.util.List;

public final class BuildStep implements Step {
    public static final String NAME = "build";

    private final GradleClient gradle;

    public BuildStep(GradleClient gradle) { this.gradle = gradle; }

    @Override public String name() { return NAME; }

    @Override public boolean skipIfFailed(String previous) {
        return GitCloneStep.NAME.equals(previous);
    }

    @Override public StepResult run(StepContext ctx) {
        Path projectDir = projectDir(ctx);
        ProcessResult r = gradle.run(projectDir, List.of("build", "-x", "test", "-x", "check"));
        return r.ok() ? StepResult.ok() : StepResult.fail(tail(r.stderr() + r.stdout()));
    }

    static Path projectDir(StepContext ctx) {
        Object repoPath = ctx.previous().get(GitCloneStep.NAME).data().get("repoPath");
        Path repo = Path.of((String) repoPath);
        return repo.resolve(ctx.task().path());
    }

    private static String tail(String s) {
        if (s == null) return "";
        return s.length() > 800 ? s.substring(s.length() - 800) : s;
    }
}
