package day09

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day09/Day09_test")
    val input = readInput("day09/Day09")

    val startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 114L)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Long {
    log(input)
    val report = input
        .map { line -> line.split(" ").map { it.toLong() } }
    val results = report.map { valueHistory ->
        valueHistory
            .getDifferenceSequences()
            .predictNextValues()
            .last()
    }

    return results.sum()
}

private fun List<List<Long>>.predictNextValues(): List<Long> {
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

private fun List<Long>.getDifferenceSequences(): List<List<Long>> {
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