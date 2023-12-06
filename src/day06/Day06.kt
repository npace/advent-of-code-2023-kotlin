package day06

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day06/Day06_test")
    val input = readInput("day06/Day06")

    val startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 288)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    log(input)
    val (times, recordDistances) = getTimesAndRecordDistances(input)

    return times
        .mapIndexed { index, time ->
            countOfRecordBreakingDistances(time, recordDistances[index])
        }.fold(1) { acc, i -> i * acc }
}

private fun getTimesAndRecordDistances(input: List<String>) = input.map { line ->
    line.substringAfter(":")
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
}

private fun countOfRecordBreakingDistances(time: Int, recordDistance: Int): Int {
    val distances = (0..<time).map { t ->
        val remainingTime = time - t
        val speedPerSecond = 1 * t
        val distance = remainingTime * speedPerSecond
        distance
    }
    return distances.count { it > recordDistance }
}
