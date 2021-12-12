package net.navatwo.adventofcode2021.day10

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

private fun Char.mapToClose(): Char = when (this) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> error("invalid input")
}

private fun Char.mapToOpen(): Char = when (this) {
    ')' -> '('
    ']' -> '['
    '}' -> '{'
    '>' -> '<'
    else -> error("invalid input")
}

private fun Char.isOpenChar() = when (this) {
    '(', '[', '{', '<' -> true
    else -> false
}

private fun Char.isCloseChar() = when (this) {
    ')', ']', '}', '>' -> true
    else -> false
}

sealed class Day10Solution : Solution<Day10Solution.Input> {
    object Part1 : Day10Solution() {
        private fun computeCloseScore(c: Char): Long = when (c) {
            ')' -> 3L
            ']' -> 57L
            '}' -> 1197L
            '>' -> 25137L
            else -> error("error")
        }

        override fun solve(input: Input): ComputedResult {
            val stack = ArrayDeque<Char>()
            val totalScore = input.lines.asSequence()
                .mapNotNull { line ->
                    findCorruptedChar(stack, line)
                        ?.let { computeCloseScore(it) }
                }
                .sum()

            return ComputedResult.Simple(totalScore)
        }
    }

    object Part2 : Day10Solution() {
        private fun computeCloseScore(c: Char): Long = when (c) {
            ')' -> 1L
            ']' -> 2L
            '}' -> 3L
            '>' -> 4L
            else -> error("error")
        }

        override fun solve(input: Input): ComputedResult {
            val stack = ArrayDeque<Char>()

            val scores = input.lines
                .asSequence()
                .map { line ->
                    if (findCorruptedChar(stack, line) != null) {
                        0
                    } else {
                        var acc = 0L
                        while (stack.isNotEmpty()) {
                            val c = stack.removeLast()
                            acc = acc * 5 + computeCloseScore(c.mapToClose())
                        }
                        acc
                    }
                }
                .filter { it > 0 }
                .sorted()
                .toList()

            val middleScore = scores[scores.size / 2]
            return ComputedResult.Simple(middleScore)
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(lines.map { line -> line.toCharArray() })
    }

    protected fun findCorruptedChar(stack: ArrayDeque<Char>, line: CharArray): Char? {
        stack.clear()

        val corruptedChar = line.firstOrNull { c ->
            when {
                c.isOpenChar() -> {
                    stack.addLast(c)
                    false
                }
                c.isCloseChar() -> {
                    val closeChar = c.mapToOpen()
                    val openChar = stack.removeLastOrNull()
                    openChar != closeChar
                }
                else -> error("Invalid input")
            }
        }

        return corruptedChar
    }

    data class Input(
        val lines: List<CharArray>,
    )
}