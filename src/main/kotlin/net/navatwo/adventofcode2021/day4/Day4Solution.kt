package net.navatwo.adventofcode2021.day4

import net.navatwo.adventofcode2021.framework.Solution

private val CARD_ROWS = 5

sealed class Day4Solution<S> : Solution<Day4Solution.Input, S> {
    object Part1 : Day4Solution<Int>() {
        override fun solve(input: Input): Int {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        val numbersLine = lines[0]
        val firstCardIndex = 2

        val numbers = numbersLine.splitToSequence(',')
            .map { it.toInt() }
            .toList()
        val cards = (firstCardIndex..lines.size step (CARD_ROWS + 1)).map { firstLineIndex ->
            val cardLines = lines.subList(firstLineIndex, firstLineIndex + CARD_ROWS)
            val board = cardLines.map { line ->
                line.splitToSequence(' ')
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
                    .map { Card.Element(it, false) }
                    .toList()
            }
            Card(board)
        }

        return Input(numbers = numbers, cards = cards)
    }

    data class Input(
        val numbers: List<Int>,
        val cards: List<Card>,
    )

    data class Card(
        val board: List<List<Element>>,
    ) {
        data class Element(val value: Int, val picked: Boolean)
    }
}