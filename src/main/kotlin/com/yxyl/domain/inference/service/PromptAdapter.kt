package com.yxyl.domain.inference.service

import com.yxyl.common.entity.enums.TransformerType
import com.yxyl.domain.chat.model.command.ChatCommand

interface PromptAdapter {

    val type: TransformerType

    fun buildPromptTemplate(messages: List<ChatCommand.Message>): String

}

class Llama2PromptAdapter : PromptAdapter {

    override val type: TransformerType
        get() = TransformerType.LLama2

    override fun buildPromptTemplate(messages: List<ChatCommand.Message>): String {
        if (messages.isEmpty()) {
            return ""
        }
        val instStartTag = "[INST]"
        val instEndTag = "[/INST]"
        val sysStartTag = "<<SYS>>"
        val sysEndTag = "<</SYS>>"
        return messages.joinToString("") { message ->
            "${message.content}$instEndTag"
        }
    }

}
