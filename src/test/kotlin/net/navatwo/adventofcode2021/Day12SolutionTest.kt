package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day12.Day12Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE_1 = "day12/p1_sample.txt"
private const val SAMPLE_RESOURCE_2 = "day12/p2_sample.txt"
private const val SAMPLE_RESOURCE_3 = "day12/p3_sample.txt"
private const val INPUT_RESOURCE = "day12/p1_input.txt"
private val part1 = Day12Solution.Part1
private val part2 = Day12Solution.Part2

class Day12SolutionTest {
    @Test
    fun `p1 sample 1`() {
        val input = part1.parseResource(SAMPLE_RESOURCE_1)

        val result = part1.solve(input)
        assertThat(result).isComputed(10)
    }

    @Test
    fun `p1 sample 2`() {
        val input = part1.parseResource(SAMPLE_RESOURCE_2)

        val result = part1.solve(input)
        assertThat(result).isComputed(19)
    }

    @Test
    fun `p1 sample 3`() {
        val input = part1.parseResource(SAMPLE_RESOURCE_3)

        val result = part1.solve(input)
        assertThat(result).isComputed(226)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isComputed(5228)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample 1`() {
        val input = part2.parseResource(SAMPLE_RESOURCE_1)

        val result = part2.solve(input)
        assertThat(result).isComputed(195)
    }

    @Test
    fun `p2 - sample 2`() {
        val input = part2.parseResource(SAMPLE_RESOURCE_2)

        val result = part2.solve(input)
        assertThat(result).isComputed(195)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(437)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}