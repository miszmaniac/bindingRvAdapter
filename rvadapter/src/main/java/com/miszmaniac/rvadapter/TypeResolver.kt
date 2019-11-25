package com.miszmaniac.rvadapter

import java.lang.reflect.Type

data class TypeResolver<DataType>(
    val type: Type,
    val condition: (data: DataType) -> Boolean = { true }
) {

    @Suppress("UNCHECKED_CAST")
    fun matches(itemType: Class<out Any>, item: Any) =
        resolveType(itemType) && (this as TypeResolver<Any>).condition(item)

    private fun resolveType(itemType: Class<out Any>) = when (type) {
        is Class<*> -> type.isAssignableFrom(itemType)
        else -> type == itemType
    }

}