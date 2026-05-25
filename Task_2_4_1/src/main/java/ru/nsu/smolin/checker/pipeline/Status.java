package ru.nsu.smolin.checker.pipeline;

/**
 * Статус результата выполнения шага пайплайна.
 * OK — шаг успешно завершился, FAIL — ошибка, SKIPPED — пропущен из-за провала зависимости.
 */
public enum Status { OK, FAIL, SKIPPED }
