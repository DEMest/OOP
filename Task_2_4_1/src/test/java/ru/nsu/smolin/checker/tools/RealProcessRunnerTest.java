package ru.nsu.smolin.checker.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RealProcessRunnerTest {
    @Test
    void capturesStdoutAndExitCode(@TempDir Path tmp) {
        ProcessResult r = new RealProcessRunner().run(
            isWindows() ? List.of("cmd", "/c", "echo hello") : List.of("echo", "hello"),
            tmp, Duration.ofSeconds(5));
        assertTrue(r.ok());
        assertTrue(r.stdout().trim().endsWith("hello"));
    }

    @Test
    void capturesNonZeroExit(@TempDir Path tmp) {
        ProcessResult r = new RealProcessRunner().run(
            isWindows() ? List.of("cmd", "/c", "exit 7") : List.of("sh", "-c", "exit 7"),
            tmp, Duration.ofSeconds(5));
        assertFalse(r.ok());
        assertEquals(7, r.exitCode());
    }

    @Test
    void timesOut(@TempDir Path tmp) {
        ProcessResult r = new RealProcessRunner().run(
            isWindows()
                ? List.of("cmd", "/c", "for /L %x in (1,1,100000000) do @")
                : List.of("sh", "-c", "while :; do :; done"),
            tmp, Duration.ofMillis(300));
        assertTrue(r.timedOut());
        assertFalse(r.ok());
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
