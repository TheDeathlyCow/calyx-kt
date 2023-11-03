package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.UniqNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UniqNodeTest {

    @Test
    fun uniqNodesCycleThroughEachTemplateInBranch() {
        val registry = Registry()

        registry.defineRule("medal", listOf("gold", "silver", "bronze"))
        registry.resetEvaluationContext()

        val expansions = (0..2)
            .map {
                UniqNode("medal", registry)
                    .evaluate(Options())
                    .tail[0].tail[0].tail[0]
                    .term
            }
            .toList()

        assertEquals(3, expansions.size)
        // expansions are converted to a set to determine that they are distinct
        assertEquals(3, expansions.toSet().size)
    }

}