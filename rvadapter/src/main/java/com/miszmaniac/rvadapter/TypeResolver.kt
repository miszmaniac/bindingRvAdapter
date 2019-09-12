package com.miszmaniac.rvadapter

import java.lang.reflect.Type

data class TypeResolver<DataType>(
    val type: Type,
    val condition: (data: DataType) -> Boolean = { true }
)