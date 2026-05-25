package ru.nsu.smolin.checker.pipeline;

/**
 * Один шаг пайплайна проверки задачи студента.
 * Реализации не должны бросать исключения наружу — {@link Pipeline} ловит их и превращает в FAIL.
 */
public interface Step {
    /** Уникальное имя шага, используется как ключ в карте результатов и для пропуска зависимых шагов. */
    String name();

    /** Выполняет шаг в заданном контексте и возвращает результат. */
    StepResult run(StepContext ctx);

    /**
     * Возвращает {@code true}, если этот шаг следует пропустить (получить SKIPPED) при FAIL указанного шага.
     * По умолчанию шаги выполняются независимо.
     */
    default boolean skipIfFailed(String previousStepName) { return false; }
}
