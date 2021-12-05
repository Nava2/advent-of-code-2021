package net.navatwo.adventofcode2021.day4

import net.navatwo.adventofcode2021.framework.Solution
import java.util.Objects

private val CARD_ROWS = 5

sealed class Day4Solution<S> : Solution<Day4Solution.Input, S> {
    object Part1 : Day4Solution<Result>() {
        override fun solve(input: Input): Result {
            val numbers = input.numbers
            val cards = input.cards.associateWith { Card.create(it) }

            return numbers.asSequence()
                .mapNotNull { pickedNumber ->
                    val (c, card) = cards.entries
                        .firstOrNull { (_, card) ->
                            card.pickValue(pickedNumber)
                        } ?: return@mapNotNull null

                    Result(
                        number = pickedNumber,
                        card = c,
                        computed = card.result(pickedNumber),
                    )
                }
                .first()
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
                    .toList()
            }
            Input.Card(board)
        }

        return Input(numbers = numbers, cards = cards)
    }

    data class Input(
        val numbers: List<Int>,
        val cards: List<Card>,
    ) {
        data class Card(
            val board: List<List<Int>>,
        )
    }

    data class Result(
        val number: Int,
        val card: Input.Card,
        val computed: Int,
    )

    private class Card(
        val board: List<List<Element>>,
        private val elementsByNumber: Map<Int, Element>,
    ) {
        private val valueLocations: Map<Int, Pair<Int, Int>>

        init {
            val valueLocations = HashMap<Int, Pair<Int, Int>>(elementsByNumber.size)
            for ((r, row) in board.withIndex()) {
                for ((c, value) in row.withIndex()) {
                    valueLocations[value.value] = r to c
                }
            }

            this.valueLocations = valueLocations
        }

        private val columnCount = MutableList(CARD_ROWS) { 0 }
        private val rowCount = MutableList(CARD_ROWS) { 0 }

        fun pickValue(value: Int): Boolean {
            val (row, column) = valueLocations[value] ?: return false

            val element = board[row][column]
            if (!element.picked) {
                element.picked = true
                columnCount[column] += 1
                rowCount[row] += 1
            }

            return columnCount[column] == CARD_ROWS || rowCount[row] == CARD_ROWS
        }

        fun result(number: Int): Int {
            val unmarkedSum = board.sumOf { row ->
                row.asSequence().filterNot { it.picked }.sumOf { it.value }
            }

            return unmarkedSum * number
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Card) return false
            return board == other.board
        }

        override fun hashCode(): Int {
            return Objects.hash(board)
        }

        class Element(val value: Int, var picked: Boolean) {
            override fun equals(other: Any?): Boolean {
                if (other !is Element) return false
                return value == other.value
            }

            override fun hashCode(): Int {
                return Objects.hash(value)
            }
        }

        companion object {
            fun create(card: Input.Card): Card {
                val elementBoard = ArrayList<ArrayList<Element>>(CARD_ROWS)
                val elementsByNumber = HashMap<Int, Element>(CARD_ROWS * CARD_ROWS)

                for (row in card.board) {
                    val elementRow = ArrayList<Element>(CARD_ROWS)
                    elementBoard.add(elementRow)

                    for (value in row) {
                        val element = Element(value, false)
                        elementRow.add(element)
                        elementsByNumber[value] = element
                    }
                }

                return Card(board = elementBoard, elementsByNumber = elementsByNumber)
            }
        }
    }
}