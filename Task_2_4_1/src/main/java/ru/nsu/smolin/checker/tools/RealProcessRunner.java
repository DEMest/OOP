package ru.nsu.smolin.checker.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class RealProcessRunner implements ProcessRunner {
    @Override
    public ProcessResult run(List<String> command, Path workDir, Duration timeout, Map<String, String> env) {
        ProcessBuilder pb = new ProcessBuilder(command).directory(workDir.toFile());
        pb.environment().putAll(env);
        pb.redirectErrorStream(false);
        try {
            Process p = pb.start();
            StreamGobbler outG = new StreamGobbler(p.getInputStream());
            StreamGobbler errG = new StreamGobbler(p.getErrorStream());
            outG.start();
            errG.start();
            boolean done = p.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!done) {
                p.destroyForcibly();
                outG.join(500);
                errG.join(500);
                return new ProcessResult(-1, outG.text(), errG.text(), true);
            }
            outG.join();
            errG.join();
            return new ProcessResult(p.exitValue(), outG.text(), errG.text(), false);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ProcessResult(-1, "", e.getMessage(), false);
        }
    }

    private static final class StreamGobbler extends Thread {
        private final java.io.InputStream in;
        private final StringBuilder buf = new StringBuilder();
        StreamGobbler(java.io.InputStream in) { this.in = in; setDaemon(true); }
        @Override public void run() {
            try (var r = new java.io.BufferedReader(new java.io.InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) buf.append(line).append('\n');
            } catch (IOException ignored) {}
        }
        synchronized String text() { return buf.toString(); }
    }
}
