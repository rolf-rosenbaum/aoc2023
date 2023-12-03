import Color.BLUE
import Color.GREEN
import Color.RED

fun main() {


    fun part1(input: List<String>): Int =
        input.map(String::toGame)
            .filter { g ->
                g.grabs
                    .none {
                        it.any { cube -> cube.count > cube.color.max }
                    }
            }.sumOf { it.id }

    fun part2(input: List<String>): Int {
        return input.map(String::toGame)
            .sumOf { it.minPower() }

    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    check(part1(testInput) == 8)
    part1(input).println()

    check(part2(testInput) == 2286)
    part2(input).println()
}


fun String.toGame(): Game {
    val (gameId, game) = split(":")
    
    val grabs = game.trim().split(";").map { grab ->
        grab.split(",")
            .map(String::trim)
            .map {
                val (n, c) = it.split(" ")
                Cube(color = c.trim().toColor(), count = n.trim().toInt())
            }
    }
    val id = gameId.split(" ").second().toInt()
    return Game(id, grabs)
}

typealias Grab = List<Cube>

data class Game(
    val id: Int,
    val grabs: List<Grab>
) {
    fun minPower(): Int {
        return Color.entries.fold(1) { acc, color ->
            acc * grabs.flatMap { g ->
                g.filter { it.color == color }
            }.maxOf { it.count }
        }
    }
}

data class Cube(
    val color: Color,
    val count: Int
)

enum class Color(val max: Int) {
    RED(12), GREEN(13), BLUE(14);
}

fun String.toColor() = when (this) {
    "red" -> RED
    "green" -> GREEN
    "blue" -> BLUE
    else -> error("no color")
}