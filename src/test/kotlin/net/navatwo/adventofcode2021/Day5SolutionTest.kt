package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day5.Day5Solution
import net.navatwo.adventofcode2021.day5.Day5Solution.Input.Line
import net.navatwo.adventofcode2021.day5.Day5Solution.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day5/p1_sample.txt"
private const val INPUT_RESOURCE = "day5/p1_input.txt"
private val part1 = Day5Solution.Part1
private val part2 = Day5Solution.Part2

class Day5SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)

        assertThat(input.lines).contains(
            Line(
                from = Point(0, 9),
                to = Point(5, 9),
            ),
            Line(
                from = Point(8, 0),
                to = Point(0, 8),
            ),
            Line(
                from = Point(9, 4),
                to = Point(3, 4),
            ),
            Line(
                from = Point(2, 2),
                to = Point(2, 1),
            ),
            Line(
                from = Point(7, 0),
                to = Point(7, 4),
            ),
        )

        val result = part1.solve(input)
        assertThat(result.computed).isEqualTo(5)
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result.computed).isEqualTo(5774)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        val result = part2.solve(input)
        assertThat(result.computed).isEqualTo(12)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result.computed).isEqualTo(18423)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}