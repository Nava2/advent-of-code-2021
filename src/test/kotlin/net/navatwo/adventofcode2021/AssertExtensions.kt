package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.framework.ComputedResult
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import java.util.function.Consumer

fun <ACTUAL : ComputedResult> ObjectAssert<ACTUAL>.isComputed(computed: Long): ObjectAssert<ACTUAL> {
    return satisfies(
        Consumer { v ->
            assertThat(v.computed).isEqualTo(computed)
        }
    )
}