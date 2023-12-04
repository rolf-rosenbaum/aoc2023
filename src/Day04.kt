import kotlin.math.pow

typealias Pile = Map<Int, MutableList<Card>>

fun main() {
    fun part1(input: List<String>): Int {
        val cards = input.map(String::toCard)
        return cards.sumOf { 2.0.pow(it.countWinningNumbers().toDouble() - 1.0).toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map(String::toCard)
        val cardsCount = cards.associate { it.id to 1 }.toMutableMap()
        cards.forEach { card ->
            val winning = card.countWinningNumbers()
            ((card.id + 1)..(card.id + winning)).forEach { id ->
                val count = cardsCount[card.id]
                if (count != null) {
                    cardsCount[id] = cardsCount[id]!! + count
                }
            }
        }
        return cardsCount.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day04")
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()
}

fun String.toCard(): Card {
    val (cardId, allNumbers) = this.split(":")
    val id = cardId.split("   ", "  ", " ").second().toInt()
    val (win, my) = allNumbers.split("|").map(String::trim)
    val winningNumbers = win.split("  ", " ").map { it.trim().toInt() }
    val myNumbers = my.split("  ", " ").map { it.trim().toInt() }
    return Card(id, winningNumbers, myNumbers)
}

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>
) {
    fun countWinningNumbers(): Int = myNumbers.intersect(winningNumbers.toSet()).size
}