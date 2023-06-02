package com.github.thedeathlycow.calyx.kt

import java.util.*

class Cycle(
    private val options: Options,
    private val count: Int
) {

    private var index: Int = 0
    private var sequence: MutableList<Int> = mutableListOf()


    init {
        require(count > 0) { "'count' must be greater than zero" }

        this.index = this.count - 1
    }

    fun shuffle() {
        this.sequence = (0 until this.count).toMutableList()

        var current = count
        var target: Int
        while (current > 1) {
            target = options.random.nextInt(current)
            current--
            Collections.swap(sequence, target, current)
        }
    }

    fun poll(): Int {
        this.index++

        if (this.index == this.count) {
            this.shuffle()
            this.index = 0
        }

        return sequence[index]
    }

}