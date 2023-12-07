package day07

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day07/Day07_test")
    val input = readInput("day07/Day07")

    val startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 6440)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    log(input)
    val hands = input.map { line ->
        val cards = line.substringBefore(" ").map { Card.valueOf(it.toString()) }
        val bid = line.substringAfter(" ").toInt()
        Hand(
            cards = cards,
            bid = bid,
            strength = calculateHandStrength(cards),
        )
    }
    val rankedHands = rankCards(hands)
    log("ranked hands: ${rankedHands.joinToString("\n")}")
    return calculateWinnings(rankedHands)
}

private fun calculateHandStrength(cards: List<Card>): HandStrength {
    val cardFrequencyMap = mutableMapOf<Card, Int>()
    cards.forEach {
        cardFrequencyMap[it] = cardFrequencyMap.getOrDefault(it, 0) + 1
    }
    return when {
        cardFrequencyMap.size == 1 -> HandStrength.FiveOfAKind
        cardFrequencyMap.any { it.value == 4 } -> HandStrength.FourOfAKind
        cardFrequencyMap.any { it.value == 3 } && cardFrequencyMap.any { it.value == 2 } -> HandStrength.FullHouse
        cardFrequencyMap.any { it.value == 3 } -> HandStrength.ThreeOfAKind
        cardFrequencyMap.count { it.value == 2 } == 2 -> HandStrength.TwoPair
        cardFrequencyMap.any { it.value == 2 } -> HandStrength.OnePair
        else -> HandStrength.HighCard
    }
}

private fun rankCards(hands: List<Hand>): List<Pair<Hand, Int>> {
    return hands
        .sortedWith(handComparator)
        .mapIndexed { index, hand -> hand to index + 1 }
}

private val handComparator = Comparator<Hand> { o1, o2 ->
    if (o1.strength > o2.strength) {
        -1
    } else if (o1.strength < o2.strength) {
        1
    } else {
        o1.cards.forEachIndexed { index, card1 ->
            val card2 = o2.cards[index]
            if (card1 > card2) {
                return@Comparator -1
            } else if (card1 < card2) {
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
