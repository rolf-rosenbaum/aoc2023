import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readLines()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.writeToConsole() = println(this)

fun <T> List<T>.second() = this[1]
fun <T, R> Pair<T, R>.reverse() = second to first

data class Point(val x: Int, val y: Int) {
    fun neighbours(includeCenter: Boolean = false): List<Point> =
        if (includeCenter) listOf(Point(x, y + 1), Point(x + 1, y), this, Point(x, y - 1), Point(x - 1, y))
        else listOf(Point(x, y + 1), Point(x + 1, y), Point(x, y - 1), Point(x - 1, y))

    /**
     * includes diagonal neighbors
     */

    fun allNeighbours(): Set<Point> =
        setOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1)
        )

    fun leftOf() = Point(x - 1, y)
    fun rightOf() = Point(x + 1, y)
    fun topOf() = Point(x, y - 1)
    fun bottomOf() = Point(x, y + 1)

    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(other.x -x, other.y - y)
}

fun IntRange.fullyContains(other: IntRange) =
    contains(other.first) && contains(other.last)

fun IntRange.overlapsWith(other: IntRange) =
    contains(other.first) || contains(other.last)

fun IntRange.union(other: IntRange): IntRange? {
    return if (overlapsWith(other))
        IntRange(minOf(first, other.first), maxOf(last, other.last))
    else null
}

fun List<Int>.findPattern(minWindow: Int = 1, startIndex: Int = 1): Pair<Int, Int> {
    (startIndex..size / 2).forEach { windowSize ->
        print("$windowSize\r")
        val tmp = this.windowed(windowSize)
        tmp.forEachIndexed { index, intList ->
            if (index + windowSize >= tmp.size)
                return@forEachIndexed
            if (intList == tmp[index + windowSize])
                return index + 1 to windowSize
        }
    }
    error("no pattern")
}

fun Long.primeFactors(): List<Long> = mutableListOf<Long>().let { f ->
    (2..this / 2).filter { this % it == 0L }
        .let {
            f.addAll(it)
            if (f.isEmpty()) listOf(this) else f
        }
}

fun Collection<Point>.maxX() = maxOf{it.x}
fun Collection<Point>.maxY() = maxOf{it.y}
fun Collection<Point>.minX() = minOf{it.x}
fun Collection<Point>.minY() = minOf{it.y}
