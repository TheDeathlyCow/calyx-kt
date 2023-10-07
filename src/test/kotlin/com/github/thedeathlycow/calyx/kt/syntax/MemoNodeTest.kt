package com.github.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.MemoNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MemoNodeTest {

    @Test
    fun memoNodeReferToIdenticalSymbolExpansion() {
        val registry = Registry()
        registry.defineRule("one", listOf("ONE", "One", "1"))
        registry.resetEvaluationContext()

        val nodes = listOf(
            MemoNode("one", registry),
            MemoNode("one", registry),
            MemoNode("one", registry)
        )

        val expansions = mutableListOf<Expansion>()
        nodes.forEach {
            expansions.add(it.evaluate(Options()))
        }

        val terms = mutableListOf<String>()
        expansions.forEach {
            terms.add(it.tail[0].tail[0].tail[0].term)
        }

        assertEquals(3, terms.size)
        // remove all duplicate elements with toSet() to reduce to a singleton set
        assertEquals(1, terms.toSet().size)
    }

}