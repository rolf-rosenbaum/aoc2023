fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.toPokerHand() }.sorted()
        .mapIndexed { index, pokerHand -> (index + 1) * pokerHand.bid }.sum()

    fun part2(input: List<String>): Int = input
        .map { it.pokerHandWithJokers() }.sorted()
        .mapIndexed { index, pokerHand -> (index + 1) * pokerHand.bid }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")
    check(part1(testInput) == 6440)
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}

data class PokerHand(
    val withJokers: Boolean = false,
    val cards: List<Int>,
    val bid: Int
) : Comparable<PokerHand> {
    private val cardCounts get() = cards.associateWith { c -> cards.count { it == c && it != 1 } }
    private val countJokers: Int get() = cards.count { it == 1 }

    private val rank: Int
        get() = if (hasFiveOfAKind(withJokers)) 6
        else
            if (hasFourOfAKind(withJokers)) 5 else
                if (hasFullHouse(withJokers)) 4 else
                    if (hasThreeOfAKind(withJokers)) 3 else
                        if (hasTwoPair(withJokers)  ) 2 else
                            if (hasPair(withJokers)) 1 else 0

    private fun hasFiveOfAKind(jokers: Boolean = false) = if(jokers) fiveOfAKindWithJokers() else cards.count { it == cards.first() } == 5
    private fun hasFourOfAKind(jokers: Boolean = false) = if(jokers) fourOfAKindWithJokers() else cards.any { c -> cards.count { c == it } == 4 }
    private fun hasFullHouse(jokers: Boolean = false) = if(jokers) fullHouseWithJokers() else hasThreeOfAKind() && hasPair()
    private fun hasThreeOfAKind(jokers: Boolean = false) = if (jokers) threeOfAKindWithJokers() else cards.any { c -> cards.count { c == it } == 3 }
    private fun hasTwoPair(jokers: Boolean = false) = if (jokers) twoPairWithJokers() else !hasThreeOfAKind() && cards.distinct().size == 3
    private fun hasPair(jokers: Boolean = false) = if (jokers) pairWithJokers() else cards.any { c -> cards.count { c == it } == 2 }

    private fun fiveOfAKindWithJokers() = cardCounts.values.maxOf { it } + countJokers == 5 || countJokers > 3
    private fun fourOfAKindWithJokers() = cardCounts.values.maxOf { it } + countJokers == 4
    private fun fullHouseWithJokers() = if (countJokers == 0) threeOfAKindWithJokers() && cards.distinct().size == 2 else cards.distinct().size == 3
    private fun threeOfAKindWithJokers() = cardCounts.values.maxOf { it } + countJokers == 3
    private fun twoPairWithJokers() = countJokers == 0 && cardCounts.filter { it.value == 2 }.size == 2
    private fun pairWithJokers() = if (countJokers == 0) cards.distinct().size == 4 else countJokers == 1

    override fun compareTo(other: PokerHand): Int {
        return if (rank == other.rank) {
            compareCards(other)
        } else rank.compareTo(other.rank)
    }

    private fun compareCards(other: PokerHand): Int {
        return cards.mapIndexed { i, card ->
            card.compareTo(other.cards[i])
        }.first { it != 0 }
    }
}

fun Char.toCardValue() = when (this) {
    '2' -> 2
    '3' -> 3
    '4' -> 4
    '5' -> 5
    '6' -> 6
    '7' -> 7
    '8' -> 8
    '9' -> 9
    'T' -> 10
    'J' -> 11
    'Q' -> 12
    'K' -> 13
    'A' -> 14
    else -> 0
}

fun Char.toCardValueWithJokers() = when (this) {
    'J' -> 1
    '2' -> 2
    '3' -> 3
    '4' -> 4
    '5' -> 5
    '6' -> 6
    '7' -> 7
    '8' -> 8
    '9' -> 9
    'T' -> 10
    'Q' -> 11
    'K' -> 12
    'A' -> 13
    else -> 0
}

fun String.toPokerHand(): PokerHand {
    return PokerHand(
        cards = substringBefore(" ").map(Char::toCardValue),
        bid = substringAfter(" ").toInt()
    )
}

fun String.pokerHandWithJokers(): PokerHand {
    return PokerHand(
        withJokers = true,
        cards = substringBefore(" ").map(Char::toCardValueWithJokers),
        bid = substringAfter(" ").toInt()
    )
}
