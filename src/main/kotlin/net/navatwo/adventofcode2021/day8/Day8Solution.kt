package net.navatwo.adventofcode2021.day8

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
    object Part1 : Day8Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(10)
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(
        val numbers: List<Int>,
    )
}