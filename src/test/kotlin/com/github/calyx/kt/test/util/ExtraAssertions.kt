package com.github.calyx.kt.test.util

import kotlin.test.assertTrue

fun <T : Number> assertNumberWithinPercentRange(expected: T, actual: T, allowedError: Double) {
    val expectedDouble = expected.toDouble()
    val allowedRange = (((1 - allowedError) * expectedDouble)..((1 + allowedError) * expectedDouble))
    assertTrue(actual.toDouble() in allowedRange)
}