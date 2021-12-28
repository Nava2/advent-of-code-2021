package net.navatwo.adventofcode2021.day16

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day16Solution : Solution<Day16Solution.Input> {
    object Part1 : Day16Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("Not yet implemented")
        }
    }

    object Part2 : Day16Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("Not yet implemented")
        }
    }

    override fun parse(lines: List<String>): Input {
        val hexDigits = lines.first().map { it.digitToInt(16) }
        val bits = hexDigits.flatMap { b ->
            b.toString(2)
                .padStart(4, '0')
                .map { it == '1' }
        }

        return Input(Packet.parse(bits))
    }

    data class Input(
        val packets: List<Packet>,
    )

    sealed interface Packet {

        val version: Int
        val typeId: TypeId

        enum class TypeId(val id: Int) {
            LITERAL(4),
            ;

            companion object {
                fun valueOf(id: Int): TypeId {
                    return values().single { it.id == id }
                }
            }
        }

        data class Literal(
            override val version: Int,
            override val typeId: TypeId,
            val literals: List<Int>
        ) : Packet {
            companion object {
                fun parseLiterals(version: Int, typeId: TypeId, buffer: ConsumableBuffer): Literal {
                    val literals = mutableListOf<Int>()
                    do {
                        val isLast = buffer.consumeInt(1) == 0
                        val elements = buffer.consumeInt(4)
                        literals.add(elements)
                    } while (buffer.hasNext() && !isLast)

                    buffer.takeUntil(3) { it } // take until there's a non-zero

                    return Literal(version, typeId, literals)
                }
            }
        }

        class ConsumableBuffer(private val buffer: List<Boolean>) {
            private var index = 0

            fun hasNext(): Boolean = index <= buffer.lastIndex

            fun consumeInt(n: Int): Int {
                val result = buffer.subList(index, index + n)
                index += n
                return result.toInt()
            }

            fun takeUntil(maxIterations: Int, predicate: (Boolean) -> Boolean): Iterable<Boolean> {
                var iterIndex = index
                while (
                    iterIndex <= buffer.lastIndex &&
                    iterIndex - index < maxIterations &&
                    !predicate(buffer[iterIndex])
                ) {
                    iterIndex += 1
                }

                val result = buffer.subList(index, iterIndex)
                index = iterIndex
                return result
            }
        }

        companion object {

            fun parse(bits: List<Boolean>): List<Packet> {
                val buffer = ConsumableBuffer(bits)

                val results = mutableListOf<Packet>()
                while (buffer.hasNext()) {
                    val version = buffer.consumeInt(3)

                    val packet = when (val typeId = TypeId.valueOf(buffer.consumeInt(3))) {
                        TypeId.LITERAL -> Literal.parseLiterals(version, typeId, buffer)
                    }

                    results.add(packet)
                }

                return results
            }

            private fun Iterable<Boolean>.toInt(): Int {
                return fold(0) { acc, v -> (acc shl 1) or (if (v) 1 else 0) }
            }

            private fun <T> Iterator<T>.take(n: Int): List<T> {
                return List(n) {
                    check(hasNext()) { "No values remain" }
                    next()
                }
            }
        }
    }
}