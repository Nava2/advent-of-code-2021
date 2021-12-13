package net.navatwo.adventofcode2021.day12

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day12Solution : Solution<Day12Solution.Input> {
    object Part1 : Day12Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    object Part2 : Day12Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(
        val connections: List<Connection>,
    )

    data class Connection(val a: String, val b: String)
}