import Direction.East
import Direction.South
import java.util.PriorityQueue

typealias City = Map<Point, Int>

fun main() {
    fun part1(input: List<String>): Int {
        val city = input.toCity()
        return city.navigateCrucible(validMove = { wayPoint, direction ->
            wayPoint.steps < 3 || wayPoint.direction != direction
        }, startDirection = South)
    }

    fun part2(input: List<String>): Int {
        val city = input.toCity()
        return listOf(South, East).minOf {
            city.navigateCrucible(4, it) { wayPoint, direction ->
                if (wayPoint.steps > 9) wayPoint.direction != direction
                else if (wayPoint.steps < 4) wayPoint.direction == direction
                else true
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    val part1Test = part1(testInput)
    check(part1Test == 102)

    val input = readInput("Day17")
    part1(input).writeToConsole()
    val part2Test = part2(testInput)
    check(part2Test == 94)

    part2(input).writeToConsole()
}

fun City.navigateCrucible(minSteps: Int = 1, startDirection: Direction, validMove: (WayPoint, Direction) -> Boolean): Int {
    val boundsX = 0..keys.maxX()
    val boundsY = 0..keys.maxY()
    val way = PriorityQueue<WayPointWithLoss>()

    val start = WayPoint(Point(0, 0), startDirection, 0)
    val goal = Point(boundsX.last, boundsY.last)
    val visited = mutableSetOf(start)
    way.add(WayPointWithLoss(start, 0))

    while (way.isNotEmpty()) {
        val (current, loss) = way.poll()
        if (current.position == goal && current.steps >= minSteps) return loss

        current.direction.forwardDirections()
            .filter { direction -> (current.position + direction.vector).x in boundsX && (current.position + direction.vector).y in boundsY}
            .filter { validMove(current, it) }
            .map { current.next(it) }
            .filterNot { it in visited }
            .forEach { wayPoint ->
                way += WayPointWithLoss(wayPoint, loss + this[wayPoint.position]!!)
                visited += wayPoint
            }
    }
    error("no way to goal")
}

fun List<String>.toCity(): City {
    val city = mutableMapOf<Point, Int>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            city[Point(x, y)] = c.digitToInt()
        }
    }
    return city
}

data class WayPointWithLoss(val wayPoint: WayPoint, val loss: Int) : Comparable<WayPointWithLoss> {
    override fun compareTo(other: WayPointWithLoss): Int = loss - other.loss
}

data class WayPoint(
    val position: Point,
    val direction: Direction,
    val steps: Int
) {
    fun next(nextDirection: Direction): WayPoint {
        return WayPoint(
            position = position + nextDirection.vector,
            direction = nextDirection,
            steps = if (direction == nextDirection) steps + 1 else 1
        )
    }
}