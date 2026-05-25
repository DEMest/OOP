package ru.nsu.smolin.checker.pipeline;

import ru.nsu.smolin.checker.tools.GitClient;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class GitCloneStep implements Step {
    public static final String NAME = "git-clone";

    private final GitClient git;
    private final Path reposRoot;

    public GitCloneStep(GitClient git, Path reposRoot) {
        this.git = git;
        this.reposRoot = reposRoot;
    }

    @Override public String name() { return NAME; }

    @Override public StepResult run(StepContext ctx) {
        Path dest = reposRoot.resolve(ctx.student().github());
        GitClient.Result r = git.cloneOrUpdate(ctx.student().repo(), dest);
        if (!r.ok()) return StepResult.fail(r.message());

        LocalDate submitted = git.lastCommitDate(dest, ctx.task().path());

        Map<String, Object> data = new HashMap<>();
        data.put("repoPath", dest.toString());
        if (submitted != null) data.put("submissionDate", submitted);
        return new StepResult(Status.OK, data, r.message());
    }
}
