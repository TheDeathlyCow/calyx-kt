package com.github.calyx.kt.test

import com.github.thedeathlycow.calyx.kt.Cycle
import com.github.thedeathlycow.calyx.kt.Options
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CycleTest {

    @Test
    fun cycleLengthOneAlwaysReturnsZerothIndex() {
        val cycle = Cycle(Options(), 1)

        assertEquals(0, cycle.poll())
        assertEquals(0, cycle.poll())
        assertEquals(0, cycle.poll())
    }

}