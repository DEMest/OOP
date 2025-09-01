package ru.nsu.smolin

object Renderer {
    private const val RESET = "\u001B[0m"
    private const val RED = "\u001B[31m"
    private const val BLACK = "\u001B[30m"

    private fun colorWrap(text: String, color: String): String {
        return if(Config.useColors) "$color$text$RESET" else text
    }

    /**
     * Showing start display
     *
     */
    fun showSplash() {
        val redDiamond = colorWrap("♦", RED)
        val blackClub = colorWrap("♣", BLACK)
        val blackSpade = colorWrap("♠", BLACK)
        val redHeart = colorWrap("♥", RED)
        val banner = """
╔══════════════════════════════════════════════════════╗
║                                                      ║
║                      ┌─────────┐                     ║
║                      │A        │                     ║
║                      │         │                     ║
║                      │    $blackSpade    │                     ║
║                      │         │                     ║
║                      │        A│                     ║
║                      └─────────┘                     ║
║                                                      ║
║                $redDiamond $blackClub   BLACKJACK  $blackSpade $redHeart                  ║
║                                                      ║
║               Press${colorWrap(" ENTER", RED)} to start...                ║
╚══════════════════════════════════════════════════════╝
""".trimEnd()
        println(banner)
    }

    /**
     * Showing hands
     *
     */
    fun showHands(player: List<Card>, dealer: List<Card>, hideDealerHole: Boolean) {
        val playerStr = player.joinToString(" ") { it.short() }
        val dealerStr = if (hideDealerHole && dealer.size >= 2)
            dealer.first().short() + " [XX]" else dealer.joinToString(" ") { it.short() }
        val dealerSum = if (hideDealerHole) "" else " (сумма: ${Scoring.total(dealer)})"
        println(
            """
        |Ваши карты: $playerStr (сумма: ${Scoring.total(player)})
        |Карты дилера: $dealerStr$dealerSum
        |Выберите действие: Take / Pass / Info (или Exit)
        """.trimMargin()
        )
    }

    fun showRoundHeader(n: Int) {
        println("\n-------------- ROUND $n --------------\n")
    }
}