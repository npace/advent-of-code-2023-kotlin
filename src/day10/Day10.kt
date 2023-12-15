package day10

import assertEqual
import log
import readInput

fun main() {
    val testInput1 = readInput("day10/Day10_test")
    val testInput2 = readInput("day10/Day10_test2")
    val input = readInput("day10/Day10")

    val startTime = System.currentTimeMillis()
    assertEqual(part1(testInput1), 4)
    assertEqual(part1(testInput2), 8)
    log("Part 1 result: ${part1(input)}, took ${System.currentTimeMillis() - startTime}ms")
}

private fun part1(input: List<String>): Int {
    log(input.joinToString("\n"))
    val grid = input.parseGrid()
    val loop = grid.findLoop()
    grid.printInGrid(loop)
    return loop.size / 2
}

class Grid(private val gridElements: List<List<GridElement>>) {
class Grid(private val elements: List<List<GridElement>>) {
    private val creature = findCreature()

    fun findLoop(): Collection<GridElement> {
        log("Creature is at ${creature.x}, ${creature.y}")
        val (startTile, startDirection) = creatureTileAndDirections()
        log("Start Tile:\n$startTile")
        log("Start direction:\n$startDirection")

        // find loop starting from creature
        val loop = mutableSetOf(creature)
        var currentDirection = startTile.nextDirection(startDirection)
        var current = creature.goInDirection(currentDirection)!!
        while (current != creature) {
            loop.add(current)
            currentDirection = current.tile.nextDirection(currentDirection)
            current = current.goInDirection(currentDirection)!!
        }

        return loop
    }

    private fun findCreature(): GridElement {
        return elements.flatten().first { it.tile == Tile.Creature }
    }

    private fun creatureTileAndDirections(): Pair<Tile, Direction> {
        val left = creature.leftElement()
        val up = creature.upElement()
        val right = creature.rightElement()
        val down = creature.downElement()
        log("Adjacent tiles are:")
        printInGrid(listOfNotNull(left, up, right, down))
        val potentialTilesForCreature = mutableListOf<Pair<Tile, Direction>>()
        if (left.connectsRight() && up.connectsDown()) {
            potentialTilesForCreature.add(Tile.UpToLeft to Direction.Up)
        }
        if (left.connectsRight() && down.connectsUp()) {
            potentialTilesForCreature.add(Tile.LeftToDown to Direction.Left)
        }
        if (right.connectsLeft() && up.connectsDown()) {
            potentialTilesForCreature.add(Tile.UpToRight to Direction.Right)
        }
        if (right.connectsLeft() && down.connectsUp()) {
            potentialTilesForCreature.add(Tile.RightToDown to Direction.Down)
        }
        if (left.connectsRight() && right.connectsLeft()) {
            potentialTilesForCreature.add(Tile.LeftToRight to Direction.Right)
        }
        if (up.connectsDown() && down.connectsUp()) {
            potentialTilesForCreature.add(Tile.UpToDown to Direction.Down)
        }
        assertEqual(potentialTilesForCreature.size, 1)
        return potentialTilesForCreature.first()
    }

    private fun GridElement?.connectsRight() = connects("Right")
    private fun GridElement?.connectsLeft() = connects("Left")
    private fun GridElement?.connectsUp() = connects("Up")
    private fun GridElement?.connectsDown() = connects("Down")

    private fun GridElement?.connects(direction: String) = this?.tile?.name?.contains(direction) == true

    private fun GridElement.goInDirection(direction: Direction): GridElement? {
        return when (direction) {
            Direction.Left -> leftElement()
            Direction.Up -> upElement()
            Direction.Right -> rightElement()
            Direction.Down -> downElement()
        }
    }

    private fun GridElement.upElement(): GridElement? = elementAt(x, y - 1)
    private fun GridElement.downElement(): GridElement? = elementAt(x, y + 1)
    private fun GridElement.leftElement(): GridElement? = elementAt(x - 1, y)
    private fun GridElement.rightElement(): GridElement? = elementAt(x + 1, y)

    private fun elementAt(x: Int, y: Int): GridElement? {
        val isInBounds = x < 0 || x > elements.size
                || y < 0 || y > elements.first().size
        return if (isInBounds)
            null
        else
            elements[y][x]
    }

    fun printInGrid(subGroup: Collection<GridElement>) {
        elements.forEach { line ->
            log(buildString {
                line.forEach { element ->
                    if (element in subGroup) {
                        append(element.tile)
                    } else {
                        append(".")
                    }
                }
            })
        }
    }

    override fun toString(): String {
        return elements.joinToString("\n")
    }
}

data class GridElement(
    val x: Int,
    val y: Int,
    val tile: Tile,
) {
    override fun toString(): String {
        return "$tile [$x,$y]"
    }
}

enum class Direction {
    Left, Up, Right, Down;

    val opposite get() = entries[(ordinal + 2) % entries.size]
}

enum class Tile(val symbol: Char, private vararg val connections: Direction) {
    UpToDown('|', Direction.Up, Direction.Down),
    LeftToRight('-', Direction.Left, Direction.Right),
    UpToRight('L', Direction.Up, Direction.Right),
    RightToDown('F', Direction.Down, Direction.Right),
    LeftToDown('7', Direction.Down, Direction.Left),
    UpToLeft('J', Direction.Up, Direction.Left),
    Creature('S'),
    Ground('.');

    fun nextDirection(previousDirection: Direction): Direction {
        return connections.first { it != previousDirection.opposite }
    }

    override fun toString(): String {
        return when (this) {
            UpToDown -> "┃"
            LeftToRight -> "━"
            UpToRight -> "┗"
            RightToDown -> "┏"
            LeftToDown -> "┓"
            UpToLeft -> "┛"
            else -> symbol.toString()
        }
    }
}

private fun List<String>.parseGrid(): Grid {
    return Grid(mapIndexed { yIndex, line ->
        line.mapIndexed { xIndex, char ->
            GridElement(xIndex, yIndex, char.parseTile())
        }
    })
}

private fun Char.parseTile(): Tile {
    return Tile.entries.first { it.symbol == this }
}
