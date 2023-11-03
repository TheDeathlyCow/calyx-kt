package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Result
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ResultTest {

    private fun atomTemplateTree(): Expansion = Expansion(
        Expansion.Symbol.TEMPLATE, Expansion(Expansion.Symbol.ATOM, "A T O M")
    )

    private fun tripleAtomTemplateTree(): Expansion = Expansion(
        Expansion.Symbol.TEMPLATE,
        listOf(
            Expansion(Expansion.Symbol.ATOM, "O N E"),
            Expansion(Expansion.Symbol.ATOM, " | "),
            Expansion(Expansion.Symbol.ATOM, "T W O")
        )
    )

    @Test
    fun wrapExpressionTreeTest() {
        val result = Result(atomTemplateTree())

        assertEquals(Expansion.Symbol.TEMPLATE, result.tree.symbol)
        assertEquals(Expansion.Symbol.ATOM, result.tree.tail[0].symbol)
        assertEquals("A T O M", result.tree.tail[0].term)
    }

    @Test
    fun flattensExpressionTreeTest() {
        val result = Result(tripleAtomTemplateTree())

        assertEquals("O N E | T W O", result.text)
    }

}