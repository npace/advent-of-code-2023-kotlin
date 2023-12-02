package day02

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day02/Day02_test")
    assertEqual(part1(testInput1), 8)
    val input = readInput("day02/Day02")
    log(part1(input))
}

const val MAX_RED = 12
const val MAX_GREEN = 13
const val MAX_BLUE = 14

val maxAmountByColor = mapOf(
    "red" to MAX_RED,
    "green" to MAX_GREEN,
    "blue" to MAX_BLUE,
)
data class Game(
    val id: Int,
    val isPossible: Boolean,
)

fun part1(input: List<String>): Int {
    val games = input.map { it.parseGame() }
    log(games.map { it.toString() + it.isPossible })
    return games.sumOf { if (it.isPossible) it.id else 0 }
}

private fun String.parseGame(): Game {
    log(this)
    val id = parseId()
    var isPossible = true
    val sets = removeRange(0, indexOf(": ") + 2)
        .split("; ")
    sets.forEach { set ->
        val cubes = set.split(", ")
        log("cubes  $cubes")
        cubes.forEach { cube ->
            val amount = cube.substringBefore(" ").toInt()
            val color = cube.substringAfter(" ")
            log("cube    $cube: amount: $amount, color: $color")
            if (amount > maxAmountByColor[color]!!) {
                isPossible = false
            }
        }
    }

    return Game(id, isPossible)
}

private fun String.parseId(): Int {
    return substringBefore(":")
        .removePrefix("Game ")
        .toInt()
}