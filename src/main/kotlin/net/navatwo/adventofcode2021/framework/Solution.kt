package net.navatwo.adventofcode2021.framework

interface Solution<I, out T> {
    fun parse(lines: List<String>): List<I>

    fun solve(inputs: List<I>): T
}