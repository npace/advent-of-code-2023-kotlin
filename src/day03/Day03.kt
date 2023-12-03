package day03

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day03/Day03_test")
    assertEqual(part1(testInput1), 4361)
    val input = readInput("day03/Day03")
    log(part1(input))
}

fun part1(input: List<String>): Int {
    val allNumbers = input.flatMapIndexed { index, line ->
        line.readNumbers(index)
    }

    return allNumbers.filter { hasSymbolsAroundNumber(input, it) }
        .sumOf { it.value }
}

private data class Number(
    val value: Int,
    val lineIndex: Int,
    val positionInLine: IntRange,
)

private class NumberReader(private val lineNumber: Int, private val line: String) {
    private var numberStringBuilder: StringBuilder? = null
    private val numbers = mutableListOf<Number>()

    fun read(): List<Number> {
        numbers.clear()
        line.forEachIndexed { i, c ->
            if (c.isDigit()) {
                addToNumberString(c)
            } else {
                checkNumberEnd(i)
            }
            if (i == line.lastIndex) {
                checkNumberEnd(i)
            }
        }
        return numbers
    }

    private fun addToNumberString(char: Char) {
        if (numberStringBuilder == null) {
            numberStringBuilder = StringBuilder()
        }
        numberStringBuilder!!.append(char)
    }

    private fun checkNumberEnd(indexInLine: Int) {
        if (numberStringBuilder != null) {
            val number = numberStringBuilder!!.toNumber(lineNumber, indexInLine)
            numbers.add(number)
            numberStringBuilder = null
        }
    }

    private fun CharSequence.toNumber(line: Int, indexInLine: Int): Number {
        val numberString = toString()
        val value = numberString.toInt()
        val position = IntRange(indexInLine - numberString.length, indexInLine - 1)
        return Number(value, line, position)
    }
}

private fun String.readNumbers(lineNumber: Int): List<Number> {
    val reader = NumberReader(lineNumber, this)
    return reader.read()
}

private fun hasSymbolsAroundNumber(input: List<String>, number: Number): Boolean {
    val line = input[number.lineIndex]
    val startX = (number.positionInLine.first - 1).coerceAtLeast(0)
    val startY = (number.lineIndex - 1).coerceAtLeast(0)
    val endX = (number.positionInLine.last + 1).coerceAtMost(line.length - 1)
    val endY = (number.lineIndex + 1).coerceAtMost(input.size - 1)

    for (y in startY..endY) {
        for (x in startX..endX) {
            val char = input[y][x]
            if (!char.isDigit() && char != '.') {
                return true
            }
        }
    }

    return false
}
