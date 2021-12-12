package net.navatwo.adventofcode2021.day10

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

private val CHUNK_CHARS = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val CHUNK_CHARS_BY_CLOSE = CHUNK_CHARS.entries.associate { (o, c) -> c to o }

private val OPEN_CHARS = CHUNK_CHARS.keys
private val CLOSE_CHARS = CHUNK_CHARS.values.toSet()

sealed class Day10Solution : Solution<Day10Solution.Input> {
    object Part1 : Day10Solution() {
        private val CLOSE_SCORE: Map<Char, Long> = mapOf(
            ')' to 3L,
            ']' to 57L,
            '}' to 1197L,
            '>' to 25137L,
        )

        override fun solve(input: Input): ComputedResult {
            val stack = ArrayDeque<Char>()
            val totalScore = input.lines.asSequence()
                .mapNotNull { line ->
                    findCorruptedChar(stack, line)
                        ?.let { CLOSE_SCORE.getValue(it) }
                }
                .sum()

            return ComputedResult.Simple(totalScore)
        }
    }

    object Part2 : Day10Solution() {
        override fun solve(input: Input): ComputedResult {
            return ComputedResult.Simple(10L)
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(lines.map { line -> line.toCharArray() })
    }

    protected fun findCorruptedChar(stack: ArrayDeque<Char>, line: CharArray): Char? {
        stack.clear()

        val corruptedChar = line.firstOrNull { c ->
            when (c) {
                in OPEN_CHARS -> {
                    stack.addLast(c)
                    false
                }
                in CLOSE_CHARS -> {
                    val closeChar = CHUNK_CHARS_BY_CLOSE.getValue(c)
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