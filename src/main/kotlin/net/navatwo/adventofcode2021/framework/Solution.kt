package net.navatwo.adventofcode2021.framework

interface Solution<I, out R : ComputedResult> {
    fun parse(lines: List<String>): I

    fun solve(input: I): R

}