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

        assertThat(input.numbers).startsWith(7, 4, 9, 5, 11)
        assertThat(input.cards).hasSize(3)

        assertThat(input.cards).containsExactly(
            createCard(
                listOf(22, 13, 17, 11, 0),
                listOf(8, 2, 23, 4, 24),
                listOf(21, 9, 14, 16, 7),
                listOf(6, 10, 3, 18, 5),
                listOf(1, 12, 20, 15, 19),
            ),
            createCard(
                listOf(3, 15, 0, 2, 22),
                listOf(9, 18, 13, 17, 5),
                listOf(19, 8, 7, 25, 23),
                listOf(20, 11, 10, 24, 4),
                listOf(14, 21, 16, 12, 6),
            ),
            createCard(
                listOf(14, 21, 17, 24, 4),
                listOf(10, 16, 15, 9, 19),
                listOf(18, 8, 23, 26, 20),
                listOf(22, 11, 13, 6, 5),
                listOf(2, 0, 12, 3, 7),
            ),
        )

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

private fun createCard(vararg rows: List<Int>): Day4Solution.Card = Day4Solution.Card(
    board = rows.map { row -> row.map { Day4Solution.Card.Element(it, false) } },
)