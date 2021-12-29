package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.benchmarks.Benchmark
import net.navatwo.adventofcode2021.day16.Day16Solution
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet.TypeId.LESS_THAN
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet.TypeId.LITERAL
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet.TypeId.MAXIMUM
import net.navatwo.adventofcode2021.day16.Day16Solution.Packet.TypeId.SUM
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.Test
import java.util.function.Consumer

private const val INPUT_RESOURCE = "day16/p1_input.txt"
private val part1 = Day16Solution.Part1
private val part2 = Day16Solution.Part2

class Day16SolutionTest {
    @Test
    fun `p1 sample - 1`() {
        val literalInput = part1.parse(listOf("D2FE28"))
        val packet = Packet.parse(literalInput.bits)
        assertThat(packet.sumVersions()).isEqualTo(6)
        assertThat(packet).matchesPacket(
            Packet.Literal(
                version = 6,
                typeId = LITERAL,
                literal = 2021L,
            ),
        )
    }

    @Test
    fun `p1 sample - 2`() {
        val operatorInput0 = part1.parse(listOf("38006F45291200"))
        val packet = Packet.parse(operatorInput0.bits)
        assertThat(packet.sumVersions()).isEqualTo(9)

        assertThat(packet).matchesPacket(
            Packet.Operator(
                version = 1,
                typeId = LESS_THAN,
                packets = listOf(
                    Packet.Literal(
                        version = 6,
                        typeId = LITERAL,
                        literal = 10L,
                    ),
                    Packet.Literal(
                        version = 2,
                        typeId = LITERAL,
                        literal = 20L,
                    ),
                )
            ),
        )
    }

    @Test
    fun `p1 sample - 3`() {
        val operatorInput1 = part1.parse(listOf("EE00D40C823060"))

        val packet = Packet.parse(operatorInput1.bits)
        assertThat(packet).matchesPacket(
            Packet.Operator(
                version = 7,
                typeId = MAXIMUM,
                packets = listOf(
                    Packet.Literal(
                        version = 2,
                        typeId = LITERAL,
                        literal = 1L,
                    ),
                    Packet.Literal(
                        version = 4,
                        typeId = LITERAL,
                        literal = 2L,
                    ),
                    Packet.Literal(
                        version = 1,
                        typeId = LITERAL,
                        literal = 3L,
                    ),
                )
            ),
        )

        assertThat(part1.solve(operatorInput1)).isComputed(14)
        assertThat(part2.solve(operatorInput1)).isComputed(3)
    }

    @Test
    fun `p1 sample - solve 1`() {
        val input = part1.parse(listOf("8A004A801A8002F478"))

        assertThat(part1.solve(input)).isComputed(16)
        assertThat(part2.solve(input)).isComputed(15)
    }

    @Test
    fun `p1 sample - solve 2`() {
        val input = part1.parse(listOf("620080001611562C8802118E34"))

        assertThat(part1.solve(input)).isComputed(12)
        assertThat(part2.solve(input)).isComputed(46)

        assertThat(Packet.parse(input.bits)).matchesPacket(
            Packet.Operator(
                version = 3,
                typeId = SUM, // 46
                packets = listOf(
                    Packet.Operator(
                        version = 0,
                        typeId = SUM, // 21
                        packets = listOf(
                            Packet.Literal(
                                version = 0,
                                typeId = LITERAL,
                                literal = 10L,
                            ),
                            Packet.Literal(
                                version = 5,
                                typeId = LITERAL,
                                literal = 11L,
                            ),
                        ),
                    ),
                    Packet.Operator(
                        version = 1,
                        typeId = SUM, // 25
                        packets = listOf(
                            Packet.Literal(
                                version = 0,
                                typeId = LITERAL,
                                literal = 12L,
                            ),
                            Packet.Literal(
                                version = 3,
                                typeId = LITERAL,
                                literal = 13L,
                            ),
                        ),
                    ),
                ),
            )
        )
    }

