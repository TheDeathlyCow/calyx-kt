package com.github.thedeathlycow.calyx.kt

class UndefinedRule(
    symbol: String
): Exception("Undefined rule '$symbol'")

class UndefinedFilter(
    symbol: String
): Exception("Undefined filter '$symbol'")

class NonStaticFilter(
    symbol: String
): Exception("Filter not defined as static '$symbol' (annotate with @JvmStatic if using Kotlin)")

class IncorrectFilterSignature(
    symbol: String,
    cause: Throwable
): Exception("incorrect method signature for filter: '$symbol'", cause)