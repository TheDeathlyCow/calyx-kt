package com.github.calyx.kt.test.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.syntax.AtomNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AtomNodeTest {

    @Test
    fun atomTermTest() {
        val atom = AtomNode("T E R M")

        val exp = atom.evaluate(Options())

        assertEquals(Expansion.Symbol.ATOM, exp.symbol)
        assertEquals("T E R M", exp.term)
    }

}