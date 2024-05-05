package com.yxyl.domain.chat.model.command

data class ChatCommand(
    val id:String,
    val model: String,
    val messages: List<Message>,
    val maxTokens: Int = 256,
    val temperature: Float = 1.0f,
    val topP: Float = 1.0f,
    val n: Int = 1,
) {
    data class Message(
        val content: String
    )
}
