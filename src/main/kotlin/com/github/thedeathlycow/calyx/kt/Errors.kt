package com.github.thedeathlycow.calyx.kt

class UndefinedRule(
    symbol: String
): Exception("Undefined rule '$symbol'")
