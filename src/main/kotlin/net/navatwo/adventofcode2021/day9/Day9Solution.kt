package net.navatwo.adventofcode2021.day9

import net.navatwo.adventofcode2021.day8.Signal.A
import net.navatwo.adventofcode2021.day8.Signal.B
import net.navatwo.adventofcode2021.day8.Signal.C
import net.navatwo.adventofcode2021.day8.Signal.D
import net.navatwo.adventofcode2021.day8.Signal.E
import net.navatwo.adventofcode2021.day8.Signal.F
import net.navatwo.adventofcode2021.day8.Signal.G
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day9Solution : Solution<Day9Solution.Input> {
    object Part1 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(10)
        }
    }

    object Part2 : Day9Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(100)
        }
    }

    override fun parse(lines: List<String>): Input {
        TODO("oof")
    }

    data class Input(
        val numbers: List<Int>,
    )
}