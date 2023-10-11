package com.festago.festago.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.festago.festago.R
import com.festago.festago.presentation.ui.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

class TicketEntryService : FirebaseMessagingService() {

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "ENTRY_ALERT", "공연 입장 알림", NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        when (remoteMessage.from) {
            "ENTRY_ALERT" -> {
                sendNotification(remoteMessage)
            }

            "ENTRY_PROCESS" -> {
                runBlocking {
                    // TODO: 입장완료 로직인지 확인하는 로직 추가 필요
                    ticketStateChangeEvent.emit(Unit)
                }
            }

            else -> {}
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val intent = HomeActivity.getIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, "ENTRY_ALERT")
            .setSmallIcon(R.mipmap.ic_festago_logo_round)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // TODO: 토큰이 변경되었을 때 처리
    }

    companion object {
        val ticketStateChangeEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    }
}
