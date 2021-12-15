package net.navatwo.adventofcode2021.day14

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import java.util.LinkedList

sealed class Day14Solution : Solution<Day14Solution.Input> {
    object Part1 : Day14Solution() {
        override fun solve(input: Input): ComputedResult {
            val chain = LinkedList(input.template)

            val rules = input.rules

            repeat(10) {
                val elementsToInsert = chain.asSequence()
                    .zipWithNext()
                    .map { (turtle, rabbit) ->
                        rules[turtle to rabbit]
                    }
                    .toList()

                val iter = chain.listIterator().apply { next() }
                for (element in elementsToInsert) {
                    if (element != null) {
                        iter.add(element)
                    }

                    iter.next()
                }
            }

            val mostAndLeast = computeMostAndLeast(chain)
            val (_, mostCount) = mostAndLeast.most
            val (_, leastCount) = mostAndLeast.least

            return ComputedResult.Simple(mostCount - leastCount)
        }
    }

    object Part2 : Day14Solution() {
        private const val LAST_STEP: Int = 40

        override fun solve(input: Input): ComputedResult {
            val rules = input.rules
            val template = input.template

            var pairFrequencies = template.zipWithNext()
                .map { it to 1L }
                .groupBy({ it.first }) { it.second }
                .mapValues { it.value.sum() }

            var elementCounts = template.groupBy { it }
                .mapValues { it.value.size.toLong() }
                .toMutableMap()

            repeat(LAST_STEP) {
                val stepFrequencies = mutableMapOf<Pair<Element, Element>, Long>()
                val stepElementCounts = elementCounts.toMutableMap()

                for ((pair, multiplier) in pairFrequencies.entries) {
                    val insert = rules.getValue(pair)
                    updateCounts(
                        stepFrequencies = stepFrequencies,
                        stepElementCounts = stepElementCounts,
                        turtle = pair.first,
                        rabbit = pair.second,
                        insert = insert,
                        multiplier = multiplier,
                    )
                }

                pairFrequencies = stepFrequencies
                elementCounts = stepElementCounts
            }

            val mostAndLeast = computeResults(elementCounts)
            val (_, mostCount) = mostAndLeast.most
            val (_, leastCount) = mostAndLeast.least

            return ComputedResult.Simple(mostCount - leastCount)
        }

        private fun updateCounts(
            stepFrequencies: MutableMap<Pair<Element, Element>, Long>,
            stepElementCounts: MutableMap<Element, Long>,
            turtle: Element,
            rabbit: Element,
            insert: Element,
            multiplier: Long
        ) {
            stepFrequencies.compute(turtle to insert) { _, count ->
                (count ?: 0L) + multiplier
            }
            stepFrequencies.compute(insert to rabbit) { _, count ->
                (count ?: 0L) + multiplier
            }

//            stepElementCounts.compute(turtle) { _, c -> (c ?: 0L) + multiplier }
            stepElementCounts.compute(insert) { _, c -> (c ?: 0L) + multiplier }
//            stepElementCounts.compute(rabbit) { _, c -> (c ?: 0L) + multiplier }
        }
    }

    data class RulesTable(
        private val table: Array<Array<Element?>?>,
        private val minElement: Element,
    ) {
        fun getPair(turtle: Element, rabbit: Element): Element? {
            return table[turtle.e.code - minElement.e.code]
                ?.get(rabbit.e.code - minElement.e.code)
        }

        companion object {
            fun load(
                template: Iterable<Element>,
                rules: Map<Pair<Element, Element>, Element>,
            ): RulesTable {
                val elementSet = template.toSet()
                val minElement = elementSet.minOf { it.e }
                val maxElement = elementSet.maxOf { it.e }
                val spread = maxElement - minElement + 1

                return RulesTable(
                    table = Array(spread) { i1 ->
                        val c1 = Element(minElement + i1)
                        Array(spread) { i2 ->
                            val c2 = Element(minElement + i2)
                            rules[c1 to c2]
                        }.takeIf { it.any { e -> e != null } }
                    },

                    minElement = Element(minElement),
                )
            }
        }

    }

    override fun parse(lines: List<String>): Input {
        val linesIterator = lines.iterator()
        val template = linesIterator.next().map { Element(it) }
        linesIterator.next()

        val rules = mutableMapOf<Pair<Element, Element>, Element>()
        while (linesIterator.hasNext()) {
            val line = linesIterator.next()

            val pair = Element(line[0]) to Element(line[1])
            val insert = Element(line[line.lastIndex])

            rules[pair] = insert
        }

        return Input(
            template = template,
            rules = rules,
        )
    }

    protected fun computeCounts(polymer: Iterable<Element>): Map<Element, Long> {
        val countMap = mutableMapOf<Element, Long>()

        for (element in polymer) {
            countMap.compute(element) { _, count ->
                (count ?: 0L) + 1L
            }
        }

        return countMap
    }

    protected fun computeMostAndLeast(polymer: Iterable<Element>): Results {
        val countMap = computeCounts(polymer)
        return computeResults(countMap)
    }

    protected fun computeResults(countMap: Map<Element, Long>): Results {
        var minElement = Element('0')
        var minCount = Long.MAX_VALUE

        var maxElement = Element('0')
        var maxCount = Long.MIN_VALUE

        for ((element, count) in countMap) {
            if (count < minCount) {
                minElement = element
                minCount = count
            }

            if (count > maxCount) {
                maxElement = element
                maxCount = count
            }
        }

        return Results(
            most = maxElement to maxCount,
            least = minElement to minCount,
        )
    }

    data class Results(
        val most: Pair<Element, Long>,
        val least: Pair<Element, Long>,
    )


    data class Input(
        val template: List<Element>,
        val rules: Map<Pair<Element, Element>, Element>
    )

    class MutableLong(var n: Long)

    @JvmInline
    value class Element(val e: Char)
}