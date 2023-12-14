fun main() {
    fun part1(input: List<String>): Int = input.toMaze().loop.size / 2

    fun part2(input: List<String>): Int = input.toMaze().countInsideLoop()

    val input = readInput("Day10")
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 80)

    part1(input).writeToConsole()
    check(part2(testInput) == 10)
    part2(input).writeToConsole()
}

private fun Maze.hasConnectionBetween(a: Point, b: Point): Boolean {

    return field[b]?.toPipe() == Pipe.START || when (field[a]!!.toPipe()) {
        Pipe.NORTH_SOUTH -> b == a.topOf() || b == a.bottomOf()
        Pipe.NORTH_WEST -> b == a.topOf() || b == a.leftOf()
        Pipe.NORTH_EAST -> b == a.topOf() || b == a.rightOf()
        Pipe.EAST_WEST -> b == a.leftOf() || b == a.rightOf()
        Pipe.SOUTH_WEST -> b == a.bottomOf() || b == a.leftOf()
        Pipe.SOUTH_EAST -> b == a.bottomOf() || b == a.rightOf()
        Pipe.START -> true
    }
}

data class Maze(
    val field: Map<Point, Char>,
    val height: Int,
    val width: Int
) {
    val loop get() = findLoop()

    private fun findLoop(): Set<Point> {
        val start = field.filterValues { it == 'S' }.keys.first()
        var current = start

        return mutableSetOf(current).let {
            do {
                current = next(current, it)
                it.add(current)
            } while (current != start)
            it
        }
    }

    fun countInsideLoop(): Int {
        val loop = loop
        val verticalPipes = "|LJS"

        var count = 0

        (0 until height).forEach { y ->
            var inside = false
            (0 until width).forEach { x ->
                Point(x, y).let {
                    if (it in loop && verticalPipes.contains(field[it])) inside = !inside
                    if (it !in loop && inside) count++
                }
            }
        }
        return count
    }

    private fun next(current: Point, loop: Set<Point>): Point = current.neighbours()
        .intersect(field.keys)
        .filterNot { it in loop }
        .firstOrNull { to ->
            hasConnectionBetween(current, to)
        }
        ?: loop.first()
}

fun List<String>.toMaze(): Maze {
    val field = mutableMapOf<Point, Char>()
    this.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c in "S|-J7FL".toSet()) field[Point(x, y)] = c
        }
    }
    return Maze(field, size, first().length)
}

fun String.contains(c: Char?) = c != null && c in this

fun Char.toPipe() = Pipe.from(this)

enum class Pipe {
    START,
    NORTH_SOUTH,
    NORTH_WEST,
    NORTH_EAST,
    EAST_WEST,
    SOUTH_WEST,
    SOUTH_EAST;

    companion object {
        fun from(c: Char): Pipe = when (c) {
            '|' -> NORTH_SOUTH
            '-' -> EAST_WEST
            'L' -> NORTH_EAST
            'J' -> NORTH_WEST
            '7' -> SOUTH_WEST
            'F' -> SOUTH_EAST
            'S' -> START
            else -> error("broken pipe")
        }
    }
}