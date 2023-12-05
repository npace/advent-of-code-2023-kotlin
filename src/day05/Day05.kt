package day05

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day05/Day05_test")
    val input = readInput("day05/Day05")

    log("\nPart 1:")
    assertEqual(part1(testInput1), 35L)
    log(part1(input))

    log("\nPart 2:")
    assertEqual(part2(testInput1), 46L)
    log(part2(input))
}

data class AlmanacBlock(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)
data class AlmanacMap(val from: String, val to: String, val blocks: List<AlmanacBlock>)

fun part1(input: List<String>): Long {
    log(input)
    log("\n****************\n")

    val seeds = parseSeeds(input)
    val almanacMaps = parseAlmanacMaps(input)
    val mappings = calculateMappings(seeds, almanacMaps)

    log("mappings: $mappings")

    return mappings.find { it.first == "location" }!!.second.min()
}

fun part2(input: List<String>): Long {
    log(input)
    log("\n****************\n")

    val seeds = parseSeeds(input)
    val seedRanges = parseSeedRanges(seeds)
    val allConsideredSeeds = seedRanges.map {
        it.toList()
    }.flatten()
    log("all considered seeds: $allConsideredSeeds")
    val almanacMaps = parseAlmanacMaps(input)
    val mappings = calculateMappings(allConsideredSeeds, almanacMaps)

    log(mappings)

    return mappings.find { it.first == "location" }!!.second.min()
}

private fun parseSeedRanges(seeds: Sequence<Long>): Sequence<LongRange> {
    val seedRanges = seeds.windowed(2, 2)
    return seedRanges.map {
        val (start, size) = it
        LongRange(start, start + size - 1)
    }
}

private fun parseSeeds(input: List<String>): Sequence<Long> {
    return input.first().substringAfter(":").split(" ").filter { it.isNotEmpty() }
        .map { it.toLong() }.asSequence()
}

private fun parseAlmanacMaps(input: List<String>): Sequence<AlmanacMap> {
    val maps = mutableListOf(mutableListOf<String>())
    input.drop(2).forEach { line ->
        if (line.isNotBlank()) {
            maps.last().add(line)
        } else {
            maps.add(mutableListOf())
        }
    }
    return maps.map { mapLines ->
        val (from, _, to) = mapLines.first().substringBefore(" ").split("-")
        val mappings = mapLines.drop(1).map { blockLine ->
            val (dst, src, len) = blockLine.split(" ").map { it.toLong() }
            AlmanacBlock(dst, src, len)
        }
        AlmanacMap(from, to, mappings)
    }.asSequence()
}

private fun calculateMappings(seeds: Sequence<Long>, almanacMaps: Sequence<AlmanacMap>): Sequence<Pair<String, Sequence<Long>>> {
    val mappings = mutableListOf("seed" to seeds)
    almanacMaps.forEach { almanacMap ->
        val fromVals = mappings.last().second
        val toVals = fromVals.map { from ->
            val mappedVals = mutableListOf<Long>()
            var wasMapped = false
            almanacMap.blocks.forEach { block ->
                if (from >= block.sourceRangeStart && from <= block.sourceRangeStart + block.rangeLength) {
                    mappedVals.add(from + block.destinationRangeStart - block.sourceRangeStart)
                    wasMapped = true
                }
            }
            if (!wasMapped) {
                mappedVals.add(from)
            }
            mappedVals
        }.flatten()
        mappings.add(almanacMap.to to toVals)
    }
    return mappings.asSequence()
}
