package ru.nsu.smolin.checker.dsl

import org.codehaus.groovy.control.CompilerConfiguration
import ru.nsu.smolin.checker.model.CheckerConfig
import java.nio.file.Path

// Точка входа в загрузку Groovy DSL-конфигурации чекера.
// Парсит checker.groovy (вместе со всеми apply-from импортами) в единый CheckerConfig.
class ConfigLoader {
    // Загружает скрипт и возвращает неизменяемую конфигурацию.
    static CheckerConfig load(Path scriptPath) {
        CheckerContext ctx = new CheckerContext()
        executeInto(scriptPath, ctx)
        ctx.toConfig()
    }

    // Выполняет указанный скрипт в существующем контексте.
    // Используется самим apply-from для исполнения вложенных файлов в общем накопителе.
    static void executeInto(Path scriptPath, CheckerContext ctx) {
        if (!java.nio.file.Files.isRegularFile(scriptPath)) {
            throw new IllegalArgumentException("config not found: $scriptPath")
        }
        CompilerConfiguration cc = new CompilerConfiguration()
        cc.scriptBaseClass = CheckerScript.name
        cc.sourceEncoding = 'UTF-8'
        GroovyShell shell = new GroovyShell(ConfigLoader.classLoader, new Binding(), cc)

        CheckerScript script = (CheckerScript) shell.parse(scriptPath.toFile())
        script._ctx = ctx
        script._scriptDir = scriptPath.toAbsolutePath().parent
        script.run()
    }
}
