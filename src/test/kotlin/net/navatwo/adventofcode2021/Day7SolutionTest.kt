package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day7.Day7Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day7/p1_sample.txt"
private const val INPUT_RESOURCE = "day7/p1_input.txt"
private val part1 = Day7Solution.Part1
private val part2 = Day7Solution.Part1

class Day7SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)

        runAndAssert(
            input = input,
            days = 2,
            expected = listOf(1, 2, 1, 6, 0, 8),
        )

        val result = part1.solve(input)
        assertThat(result).isComputed(37)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isComputed(363101)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(26984457539L)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(1644286074024L)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }

    private fun runAndAssert(input: Day7Solution.Input, days: Int, expected: Collection<Int>) {
//        val result = Day6Solution.computeFish(input, days)
//
//        assertThat(result.fishTable.fishes()).containsExactlyInAnyOrderElementsOf(
//            expected.sorted().map { AnglerFish(it) },
//        )
//        assertThat(result.computed).isEqualTo(expected.size.toLong())
    }
}