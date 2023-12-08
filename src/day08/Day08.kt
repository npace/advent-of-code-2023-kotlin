package day08

import assertEqual
import log
import readInput
import java.math.BigInteger

fun main() {
    val testInput1 = readInput("day08/Day08_test")
    val testInput2 = readInput("day08/Day08_test2")
    val testInput3 = readInput("day08/Day08_test3")
    val input = readInput("day08/Day08")

    var startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 2)
    assertEqual(part1(testInput2), 6)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")

    startTime = System.currentTimeMillis()
    assertEqual(part2(testInput3), BigInteger.valueOf(6))
    log("Part 2 result: ${part2(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    val instructions = input.first()
    val network = parseNetwork(input)
    return searchNetwork(instructions, network)
}

private fun part2(input: List<String>): BigInteger {
    val instructions = input.first()
    val network = parseNetwork(input)
    return searchNetworkAsGhost(instructions, network)
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

private fun searchNetworkAsGhost(instructions: String, network: Network): BigInteger {
    val currentNodes = network.values.filter { it.name.endsWith("A") }
    val isAtFinish: (String) -> Boolean = { name -> name.endsWith("Z") }

    val counts = currentNodes.map { node ->
        var c = 0
        var n = node
        log("Step $c, node $n")
        while (!isAtFinish(n.name)) {
            val instruction = instructions[c % instructions.length].toString()
            n = if (instruction == "L") {
                network[n.leftName]!!
            } else {
                network[n.rightName]!!
            }
            c++
        }
        c
    }

    val result = counts
        .map { BigInteger.valueOf(it.toLong()) }
        .reduce { acc, i ->
            acc * i / acc.gcd(i)
        }
    log("counts are $counts, result is $result")
    return result
}


data class Node(val name: String, val leftName: String, val rightName: String)

typealias Network = Map<String, Node>