package net.navatwo.adventofcode2021.framework

interface ComputedResult {
    val computed: Long

    data class Simple(
        override val computed: Long,
    ) : ComputedResult {
        constructor(computed: Int) : this(computed = computed.toLong())
    }
}