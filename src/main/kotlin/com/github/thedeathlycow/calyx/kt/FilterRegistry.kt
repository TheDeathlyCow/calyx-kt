package com.github.thedeathlycow.calyx.kt

import java.lang.reflect.Method


internal class FilterRegistry {

    private val filterHolders: MutableMap<String, Pair<Any, Method>> = mutableMapOf()

    operator fun get(name: String): (String, Options) -> String {
        val pair: Pair<Any, Method> = filterHolders[name] ?: throw UndefinedFilter(name)
        return { input, options ->
            try {
                pair.second.invoke(pair.first, input, options) as String
            } catch (e: IllegalArgumentException) {
                throw IncorrectFilterSignature(name, e)
            }
        }
    }

    fun addFilters(instance: Any) {
        for (method in instance.javaClass.methods) {
            val annotation: FilterName? = method.getAnnotation(FilterName::class.java)
            if (annotation != null) {
                val name = annotation.name
                filterHolders[name] = instance to method
            }
        }
    }

}