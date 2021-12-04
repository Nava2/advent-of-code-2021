package net.navatwo.adventofcode2021.day3

import net.navatwo.adventofcode2021.framework.Solution
import java.util.BitSet

sealed class Day3Solution : Solution<List<BitSet>, Day3Solution.PowerRate> {
    object Part1 : Day3Solution() {
        override fun solve(input: List<BitSet>): PowerRate {
            val (transposed, width) = transposeBitSets(input)

            val halfWidth = input.size / 2

            val gammaRateBitSet = BitSet(input.size)
            for ((idx, column) in transposed.withIndex()) {
                val oneIsMostCommon = column.cardinality() > halfWidth
                gammaRateBitSet.set(idx, oneIsMostCommon)
            }

            val gammaRate = (0 until width).fold(0) { value, index ->
                (value shl 1) + (if (gammaRateBitSet.get(index)) 1 else 0)
            }

            val mask = (1 shl width) - 1
            val epsilonRate = (gammaRate and mask).inv() and mask
            return PowerRate(
                gammaRate = gammaRate,
                epsilonRate = epsilonRate,
            )
        }
    }

    override fun parse(lines: List<String>): List<BitSet> {
        return lines.map { line -> line.toBitSet() }
    }

    protected fun transposeBitSets(bitSets: Collection<BitSet>): TransposeAndWidth {
        val width = bitSets.maxOf { it.length() }
        val result = List(width) { BitSet(bitSets.size) }

        for ((y, bitSet) in bitSets.withIndex()) {
            for (x in 0 until width) {
                val bitValue = bitSet.get(x)
                val resultBitSet = result[x]
                resultBitSet.set(y, bitValue)
            }
        }

        return TransposeAndWidth(transposed = result, width = width)
    }

    protected data class TransposeAndWidth(
        val transposed: List<BitSet>,
        val width: Int,
    )

    data class PowerRate(
        val gammaRate: Int,
        val epsilonRate: Int,
    ) {
        val consumption = gammaRate * epsilonRate
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