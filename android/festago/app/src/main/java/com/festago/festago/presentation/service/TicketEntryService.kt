package com.festago.festago.presentation.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

class TicketEntryService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        runBlocking {
            // TODO: 입장완료 로직인지 확인하는 로직 추가 필요
            ticketStateChangeEvent.emit(Unit)
        }
    }

    override fun onNewToken(token: String) {
        // TODO: 토큰이 변경되었을 때 처리
    }

    companion object {
        val ticketStateChangeEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    }
}
