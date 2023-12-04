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

    assertEqual(part2(testInput1), 30)
    log(part2(input))
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

private fun part2(input: List<String>): Int {
    val cards = input
        .map(::parseCard)
    return calculateNumberOfCardsWon(cards)
}

private fun guessedNumbers(card: Card): List<Int> {
    val winningNumbersMap = card.winningNumbers.associateWith { true }
    return card.playerNumbers
        .filter { winningNumbersMap.getOrDefault(it, false) }
}

private fun calculateWinning(card: Card): Int {
    val guessedNumbersCount = guessedNumbers(card).size
    return if (guessedNumbersCount == 0) {
        0
    } else {
        BigInteger.valueOf(2).pow(guessedNumbersCount - 1).toInt()
    }
}

private fun calculateNumberOfCardsWon(cards: List<Card>): Int {
    val cardInstances = MutableList(cards.size) { 1 }
    cardInstances.forEachIndexed { index, _ ->
        if (index < cards.lastIndex) {
            repeat(cardInstances[index]) {
                val guessedNumbersCount = guessedNumbers(cards[index]).size
                (index + 1..index + guessedNumbersCount).forEach {
                    cardInstances[it] = cardInstances[it] + 1
                }
            }
        }
    }
    return cardInstances.sum()
}

private fun parseCard(line: String): Card {
    val numberLists = line.substringAfter(":")
        .split("|")
        .map {
            it.trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        }
    return Card(numberLists[0], numberLists[1])
}
