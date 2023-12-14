import java.time.OffsetDateTime

fun main() {
    fun part1(input: List<String>): Long {
        val almanac = input.toAlmanac()
        return almanac.nearestLocation()
    }

    fun part2(input: List<String>): Long {
        val almanac = input.toAlmanac()
        val now = OffsetDateTime.now().toEpochSecond()
        val almanacPart2 = almanac.copy(
            seeds = almanac.seeds.chunked(2).map { it.first().first..it.first().first + it.second().first }
        )
        val nearestLocation = almanacPart2.nearestLocation()
        println("Done in ${OffsetDateTime.now().toEpochSecond() - now} sec.")
        return nearestLocation
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

//    check(part1(testInput) == 35L)
//    part1(input).println()

//    check(part2(testInput) == 46L)
    part2(input).writeToConsole()
}

fun String.toRanges(): Pair<LongRange, LongRange> {
    val (destStart, sourceStart, len) = this.split(" ").map(String::toLong)

    val sourceRange = LongRange(sourceStart, sourceStart + len - 1)
    val destRange = LongRange(destStart, destStart + len - 1)

    return sourceRange to destRange
}

fun List<String>.toAlmanac(): Almanac {

    val seeds = first().split(": ").second().split(" ").map { it.toLong() }

    var rest = this.drop(3)

    val seedToSoil = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val soilToFertilizer = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val fertilizerToWater = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val waterToLight = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val lightToTemperature = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val temperatureToHumidity = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }
    rest = rest.dropWhile { it.isBlank() || it.first().isDigit() }.drop(1)

    val humidityToLocation = rest.takeWhile { it.isNotBlank() && it.first().isDigit() }

    return Almanac(
        seeds = seeds.map { it..it },
        seedToSoil = seedToSoil.map { it.toRanges() },
        soilToFertilizer = soilToFertilizer.map { it.toRanges() },
        fertilizerToWater = fertilizerToWater.map { it.toRanges() },
        waterToLight = waterToLight.map { it.toRanges() },
        lightToTemperature = lightToTemperature.map { it.toRanges() },
        temperatureToHumidity = temperatureToHumidity.map { it.toRanges() },
        humidityToLocation = humidityToLocation.map { it.toRanges() },
    )
}

data class Almanac(
    val seeds: List<LongRange>,
    val seedToSoil: List<Pair<LongRange, LongRange>>,
    val soilToFertilizer: List<Pair<LongRange, LongRange>>,
    val fertilizerToWater: List<Pair<LongRange, LongRange>>,
    val waterToLight: List<Pair<LongRange, LongRange>>,
    val lightToTemperature: List<Pair<LongRange, LongRange>>,
    val temperatureToHumidity: List<Pair<LongRange, LongRange>>,
    val humidityToLocation: List<Pair<LongRange, LongRange>>,
) {
    fun nearestLocation(): Long {
        
        var nearest: Long = Long.MAX_VALUE
        seeds.parallelStream().forEach { range ->
            range.forEach { seed ->
                var tmp = seed
                var diff = seedToSoil.firstOrNull {
                    tmp in it.first
                }?.let {
                    it.second.first - it.first.first
                } ?: 0
                tmp += diff

                diff = soilToFertilizer.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff

                diff = fertilizerToWater.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff

                diff = waterToLight.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff

                diff = lightToTemperature.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff

                diff = temperatureToHumidity.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff

                diff = humidityToLocation.firstOrNull {
                    tmp in it.first
                }?.let { it.second.first - it.first.first } ?: 0
                tmp += diff
                
                if (nearest > tmp) nearest = tmp
            }
            println(nearest)
        }
        return nearest
    }
}

