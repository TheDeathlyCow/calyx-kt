package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.TemplateNode
import java.math.BigDecimal

class WeightedBranch(
    private val productions: List<WeightedProduction>,
    private val registry: Registry
) : ProductionBranch {

    private val sumOfWeights: Double

    data class WeightedProduction(
        val weight: Double,
        val production: Production
    )

    init {

        require(this.productions.none { p -> p.weight <= 0.0 }) {
            "All weights must be greater than zero"
        }

        sumOfWeights = this.productions.sumOf { p -> p.weight }

    }

    companion object {

        @JvmName("parseInt")
        fun parse(raw: Map<String, Int>, registry: Registry): WeightedBranch {
            val weightedProds = raw.asSequence()
                .map {
                    WeightedProduction(
                        production = TemplateNode.parse(it.key, registry),
                        weight = it.value.toDouble()
                    )
                }
                .toList()
            return WeightedBranch(weightedProds, registry)
        }

        @JvmName("parseDouble")
        fun parse(raw: Map<String, Double>, registry: Registry): WeightedBranch {
            require(raw.all { it.value.isFinite() }) {
                "Weights may not be infinite"
            }
            require(raw.all { !it.value.isNaN() }) {
                "Weights may not be NaN"
            }

            val weightedProds = raw.asSequence()
                .map {
                    WeightedProduction(
                        production = TemplateNode.parse(it.key, registry),
                        weight = it.value
                    )
                }
                .toList()

            return WeightedBranch(weightedProds, registry)
        }

        @JvmName("parseDecimal")
        fun parse(raw: Map<String, BigDecimal>, registry: Registry): WeightedBranch {
            val weightedProds = raw.asSequence()
                .map {
                    WeightedProduction(
                        production = TemplateNode.parse(it.key, registry),
                        weight = it.value.toDouble()
                    )
                }
                .toList()

            return WeightedBranch(weightedProds, registry)
        }
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


    override val size: Int
        get() = productions.size

}