package com.github.thedeathlycow.calyx.kt.grammar

import com.github.thedeathlycow.calyx.kt.Grammar
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LooseModeTest {

    @Test
    fun undefinedRuleGeneratesEmptyAtom() {
        val grammar = Grammar(strict = false)

        grammar.start(listOf("Hello {world}!"))

        val result = grammar.generate()

        assertEquals("Hello !", result.text)
    }

}