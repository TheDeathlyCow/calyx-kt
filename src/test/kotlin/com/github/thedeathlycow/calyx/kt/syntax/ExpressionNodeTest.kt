package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.ExpressionNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExpressionNodeTest {

    @Test
    fun expressionTest() {
        val registry = Registry()
        registry.defineRule("term", listOf("T E R M"))

        val expression = ExpressionNode("term", registry)

        val exp = expression.evaluate(Options())

        assertEquals(Expansion.Symbol.EXPRESSION, exp.symbol)
        assertEquals(Expansion.Symbol.TEMPLATE, exp.tail[0].symbol)
        assertEquals(Expansion.Symbol.ATOM, exp.tail[0].tail[0].symbol)
        assertEquals("T E R M", exp.tail[0].tail[0].term)
    }

}