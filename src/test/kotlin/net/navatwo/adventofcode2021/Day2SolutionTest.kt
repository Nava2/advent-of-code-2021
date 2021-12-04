package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day2.Day2Solution
import net.navatwo.adventofcode2021.day2.Day2Solution.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day2SolutionTest {
    @Test
    fun `p1 sample`() {
        val resourceName = "day2/p1_sample.txt"
        val solution = Day2Solution.Part1
        val inputs = solution.parseResource(resourceName)
        assertThat(inputs).containsExactly(
            Action.Forward(5),
            Action.Down(5),
            Action.Forward(8),
            Action.Up(3),
            Action.Down(8),
            Action.Forward(2),
        )

        assertThat(solution.solve(inputs)).isEqualTo(150)
    }

    @Test
    fun p1() {
        val resourceName = "day2/p1_input.txt"
        val solution = Day2Solution.Part1
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(2070300)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }

    @Test
    fun `p2 - sample`() {
        val resourceName = "day2/p1_sample.txt"
        val solution = Day2Solution.Part2
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(900)
    }

    @Test
    fun p2() {
        val resourceName = "day2/p1_input.txt"
        val solution = Day2Solution.Part2
        val inputs = solution.parseResource(resourceName)
        assertThat(solution.solve(inputs)).isEqualTo(2078985210)

        Benchmark.run(
            inputContent = loadLines(resourceName),
            solution = solution,
        )
    }
}