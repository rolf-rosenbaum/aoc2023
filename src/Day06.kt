typealias Race = Pair<Int, Int>

fun main() {

    fun part1(races: List<Race>): Int {

        val foo = races.map {
            it.waysToWin()
        }
        return foo.reduce(Int::times)
    }
    
    fun part2(): Int {
        val time = 41667266L
        val distance = 244104712281040L

        return (1..time).count { (time - it) * it > distance }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(testRaces) == 288)

    part1(races).writeToConsole()
    part2().writeToConsole()
}

private fun Pair<Int, Int>.waysToWin(): Int {
    val (time, distance) = this
    return (1..time).count { (time - it) * it > distance }
}

val races = listOf(
    41 to 244,
    66 to 1047,
    72 to 1228,
    66 to 1040
)

val testRaces = listOf(
    7 to 9,
    15 to 40,
    30 to 200
)
