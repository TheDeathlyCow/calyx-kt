package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry

class WeightedBranch(
    private val productions: Array<WeightedProduction>,
    private val registry: Registry
) : ProductionBranch {

    private val sumOfWeights: Double

    data class WeightedProduction(
        val weight: Double,
        val production: Production
    )

    init {

        require(this.productions.asSequence().none { p -> p.weight <= 0.0 }) {
            "All weights must be greater than zero"
        }

        sumOfWeights = this.productions.asSequence().sumOf { p -> p.weight }

    }

    override fun evaluateAt(index: Int, options: Options): Expansion {
        val tail = productions[index].production.evaluate(options)

        return Expansion(Expansion.Symbol.WEIGHTED_BRANCH, tail)
    }

    override fun evaluate(options: Options): Expansion {
        var max = this.sumOfWeights
        val waterMark = options.random.nextDouble() * this.sumOfWeights
        val prod = this.productions.asSequence().firstOrNull { wp ->
            max -= wp.weight
            waterMark >= max
        }

        if (prod !== null) {
            return Expansion(Expansion.Symbol.WEIGHTED_BRANCH, prod.production.evaluate(options))
        } else {
            error { "Unable to evaluate weighted branch" }
        }

    }


    override val length: Int
        get() = productions.size

}