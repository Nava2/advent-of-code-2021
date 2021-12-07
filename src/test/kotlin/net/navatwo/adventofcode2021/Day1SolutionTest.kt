package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day1.Day1Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1SolutionTest {
    @Test
    fun `p1 sample`() {
        val resourceName = "day1/p1_sample.txt"
        val solution = Day1Solution.Part1
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part1.solve(input)).isComputed(7)
    }

    @Test
    fun `p1`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part1
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part1.solve(input)).isComputed(1681)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }

    @Test
    fun `p2 sample`() {
        val resourceName = "day1/p1_sample.txt"
        val solution = Day1Solution.Part2
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part2.solve(input)).isComputed(5)
    }

    @Test
    fun `p2`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part2
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part2.solve(input)).isComputed(1704L)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }
}