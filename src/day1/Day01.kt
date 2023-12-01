package day1

import assertEqual
import println
import readInput

fun main() {
    fun calibrationValue(line: String): Int {
        val firstDigit = line.firstOrNull { it.isDigit() }
        val lastDigit = line.lastOrNull { it.isDigit() }
        val calibrationValue = buildString {
            firstDigit?.let { append(it) }
            lastDigit?.let { append(it) }
        }
        return if (calibrationValue.isEmpty()) 0 else calibrationValue.toInt()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { calibrationValue(it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day1/Day01_test")
    assertEqual(part1(testInput), 142)

    val input = readInput("day1/Day01")
    part1(input).println()
    part2(input).println()
}
