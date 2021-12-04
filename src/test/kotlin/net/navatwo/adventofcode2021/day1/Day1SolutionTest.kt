package net.navatwo.adventofcode2021.day1

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.loadLines
import net.navatwo.adventofcode2021.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1SolutionTest {
    @Test
    fun `p1 sample`() {
        val resourceName = "day1/p1_sample.txt"
        val solution = Day1Solution.Part1()
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(7)
    }

    @Test
    fun `p1`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part1()
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(1681)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }

    @Test
    fun `p2 sample`() {
        val resourceName = "day1/p1_sample.txt"
        val solution = Day1Solution.Part2()
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(5)
    }

    @Test
    fun `p2`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part2()
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(1704)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }
}