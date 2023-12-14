import java.time.LocalDateTime
import java.time.ZoneOffset

fun main() {
    fun part1(input: List<String>): Long {
        return input.map(String::toSpringRow).sumOf { it.count(it.springs, it.groupSizes) }
    }

    fun part2(input: List<String>): Long {
        return input.map { it.toSpringRow().unfold() }.sumOf { it.count(it.springs, it.groupSizes) }
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day12")
    val testInput = readInput("Day12_test")
    part1(testInput).writeToConsole()
    check(part1(testInput) == 21L)

    val now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    part1(input).writeToConsole()
    val afterPart1 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    println("Duration: ${afterPart1 - now}s")

    check(part2(testInput) == 525152L)
    println("check for part2 done")
    part2(input).writeToConsole()
    val afterPart2 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    println("Duration: ${afterPart2 - afterPart1}s")
}

fun String.toSpringRow(): SpringRow {
    return split(" ").let {
        SpringRow(
            it.first(),
            it.second().split((",")).map(String::toInt)
        )
    }
}

data class SpringRow(
    val springs: String,
    val groupSizes: List<Int>
) {
    private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun count(springs: String, groups: List<Int>): Long {
        if (springs.isEmpty()) return if (groups.isEmpty()) 1 else 0
        if (groups.isEmpty()) return if (springs.contains('#')) 0 else 1

        return cache.getOrPut(springs to groups) {
            val current = springs.first()
            val resultIfGood = if (current != '#')
                count(springs.drop(1), groups) else 0

            val resultIfDamaged = when {
                current == '.' -> 0
                groups.first() > springs.length -> 0
                springs.take(groups.first()).contains('.') -> 0
                groups.first() != springs.length && springs[groups.first()] == '#' -> 0 
                else -> count(springs.drop(groups.first() + 1), groups.drop(1))
            }
            resultIfDamaged + resultIfGood
        }
    }

    fun unfold(): SpringRow {
        return copy(
            springs = "$springs?$springs?$springs?$springs?$springs",
            groupSizes = groupSizes + groupSizes + groupSizes + groupSizes + groupSizes
        )
    }
}

