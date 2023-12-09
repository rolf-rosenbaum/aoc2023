fun main() {

    fun part1(input: List<String>): Int = input
        .map { line -> line.split(" ").map { it.toInt() } }
        .sumOfNextElements()

    fun part2(input: List<String>): Int = input
        .map { line -> line.split(" ").map { it.toInt() }.reversed() }
        .sumOfNextElements()

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day09")
    val testInput = readInput("Day09_test")

    check(part1(testInput) == 114)
    part1(input).println()

    check(part2(testInput) == 2)
    part2(input).println()
}

private fun List<List<Int>>.sumOfNextElements() =
    map { original ->
        generateSequence(original, List<Int>::nextSequence).takeWhile { it.any { n -> n != 0 } }.toList()
    }.sumOf { it.sumOf(List<Int>::last) }

private fun List<Int>.nextSequence(): List<Int> = windowed(2).map { it.second() - it.first() }
