fun main() {
    val input = readInput("Day01")
    val testInput = readInput("Day01_test")

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.filter { it.isDigit() }
        }.sumOf {
            "${it.first()}${it.last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf{it.firstAndLastNumberAdded()}
    }

    check(part1(testInput) == 220)
    part1(input).writeToConsole()

    check(part2(testInput) == 281)
    part2(input).writeToConsole()
}

fun String.firstAndLastNumberAdded(): Int {
    val firstNumberWord = this.findAnyOf(digitWords.keys)
    val lastNumberWord = this.findLastAnyOf(digitWords.keys)
    val firstDigit = this.firstOrNull { it.isDigit() }
    val firstDigitPos = this.indexOfFirst{it == firstDigit} 
    val lastDigit = this.lastOrNull { it.isDigit() }
    val lastDigitPos = this.indexOfLast{it == lastDigit}
    
    val first = if (firstDigitPos < (firstNumberWord?.first ?: 1000)) firstDigit.toString() else digitWords[firstNumberWord!!.second]!!.toString()
    val second = if (lastDigitPos > (lastNumberWord?.first ?: -1)) lastDigit.toString() else digitWords[lastNumberWord!!.second]!!.toString()
    return (first + second).toInt()
}

val digitWords = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)
