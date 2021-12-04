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
//        assertThat(input).containsExactly(
//            Action.Forward(5),
//            Action.Down(5),
//            Action.Forward(8),
//            Action.Up(3),
//            Action.Down(8),
//            Action.Forward(2),
//        )

        assertThat(part1.solve(input)).isEqualTo(150)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)
        assertThat(part1.solve(input)).isEqualTo(2070300)

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