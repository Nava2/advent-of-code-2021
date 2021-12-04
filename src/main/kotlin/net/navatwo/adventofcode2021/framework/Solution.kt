package net.navatwo.adventofcode2021.framework

interface Solution<I, out T> {
    fun parse(lines: List<String>): I

    fun solve(input: I): T
}