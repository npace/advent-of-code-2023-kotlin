package day08

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day08/Day08_test")
    val testInput2 = readInput("day08/Day08_test2")
    val input = readInput("day08/Day08")

    val startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 2)
    assertEqual(part1(testInput2), 6)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    val instructions = input.first()
    val network = parseNetwork(input)
    return searchNetwork(instructions, network)
}

private fun parseNetwork(input: List<String>): Network = input.drop(2).map {
    val name = it.substringBefore(" =")
    val (left, right) = it.substringAfter("(")
        .substringBefore(")")
        .split(",")
        .map { n -> n.trim() }
    Node(name, left, right)
}.associateBy { it.name }

private fun searchNetwork(instructions: String, network: Network): Int {
    val start = "AAA"
    val finish = "ZZZ"
    var count = 0
    var currentNodeName = start
    while (currentNodeName != finish) {
        val nextInstruction = instructions[count % instructions.length].toString()
        currentNodeName = if (nextInstruction == "L") {
            network[currentNodeName]!!.leftName
        } else {
            network[currentNodeName]!!.rightName
        }
        count++
    }
    return count
}

data class Node(val name: String, val leftName: String, val rightName: String)

typealias Network = Map<String, Node>