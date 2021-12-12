package net.navatwo.adventofcode2021.day10

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day10Solution : Solution<Day10Solution.Input> {
    object Part1 : Day10Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(10L)
        }
    }

    object Part2 : Day10Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(10L)
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(
        val numbers: List<Int>,
    )
}