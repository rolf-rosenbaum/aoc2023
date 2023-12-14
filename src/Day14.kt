fun main() {

    fun part1(input: List<String>): Int {

        val dish = input.toDish()
        val finalDish = dish.tiltNorth()

        return finalDish.load()
    }

    fun part2(input: List<String>): Int {
        var dish = input.toDish()
        val loads = mutableListOf<Int>()
        val cycles = 1000000000L
        
        repeat(200) {
            dish = dish.spinCycle()
            loads.add(dish.load())
        }
        val pattern = loads.findPattern(startIndex = 2)
        val index = pattern.first + (cycles % pattern.second).toInt()
        return loads[index] 
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)

    val input = readInput("Day14")
    part1(input).writeToConsole()

//    check(part2(testInput) == 64)
    part2(input).writeToConsole()
}

data class Dish(
    val cubes: Set<Point>,
    val rocks: Set<Point>,
    val dimension: Int,
    val rockMoved: Boolean = true
) {
    private fun isRock(p: Point) = rocks.contains(p)
    private fun isCube(p: Point) = cubes.contains(p)

    private fun rollOneRowNorth(): Dish {
        val newRocks = mutableSetOf<Point>()
        var rockMoved = false
        newRocks.addAll(rocks.filter { it.y == 0 })
        (1..rocks.maxY()).forEach { y ->
            val oldRocks = rocks.filter { it.y == y }
            oldRocks.forEach { rock ->
                val newPlaceForRock = rock.topOf()
                if (newPlaceForRock.y >= 0 && !isRock(newPlaceForRock) && !isCube(newPlaceForRock)) {
                    newRocks.add(newPlaceForRock)
                    rockMoved = true
                } else {
                    newRocks.add(rock)
                }
            }
        }
        return copy(rocks = newRocks, rockMoved = rockMoved)
    }

    private fun rollOneRowWest(): Dish {
        val newRocks = mutableSetOf<Point>()
        var rockMoved = false
        newRocks.addAll(rocks.filter { it.x == 0 })
        (1..rocks.maxX()).forEach { x ->
            val oldRocks = rocks.filter { it.x == x }
            oldRocks.forEach { rock ->
                val newPlaceForRock = rock.leftOf()
                if (newPlaceForRock.x >= 0 && !isRock(newPlaceForRock) && !isCube(newPlaceForRock)) {
                    newRocks.add(newPlaceForRock)
                    rockMoved = true
                } else {
                    newRocks.add(rock)
                }
            }
        }
        return copy(rocks = newRocks, rockMoved = rockMoved)
    }
    
    private fun rollOneRowSouth(): Dish {
        val newRocks = mutableSetOf<Point>()
        var rockMoved = false
        newRocks.addAll(rocks.filter { it.y == dimension -1 })
        (dimension downTo 0).forEach { y ->
            val oldRocks = rocks.filter { it.y == y }
            oldRocks.forEach { rock ->
                val newPlaceForRock = rock.bottomOf()
                if (newPlaceForRock.y < dimension && !isRock(newPlaceForRock) && !isCube(newPlaceForRock)) {
                    newRocks.add(newPlaceForRock)
                    rockMoved = true
                } else {
                    newRocks.add(rock)
                }
            }
        }
        return copy(rocks = newRocks, rockMoved = rockMoved)
    }

    private fun rollOneRowEast(): Dish {
        val newRocks = mutableSetOf<Point>()
        var rockMoved = false
        newRocks.addAll(rocks.filter { it.x == dimension -1 })
        (dimension downTo 0).forEach { x ->
            val oldRocks = rocks.filter { it.x == x }
            oldRocks.forEach { rock ->
                val newPlaceForRock = rock.rightOf()
                if (newPlaceForRock.x < dimension && !isRock(newPlaceForRock) && !isCube(newPlaceForRock)) {
                    newRocks.add(newPlaceForRock)
                    rockMoved = true
                } else {
                    newRocks.add(rock)
                }
            }
        }
        return copy(rocks = newRocks, rockMoved = rockMoved)
    }
    
    fun tiltNorth() = generateSequence(this) { it.rollOneRowNorth() }.takeWhile { it.rockMoved }.last()
    private fun tiltWest() = generateSequence(this) { it.rollOneRowWest() }.takeWhile { it.rockMoved }.last()
    private fun tiltSouth() = generateSequence(this) { it.rollOneRowSouth() }.takeWhile { it.rockMoved }.last()
    private fun tiltEast() = generateSequence(this) { it.rollOneRowEast() }.takeWhile { it.rockMoved }.last()
    
    fun spinCycle() = tiltNorth().tiltWest().tiltSouth().tiltEast()
    fun load() = rocks.sumOf {
        dimension - it.y
    }


    fun prettyPrint() {
        println()
        (0 until dimension).forEach { y ->
            (0 until dimension).forEach { x ->
                val p = Point(x, y)
                if (cubes.contains(p)) {
                    print("#")
                } else if (rocks.contains(p)) {
                    print("O")
                } else print(".")
            }
            println()
        }
    }
}

fun List<String>.toDish(): Dish {
    val cubes = mutableSetOf<Point>()
    val rocks = mutableSetOf<Point>()

    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                'O' -> rocks.add(Point(x, y))
                '#' -> cubes.add(Point(x, y))
            }
        }
    }
    return Dish(cubes = cubes, rocks = rocks, dimension = size)
}