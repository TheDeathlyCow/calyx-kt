package com.github.thedeathlycow.calyx.kt

class Grammar(
    options: Options
) {

    private val registry: Registry

    init {
        this.registry = Registry(options)
    }

    constructor() : this(Options())

    fun start(productions: Array<String>): Grammar {
        //this.registry.de
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