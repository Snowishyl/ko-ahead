package com.yxyl.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.routing.*
import com.yxyl.router.chatRouter

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        chatRouter()
    }
}
