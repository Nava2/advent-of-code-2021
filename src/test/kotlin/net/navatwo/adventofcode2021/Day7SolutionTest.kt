package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day7.Day7Solution
import net.navatwo.adventofcode2021.day7.Day7Solution.Crab
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

        assertThat(input.crabs).containsExactlyInAnyOrderElementsOf(
            listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14).map { Crab(it) }
        )

        runAndAssert(
            input = input,
            position = 2,
            expected = mapOf(
                0 to 2,
                1 to 2,
                2 to 0,
                4 to 2,
                7 to 5,
                14 to 12,
                16 to 14,
            ),
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
        assertThat(result).isComputed(168)
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

    private fun runAndAssert(input: Day7Solution.Input, position: Int, expected: Map<Int, Long>) {
        val crabTable = Day7Solution.CrabTable.load(input.crabs)
        assertThat(expected).allSatisfy { fromPosition, fuelCost ->
            assertThat(crabTable.computeFuelCostBetween(fromPosition, position)).isEqualTo(fuelCost)
        }
    }
}