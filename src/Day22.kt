fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 1)

    val input = readInput("Day22")
    part1(input).writeToConsole()
    part2(input).writeToConsole()
}
