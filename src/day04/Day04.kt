package day04

import assertEqual
import log
import readInput
import java.math.BigInteger

fun main() {
    val testInput1 = readInput("day04/Day04_test")
    assertEqual(part1(testInput1), 13)
    val input = readInput("day04/Day04")
    log(part1(input))
}

data class Card(
    val winningNumbers: List<Int>,
    val playerNumbers: List<Int>,
)

private fun part1(input: List<String>): Int {
    return input
        .map(::parseCard)
        .sumOf(::calculateWinning)
}

private fun calculateWinning(card: Card): Int {
    val winningNumbersMap = card.winningNumbers.associateWith { true }
    val guessedNumbersCount = card.playerNumbers
        .filter { winningNumbersMap.getOrDefault(it, false) }
        .size
    return if (guessedNumbersCount == 0) {
        0
    } else {
        BigInteger.valueOf(2).pow(guessedNumbersCount - 1).toInt()
    }
}

private fun parseCard(line: String): Card {
    val numberLists = line.substringAfter(":")
        .split("|")
        .map {
            it.trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        }
    return Card(numberLists[0], numberLists[1])
}
