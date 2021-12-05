package net.navatwo.adventofcode2021.day3

import net.navatwo.adventofcode2021.framework.Solution
import java.util.BitSet
import java.util.LinkedList
import kotlin.math.roundToInt

sealed class Day3Solution<S> : Solution<Day3Solution.Input, S> {
    object Part1 : Day3Solution<PowerRate>() {
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

    object Part2 : Day3Solution<LifeSupportRate>() {
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
            val possibleValues = LinkedList(input)

            var bitIndex = 0
            while (possibleValues.size != 1) {
                cleanUnmatchedValuesBasedOnBitIndex(possibleValues, useMostCommonBit, bitIndex)

                bitIndex++
            }

            return possibleValues.single()
        }

        internal fun cleanUnmatchedValuesBasedOnBitIndex(
            possibleValues: MutableList<BitSet>,
            useMostCommonBit: Boolean,
            bitIndex: Int
        ) {
            val mostCommonBit = isOneMostCommonAtIndex(possibleValues, bitIndex)

            val bitToKeep = if (useMostCommonBit) {
                mostCommonBit
            } else {
                mostCommonBit xor 1
            }

            val reverseIterator = possibleValues.listIterator()
            while (reverseIterator.hasNext()) {
                val bitSet = reverseIterator.next()

                val bitValue = if (bitSet.get(bitIndex)) 1 else 0
                if (bitToKeep != bitValue) {
                    reverseIterator.remove()
                }
            }
        }

        internal fun isOneMostCommonAtIndex(
            possibleValues: Collection<BitSet>,
            bitIndex: Int,
        ): Int {
            var oneCount = 0

            for (bitSet in possibleValues) {
                if (bitSet.get(bitIndex)) {
                    oneCount += 1
                }
            }

            val halfSize = (possibleValues.size / 2.0).roundToInt()
            return if (oneCount >= halfSize) 1 else 0
        }

        internal fun countMostCommon(
            possibleValues: Collection<BitSet>,
            width: Int,
        ): BitSet {
            val oneCounts = IntArray(width)

            for (bitSet in possibleValues) {
                for (bitIndex in 0 until width) {
                    if (bitSet.get(bitIndex)) {
                        oneCounts[bitIndex] += 1
                    }
                }
            }

            val halfSize = (possibleValues.size / 2.0).roundToInt()
            val result = BitSet(width)
            for ((bitIndex, bitValue) in oneCounts.asSequence().map { it > halfSize }.withIndex()) {
                result.set(bitIndex, bitValue)
            }
            return result
        }
    }

    data class PowerRate(
        val gammaRate: Int,
        val epsilonRate: Int,
    ) {
        val consumption = gammaRate * epsilonRate
    }

    data class LifeSupportRate(
        val oxygenGeneratorRate: Int,
        val co2ScrubberRate: Int,
    ) {
        val consumption = oxygenGeneratorRate * co2ScrubberRate
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