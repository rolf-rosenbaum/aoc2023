fun main() {
    val testInput = readInput("Day08_test")
    val input = readInput("Day08")


    fun part1(input: List<String>): Int {
        val directions = Directions(input.first())
        val nodes = input.drop(2).map(String::toNode).associateBy(Node::name)

        val start = nodes["AAA"]
        val goal = nodes["ZZZ"]
        var current = start
        var steps = 0
        while (current != goal) {
            when (directions.next()) {
                'L' -> current = nodes[current?.left]
                'R' -> current = nodes[current?.right]
            }
            steps++
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val directions = Directions(input.first())
        val nodes = input.drop(2).map(String::toNode).associateBy(Node::name)

        val startList = nodes.filterKeys { it.endsWith("A") }.values
        val cycleLengths = startList.map {
            directions.reset()
            it.cycleLength(directions, nodes)
        }

        val primes = cycleLengths.flatMap { it.primeFactors() }
        return primes.distinct().reduce(Long::times)
    }

    // test if implementation meets criteria from the description, like:
//    check(part1(testInput) == 6)
    part1(input).println()

    check(part2(testInput) == 6L)
    part2(input).println()
}

fun Node.cycleLength(directions: Directions, nodes: Map<String, Node>): Long {
    var current = this
    var steps = 0L

    while (!current.name.endsWith("Z")) {
        when (directions.next()) {
            'L' -> current = nodes[current.left]!!
            'R' -> current = nodes[current.right]!!
        }
        steps++
    }
    return steps
}

fun Long.primeFactors(): List<Long> = mutableListOf<Long>().let { f ->
    (2..this / 2).filter { this % it == 0L }
        .let {
            f.addAll(it)
            if (f.isEmpty()) listOf(this) else f
        }
}

data class Directions(
    val directions: String
) {
    private var index = 0
    fun next(): Char {
        if (index == directions.length) reset()
        return directions[index++]
    }

    fun reset() {
        index = 0
    }
}

data class Node(
    val name: String,
    val left: String,
    val right: String
)

fun String.toNode(): Node {
    val name = substringBefore(" = ")
    val left = substringAfter("(").substringBefore(",")
    val right = substringAfter(", ").substringBefore(")")
    return Node(name = name, left = left, right = right)
}
// 987134413941735755
// 987134413941735755
