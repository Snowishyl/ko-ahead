package com.yxyl.domain.chat.model.res
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.yxyl.common.entity.enums.FinishReason
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Date

data class ChatResult(
    val id: String,
    @JsonSerialize
    val createdAt: Date,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
) {
    data class Choice(
        val index: Int,
        val message: Message,
        val finishReason: FinishReason
    )

    data class Message(
        val content: String
    )

    data class Usage(
        val promptTokens: Int,
        val completionTokens: Int,
        val totalTokens: Int
    )

}
