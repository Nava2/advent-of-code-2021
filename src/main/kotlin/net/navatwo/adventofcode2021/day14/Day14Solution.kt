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
            val rulesTable = RulesTable.load(input.template, input.rules)

            val queue = ArrayDeque<Step>()

            val counters = mutableMapOf<Element, MutableLong>()

            for ((turtle, rabbit) in input.template.zipWithNext()) {
                counters.incCounter(turtle)
                counters.incCounter(rabbit)

                queue.addFirst(Step(1, turtle, rabbit))
            }

            var loopCounter = 0L
            while (queue.isNotEmpty()) {
                val (step, turtle, rabbit) = queue.removeLast()

                if (step == LAST_STEP) continue
                val insert = rulesTable.getPair(turtle, rabbit) ?: continue

                counters.incCounter(insert)

                queue.addLast(Step(step + 1, turtle, insert))
                queue.addLast(Step(step + 1, insert, rabbit))
                loopCounter += 1
            }

            val mostAndLeast = computeResults(counters.mapValues { it.value.n })
            val (_, mostCount) = mostAndLeast.most
            val (_, leastCount) = mostAndLeast.least

            return ComputedResult.Simple(mostCount - leastCount)
        }

        data class Step(val step: Int, val turtle: Element, val rabbit: Element)

        fun <K> MutableMap<K, MutableLong>.incCounter(key: K) {
            val counter = getOrPut(key) { MutableLong(0) }
            counter.n += 1
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

    protected fun computeMostAndLeast(polymer: Iterable<Element>): Results {
        val countMap = mutableMapOf<Element, Long>()

        for (element in polymer) {
            countMap.compute(element) { _, count ->
                (count ?: 0L) + 1L
            }
        }

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