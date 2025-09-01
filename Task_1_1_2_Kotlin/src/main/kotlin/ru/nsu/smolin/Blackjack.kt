package ru.nsu.smolin

class Blackjack {
    private var deck = Deck()
    private val player = mutableListOf<Card>()
    private val dealer = mutableListOf<Card>()
    private var round = 0

    /**
     * Main game flow handler
     *
     */
    fun run() {
        Renderer.showSplash()
        readln()

        while(true) {
            val finished = startRound()
            if (!finished) {
                if (!playerTurn()) {
                    dealerTurn()
                    settle()
                }
            }
            if (!askContinue()) break
        }
        println("Конец игры.")
    }

    /**
     * Main logic at starting the round
     *
     */
    private fun startRound(): Boolean {
        round++
        Renderer.showRoundHeader(round)
        if(deck.size() < 15) {
            println("Недостаточно карт, создаем новую колоду и тасуем...")
            deck = Deck()
            deck.shuffleAnimation()
        } else {
            deck.shuffleAnimation()
        }
        player.clear(); dealer.clear()
        repeat(2) { player += deck.draw(); dealer += deck.draw() }
        Renderer.showHands(player, dealer, hideDealerHole = true)

        val p = Scoring.total(player)
        val d = Scoring.total(dealer)
        return when {
            p == 21 && d == 21 -> {
                Renderer.showHands(player, dealer, hideDealerHole = false)
                println("Оба с блэкджеком — ничья.")
                true
            }

            p == 21 -> {
                Renderer.showHands(player, dealer, hideDealerHole = false)
                println("Блэкджек! Вы выиграли.")
                true
            }

            d == 21 -> {
                Renderer.showHands(player, dealer, hideDealerHole = false)
                println("У дилера блэкджек — вы проиграли.")
                true
            }

            else -> {
                Renderer.showHands(player, dealer, hideDealerHole = true)
                false
            }
        }
    }

    /**
     * Processing player actions.
     * Taking cards, passing turn, exit game, printing condition.
     *
     */
    private fun playerTurn(): Boolean {
        while(true) {
            when(parseCommand(readln())) {
                Command.TAKE -> {
                    val c = deck.draw()
                    player += c
                    val sum = Scoring.total(player)
                    println("Вы взяли: ${c.short()} (сумма: $sum)")
                    if (sum > 21) {
                        println("Перебор, вы проиграли.")
                        return true
                    }
                    if (sum == 21) {
                        println("21! Переходим к диллеру.")
                        return false
                    }
                    Renderer.showHands(player, dealer, hideDealerHole = true)
                }
                Command.PASS -> return false
                Command.INFO -> Renderer.showHands(player, dealer, hideDealerHole = true)
                Command.EXIT -> {
                    println("Выход из игры.")
                    kotlin.system.exitProcess(0)
                }
                Command.UNKNOWN -> println("Неизвестная команда. Доступно Take / Pass / Info / Exit")
            }
        }
    }

    /**
     * The dealer open the cards and takes according to the rule
     * if his total is 17 and not less than the player's total.
     *
     */
    private fun dealerTurn() {
        var delayMs = 800L
        println("\nДилер раскрывает карты: " +
                dealer.joinToString(" ") { it.short() } +
                " (сумма: ${Scoring.total(dealer)})")
        while (Scoring.total(dealer) < 17 || Scoring.total(dealer) < Scoring.total(player)) {
            if (Scoring.total(dealer) > Scoring.total(player)) break
            sleep(delayMs)
            val c = deck.draw()
            dealer += c
            println("Дилер тянет карту: ${c.short()} (сумма: ${Scoring.total(dealer)})")
            delayMs = nextDelay(delayMs)
        }
    }

    private fun sleep(totalMs: Long) {
        if (totalMs <= 0) return
        val step = 200L
        var elapsed = 0L
        var dots = 0
        while (elapsed < totalMs) {
            print("\rДилер берёт" + ".".repeat(dots % 4).padEnd(3, ' '))
            Thread.sleep(step)
            elapsed += step
            dots++
        }
        print("\r" + " ".repeat(14) + "\r")
    }

    private fun nextDelay(currentMs: Long, startMs: Long = 300, grow: Double = 2.0, maxMs: Long = 2000): Long {
        if (currentMs <= 0) return startMs
        val next = (currentMs * grow).toLong()
        return next.coerceAtMost(maxMs)
    }

    /**
     * Announcement of the game results
     *
     */
    private fun settle() {
        val ps = Scoring.total(player)
        val ds = Scoring.total(dealer)
        println(
            """
            |Ваши карты: ${player.joinToString(" ") { it.short() }} (сумма: $ps)
            |Карты дилера: ${dealer.joinToString(" ") { it.short() }} (сумма: $ds)
            """.trimMargin()
        )
        when {
            ds > 21 -> println("У дилера перебор - вы выиграли.")
            ps > ds -> println("Сумма ваших карт больше - вы выграли")
            ps < ds -> println("Сумма ваших карт меньше - вы проиграли")
            else    -> println("Ничья")
        }
    }

    /**
     * Asking user if he wants to continue playing
     *
     */
    private fun askContinue(): Boolean {
        println("\nНажмитe ENTER, что бы продолжить, или введите 'exit' для выхода.")
        return readln().trim().lowercase() != "exit"
    }
}