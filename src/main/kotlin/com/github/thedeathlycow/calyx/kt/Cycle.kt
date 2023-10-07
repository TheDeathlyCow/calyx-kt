package com.github.thedeathlycow.calyx.kt

class Cycle(
    count: Int
) {

    private var index: Int = 0
    private val sequence: IntArray

    init {
        require(count > 0) { "'count' must be greater than zero" }

        this.index = count - 1 // defers shuffling to first poll() call
        sequence = IntArray(count) {
            it
        }
    }

    fun poll(options: Options): Int {
        this.index++

        if (this.index >= this.sequence.size) {
            this.sequence.shuffle(options.random)
            this.index = 0
        }

        return sequence[index]
    }

}