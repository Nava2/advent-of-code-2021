package net.navatwo.adventofcode2021.day13

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day13Solution : Solution<Day13Solution.Input> {
    object Part1 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("foo bar")
        }
    }

    object Part2 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(
        val numbers: List<Int>,
    )
}