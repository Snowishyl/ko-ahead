package com.yxyl.common.assemble

import org.mapstruct.Mapper
import com.yxyl.domain.chat.model.command.ChatCommand
import com.yxyl.domain.chat.model.req.ChatCompletionsRequest
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy
import java.util.*

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = [UUID::class])
interface ChatAssemble {

    @Mappings(
        Mapping(target = "id", expression = "java(\"chatcmpl-\" + UUID.randomUUID().toString().replace(\"-\", \"\"))"),
    )
    fun toChatCommand(req: ChatCompletionsRequest): ChatCommand

}