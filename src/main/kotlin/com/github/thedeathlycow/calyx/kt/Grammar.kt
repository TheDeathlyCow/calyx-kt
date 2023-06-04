package com.github.thedeathlycow.calyx.kt

import java.math.BigDecimal
import kotlin.reflect.KClass

class Grammar(
    options: Options
) {

    private val registry: Registry

    init {
        this.registry = Registry(options)
    }

    constructor() : this(Options())

    fun start(productions: Array<String>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun start(productions: Map<String, Int>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun start(productions: Map<String, Double>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun start(productions: Map<String, BigDecimal>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun rule(name: String, productions: Array<String>): Grammar {
        registry.defineRule(name, productions)
        return this
    }

    fun rule(name: String, production: String): Grammar {
        registry.defineRule(name, arrayOf(production))
        return this
    }

    fun rule(name: String, production: Map<String, Int>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    fun rule(name: String, production: Map<String, Double>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    fun rule(name: String, production: Map<String, BigDecimal>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    fun filters(filters: KClass<Filters>): Grammar {
        registry.addFilterClass(filters)
        return this
    }

    fun generate(): Result {
        return Result(registry.evaluate("start"))
    }

    fun generate(startSymbol: String): Result {
        return Result(registry.evaluate(startSymbol))
    }

    fun generate(context: Map<String, Array<String>>): Result {
        return Result(registry.evaluate("start", context))
    }

    fun generate(startSymbol: String, context: Map<String, Array<String>>): Result {
        return Result(registry.evaluate(startSymbol, context))
    }
}