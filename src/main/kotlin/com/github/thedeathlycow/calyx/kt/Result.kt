package com.github.thedeathlycow.calyx.kt

class Result(
    private val root: Expansion
) {

    val tree: Expansion
        get() = this.root

    val text: String
        get() = root.flatten().toString()

    override fun toString(): String {
        return this.text
    }
}