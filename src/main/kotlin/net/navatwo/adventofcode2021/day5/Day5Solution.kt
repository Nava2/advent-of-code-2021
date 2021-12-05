package net.navatwo.adventofcode2021.day5

import net.navatwo.adventofcode2021.framework.Solution

sealed class Day5Solution<S> : Solution<Day5Solution.Input, S> {
    object Part1 : Day5Solution<Result>() {
        override fun solve(input: Input): Result {
            TODO()
        }
    }

    object Part2 : Day5Solution<Result>() {
        override fun solve(input: Input): Result {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(val number: Int)

    data class Result(val computed: Int)
}