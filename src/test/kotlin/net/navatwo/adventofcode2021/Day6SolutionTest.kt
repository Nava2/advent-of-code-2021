package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day6.Day6Solution
import net.navatwo.adventofcode2021.day6.Day6Solution.AnglerFish
import net.navatwo.adventofcode2021.day6.Day6Solution.Input
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day6/p1_sample.txt"
private const val INPUT_RESOURCE = "day6/p1_input.txt"
private val part1 = Day6Solution.Part1
private val part2 = Day6Solution.Part2

class Day6SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)

        assertThat(input.fish).containsExactly(
            AnglerFish(3),
            AnglerFish(4),
            AnglerFish(3),
            AnglerFish(1),
            AnglerFish(2),
        )

        runAndAssert(
            input = input,
            days = 2,
            expected = listOf(1, 2, 1, 6, 0, 8),
        )

        runAndAssert(
            input = input,
            days = 3,
            expected = listOf(0, 1, 0, 5, 6, 7, 8),
        )

        runAndAssert(
            input = input,
            days = 18,
            expected = listOf(6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 0, 1, 1, 1, 2, 2, 3, 3, 4, 6, 7, 8, 8, 8, 8),
        )

        val result = part1.solve(input)
        assertThat(result).isComputed(5934)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result.computed).isEqualTo(363101)

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

    private fun runAndAssert(input: Input, days: Int, expected: Collection<Int>) {
        val result = Day6Solution.computeFish(input, days)

        assertThat(result.fishTable.fishes()).containsExactlyInAnyOrderElementsOf(
            expected.sorted().map { AnglerFish(it) },
        )
        assertThat(result).isComputed(expected.size.toLong())
    }
}