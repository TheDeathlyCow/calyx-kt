package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.production.ProductionBranch

class Rule(
    private val term: String,
    private val production: ProductionBranch
): ProductionBranch by production {

    companion object {

        fun empty(term: String): Rule {
            TODO("not yet implemented")
        }

    }

}