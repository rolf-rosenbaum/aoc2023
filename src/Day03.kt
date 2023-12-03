typealias Machine = Map<Point, Char>

fun main() {

    fun part1(input: List<String>): Int {

        val machine = input.parseMachine()
        val digitPoints = machine.filter { it.value.isSymbol() }
            .flatMap { (p, _) ->
                p.allNeighbours().filter { machine[it]?.isDigit() == true }
            }

        val used = mutableListOf<List<Point>>()
        return digitPoints.sumOf {
            val (number, points) = it.toNumber(machine)
            if (!used.contains(points)) {
                used.add(points)
                number
            } else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.parseMachine().sumUpGearRatios()
    }

    val input = readInput("Day03")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}

fun Char.isSymbol() = !this.isDigit() && this != '.'
fun Char.isGearSymbol() = this == '*'

fun Machine.sumUpGearRatios(): Int = this.filter { it.value.isGearSymbol() }.keys
    .sumOf {
        val adjacentNumbers = it.adjacentNumbers(this)
        if (adjacentNumbers.size == 2) {
            adjacentNumbers.first() * adjacentNumbers.second()
        } else 0
    }

fun Point.toNumber(machine: Machine): Pair<Int, List<Point>> {
    val start = startOfNumber(machine)
    return findNumber(start, machine)
}

private fun findNumber(start: Point, machine: Machine): Pair<Int, List<Point>> {
    val usedDigits = mutableListOf(start)
    var left = start.leftOf()
    val number = mutableListOf<Char>()
    number.add(machine[start]!!)
    while (machine[left]?.isDigit() == true) {
        number.add(machine[left]!!)
        usedDigits.add(left)
        left = left.leftOf()
    }
    return number.joinToString("").toInt() to usedDigits
}

private fun Point.startOfNumber(machine: Machine): Point {
    var start = rightOf()
    while (machine[start]?.isDigit() == true) {
        start = start.rightOf()
    }
    return start.leftOf()
}

fun Point.adjacentNumbers(machine: Machine): List<Int> {
    return allNeighbours().filter { machine[it]?.isDigit() == true }
        .map { it.toNumber(machine) }
        .distinctBy { it.second }
        .map { it.first }
}

fun List<String>.parseMachine(): Machine {
    val m = mutableMapOf<Point, Char>()
    this.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            if (char != '.') m[Point(x, y)] = char
        }
    }
    return m
}