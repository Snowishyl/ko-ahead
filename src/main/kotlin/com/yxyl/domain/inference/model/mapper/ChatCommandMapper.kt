package com.yxyl.domain.inference.model.mapper

import com.yxyl.domain.chat.model.command.ChatCommand
import com.yxyl.infrastructure.entity.Sampler

class ChatCommandMapper {
    companion object {
        fun toSampler(chatCommand: ChatCommand, vocabSize: Int): Sampler {
            return Sampler(
                vocabSize,
                chatCommand.temperature,
                chatCommand.topP,
                rngState = System.currentTimeMillis() / 1000L
            )
        }
    }

}