package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day3.Day3Solution
import net.navatwo.adventofcode2021.day3.Day3Solution.Companion.cleanUnmatchedValuesBasedOnBitIndex
import net.navatwo.adventofcode2021.day3.Day3Solution.Companion.isOneMostCommonAtIndex
import net.navatwo.adventofcode2021.day3.Day3Solution.LifeSupportRate
import net.navatwo.adventofcode2021.day3.toBitSet
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.junit.jupiter.api.Test
import java.util.BitSet

private const val SAMPLE_RESOURCE = "day3/p1_sample.txt"
private const val INPUT_RESOURCE = "day3/p1_input.txt"
private val part1 = Day3Solution.Part1
private val part2 = Day3Solution.Part2

class Day3SolutionTest {
    @Test
    fun `p1 sample`() {
        val input = part1.parseResource(SAMPLE_RESOURCE)
        assertThat(input.bitSets.take(4)).containsExactlySets(
            "00100",
            "11110",
            "10110",
            "10111",
        )

        val result = part1.solve(input)
        assertThat(result.consumption).isEqualTo(198)
        assertThat(result).isEqualTo(Day3Solution.PowerRate(22, 9))

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
    fun `p2 - isOneMostCommonAtIndex`() {
        val inputs = listOf(
            "10".toBitSet(),
            "11".toBitSet(),
        )

        assertThat(
            inputs.isOneMostCommonAtIndex(
                bitIndex = 0,
            )
        ).isEqualTo(1)

        assertThat(
            inputs.isOneMostCommonAtIndex(
                bitIndex = 1,
            )
        ).isEqualTo(1)
    }

    @Test
    fun `p2 - cleanUnmatchedValuesBasedOnBitIndex - use most common bit`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        var bitSets = input.bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = true,
            bitIndex = 0,
        )
        assertThat(bitSets).containsExactlySets(
            "11110",
            "10110",
            "10111",
            "10101",
            "11100",
            "10000",
            "11001",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = true,
            bitIndex = 1,
        )
        assertThat(bitSets).containsExactlySets(
            "10110",
            "10111",
            "10101",
            "10000",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = true,
            bitIndex = 2,
        )
        assertThat(bitSets).containsExactlySets(
            "10110",
            "10111",
            "10101",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = true,
            bitIndex = 3,
        )
        assertThat(bitSets).containsExactlySets(
            "10110",
            "10111",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = true,
            bitIndex = 4,
        )
        assertThat(bitSets).containsExactlySets(
            "10111",
        )
    }

    @Test
    fun `p2 - cleanUnmatchedValuesBasedOnBitIndex - use least common bit`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        var bitSets = input.bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = false,
            bitIndex = 0,
        )
        assertThat(bitSets).containsExactlySets(
            "00100",
            "01111",
            "00111",
            "00010",
            "01010",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = false,
            bitIndex = 1,
        )
        assertThat(bitSets).containsExactlySets(
            "01111",
            "01010",
        )

        bitSets = bitSets.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit = false,
            bitIndex = 2,
        )
        assertThat(bitSets).containsExactlySets(
            "01010",
        )
    }

    @Test
    fun `p2 - searchForValue - use least common bit`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        assertThat(
            Day3Solution.searchForValue(
                input = input.bitSets,
                useMostCommonBit = false,
            )
        ).isEqualTo("01010".toBitSet())
    }

    @Test
    fun `p2 - searchForValue - use most common bit`() {
        val input = part2.parseResource(SAMPLE_RESOURCE)

        assertThat(
            Day3Solution.searchForValue(
                input = input.bitSets,
                useMostCommonBit = true,
            )
        ).isEqualTo("10111".toBitSet())
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

private fun ListAssert<BitSet>.containsExactlySets(vararg values: String) {
    containsExactly(*values.map { it.toBitSet() }.toTypedArray())
}