package net.navatwo.adventofcode2021.framework

interface Solution<I> {
    fun parse(lines: List<String>): I

    fun solve(input: I): ComputedResult
}