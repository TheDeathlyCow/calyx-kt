package com.github.thedeathlycow.calyx.kt


internal class FilterRegistry {

    private val filterHolders: MutableMap<String, (String, Options) -> String> = mutableMapOf()

    operator fun get(name: String): (String, Options) -> String {
        val filter = filterHolders[name] ?: throw UndefinedFilter(name)
        return { input, options ->
            try {
                filter(input, options)
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
                filterHolders[name] = { input, options ->
                    method(instance, input, options) as String
                }
            }
        }
    }

}