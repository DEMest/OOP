package ru.nsu.smolin.checker.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.smolin.checker.app.CheckerApp;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CheckerE2ETest {

    @Test
    void runsWholePipelineAgainstLocalFixtureRepo(@TempDir Path tmp) throws Exception {
        Path fixture = Path.of("src/test/resources/fixtures/sample-repo").toAbsolutePath();
        Path bareRepo = tmp.resolve("bare.git");
        initBareRepoFrom(fixture, bareRepo);

        Path workDir = tmp.resolve("work");
        Files.createDirectories(workDir);
        writeConfig(workDir, bareRepo.toUri().toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int code = new CheckerApp(workDir, new PrintStream(baos, true, "UTF-8")).test();

        assertEquals(0, code);
        String html = baos.toString("UTF-8");
        assertTrue(html.contains("<!doctype html>"));
        assertTrue(html.contains("Task_demo"));
        assertTrue(html.contains("DEMest"));
    }

    private static void writeConfig(Path workDir, String repoUri) throws IOException {
        Files.writeString(workDir.resolve("tasks.groovy"), """
            tasks {
                task('Task_demo') {
                    title 'Demo'
                    maxPoints 3
                    softDeadline '2099-01-01'
                    hardDeadline '2099-01-15'
                    path 'Task_demo'
                }
            }
            """);
        Files.writeString(workDir.resolve("groups.groovy"), """
            group('G1') {
                student {
                    github 'DEMest'
                    name 'Daniel Smolin'
                    repo '""" + repoUri + """
'
                }
            }
            """);
        Files.writeString(workDir.resolve("checker.groovy"), """
            apply from: 'tasks.groovy'
            apply from: 'groups.groovy'
            assignment {
                group 'G1', tasks: ['Task_demo']
            }
            """);
    }

    private static void initBareRepoFrom(Path fixture, Path bareRepo) throws Exception {
        Path stage = bareRepo.getParent().resolve("stage");
        copyTree(fixture, stage);
        run(stage, "git", "init", "-b", "master");
        run(stage, "git", "config", "user.email", "t@t");
        run(stage, "git", "config", "user.name", "t");
        run(stage, "git", "add", ".");
        run(stage, "git", "commit", "-m", "init");
        run(stage.getParent(), "git", "clone", "--bare", stage.toString(), bareRepo.toString());
    }

    private static void copyTree(Path src, Path dst) throws IOException {
        try (Stream<Path> walk = Files.walk(src)) {
            for (Path p : (Iterable<Path>) walk::iterator) {
                Path target = dst.resolve(src.relativize(p).toString());
                if (Files.isDirectory(p)) Files.createDirectories(target);
                else { Files.createDirectories(target.getParent()); Files.copy(p, target); }
            }
        }
    }

    private static void run(Path workDir, String... cmd) throws Exception {
        Process p = new ProcessBuilder(cmd).directory(workDir.toFile()).redirectErrorStream(true).start();
        p.getInputStream().readAllBytes();
        if (!p.waitFor(60, java.util.concurrent.TimeUnit.SECONDS)) {
            p.destroyForcibly(); throw new IllegalStateException("timeout: " + String.join(" ", cmd));
        }
        if (p.exitValue() != 0) throw new IllegalStateException("failed: " + String.join(" ", cmd));
    }
}
