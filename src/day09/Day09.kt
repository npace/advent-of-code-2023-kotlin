package day09

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day09/Day09_test")
    val input = readInput("day09/Day09")

    var startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 114L)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")

    startTime = System.currentTimeMillis()
    assertEqual(part2(testInput1), 2L)
    log("Part 2 result: ${part2(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Long {
    val results = parseReport(input).map { valueHistory ->
        valueHistory
            .getDifferenceSequences()
            .predictNextValues()
            .last()
    }

    return results.sum()
}

private fun part2(input: List<String>): Long {
    log(input)
    val results = parseReport(input).map { valueHistory ->
        valueHistory
            .getDifferenceSequences()
            .predictPreviousValues()
            .last()
    }
    return results.sum()
}

typealias Report = List<List<Long>>

private fun parseReport(input: List<String>): Report {
    return input
        .map { line -> line.split(" ").map { it.toLong() } }
}

private fun Report.predictNextValues(): List<Long> {
    val lastValues = map { it.last() }.reversed()
    val predictions = lastValues.foldIndexed(mutableListOf<Long>()) { index, acc, lastValue ->
        acc.add(
            if (index == 0) {
                0
            } else {
                lastValue + acc[index - 1]
            }
        )
        acc
    }
    return predictions
}

private fun Report.predictPreviousValues(): List<Long> {
    val firstValues = map { it.first() }.reversed()
    val predictions = firstValues.foldIndexed(mutableListOf<Long>()) { index, acc, firstValue ->
        acc.add(
            if (index == 0) {
                0
            } else {
                firstValue - acc[index - 1]
            }
        )
        acc
    }
    return predictions
}

private fun List<Long>.getDifferenceSequences(): Report {
    var values = this
    val allSequences = mutableListOf(values)
    while (values.any { it != 0L }) {
        values = differencesBetweenValues(values)
        allSequences.add(values)
    }
    log(allSequences.joinToString("\n"))
    return allSequences
}

private fun differencesBetweenValues(values: List<Long>) = values.mapIndexedNotNull { index, value ->
    if (index > 0) {
        value - values[index - 1]
    } else {
        null
    }
}