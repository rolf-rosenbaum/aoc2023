typealias Part = Map<Char, Int>

fun main() {
    fun part1(input: List<String>, partsInput: List<String>): Int {
        val parts = partsInput.map(String::toPart)
        val workflows = input.map(String::toWorkFlow).associateBy { it.name }

        return parts.filter { workflows.accept(it) }.sumOf { it.xmas() }

    }

    fun part2(input: List<String>): Long {
        val workflows = input.map(String::toWorkFlow).associateBy { it.name }

        return (1..100).flatMap { x ->
            (1..100).flatMap { m ->
                (1..100).flatMap { a ->
                    (1..100).map { s ->
                        mapOf('x' to x,
                        'm' to m,
                        'a' to a,
                        's' to s)
                    }
                }
            }
        }.count { workflows.accept(it) }.toLong()

//        val ranges = mapOf(
//            'x' to 1..4000,
//            'm' to 1..4000,
//            'a' to 1..4000,
//            's' to 1..4000,
//        )
//
//        return workflows.possiibiliies("in", ranges)
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day19")
    val partsInput = readInput("Day19Parts")
    val partsTestInput = readInput("Day19Parts_test")
    val testInput = readInput("Day19_test")

    check(part1(testInput, partsTestInput) == 19114)
    part1(input, partsInput).writeToConsole()

    val checkPart2 = part2(testInput)
    check(checkPart2 == 167409079868000L)
    part2(input).writeToConsole()
}

fun String.toWorkFlow(): Workflow {
    val name = substringBefore("{")
    val rulesStrings = substringAfter("{").split(",")
    val rules = rulesStrings.map {
        if (it.contains(":")) {
            val target = it.substringAfter(":")
            val cond = it.substringBefore(":")
            Rule(target = target, condition = Condition(cond.first(), cond.second(), cond.drop(2).toInt()))
        } else {
            Rule(target = it.replace("}", ""))
        }
    }
    return Workflow(name, rules)
}

fun String.toPart(): Part {
    return drop(1).dropLast(1).split(",").associate {
        it.split("=").let {
            it.first().first() to it.second().toInt()
        }
    }
}

fun Map<String, Workflow>.accept(part: Part): Boolean {

    fun process(wf: Workflow, part: Part): Boolean {
        wf.rules.forEach {
            if (it.condition == null) {
                return when (it.target) {
                    "A" -> true
                    "R" -> false
                    else -> process(this[it.target]!!, part)
                }
            } else {
                when (it.condition.compare) {
                    '>' -> if (part[it.condition.property]!! > it.condition.goal) {
                        return when (it.target) {
                            "A" -> true
                            "R" -> false
                            else -> process(this[it.target]!!, part)
                        }
                    } else {
                        return@forEach
                    }

                    '<' -> if (part[it.condition.property]!! < it.condition.goal) {
                        return when (it.target) {
                            "A" -> true
                            "R" -> false
                            else -> process(this[it.target]!!, part)
                        }
                    } else {
                        return@forEach
                    }

                    else -> error("invalid rule")
                }

            }
        }
        return false
    }
    return process(this["in"]!!, part)
}

fun Map<String, Workflow>.possiibiliies(target: String, ranges: Map<Char, IntRange>): Long {
    return when (target) {
        "A" -> ranges.values.map { (it.last - it.start + 1).toLong() }.reduce(Long::times)
        "R" -> 0
        else -> {
            val newRanges = ranges.toMutableMap()
            this.getValue(target).rules.sumOf { rule ->
                if (rule.condition == null) {
                    possiibiliies(rule.target, newRanges)
                } else {
                    when (rule.condition.compare) {
                        '<' -> {
                            newRanges[rule.condition.property] = newRanges[rule.condition.property]!!.merge(rule.range())
                        }

                        '>' -> {
                            newRanges[rule.condition.property] = newRanges[rule.condition.property]!!.merge(rule.range())
                        }
                    }
                    possiibiliies(rule.target, newRanges).also {
                        newRanges[rule.condition.property] = newRanges[rule.condition.property]!!.merge(rule.reversedRange())
                    }
                }
            }
        }
    }
}

const val MAX_REACHABLE = 4000L * 4000L * 4000L * 4000L

fun Part.xmas() = values.sum()
fun String.second() = this[1]
data class Workflow(val name: String, val rules: List<Rule>)
data class Rule(val condition: Condition? = null, val target: String) {
    fun range() = when (condition?.compare) {
        '<' -> 1..condition.goal
        '>' -> condition.goal + 1..4000
        else -> 1..4000
    }

    fun reversedRange() = when (condition?.compare) {
        '<' -> condition.goal..4000
        '>' -> 1..condition.goal
        else -> 1..4000
    }
}

data class Condition(val property: Char, val compare: Char, val goal: Int)
