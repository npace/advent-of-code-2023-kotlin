package day07

import assertEqual
import day07.Card.J
import log
import readInput

fun main() {
    val testInput1 = readInput("day07/Day07_test")
    val input = readInput("day07/Day07")

    var startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 6440)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")

    startTime = System.currentTimeMillis()
    assertEqual(part2(testInput1), 5905)
    log("Part 2 result: ${part2(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    val hands = parseHands(input, false)
    val rankedHands = rankCards(hands, false)
    return calculateWinnings(rankedHands)
}

private fun part2(input: List<String>): Int {
    val hands = parseHands(input, true)
    val rankedHands = rankCards(hands, true)
    return calculateWinnings(rankedHands)
}

private fun parseHands(input: List<String>, useJoker: Boolean) = input.map { line ->
    val cards = line.substringBefore(" ").map { Card.valueOf(it.toString()) }
    val bid = line.substringAfter(" ").toInt()
    Hand(
        cards = cards,
        bid = bid,
        strength = calculateHandStrength(cards, useJoker),
    )
}

private fun calculateHandStrength(cards: List<Card>, useJoker: Boolean = false): HandStrength {
    val cardFrequencyMap = mutableMapOf<Card, Int>()
    cards.forEach {
        cardFrequencyMap[it] = cardFrequencyMap.getOrDefault(it, 0) + 1
    }
    val jokersCount = cardFrequencyMap[J]
    if (useJoker && jokersCount != null) {
        val mostFrequentCard = cardFrequencyMap.toList().sortedByDescending { it.second }
            .firstOrNull { it.first != J }?.first
        if (mostFrequentCard != null) {
            cardFrequencyMap[mostFrequentCard] = cardFrequencyMap[mostFrequentCard]!! + jokersCount
            cardFrequencyMap[J] = cardFrequencyMap[J]!! - jokersCount
        }
    }
    val handStrength = when {
        cardFrequencyMap.filter { it.value > 0 }.size == 1 -> HandStrength.FiveOfAKind
        cardFrequencyMap.any { it.value == 4 } -> HandStrength.FourOfAKind
        cardFrequencyMap.any { it.value == 3 } && cardFrequencyMap.any { it.value == 2 } -> HandStrength.FullHouse
        cardFrequencyMap.any { it.value == 3 } -> HandStrength.ThreeOfAKind
        cardFrequencyMap.count { it.value == 2 } == 2 -> HandStrength.TwoPair
        cardFrequencyMap.any { it.value == 2 } -> HandStrength.OnePair
        else -> HandStrength.HighCard
    }
    return handStrength
}

private fun rankCards(hands: List<Hand>, useJoker: Boolean): List<Pair<Hand, Int>> {
    return hands
        .sortedWith(handComparator(useJoker))
        .mapIndexed { index, hand -> hand to index + 1 }
}

private fun handComparator(useJoker: Boolean) = Comparator<Hand> { o1, o2 ->
    if (o1.strength > o2.strength) {
        -1
    } else if (o1.strength < o2.strength) {
        1
    } else {
        val cardComparator = cardComparator(useJoker)
        o1.cards.forEachIndexed { index, card1 ->
            val card2 = o2.cards[index]
            val comparison = cardComparator.compare(card1, card2)
            if (comparison == 1) {
                return@Comparator -1
            } else if (comparison == -1) {
                return@Comparator 1
            }
        }
        return@Comparator 0
    }
}

private fun calculateWinnings(rankedCards: List<Pair<Hand, Int>>): Int {
    return rankedCards.fold(0) { acc: Int, rankedHand: Pair<Hand, Int> ->
        val (card, rank) = rankedHand
        acc + (card.bid * rank)
    }
}

@Suppress("EnumEntryName")
enum class Card {
    A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`;

    fun getStrength(useJoker: Boolean): Int {
        if (useJoker) {
            if (this == J) return entries.size - 1
            else if (this > J) return ordinal - 1
        }
        return ordinal
    }
}

private fun cardComparator(useJoker: Boolean) = Comparator<Card> { o1, o2 ->
    if (o1.getStrength(useJoker) > o2.getStrength(useJoker))
        1
    else if (o1.getStrength(useJoker) < o2.getStrength(useJoker))
        -1
    else
        0
}

data class Hand(val cards: List<Card>, val bid: Int, val strength: HandStrength)

enum class HandStrength {
    FiveOfAKind, //AAAAA
    FourOfAKind, //AA8AA
    FullHouse, //23332
    ThreeOfAKind, //TTT98
    TwoPair, //23432
    OnePair, //A23A4
    HighCard; //23456
}
