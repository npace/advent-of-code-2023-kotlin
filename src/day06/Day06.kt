package day06

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day06/Day06_test")
    val input = readInput("day06/Day06")

    var startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 288)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")

    startTime = System.currentTimeMillis()
    assertEqual(part2(testInput1), 71503)
    log("Part 2 result: ${part2(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    log(input)
    val (times, recordDistances) = getTimesAndRecordDistances(input)

    return times
        .mapIndexed { index, time ->
            countOfRecordBreakingDistances(time, recordDistances[index])
        }.marginOfError()
}

private fun List<Int>.marginOfError() = fold(1) { acc, i -> i * acc }

private fun part2(input: List<String>): Int {
    log(input)
    val (time, recordDistance) = getTimeAndRecordDistance(input)
    return countOfRecordBreakingDistances(time, recordDistance)
}

private fun getTimesAndRecordDistances(input: List<String>) = input.map { line ->
    line.substringAfter(":")
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toLong() }
}

private fun getTimeAndRecordDistance(input: List<String>) = input.map { line ->
    line.substringAfter(":")
        .replace(" ", "")
        .toLong()
}

private fun countOfRecordBreakingDistances(time: Long, recordDistance: Long): Int {
    val distances = (0..<time).map { t ->
        val remainingTime = time - t
        val speedPerSecond = 1 * t
        val distance = remainingTime * speedPerSecond
        distance
    }
    return distances.count { it > recordDistance }
}
