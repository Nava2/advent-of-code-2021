package net.navatwo.adventofcode2021.day3

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import java.util.BitSet
import kotlin.math.roundToInt

sealed class Day3Solution : Solution<Day3Solution.Input> {
    object Part1 : Day3Solution() {
        override fun solve(input: Input): PowerRate {
            val gammaRateBitSet = countMostCommon(input.bitSets, input.width)
            val gammaRate = gammaRateBitSet.toInt(input.width)

            val mask = (1 shl input.width) - 1
            val epsilonRate = (gammaRate and mask).inv() and mask
            return PowerRate(
                gammaRate = gammaRate,
                epsilonRate = epsilonRate,
            )
        }
    }

    object Part2 : Day3Solution() {
        override fun solve(input: Input): LifeSupportRate {
            val oxygenRating = searchForValue(input.bitSets, useMostCommonBit = true)
            val co2Rating = searchForValue(input.bitSets, useMostCommonBit = false)

            return LifeSupportRate(
                oxygenGeneratorRate = oxygenRating.toInt(input.width),
                co2ScrubberRate = co2Rating.toInt(input.width),
            )
        }
    }

    override fun parse(lines: List<String>): Input {
        var width = 0
        val bitSets = List(lines.size) {
            val line = lines[it]
            width = maxOf(width, line.length)
            line.toBitSet()
        }

        return Input(width, bitSets)
    }

    data class Input(
        val width: Int,
        val bitSets: List<BitSet>,
    )

    companion object {
        internal fun searchForValue(input: List<BitSet>, useMostCommonBit: Boolean): BitSet {
            var possibleValues = input

            var bitIndex = 0
            while (possibleValues.size != 1) {
                possibleValues = possibleValues.cleanUnmatchedValuesBasedOnBitIndex(useMostCommonBit, bitIndex)

                bitIndex++
            }

            return possibleValues.single()
        }

        internal fun Collection<BitSet>.cleanUnmatchedValuesBasedOnBitIndex(
            useMostCommonBit: Boolean,
            bitIndex: Int
        ): List<BitSet> {
            val mostCommonBit = isOneMostCommonAtIndex(bitIndex)

            val bitToKeep = if (useMostCommonBit) {
                mostCommonBit
            } else {
                mostCommonBit xor 1 // flip the bit
            }

            return filter { bitSet ->
                val bitValue = if (bitSet.get(bitIndex)) 1 else 0
                bitToKeep == bitValue
            }
        }

        internal fun Collection<BitSet>.isOneMostCommonAtIndex(
            bitIndex: Int,
        ): Int {
            var oneCount = 0

            for (bitSet in this) {
                if (bitSet.get(bitIndex)) {
                    oneCount += 1
                }
            }

            val halfSize = (this.size / 2.0).roundToInt()
            return if (oneCount >= halfSize) 1 else 0
        }

        internal fun countMostCommon(
            possibleValues: Collection<BitSet>,
            width: Int,
        ): BitSet {
            val halfSize = (possibleValues.size / 2.0).roundToInt()

            val result = BitSet(width)

            for (bitIndex in 0 until width) {
                var oneCount = 0

                for (bitSet in possibleValues) {
                    if (bitSet.get(bitIndex)) {
                        oneCount += 1
                    }
                }

                result.set(bitIndex, oneCount >= halfSize)
            }

            return result
        }
    }

    data class PowerRate(
        val gammaRate: Int,
        val epsilonRate: Int,
    ) : ComputedResult {
        override val computed: Long = gammaRate.toLong() * epsilonRate
    }

    data class LifeSupportRate(
        val oxygenGeneratorRate: Int,
        val co2ScrubberRate: Int,
    ) : ComputedResult {
        override val computed: Long = oxygenGeneratorRate.toLong() * co2ScrubberRate
    }
}

internal fun String.toBitSet(): BitSet {
    val bitset = BitSet(length)
    for ((i, c) in withIndex()) {
        when (c) {
            '0' -> bitset.clear(i)
            '1' -> bitset.set(i)
            else -> error("Invalid char $c @ $i")
        }
    }
    return bitset
}

internal fun BitSet.toInt(width: Int): Int {
    return (0 until width).fold(0) { value, index ->
        (value shl 1) + (if (get(index)) 1 else 0)
    }
}