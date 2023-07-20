package com.github.calyx.kt.test

import com.github.thedeathlycow.calyx.kt.Expansion
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExpansionTest {

    @Test
    fun flattenExpansionToAtomsTest() {
        val exp = Expansion(Expansion.Symbol.TEMPLATE, listOf(
            Expansion(Expansion.Symbol.ATOM, "-ONE-"),
            Expansion(Expansion.Symbol.ATOM, "-TWO-"),
            Expansion(Expansion.Symbol.ATOM, "-THREE-")
        ))

        val atoms = exp.flatten().toString()
        assertEquals("-ONE--TWO--THREE-", atoms)
    }

}