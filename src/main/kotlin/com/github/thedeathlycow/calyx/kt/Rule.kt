package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.production.EmptyBranch
import com.github.thedeathlycow.calyx.kt.production.ProductionBranch

class Rule(
    private val term: String,
    private val production: ProductionBranch
): ProductionBranch by production {

    companion object {

        fun empty(term: String): Rule {
            return Rule(term, EmptyBranch())
        }

    }

}