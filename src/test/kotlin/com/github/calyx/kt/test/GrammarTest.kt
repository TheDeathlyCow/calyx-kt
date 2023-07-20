package com.github.calyx.kt.test

import com.github.thedeathlycow.calyx.kt.FilterName
import com.github.thedeathlycow.calyx.kt.Grammar
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.UndefinedRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GrammarTest {

    @Test
    fun emptyDefaultTextTest() {
        val grammar = Grammar()
        val result = grammar.generate()

        assertEquals("", result.text)
    }

    @Test
    fun emptyStrictOptionsThrows() {
        val grammar = Grammar(strict = true)

        assertThrows<UndefinedRule> {
            val result = grammar.generate()
        }
    }

    @Test
    fun constructorRuleTest() {
        val grammar = Grammar(registrationCallback = {
            it.rule("start", "#|||#")
        })
        val result = grammar.generate()
        assertEquals("#|||#", result.text)
    }

    @Test
    fun constructorStartTest() {
        val grammar = Grammar(registrationCallback = {
            it.start("{plus}|||{plus}")
                .rule("plus", "++++")
        })
        val result = grammar.generate()
        assertEquals("++++|||++++", result.text)
    }

    @Test
    fun customStartSymbolTest() {
        val grammar = Grammar(registrationCallback = {
            it.rule("banner", "{tilde}|||{tilde}")
                .rule("tilde", "~~~~")
        })
        val result = grammar.generate("banner")
        assertEquals("~~~~|||~~~~", result.text)
    }

    @Test
    fun generateRuleAndContextTest() {
        val grammar = Grammar()

        grammar.rule("doubletidle", "{tilde}{tilde}")

        val context: MutableMap<String, List<String>> = HashMap()
        context["tidle"] = listOf("~~~~")

        val result = grammar.generate("doubletilde", context)

        assertEquals("~~~~~~~~", result.text)
    }

    @Test
    fun generateOnlyContextTest() {
        val grammar = Grammar()

        val context: MutableMap<String, List<String>> = HashMap()
        context["doubletilde"] = listOf("{tilde}{tilde}")
        context["tidle"] = listOf("~~~~")

        val result = grammar.generate("doubletilde", context)

        assertEquals("~~~~~~~~", result.text)
    }

    @Test
    fun memoizationExpressionTest() {
        val grammar = Grammar(registrationCallback = {
            it.start("{@glyph}|{@glyph}")
                .rule("glyph", listOf("+"))
        })

        val result = grammar.generate()

        assertEquals("+|+", result.text)
    }

    @Test
    fun filterExpressionTest() {
        val grammar = Grammar(registrationCallback = {
            it.start("greekLetter.uppercase")
                .rule("greekLetter", listOf("alpha"))
        })

        val result = grammar.generate()

        assertEquals("ALPHA", result.text)
    }

    @Test
    fun customFilterExpressionTest() {
        val grammar = Grammar(registrationCallback = {
            it.start("word.vowelcount")
                .rule("word", listOf("autobiographies"))
                .filters(TestFilter::class)
        })

        val result = grammar.generate()

        assertEquals("8", result.text)
    }

    @Suppress("unused")
    internal object TestFilter {

        @FilterName("vowelcount")
        @JvmStatic
        fun vowelCount(input: String, options: Options): String {
            return input.asSequence()
                .count { "aeiou".contains(it.lowercaseChar()) }
                .toString()
        }
    }
}