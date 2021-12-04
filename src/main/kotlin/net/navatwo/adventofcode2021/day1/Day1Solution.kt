package net.navatwo.adventofcode2021.day1

import net.navatwo.adventofcode2021.framework.Solution

object Day1Solution {
    class Part1 : Solution<Depth, Int> {
        override fun parse(lines: List<String>): List<Depth> = Day1Solution.parse(lines)

        override fun solve(inputs: List<Depth>): Int {
            return inputs.zipWithNext()
                .map { (prev, next) -> next.depth >= prev.depth }
                .count { it }
        }
    }

    class Part2 : Solution<Depth, Int> {
        override fun parse(lines: List<String>): List<Depth> = Day1Solution.parse(lines)

        override fun solve(inputs: List<Depth>): Int {
            val inputsW2 = inputs.subList(1, inputs.size)
            val inputsW3 = inputs.subList(2, inputs.size)

            val windowValues = inputs.zip(inputsW2.zip(inputsW3)) { a, (b, c) -> a.depth + b.depth + c.depth }
            return windowValues.zipWithNext()
                .map { (prev, next) -> next > prev }
                .count { it }
        }
    }

    private fun parse(lines: List<String>): List<Depth> = lines.map { Depth(it.toInt()) }

    data class Depth(val depth: Int)
}