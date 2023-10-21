package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.serialize.GrammarJsonParser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.FileReader
import java.math.BigDecimal
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

    companion object {

        private val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Grammar::class.java, GrammarJsonParser)
            .create()

        /**
         * Loads a grammar from a file, given by [fileName]. Currently only supports JSON parsing.
         *
         * @param fileName The name of the file to parse from.
         * @return Returns the Grammar described in the file.
         * @throws GrammarParseException Thrown if there is an issue parsing the grammar. The cause will contain more details.
         */
        @Throws(GrammarParseException::class)
        fun load(fileName: String): Grammar {
            try {
                return readGrammarFile(fileName)
            } catch (exception: Exception) {
                throw GrammarParseException("Error parsing grammar", exception)
            }
        }

        /**
         * Directly parses a grammar JSON string into a [Grammar]
         *
         * @param rawJson A raw JSON string to parse into a grammar
         * @return Returns the grammar representing by the JSON string
         * @throws GrammarParseException Thrown if there is an issue parsing the grammar. The cause will contain more details.
         */
        @Throws(GrammarParseException::class)
        fun loadJson(rawJson: String): Grammar {
            try {
                return gson.fromJson(rawJson, Grammar::class.java)
            } catch (exception: Exception) {
                throw GrammarParseException("Error parsing grammar", exception)
            }
        }

        private fun readGrammarFile(fileName: String): Grammar {
            FileReader(fileName).use {
                if (fileName.endsWith(".json")) {
                    return gson.fromJson(it, Grammar::class.java)
                } else if (fileName.endsWith(".yaml")) {
                    TODO("YAML support is not yet implemented, sorry!")
                } else {
                    throw IllegalArgumentException("Grammars must be specified in a JSON file!")
                }
            }
        }
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

    fun filters(filters: Any): Grammar {
        registry.addFilters(filters)
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