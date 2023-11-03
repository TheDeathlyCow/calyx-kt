package com.github.thedeathlycow.calyx.kt.serialize

import com.github.thedeathlycow.calyx.kt.Grammar
import com.github.thedeathlycow.calyx.kt.GrammarParseException
import com.google.gson.*
import java.lang.reflect.Type

object GrammarJsonParser : JsonDeserializer<Grammar> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Grammar {
        val jsonObject: JsonObject = json.asJsonObject;

        val grammar = Grammar()

        jsonObject.keySet().forEach {
            this.parseRule(grammar, it, jsonObject[it])
        }

        return grammar
    }

    private fun parseRule(grammar: Grammar, ruleName: String, json: JsonElement) {
        when {
            json.isJsonPrimitive -> {
                grammar.rule(ruleName, json.asString)
            }

            json.isJsonArray -> {
                val prods = mutableListOf<String>()
                json.asJsonArray.forEach {
                    prods.add(it.asString)
                }
                grammar.rule(ruleName, prods)
            }

            json.isJsonObject -> {
                val jsonObject = json.asJsonObject
                val prods = mutableMapOf<String, Double>()
                jsonObject.keySet().forEach {
                    prods[it] = jsonObject[it].asDouble
                }
                grammar.rule(ruleName, prods)
            }

            else -> {
                throw JsonParseException("Rule '$ruleName' is not a primitive, array, or object compound")
            }
        }
    }
}