package com.yxyl.common.conf

import  org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import com.yxyl.common.assemble.ChatAssemble
import com.yxyl.common.assemble.ChatAssembleImpl
import com.yxyl.domain.chat.service.ChatService
import com.yxyl.domain.inference.service.Llama2PromptAdapter
import com.yxyl.domain.inference.service.PromptAdapter
import com.yxyl.domain.inference.service.token.Tokenizer
import com.yxyl.domain.inference.service.token.UTF8Tokenizer

val kodein = DI {
    bind<ChatService>() with singleton { ChatService() }
    bind<ChatAssemble>() with singleton { ChatAssembleImpl() }
    bind<PromptAdapter>() with singleton { Llama2PromptAdapter() }
    bind<Tokenizer>() with singleton { UTF8Tokenizer() }
}