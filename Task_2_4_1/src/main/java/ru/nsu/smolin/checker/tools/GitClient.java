package ru.nsu.smolin.checker.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Тонкая обёртка над консольным git: умеет клонировать или обновлять локальный кэш
 * репозитория и определять дату последнего изменения произвольной поддиректории.
 */
public final class GitClient {
    private final ProcessRunner runner;
    private final Duration timeout;

    public GitClient(ProcessRunner runner, Duration timeout) {
        this.runner = runner;
        this.timeout = timeout;
    }

    /** Результат операции с репозиторием: {@code ok} — успех, {@code message} — пояснение. */
    public record Result(boolean ok, String message) {}

    /**
     * Клонирует репозиторий в {@code dest}, либо обновляет существующий клон до origin/HEAD.
     * Перед клоном запускает {@code git ls-remote} как sanity-check, чтобы не залипать
     * на интерактивном запросе аутентификации.
     */
    public Result cloneOrUpdate(String url, Path dest) {
        Map<String, String> env = Map.of("GIT_TERMINAL_PROMPT", "0");

        try { Files.createDirectories(dest.getParent()); }
        catch (java.io.IOException e) { return new Result(false, "mkdir: " + e.getMessage()); }

        ProcessResult ls = runner.run(List.of("git", "ls-remote", url), parentOrCwd(dest), timeout, env);
        if (!ls.ok()) {
            return new Result(false, "ls-remote failed: " + tail(ls.stderr()));
        }

        boolean exists = Files.isDirectory(dest.resolve(".git"));
        if (!exists) {
            ProcessResult cl = runner.run(
                List.of("git", "clone", "--no-tags", url, dest.toString()),
                parentOrCwd(dest), timeout, env);
            return cl.ok()
                ? new Result(true, "cloned")
                : new Result(false, "clone failed: " + tail(cl.stderr()));
        }

        ProcessResult fe = runner.run(List.of("git", "fetch", "--no-tags", "origin"), dest, timeout, env);
        if (!fe.ok()) return new Result(false, "fetch failed: " + tail(fe.stderr()));

        ProcessResult head = runner.run(
            List.of("git", "rev-parse", "--abbrev-ref", "origin/HEAD"), dest, timeout, env);
        String branchRef = head.ok() && !head.stdout().isBlank()
            ? head.stdout().trim()
            : "origin/master";

        ProcessResult rs = runner.run(
            List.of("git", "reset", "--hard", branchRef), dest, timeout, env);
        return rs.ok()
            ? new Result(true, "updated")
            : new Result(false, "reset failed: " + tail(rs.stderr()));
    }

    /**
     * Возвращает дату автора последнего коммита, тронувшего {@code relativePath} в репозитории.
     * Используется как дата сдачи задачи для подсчёта множителя дедлайна.
     *
     * @return {@code null}, если коммитов нет или git вернул ошибку
     */
    public LocalDate lastCommitDate(Path repoDir, String relativePath) {
        Map<String, String> env = Map.of("GIT_TERMINAL_PROMPT", "0");
        ProcessResult r = runner.run(
            List.of("git", "log", "-1", "--format=%aI", "--", relativePath),
            repoDir, timeout, env);
        if (!r.ok()) return null;
        String s = r.stdout().strip();
        if (s.isEmpty()) return null;
        try {
            return OffsetDateTime.parse(s).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }

    private static Path parentOrCwd(Path p) {
        Path parent = p.toAbsolutePath().getParent();
        return parent != null ? parent : Path.of(".");
    }

    private static String tail(String s) {
        if (s == null) return "";
        String t = s.strip();
        return t.length() <= 200 ? t : t.substring(t.length() - 200);
    }
}
