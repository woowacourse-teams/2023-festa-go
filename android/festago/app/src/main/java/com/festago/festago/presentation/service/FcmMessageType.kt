package com.festago.festago.presentation.service

enum class FcmMessageType(val id: Int, val channelId: String) {
    ENTRY_ALERT(id = 0, channelId = "ENTRY_ALERT"),
    ENTRY_PROCESS(id = 1, channelId = "ENTRY_PROCESS"),
    ;
}
