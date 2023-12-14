fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 1)

    val input = readInput("Day20")
    part1(input).writeToConsole()
    part2(input).writeToConsole()
}
