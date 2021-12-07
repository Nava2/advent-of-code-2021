package net.navatwo.adventofcode2021.day7

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day7Solution : Solution<Day7Solution.Input, ComputedResult> {
    object Part1 : Day7Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            crabs = lines.first().splitToSequence(',').map { Crab(it.toInt()) }.toList(),
        )
    }

    data class Input(
        val crabs: List<Crab>,
    )

    @JvmInline
    value class Crab(val horizontalPosition: Int)
}