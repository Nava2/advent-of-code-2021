package net.navatwo.adventofcode2021.day6

import net.navatwo.adventofcode2021.framework.Solution

sealed class Day6Solution : Solution<Day6Solution.Input, Day6Solution.Result> {
    object Part1 : Day6Solution() {
        override fun solve(input: Input): Result {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(
        val numbers: List<Int>,
    )

    data class Result(
        val computed: Int,
    )
}