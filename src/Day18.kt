import Direction.East
import Direction.North
import Direction.South
import Direction.West

fun main() {

    fun calculateSize(steps: List<DigStep>): Long {
        var current = Point(0, 0)
        val trench = mutableSetOf(current)
        steps.forEach { step ->
            current = current.move(step.direction, step.numberOfSteps)
            trench.add(current)
        }
        return trench.polygonArea(true)
    }

    fun part1(input: List<String>): Long {
        val steps = input.map(String::toDigStep)
        return calculateSize(steps)
    }

    fun part2(input: List<String>): Long {
        val steps = input.map(String::toDigStep).map { step ->
            val direction = when (step.color.last()) {
                '0' -> East
                '1' -> South
                '2' -> West
                '3' -> North
                else -> error("invalid direction $step")
            }
            val foo = step.color.drop(1).take(5).toInt(16)
            DigStep(direction, foo, step.color)
        }
        var current = Point(0, 0)
        val trench = mutableSetOf(current)

        steps.forEach { step ->
            current = current.move(step.direction, step.numberOfSteps)
            trench.add(current)
        }
        return trench.polygonArea(true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    val input = readInput("Day18")
    check(part1(testInput) == 62L)
    part1(input).writeToConsole()

    check(part2(testInput) == 952408144115L)
    part2(input).writeToConsole()
}

fun String.toDigStep(): DigStep {
    val direction = when (first()) {
        'U' -> North
        'R' -> East
        'D' -> South
        'L' -> West
        else -> error("not a valid direction: ${this.first()}")
    }
    val numberOfSteps = substringAfter(" ").substringBefore(" ").toInt()
    val color = substringAfter("(").substringBefore(")")
    return DigStep(direction, numberOfSteps, color)
}

fun Set<Point>.prettyPrint() {
    (minY()..maxY()).forEach { y ->
        (minX()..maxX()).forEach { x ->
            print(if (contains(Point(x, y))) "#" else ".")
        }
        println()
    }
}

fun Set<Point>.isTopLeft(it: Point) = it in this && it.rightOf() in this && it.bottomOf() in this
fun Set<Point>.isTopRight(it: Point) = it in this && it.leftOf() in this && it.bottomOf() in this
fun Set<Point>.isBottomLeft(it: Point) = it in this && it.rightOf() in this && it.topOf() in this
fun Set<Point>.isBottomRight(it: Point) = it in this && it.leftOf() in this && it.topOf() in this

data class DigStep(
    val direction: Direction,
    val numberOfSteps: Int,
    val color: String
)