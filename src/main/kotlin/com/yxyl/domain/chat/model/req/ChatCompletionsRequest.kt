package com.yxyl.domain.chat.model.req

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.jetbrains.annotations.NotNull
import java.io.IOException

data class ChatCompletionsRequest(
    val model: String,

    val messages: List<Message>,

    val temperature: Float? = 1.0f,

    @JsonProperty("top_p") val topP: Float? = 1.0f,
    val n: Int? = 1,

    @JsonProperty("max_tokens") val maxTokens: Int? = 1024,
    val stream: Boolean = true,
) {
    data class Message(
        @field:NotNull @JsonDeserialize(using = UppercaseDeserializer::class) val role: String,

        val content: String,
    )

}

class UppercaseDeserializer : JsonDeserializer<String>() {
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String = p.valueAsString.uppercase()
}
