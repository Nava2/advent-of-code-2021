package net.navatwo.adventofcode2021.day1

import net.navatwo.adventofcode2021.framework.Solution

sealed class Day1Solution : Solution<Day1Solution.Depth, Int> {
    object Part1 : Day1Solution() {
        override fun solve(inputs: List<Depth>): Int {
            return inputs.zipWithNext()
                .map { (prev, next) -> next.depth >= prev.depth }
                .count { it }
        }
    }

    object Part2 : Day1Solution() {
        override fun solve(inputs: List<Depth>): Int {
            val inputsW2 = inputs.subList(1, inputs.size)
            val inputsW3 = inputs.subList(2, inputs.size)

            val windowValues = inputs.zip(inputsW2.zip(inputsW3)) { a, (b, c) -> a.depth + b.depth + c.depth }
            return windowValues.zipWithNext()
                .map { (prev, next) -> next > prev }
                .count { it }
        }
    }

    override fun parse(lines: List<String>): List<Depth> = lines.map { Depth(it.toInt()) }

    data class Depth(val depth: Int)
}