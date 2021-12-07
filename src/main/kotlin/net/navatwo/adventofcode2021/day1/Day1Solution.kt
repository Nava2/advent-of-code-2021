package net.navatwo.adventofcode2021.day1

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day1Solution : Solution<List<Day1Solution.Depth>, ComputedResult> {
    object Part1 : Day1Solution() {
        override fun solve(input: List<Depth>): ComputedResult {
            return ComputedResult.Simple(
                input.zipWithNext()
                    .map { (prev, next) -> next.depth >= prev.depth }
                    .count { it },
            )
        }
    }

    object Part2 : Day1Solution() {
        override fun solve(input: List<Depth>): ComputedResult {
            val inputsW2 = input.subList(1, input.size)
            val inputsW3 = input.subList(2, input.size)

            val windowValues = input.zip(inputsW2.zip(inputsW3)) { a, (b, c) -> a.depth + b.depth + c.depth }
            return ComputedResult.Simple(
                computed = windowValues.zipWithNext()
                    .map { (prev, next) -> next > prev }
                    .count { it },
            )
        }
    }

    override fun parse(lines: List<String>): List<Depth> = lines.map { Depth(it.toInt()) }

    data class Depth(val depth: Int)
}