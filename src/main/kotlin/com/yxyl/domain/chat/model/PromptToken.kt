package com.yxyl.domain.chat.model

data class PromptToken(
    val promptTokens: IntArray,
    val numPromptTokens: Int,
    val prompt: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromptToken

        if (!promptTokens.contentEquals(other.promptTokens)) return false
        if (numPromptTokens != other.numPromptTokens) return false
        if (prompt != other.prompt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = promptTokens.contentHashCode()
        result = 31 * result + numPromptTokens
        result = 31 * result + prompt.hashCode()
        return result
    }

}