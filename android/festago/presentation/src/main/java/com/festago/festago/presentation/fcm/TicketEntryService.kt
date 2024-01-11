package com.festago.festago.presentation.fcm

import com.festago.festago.presentation.fcm.FcmMessageType.ENTRY_ALERT
import com.festago.festago.presentation.fcm.FcmMessageType.ENTRY_PROCESS
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

class TicketEntryService : FirebaseMessagingService() {

    private val notificationManager by lazy {
        com.festago.festago.presentation.fcm.NotificationManager(
            this
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        when (remoteMessage.notification?.channelId) {
            ENTRY_ALERT.channelId -> handleEntryAlert(remoteMessage)

            ENTRY_PROCESS.channelId -> {
                runBlocking {
                    ticketStateChangeEvent.emit(Unit)
                }
            }

            else -> Unit
        }
    }

    private fun handleEntryAlert(remoteMessage: RemoteMessage) {
        notificationManager.sendEntryAlertNotification(
            remoteMessage.notification?.title ?: "",
            remoteMessage.notification?.body ?: ""
        )
    }

    override fun onNewToken(token: String) {
        // TODO: 토큰이 변경되었을 때 처리
    }

    companion object {
        val ticketStateChangeEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    }
}
