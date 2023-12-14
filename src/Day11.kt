private typealias Space = Set<Point>

fun main() {
    fun part1(input: List<String>): Long = input.toGalaxies().expandEmptySpace().sumOfDistances()

    fun part2(input: List<String>): Long = input.toGalaxies().expandEmptySpace(factor = 1_000_000).sumOfDistances()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 374L)
    part1(input).writeToConsole()

    check(part2(testInput) == 82000210L)
    part2(input).writeToConsole()
}

fun Space.sumOfDistances(): Long {
    val queue = toMutableList()
    var distance = 0L
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        distance += queue.fold(0L) { sum, point ->
            sum + point.distanceTo(current)
        }
    }
    return distance
}

fun Space.expandEmptySpace(factor: Int = 2): Space {
    val emptyColumns = (minOf(Point::x)..maxOf(Point::x)).filter { col ->
        this.none { it.x == col }
    }

    val emptyRows = (minOf(Point::y)..maxOf(Point::y)).filter { row ->
        this.none { it.y == row }
    }

    return this.map { galaxy ->
        val newX = galaxy.x + (factor - 1) * emptyColumns.count { it < galaxy.x }
        Point(newX, galaxy.y)
    }.map { galaxy ->
        val newY = galaxy.y + (factor - 1) * emptyRows.count { it < galaxy.y }
        Point(newY, galaxy.x)
    }.toSet()
}

fun List<String>.toGalaxies(): Space {
    return mutableSetOf<Point>().let {
        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') it.add(Point(x, y))
            }
        }
        it
    }
}
