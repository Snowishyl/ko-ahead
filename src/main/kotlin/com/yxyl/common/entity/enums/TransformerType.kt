package com.yxyl.common.entity.enums

enum class TransformerType {
    LLama2, ;

    companion object {
        fun getByLowercase(value: String): TransformerType =
            enumValues<TransformerType>().find { it.name.lowercase() == value.lowercase() }
                ?: error("Not found $value in TransformerType")
    }
}