package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.production.EmptyBranch
import com.github.thedeathlycow.calyx.kt.production.ProductionBranch
import com.github.thedeathlycow.calyx.kt.production.UniformBranch
import com.github.thedeathlycow.calyx.kt.production.WeightedBranch
import java.math.BigDecimal

class Rule(
    private val term: String,
    private val production: ProductionBranch
): ProductionBranch by production {

    companion object {

        fun empty(term: String): Rule {
            return Rule(term, EmptyBranch())
        }

        fun build(term: String, productions: List<String>, registry: Registry): Rule {
            val branch = UniformBranch.parse(productions, registry)
            return Rule(term, branch)
        }

        fun build(term: String, productions: Map<String, Int>, registry: Registry): Rule {
            val branch = WeightedBranch.parse(productions, registry)
            return Rule(term, branch)
        }

        fun build(term: String, productions: Map<String, Double>, registry: Registry): Rule {
            val branch = WeightedBranch.parse(productions, registry)
            return Rule(term, branch)
        }

        fun build(term: String, productions: Map<String, BigDecimal>, registry: Registry): Rule {
            val branch = WeightedBranch.parse(productions, registry)
            return Rule(term, branch)
        }

    }

}