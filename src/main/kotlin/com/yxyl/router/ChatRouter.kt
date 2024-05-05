package com.yxyl.router

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.kodein.di.instance
import org.slf4j.LoggerFactory
import com.yxyl.common.assemble.ChatAssemble
import com.yxyl.common.conf.kodein
import com.yxyl.domain.chat.service.ChatService
import com.yxyl.domain.chat.model.req.ChatCompletionsRequest

class ChatRouter

fun Route.chatRouter() {

    val logger: Logger = LoggerFactory.getLogger(ChatRouter::class.java)
    val chatService: ChatService by kodein.instance()
    val chatAssemble: ChatAssemble by kodein.instance()

    route("/v1/chat") {
        post("/completions") {
            val req = call.receive<ChatCompletionsRequest>()
            val res = chatService.chat(chatAssemble.toChatCommand(req))
            logger.info("ChatCompletionsRequest:{} ", req)
            call.respond(res)
        }
    }
}