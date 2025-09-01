package ru.nsu.smolin

/**
 * Base user commands
 *
 */
enum class Command { TAKE, PASS, INFO, EXIT, UNKNOWN }

fun parseCommand(s: String): Command = when (s.trim().lowercase()) {
    "take" -> Command.TAKE
    "pass" -> Command.PASS
    "info" -> Command.INFO
    "exit" -> Command.EXIT
    "" -> Command.INFO // пустой ввод — вывести информацию/подсказку
    else -> Command.UNKNOWN
}

