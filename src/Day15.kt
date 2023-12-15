import Operation.Put
import Operation.Remove

typealias Box = MutableList<Pair<String, Int>>

fun main() {

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { s ->
            s.hash()
        }
    }

    fun part2(input: List<String>): Int {
        val lensOps = input.first().split(",").map(String::toLensOperation)
        val boxes = mutableMapOf<Int, Box>()
        lensOps.forEach { op ->
            when (op.operation) {
                Put -> {
                    val box = boxes.getOrDefault(op.box, mutableListOf())
                    val index = box.indexOfFirst { it.first == op.label }
                    if (index >= 0) {
                        box.add(index, op.label to op.focalLength!!)
                        box.removeAt(index + 1)
                    } else {
                        box.add(op.label to op.focalLength!!)
                    }
                    boxes[op.box] = box
                }
                Remove -> boxes[op.box]?.remove(boxes[op.box]?.firstOrNull { it.first == op.label })
            }
        }
        return boxes.map { (boxNumber, box) ->
            box.mapIndexed { index, lens ->
                (boxNumber + 1) * (index + 1) * lens.second
            }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)

    val input = readInput("Day15")
    part1(input).writeToConsole()
    check(part2(testInput) == 145)
    part2(input).writeToConsole()
}

fun String.toLensOperation(): LensOperation {
    val label = this.takeWhile { it.isLetter() }
    val operation = if (contains("=")) Put else Remove
    val focalLength = if (operation == Put) takeLastWhile { it.isDigit() }.toInt() else null
    return LensOperation(label, operation, focalLength)
}

private fun String.hash(): Int = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

data class LensOperation(
    val label: String,
    val operation: Operation,
    val focalLength: Int? = null
) {
    val box get() = label.hash()
}

enum class Operation { Put, Remove } 