package net.navatwo.adventofcode2021.day9

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day9Solution : Solution<Day9Solution.Input> {
    object Part1 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            val heightMap = input.heightMap

            val totalRisk = heightMap.asSequence()
                .withIndex()
                .flatMap { (y, row) ->
                    row.asSequence()
                        .withIndex()
                        .mapNotNull { (x, cell) ->
                            val isLower = listOfNotNull(
                                input.getCoord(x - 1, y),
                                input.getCoord(x, y - 1),
                                input.getCoord(x + 1, y),
                                input.getCoord(x, y + 1),
                            ).all { it.height > cell.height }

                            (y to x).takeIf { isLower }
                        }
                }
                .mapNotNull { (y, x) ->
                    input.getCoord(x, y)?.let { it.height + 1 }
                }
                .sum()

            return ComputedResult.Simple(totalRisk)
        }
    }

    object Part2 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(100)
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

    data class Input(
        val heightMap: List<List<Height>>,
    )

    fun Input.getCoord(x: Int, y: Int): Height? {
        val row = heightMap.getOrNull(y)
        return row?.getOrNull(x)
    }

    @JvmInline
    value class Height(val height: Int)
}