    @Test
    fun `p1 sample - solve 3`() {
        val input = part1.parse(listOf("A0016C880162017C3686B18A3D4780"))

        assertThat(part1.solve(input)).isComputed(31)
        assertThat(part2.solve(input)).isComputed(54)

        assertThat(Packet.parse(input.bits)).matchesPacket(
            Packet.Operator(
                version = 5,
                typeId = SUM,
                packets = listOf(
                    Packet.Operator(
                        version = 1,
                        typeId = SUM,
                        packets = listOf(
                            Packet.Operator(
                                version = 3,
                                typeId = SUM, // 54
                                packets = listOf(
                                    Packet.Literal(
                                        version = 7,
                                        typeId = LITERAL,
                                        literal = 6,
                                    ),
                                    Packet.Literal(
                                        version = 6,
                                        typeId = LITERAL,
                                        literal = 6,
                                    ),
                                    Packet.Literal(
                                        version = 5,
                                        typeId = LITERAL,
                                        literal = 12,
                                    ),
                                    Packet.Literal(
                                        version = 2,
                                        typeId = LITERAL,
                                        literal = 15,
                                    ),
                                    Packet.Literal(
                                        version = 2,
                                        typeId = LITERAL,
                                        literal = 15,
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        )
    }

    @Test
    fun p1() {
        val input = part1.parseResource(INPUT_RESOURCE)

        val result = part1.solve(input)
        assertThat(result).isComputed(955)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part1,
        )
    }

    @Test
    fun `p2 - sample 1`() {
        val input = part2.parse(listOf("C0015000016115A2E0802F182340"))
        assertThat(part2.solve(input)).isComputed(46)
    }

    @Test
    fun `p2 - sample 2`() {
        val input = part2.parse(listOf("A0016C880162017C3686B18A3D4780"))
        assertThat(part2.solve(input)).isComputed(54)
    }

    @Test
    fun `p2 - samples`() {
        val tests = mapOf(
            "C200B40A82" to 3L,
            "04005AC33890" to 54L,
            "880086C3E88112" to 7L,
            "CE00C43D881120" to 9L,
            "D8005AC2A8F0" to 1L,
            "F600BC2D8F" to 0L,
            "9C005AC2F8F0" to 0L,
            "9C0141080250320F1802104A08" to 1L,
        )
        assertThat(tests).allSatisfy { inputLine, expected ->
            val input = part2.parse(listOf(inputLine))
            assertThat(part2.solve(input)).isComputed(expected)
        }
    }

    @Test
    fun p2() {
        val input = part2.parseResource(INPUT_RESOURCE)

        val result = part2.solve(input)
        assertThat(result).isComputed(158135423448L)

        Benchmark.run(
            inputContent = loadLines(INPUT_RESOURCE),
            solution = part2,
        )
    }
}

private fun ObjectAssert<Packet.Operator>.matchesOperator(
    expected: Packet.Operator,
): ObjectAssert<Packet.Operator> {
    return satisfies(Consumer { packet ->
        assertThat(packet.version).isEqualTo(expected.version)
        assertThat(packet.typeId).isEqualTo(expected.typeId)

        assertThat(packet.packets).hasSize(expected.packets.size)

        assertThat(packet.packets.zip(expected.packets)).allSatisfy(
            Consumer { (p, e) -> assertThat(p).matchesPacket(e) }
        )
    })
}

private fun ObjectAssert<Packet.Literal>.matchesLiteral(
    expected: Packet.Literal,
): ObjectAssert<Packet.Literal> {
    return satisfies(
        Consumer { packet ->
            assertThat(packet.version).isEqualTo(expected.version)
            assertThat(packet.typeId).isEqualTo(LITERAL)
            assertThat(packet.literal).isEqualTo(expected.literal)
        }
    )
}

@Suppress("UNCHECKED_CAST")
private fun ObjectAssert<Packet>.matchesPacket(
    expected: Packet,
): ObjectAssert<Packet> {
    return satisfies(
        Consumer { packet ->
            when (packet!!) {
                is Packet.Operator ->
                    (this as ObjectAssert<Packet.Operator>).matchesOperator(expected as Packet.Operator)
                is Packet.Literal ->
                    (this as ObjectAssert<Packet.Literal>).matchesLiteral(expected as Packet.Literal)
            }
        }
    )
}