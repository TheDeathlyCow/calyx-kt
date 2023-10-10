package com.github.thedeathlycow.calyx.kt

import java.math.BigDecimal
import java.util.StringJoiner
import kotlin.random.Random
import kotlin.reflect.KClass

class Grammar(
    options: Options
) {

    private val registry: Registry

    init {
        this.registry = Registry(options)
    }

    constructor() : this(Options())

    constructor(strict: Boolean = Options.defaultStrict) : this(Options(strict))

    constructor(rng: Random, strict: Boolean = Options.defaultStrict) : this(Options(rng, strict))

    constructor(seed: Int, strict: Boolean = Options.defaultStrict) : this(Options(seed, strict))

    constructor(
        strict: Boolean = Options.defaultStrict,
        registrationCallback: Grammar.() -> Unit
    ) : this(strict) {
        registrationCallback()
    }

    constructor(
        seed: Int,
        strict: Boolean = Options.defaultStrict,
        registrationCallback: Grammar.() -> Unit
    ) : this(seed, strict) {
        registrationCallback()
    }

    fun start(productions: List<String>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun start(production: String): Grammar {
        registry.defineRule("start", listOf(production))
        return this
    }

    @JvmName("startInt")
    fun start(productions: Map<String, Int>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    @JvmName("startDouble")
    fun start(productions: Map<String, Double>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    @JvmName("startDecimal")
    fun start(productions: Map<String, BigDecimal>): Grammar {
        this.registry.defineRule("start", productions)
        return this
    }

    fun rule(name: String, productions: List<String>): Grammar {
        registry.defineRule(name, productions)
        return this
    }

    fun rule(name: String, production: String): Grammar {
        registry.defineRule(name, listOf(production))
        return this
    }

    @JvmName("ruleInt")
    fun rule(name: String, production: Map<String, Int>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    @JvmName("ruleDouble")
    fun rule(name: String, production: Map<String, Double>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    @JvmName("ruleDecimal")
    fun rule(name: String, production: Map<String, BigDecimal>): Grammar {
        registry.defineRule(name, production)
        return this
    }

    fun filters(filters: KClass<*>): Grammar {
        registry.addFilterClass(filters)
        return this
    }

    fun generate(): Result {
        return Result(registry.evaluate("start"))
    }

    fun generate(startSymbol: String): Result {
        return Result(registry.evaluate(startSymbol))
    }

    fun generate(context: Map<String, List<String>>): Result {
        return Result(registry.evaluate("start", context))
    }

    fun generate(startSymbol: String, context: Map<String, List<String>>): Result {
        return Result(registry.evaluate(startSymbol, context))
    }
}