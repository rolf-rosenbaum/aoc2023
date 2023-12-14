typealias Pattern = Set<Point>

fun main() {
    fun part1(input: List<String>): Int {
        val patterns = input.toPatterns()
        val vertical = patterns.map {
            it.findVerticalMirror()
        }

        val horizontal = patterns.map {
            it.findHorizontalMirror()
        }
        return 100 * horizontal.sum() + vertical.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).writeToConsole()
    part2(input).writeToConsole()
}

fun Pattern.findVerticalMirror(): Int {
    return 0
}

fun Pattern.isVerticalMirror(col: Int): Boolean {
    var offset = 1
    while (col + offset < maxX() && col - offset + 1 > minX()) {
        if (col(col + offset) != col(col - offset + 1)) return false
        offset++
    }
    return true
}

fun Pattern.findHorizontalMirror(): Int {
    return 0
}

private fun Pattern.col(index: Int) = filter { it.x == index }.map(Point::y)
private fun Pattern.row(index: Int) = filter { it.y == index }.map(Point::x)

fun Pattern.isHorizontalMirror(row: Int): Boolean {
    var offset = 1
    while (row + offset < maxY() && row - offset + 1 > minY()) {
        if (row(row + offset) != row(row - offset + 1))
            return false
        offset++
    }
    return true
}

fun List<String>.toPatterns(): List<Pattern> {
    val patterns = mutableListOf<Pattern>()
    var pattern: MutableSet<Point> = mutableSetOf()
    var offset = 0

    this.forEachIndexed { y, line ->
        if (line.isEmpty()) {
            patterns.add(pattern)
            pattern = mutableSetOf()
            offset = y + 1 // plus 1 for empty line
        } else {
            line.forEachIndexed { x, c ->
                if (c == '#') pattern.add(Point(x, y - offset))
            }
        }
    }
    patterns.add(pattern)
    return patterns
}