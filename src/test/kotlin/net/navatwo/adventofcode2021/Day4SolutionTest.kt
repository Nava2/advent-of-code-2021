package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day3.Day3Solution
import net.navatwo.adventofcode2021.day3.Day3Solution.LifeSupportRate
import net.navatwo.adventofcode2021.day4.Day4Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day4/p1_sample.txt"
private const val INPUT_RESOURCE = "day4/p1_input.txt"
private val part1 = Day4Solution.Part1
private val part2 = Day3Solution.Part2

class Day4SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isEqualTo(198)

    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isEqualTo(2250414)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)
        assertThat(part2.solve(input)).isEqualTo(
            LifeSupportRate(
                oxygenGeneratorRate = 23,
                co2ScrubberRate = 10,
            )
        )
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result.consumption).isEqualTo(6085575)
        assertThat(result).isEqualTo(
            LifeSupportRate(
                oxygenGeneratorRate = 1935,
                co2ScrubberRate = 3145,
            )
        )

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}