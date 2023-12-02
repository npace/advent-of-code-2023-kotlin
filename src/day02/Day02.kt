package day02

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day02/Day02_test")
    assertEqual(part1(testInput1), 8)
    val input = readInput("day02/Day02")
    log(part1(input))

    assertEqual(part2(testInput1), 2286)
    log(part2(input))
}

enum class Color(val colorName: String, val max: Int) {
    Red("red", 12),
    Green("green", 13),
    Blue("blue", 14);
}

fun String.toColor(): Color {
    return Color.entries.first { it.colorName == this }
}

data class Game(
    val id: Int,
    val isPossible: Boolean,
    val minRed: Int,
    val minGreen: Int,
    val minBlue: Int,
) {
    fun power(): Int {
        return minRed * minGreen * minBlue
    }
}

fun part1(input: List<String>): Int {
    val games = input.map { it.parseGame() }
    log(games.map { it.toString() + it.isPossible })
    return games.filter { it.isPossible }.sumOf { it.id }
}

fun part2(input: List<String>): Int {
    val games = input.map { it.parseGame() }
    log(games)
    return games.sumOf { it.power() }
}

private fun String.parseGame(): Game {
    log(this)
    val id = parseId()
    var isPossible = true
    val mins = Color.entries.associateWith { 0 }.toMutableMap()
    val sets = removeRange(0, indexOf(": ") + 2)
        .split("; ")
    sets.forEach { set ->
        val cubes = set.split(", ")
        log("cubes  $cubes")
        cubes.forEach { cube ->
            val amount = cube.substringBefore(" ").toInt()
            val color = cube.substringAfter(" ").toColor()
            log("cube    $cube: amount: $amount, color: $color")
            if (amount > color.max) {
                isPossible = false
            }
            val currentMin = mins[color]!!
            if (amount > currentMin) {
                mins[color] = amount
            }
        }
    }

    return Game(id, isPossible, mins[Color.Red]!!, mins[Color.Green]!!, mins[Color.Blue]!!)
}

private fun String.parseId(): Int {
    return substringBefore(":")
        .removePrefix("Game ")
        .toInt()
}