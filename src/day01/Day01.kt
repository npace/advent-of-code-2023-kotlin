package day01

import assertEqual
import assertNotEqual
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

    data class Word(val text: String, val digit: String)

    val digitWords = setOf(
        Word("one", "1"),
        Word("two", "2"),
        Word("three", "3"),
        Word("four", "4"),
        Word("five", "5"),
        Word("six", "6"),
        Word("seven", "7"),
        Word("eight", "8"),
        Word("nine", "9"),
    )

    val shortestWordLength = digitWords.minOf { it.text.length }
    val longestWordLength = digitWords.maxOf { it.text.length }

    fun String.possibleWordsAt(index: Int): List<String> {
        return (shortestWordLength..longestWordLength).mapNotNull { len ->
            if (index + len > length) {
                null
            } else {
                substring(index, index + len)
            }
        }
    }

    fun digitize(line: String): String {
        val result = line.mapIndexedNotNull { index, char ->
            if (char.isDigit()) {
                char.toString()
            } else {
                val possibleWords = line.possibleWordsAt(index)
                possibleWords.firstNotNullOfOrNull { possibleWord ->
                    digitWords.find { it.text ==  possibleWord}
                }?.digit
            }
        }.joinToString(separator = "")
        return result
    }

    fun part2(input: List<String>): Int {
        return input
            .map { digitize(it) }
            .sumOf { calibrationValue(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    assertEqual(part1(testInput), 142)

    val input = readInput("day01/Day01")
    part1(input).println()

    assertEqual(part2(readInput("day01/Day01_test2")), 281)
    assertEqual(part2(readInput("day01/Day01_test2_2")), 98)
    assertNotEqual(part2(input), 54683)
    part2(input).println()
}
