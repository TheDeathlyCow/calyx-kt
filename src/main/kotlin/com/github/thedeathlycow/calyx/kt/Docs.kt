package com.github.thedeathlycow.calyx.kt

fun main() {
    val grammar = Grammar()

    grammar.start("Hello World")

    println(grammar.generate())
    // > "Hello World"
}