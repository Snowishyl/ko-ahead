package com.yxyl.domain.chat.service

import com.yxyl.common.conf.kodein
import com.yxyl.common.entity.enums.FinishReason
import com.yxyl.common.entity.enums.TransformerType
import com.yxyl.domain.chat.model.PromptToken
import com.yxyl.domain.chat.model.command.ChatCommand
import com.yxyl.domain.chat.model.res.ChatResult
import com.yxyl.domain.inference.model.mapper.ChatCommandMapper
import com.yxyl.domain.inference.service.Llama2PromptAdapter
import com.yxyl.domain.inference.service.token.Tokenizer
import com.yxyl.domain.inference.service.transform.Llama2
import com.yxyl.infrastructure.entity.ModelWeight
import com.yxyl.infrastructure.entity.Vocabulary
import org.kodein.di.instance
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Date

interface IChatService {
    fun chat(cmd: ChatCommand): ChatResult?
}

class ChatService : IChatService {

    private val logger = LoggerFactory.getLogger(IChatService::class.java)

    private val tokenizer: Tokenizer by kodein.instance()

    private var vocabulary: Vocabulary

    private var modelWeight: ModelWeight

    init {
        val checkPointPath = "/home/yxyl/code/kotlin/model/stories15M.bin"
        val tokenizerPath = "/home/yxyl/code/kotlin/model/tokenizer.bin"

        modelWeight = ModelWeight(checkPointPath)
        vocabulary = Vocabulary(tokenizerPath, modelWeight)

    }

    override fun chat(cmd: ChatCommand): ChatResult {
        val transformerType = TransformerType.getByLowercase(cmd.model)
        val promptInfo = checkAndBuildPrompt(transformerType, cmd.messages)
        val step = minOf(cmd.maxTokens, modelWeight.config.seqLen)
        val tokens = generateTokens(transformerType, cmd, promptInfo, step)
        val output = tokens.drop(promptInfo.numPromptTokens)
            .map { tokenizer.decode(vocabulary, it) }
            .joinToString("")

        logger.debug("prompt: {} \n output: {} \n", promptInfo.prompt, output)
        val totalTokens = tokens.count()
        return ChatResult(
            id = cmd.id,
            createdAt = Date.from(Instant.now()),
            model = transformerType.name.lowercase(),
            choices = listOf(
                ChatResult.Choice(
                    index = 0,
                    message = ChatResult.Message(
                        content = output
                    ),
                    finishReason = if (totalTokens == step) {
                        FinishReason.LENGTH
                    } else {
                        FinishReason.STOP
                    }
                )
            ),
            usage = ChatResult.Usage(
                promptTokens = promptInfo.numPromptTokens,
                completionTokens = totalTokens.minus(promptInfo.numPromptTokens),
                totalTokens = totalTokens
            )
        )
    }


    private fun generateTokens(
        transformerType: TransformerType,
        chatCommand: ChatCommand,
        promptInfo: PromptToken,
        step: Int,
    ): Sequence<Int> {
        val transformer = Llama2()
        return transformer.iterativeGenerate(
            modelWeight,
            promptInfo.promptTokens,
            promptInfo.numPromptTokens,
            ChatCommandMapper.toSampler(chatCommand, modelWeight.config.vocabSize),
            step
        )
    }

    private fun checkAndBuildPrompt(modelType: TransformerType, messages: List<ChatCommand.Message>): PromptToken {
        val adapter = Llama2PromptAdapter()

        val prompt = adapter.buildPromptTemplate(messages)
        val promptTokens = IntArray(prompt.length + 3)
        val numPromptTokens = tokenizer.encode(vocabulary, prompt, 1, 0, promptTokens)
        require(numPromptTokens >= 1) {
            "Prompt token missing, expected at least 1 prompt token"
        }
        return PromptToken(promptTokens, numPromptTokens, prompt)
    }

}