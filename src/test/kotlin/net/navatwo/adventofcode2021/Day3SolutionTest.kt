package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day2.Day2Solution
import net.navatwo.adventofcode2021.day3.Day3Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day3/p1_sample.txt"
private const val INPUT_RESOURCE = "day3/p1_input.txt"
private val part1 = Day3Solution.Part1
private val part2 = Day2Solution.Part2

class Day3SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)
        assertThat(input.map { it.toString() }.take(4)).containsExactly(
            "{2}",
            "{0, 1, 2, 3}",
            "{0, 2, 3}",
            "{0, 2, 3, 4}",
        )

        val result = part1.solve(input)
        assertThat(result).isEqualTo(Day3Solution.PowerRate(22, 9))
        assertThat(result.consumption).isEqualTo(198)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result.consumption).isEqualTo(2250414)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)
        assertThat(part2.solve(input)).isEqualTo(900)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)
        assertThat(part2.solve(input)).isEqualTo(2078985210)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}