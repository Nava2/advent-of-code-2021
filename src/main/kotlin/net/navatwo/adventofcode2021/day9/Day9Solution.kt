package net.navatwo.adventofcode2021.day9

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import net.navatwo.adventofcode2021.getCoord
import net.navatwo.adventofcode2021.getOrNull
import net.navatwo.adventofcode2021.initializeVisitedGrid

sealed class Day9Solution : Solution<Day9Solution.Input> {
    object Part1 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            val totalRisk = input.lowPoints()
                .mapNotNull { (y, x) ->
                    input.heightMap.getCoord(x, y)?.let { it.height + 1 }
                }
                .sum()

            return ComputedResult.Simple(totalRisk)
        }
    }

    object Part2 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            val visited = input.heightMap.initializeVisitedGrid()

            val basins = input.lowPoints()
                .map { coord ->
                    val queue = ArrayDeque<Coord>()
                    queue.addFirst(coord)

                    var basinSize = 0

                    while (queue.isNotEmpty()) {
                        val toCheck = queue.removeFirst()
                        val (x, y) = toCheck

                        if (visited[y][x]) {
                            continue
                        }

                        basinSize += 1
                        visited[y][x] = true

                        val tileHeight = input.heightMap.getCoord(x, y)!!.height

                        queue.addAll(
                            forCoords(x, y).filter { (x, y) ->
                                val tile = input.heightMap.getCoord(x, y)
                                    ?: return@filter false

                                if (visited[y][x]) return@filter false

                                tile.height != 9 && tile.height > tileHeight
                            }
                        )
                    }

                    coord to basinSize
                }


            val sortedBasins = basins.sortedByDescending { (_, counter) -> counter }

            val result = sortedBasins[0].second *
                    sortedBasins[1].second *
                    sortedBasins[2].second

            return ComputedResult.Simple(result)
        }

    }

    override fun parse(lines: List<String>): Input {
        return Input(
            heightMap = lines.map { line ->
                line.map { c ->
                    Height(c.digitToInt())
                }
            }
        )
    }

    internal fun forCoords(x: Int, y: Int): Iterable<Coord> = listOf(
        Coord(x - 1, y),
        Coord(x, y - 1),
        Coord(x + 1, y),
        Coord(x, y + 1),
    )

    internal fun Input.lowPoints(): Iterable<Coord> {
        return heightMap.asSequence()
            .withIndex()
            .flatMap { (y, row) ->
                row.asSequence()
                    .withIndex()
                    .mapNotNull { (x, cell) ->
                        val isLower = forCoords(x, y).all {
                            val height = heightMap.getOrNull(it)?.height
                            height != null && height > cell.height
                        }

                        Coord(x, y).takeIf { isLower }
                    }
            }
            .toList()
    }

    data class Input(
        val heightMap: List<List<Height>>,
    )

    fun Input.isValidCoord(x: Int, y: Int): Boolean {
        return y in heightMap.indices &&
                x in heightMap.first().indices
    }

    fun Input.isValidCoord(coord: Coord): Boolean = isValidCoord(coord.x, coord.y)

    @JvmInline
    value class Height(val height: Int)
}
