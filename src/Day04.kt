import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val cards = input.map(String::toCard)
        return cards.sumOf { 2.0.pow(it.countMyWinners().toDouble() - 1.0).toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map(String::toCard)
        val cardsCountById = cards.associate { it.id to 1 }.toMutableMap()
        cards.forEach { card ->
            ((card.id + 1)..(card.id + card.countMyWinners())).forEach { id ->
                val count = cardsCountById[card.id]
                if (count != null) {
                    cardsCountById[id] = cardsCountById[id]!! + count
                }
            }
        }
        return cardsCountById.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day04")
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    part1(input).writeToConsole()

    check(part2(testInput) == 30)
    part2(input).writeToConsole()
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
    fun countMyWinners(): Int = myNumbers.intersect(winningNumbers.toSet()).size
}