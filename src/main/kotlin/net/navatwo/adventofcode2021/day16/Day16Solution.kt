package net.navatwo.adventofcode2021.day16

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day16Solution : Solution<Day16Solution.Input> {
    object Part1 : Day16Solution() {
        override fun solve(input: Input): ComputedResult {
            val packet = Packet.parse(input.bits)
            return ComputedResult.Simple(packet.sumVersions())
        }
    }

    object Part2 : Day16Solution() {
        override fun solve(input: Input): ComputedResult {
            val rootPacket = Packet.parse(input.bits)
            return ComputedResult.Simple(rootPacket.calculate())
        }
    }

    override fun parse(lines: List<String>): Input {
        val hexDigits = lines.first().map { it.digitToInt(16) }
        val bits = hexDigits.flatMap { b ->
            b.toString(2)
                .padStart(4, '0')
                .map { it == '1' }
        }

        return Input(bits)
    }

    data class Input(
        val bits: List<Boolean>,
    )

    sealed class Packet {

        abstract val version: Int
        abstract val typeId: TypeId

        abstract fun sumVersions(): Long

        abstract fun calculate(): Long

        enum class TypeId(val value: Int, val isOperator: Boolean) {
            SUM(value = 0, isOperator = true),
            PRODUCT(value = 1, isOperator = true),
            MINIMUM(value = 2, isOperator = true),
            MAXIMUM(value = 3, isOperator = true),
            LITERAL(value = 4, isOperator = false),
            GREATER_THAN(value = 5, isOperator = true),
            LESS_THAN(value = 6, isOperator = true),
            EQUAL_TO(value = 7, isOperator = true),
            ;

            companion object {
                private val byValue: Map<Int, TypeId> = values().associateBy { it.value }

                fun valueOf(id: Int): TypeId = byValue.getValue(id)
            }
        }

        data class Operator(
            override val version: Int,
            override val typeId: TypeId,
            val packets: List<Packet>,
        ) : Packet() {
            override fun sumVersions(): Long = version + packets.sumOf { it.sumVersions() }

            override fun calculate(): Long = when (typeId) {
                TypeId.SUM -> packets.sumOf { it.calculate() }
                TypeId.PRODUCT -> packets.fold(1L) { acc, packet -> acc * packet.calculate() }
                TypeId.MINIMUM -> packets.minOf { it.calculate() }
                TypeId.MAXIMUM -> packets.maxOf { it.calculate() }

                // binary operations
                TypeId.GREATER_THAN -> {
                    val (a, b) = forBinaryOperation()
                    if (a > b) 1L else 0L
                }
                TypeId.LESS_THAN -> {
                    val (a, b) = forBinaryOperation()
                    if (a < b) 1L else 0L
                }
                TypeId.EQUAL_TO -> {
                    val (a, b) = forBinaryOperation()
                    if (a == b) 1L else 0L
                }

                else -> error("unknown operation")
            }

            private fun forBinaryOperation(): Pair<Long, Long> {
                val values = packets.map { it.calculate() }
                check(values.size == 2) {
                    "values.size > 2 (n = ${values.size})"
                }

                val (a, b) = values
                return a to b
            }

            companion object {
                fun parseOperator(version: Int, typeId: TypeId, buffer: ConsumableBuffer): Operator? {
                    val operatorTypeFlag = buffer.tryConsume(1)?.single() ?: return null
                    val subpackets = if (!operatorTypeFlag) {
                        val lengthOfSubpackets = buffer.tryConsumeInt(15) ?: return null
                        parse(buffer.consumeAsBuffer(lengthOfSubpackets), maxPackets = Int.MAX_VALUE)
                    } else {
                        val numberOfSubpackets = buffer.tryConsumeInt(11) ?: return null
                        parse(buffer, maxPackets = numberOfSubpackets)
                    }

                    return Operator(version, typeId, subpackets)
                }
            }
        }

        data class Literal(
            override val version: Int,
            override val typeId: TypeId,
            val literal: Long,
        ) : Packet() {
            override fun sumVersions(): Long = version.toLong()

            override fun calculate(): Long = literal

            companion object {
                fun parseLiterals(
                    version: Int,
                    typeId: TypeId,
                    buffer: ConsumableBuffer,
                ): Literal {
                    val literals = sequence {
                        do {
                            val isLast = buffer.tryConsumeInt(1) ?: break
                            val values = buffer.tryConsume(4) ?: break

                            yieldAll(values)
                        } while (buffer.hasNext() && isLast == 1)
                    }

                    return Literal(version, typeId, literal = literals.asIterable().toLong())
                }
            }
        }

        companion object {

            fun parse(bits: List<Boolean>): Packet {
                return parseSingle(ConsumableBuffer(bits))!!
            }

            private fun parse(buffer: ConsumableBuffer, maxPackets: Int): List<Packet> {
                val results = mutableListOf<Packet>()
                while (buffer.hasNext() && results.size < maxPackets) {
                    val packet = parseSingle(buffer) ?: break
                    results.add(packet)
                }

                if (maxPackets != Int.MAX_VALUE) {
                    check(results.size == maxPackets) {
                        "Failed to read $maxPackets size (size = ${results.size})"
                    }
                }

                return results
            }

            private fun parseSingle(buffer: ConsumableBuffer): Packet? {
                val version = buffer.tryConsume(3)?.toInt() ?: return null

                val typeId = buffer.tryConsumeInt(3)?.let { TypeId.valueOf(it) } ?: return null

                return if (typeId.isOperator) {
                    Operator.parseOperator(version, typeId, buffer)
                } else {
                    Literal.parseLiterals(
                        version = version,
                        typeId = typeId,
                        buffer = buffer,
                    )
                }
            }
        }
    }

    class ConsumableBuffer(private val buffer: List<Boolean>) {
        private var index = 0

        fun hasNext(): Boolean = index <= buffer.lastIndex

        fun consumeAsBuffer(n: Int): ConsumableBuffer = ConsumableBuffer(consume(n))

        fun tryConsume(n: Int): List<Boolean>? {
            if (n == 0) return listOf()

            return if (index + n <= buffer.size) {
                consume(n)
            } else {
                null
            }
        }

        fun tryConsumeInt(n: Int): Int? {
            require(n <= 32) { "Requested too many bits as Int" }
            return tryConsume(n)?.toInt()
        }

        private fun consume(n: Int): List<Boolean> {
            require(index + n <= buffer.size) { "not enough bits available n=$n" }

            if (n == 0) return listOf()

            val result = buffer.subList(index, index + n)
            index += n
            return result
        }

        override fun toString(): String {
            return "Buffer{index=$index, length=${buffer.size}}"
        }
    }
}

private fun Iterable<Boolean>.toInt(): Int {
    return fold(0) { acc, v -> (acc shl 1) or (if (v) 1 else 0) }
}

private fun Iterable<Boolean>.toLong(): Long {
    return fold(0L) { acc, v -> (acc shl 1) or (if (v) 1L else 0L) }
}