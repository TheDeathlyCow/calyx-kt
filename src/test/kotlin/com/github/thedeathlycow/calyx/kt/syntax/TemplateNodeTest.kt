package com.github.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.TemplateNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TemplateNodeTest {

    @Test
    fun templateWithNoDelimitersTest() {
        val node = TemplateNode.parse("one two three", Registry())

        val exp = node.evaluate(Options())

        assertEquals(Expansion.Symbol.TEMPLATE, exp.symbol)
        assertEquals(Expansion.Symbol.ATOM, exp.tail[0].symbol)
        assertEquals("one two three", exp.tail[0].term)
    }

    @Test
    fun templateWithSingleExpansionTest() {
        val registry = Registry()
        registry.defineRule("one", listOf("ONE"))
        val node = TemplateNode.parse("{one} two three", registry)

        val exp = node.evaluate(Options())

        assertEquals(Expansion.Symbol.TEMPLATE, exp.symbol)
        assertEquals(Expansion.Symbol.EXPRESSION, exp.tail[0].symbol)
        assertEquals(Expansion.Symbol.TEMPLATE, exp.tail[0].tail[0].symbol)
        assertEquals(Expansion.Symbol.ATOM, exp.tail[0].tail[0].tail[0].symbol)
        assertEquals("ONE", exp.tail[0].tail[0].tail[0].term)
        assertEquals(Expansion.Symbol.ATOM, exp.tail[1].symbol)
        assertEquals(" two three", exp.tail[1].term)
    }

    @Test
    fun templateWithSpaceIsAllowed() {
        val registry = Registry()
        registry.defineRule("name", listOf("Fabian"))
        registry.defineRule("title", listOf("King"))

        val node = TemplateNode.parse("{title} {name}", registry)

        val exp = node.evaluate(Options())
        assertEquals("King Fabian", exp.flattenToString())
    }
}