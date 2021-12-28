package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day15.Day15Solution
import net.navatwo.adventofcode2021.day16.Day16Solution
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet.TypeId.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val SAMPLE_RESOURCE = "day16/p1_sample.txt"
private const val INPUT_RESOURCE = "day16/p1_input.txt"
private val part1 = Day16Solution.Part1
private val part2 = Day16Solution.Part2

class Day16SolutionTest {
    @Test
    fun `p1 sample - 1`() {
        val input = part1.parse(listOf("D2FE28"))

        assertThat(input.packets).containsExactly(
            Packet.Literal(
                version = 6,
                typeId = LITERAL,
                literals = listOf(
                    7, // 0111
                    14, // 1110
                    5, // 0101
                ),
            ),
        )
    }

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
        assertThat(result).isComputed(769)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(315)
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(2963L)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
            inputConfig = Benchmark.Stage.INPUT.config(warmupIterations = 2000U, iterations = 5_000U),
            solveConfig = Benchmark.Stage.SOLVE.config(warmupIterations = 2000U, iterations = 5_000U),
        )
    }
}