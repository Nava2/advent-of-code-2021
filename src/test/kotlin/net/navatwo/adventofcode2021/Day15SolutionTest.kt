package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day15.Day15Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day15/p1_sample.txt"
private const val INPUT_RESOURCE = "day15/p1_input.txt"
private val part1 = Day15Solution.Part1
private val part2 = Day15Solution.Part2

class Day15SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isComputed(40)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isComputed(40)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(40)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(40)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}