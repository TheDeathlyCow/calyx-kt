package com.github.thedeathlycow.calyx.kt

import kotlin.random.Random

data class Options(
    val random: Random,
    val isStrict: Boolean = defaultStrict
) {

    companion object {
        const val defaultStrict: Boolean = false
    }

    constructor(strict: Boolean = defaultStrict) : this(Random.Default, strict)

    constructor(seed: Int, strict: Boolean = defaultStrict) : this(Random(seed), strict)


}
