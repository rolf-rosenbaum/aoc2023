import Direction.East
import Direction.North
import Direction.South
import Direction.West
import kotlin.math.max



fun main() {
    fun part1(input: List<String>): Int {

        return input.parseMachine().sendLightBeam().size
    }

    fun part2(input: List<String>): Int {
        val machine = input.parseMachine()
        var mostTouched = 0
        (0..input.size).forEach {
            mostTouched = max(
                mostTouched, machine.sendLightBeam(
                    LightBeam(Point(it, 0), South)
                ).size
            )
            mostTouched = max(
                mostTouched, machine.sendLightBeam(
                    LightBeam(Point(it, input.size - 1), North)
                ).size
            )
            mostTouched = max(
                mostTouched, machine.sendLightBeam(
                    LightBeam(Point(0, it), East)
                ).size
            )
            mostTouched = max(
                mostTouched, machine.sendLightBeam(
                    LightBeam(Point(input.size - 1, it), West)
                ).size
            )
        }
        return mostTouched
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)

    val input = readInput("Day16")
    part1(input).writeToConsole()
    check(part2(testInput) == 51)
    part2(input).writeToConsole()
}

fun Map<Point, Char>.sendLightBeam(start: LightBeam = LightBeam(Point(0, 0), East)): Set<Point> {
    val touched = mutableSetOf<LightBeam>()
    val rightEdge = this.keys.maxX()
    val bottomEdge = this.keys.maxY()
    val lightBeams = mutableListOf(start)

    while (lightBeams.isNotEmpty()) {
        val head = lightBeams.removeAt(0)
        val pos = head.position
        if (pos.x < 0 || pos.x > rightEdge ||
            pos.y < 0 || pos.y > bottomEdge
        ) {
            continue
        }
        if (!touched.add(head)) continue
        val next = head.move(this[pos])
        lightBeams.addAll(next)
    }
    return touched.map { it.position }.toSet()
}


data class LightBeam(
    val position: Point,
    val direction: Direction
) {
    fun move(device: Char?): List<LightBeam> {
        when (device) {
            '-' -> return when (direction) {
                East, West -> listOf(copy(position = position + direction.vector))
                North, South -> listOf(
                    LightBeam(direction = East, position = position + East.vector),
                    LightBeam(direction = West, position = position + West.vector),
                )
            }
            '|' -> return when (direction) {
                North, South -> listOf(copy(position = position + direction.vector))
                East, West -> listOf(
                    LightBeam(direction = North, position = position + North.vector),
                    LightBeam(direction = South, position = position + South.vector),
                )
            }
            '/' -> return when (direction) {
                North -> listOf(LightBeam(direction = East, position = position + East.vector))
                East -> listOf(LightBeam(direction = North, position = position + North.vector))
                South -> listOf(LightBeam(direction = West, position = position + West.vector))
                West -> listOf(LightBeam(direction = South, position = position + South.vector))
            }
            '\\' -> return when (direction) {
                North -> listOf(LightBeam(direction = West, position = position + West.vector))
                East -> listOf(LightBeam(direction = South, position = position + South.vector))
                South -> listOf(LightBeam(direction = East, position = position + East.vector))
                West -> listOf(LightBeam(direction = North, position = position + North.vector))
            }
            else -> return listOf(copy(position = position + direction.vector))
        }
    }
}

