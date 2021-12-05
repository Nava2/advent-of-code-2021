package net.navatwo.adventofcode2021.day4

import net.navatwo.adventofcode2021.framework.Solution

sealed class Day4Solution<S> : Solution<Day4Solution.Input, S> {
    object Part1 : Day4Solution<Int>() {
        override fun solve(input: Input): Int {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO()
    }

    data class Input(val int: Int)
}