package net.navatwo.adventofcode2021.day11

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day11Solution : Solution<Day11Solution.Input> {
    object Part1 : Day11Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("oof")
        }
    }

    object Part2 : Day11Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("oof")
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            octopuses = lines.map { line ->
                line.map { Octopus(it.digitToInt()) }
            },
        )
    }

    data class Input(
        val octopuses: List<List<Octopus>>,
    )

    @JvmInline
    value class Octopus(val energy: Int)
}