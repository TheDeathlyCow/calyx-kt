package com.github.thedeathlycow.calyx.kt

class UndefinedRule(
    symbol: String
): Exception("Undefined rule '$symbol'")

class UndefinedFilter(
    symbol: String
): Exception("Undefined filter '$symbol'")