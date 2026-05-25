package ru.nsu.smolin.checker.tools;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GitClientTest {
    @Test
    void clonesWhenNoLocalRepo() {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "ok", "", false)); // ls-remote
        runner.queue(new ProcessResult(0, "Cloning...", "", false)); // clone
        GitClient git = new GitClient(runner, Duration.ofSeconds(30));

        GitClient.Result r = git.cloneOrUpdate("https://example/x.git", Path.of("build/test/no-such"));

        assertTrue(r.ok());
        assertEquals("clone", runner.calls().get(1).get(1));
    }

    @Test
    void fetchesAndResetsWhenLocalRepoExists(@org.junit.jupiter.api.io.TempDir Path tmp) throws Exception {
        Path repo = tmp.resolve("r");
        java.nio.file.Files.createDirectories(repo.resolve(".git"));

        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(0, "", "", false));        // ls-remote
        runner.queue(new ProcessResult(0, "fetched", "", false)); // fetch
        runner.queue(new ProcessResult(0, "head", "", false));    // rev-parse origin/HEAD
        runner.queue(new ProcessResult(0, "reset", "", false));   // reset --hard
        GitClient git = new GitClient(runner, Duration.ofSeconds(30));

        GitClient.Result r = git.cloneOrUpdate("https://example/x.git", repo);

        assertTrue(r.ok());
        List<String> verbs = new ArrayList<>();
        for (List<String> c : runner.calls()) verbs.add(c.get(1));
        assertEquals(List.of("ls-remote", "fetch", "rev-parse", "reset"), verbs);
    }

    @Test
    void failsWhenLsRemoteFails() {
        FakeProcessRunner runner = new FakeProcessRunner();
        runner.queue(new ProcessResult(128, "", "auth required", false));
        GitClient git = new GitClient(runner, Duration.ofSeconds(30));

        GitClient.Result r = git.cloneOrUpdate("https://example/x.git", Path.of("build/test/x"));

        assertFalse(r.ok());
        assertTrue(r.message().contains("auth required"));
    }
}
