fun main() {
    val input = readInput("Day08")

    val directions = Directions(input.first())
    val nodes = input.drop(2).map(String::toNode).associateBy(Node::name)

    fun Node.nodeSequence() = generateSequence(this) {
        when (directions.next()) {
            'L' -> nodes[it.left]!!
            'R' -> nodes[it.right]!!
            else -> error("invalid direction")
        }
    }

    fun Node.pathLengthUntil(stopCondition: (Node) -> Boolean): Long =
        nodeSequence()
            .takeWhile { !stopCondition(it) }.count().toLong()
            .also { directions.reset() }

    fun part1(): Long = nodes["AAA"]!!.pathLengthUntil { it == nodes["ZZZ"] }

    fun part2(): Long = nodes.filterKeys {
        it.endsWith("A")
    }.values
        .map { node ->
            node.pathLengthUntil { it.name.endsWith("Z") }
        }.flatMap(Long::primeFactors)
        .distinct()
        .reduce(Long::times)

    part1().println()
    part2().println()
}

private data class Directions(
    val directions: String
) {
    private var index = 0
    fun next(): Char {
        if (index == directions.length) reset()
        return directions[index++]
    }
    fun reset() { index = 0 }
}

private data class Node(
    val name: String,
    val left: String,
    val right: String
)

private fun String.toNode(): Node {
    val name = substringBefore(" = ")
    val left = substringAfter("(").substringBefore(",")
    val right = substringAfter(", ").substringBefore(")")
    return Node(name = name, left = left, right = right)
}
