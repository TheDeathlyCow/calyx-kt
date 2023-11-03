package com.github.thedeathlycow.calyx.kt


internal class FilterRegistry {

    private val filters: MutableMap<String, (String, Options) -> String> = mutableMapOf()

    operator fun get(name: String): (String, Options) -> String = filters[name] ?: throw UndefinedFilter(name)

    fun addFilters(instance: Any) {
        for (method in instance.javaClass.methods) {
            val annotation: FilterName = method.getAnnotation(FilterName::class.java) ?: continue
            val name = annotation.name
            filters[name] = { input, options ->
                try {
                    method(instance, input, options) as String
                } catch (e: IllegalArgumentException) {
                    throw IncorrectFilterSignature(name, e)
                }
            }
        }
    }

}