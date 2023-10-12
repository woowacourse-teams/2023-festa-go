package com.festago.festago.presentation.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.festago.festago.R
import com.festago.festago.presentation.service.FcmMessageType.ENTRY_ALERT
import com.festago.festago.presentation.ui.home.HomeActivity


class NotificationManager(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val intent = HomeActivity.getIntent(context).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    private val pendingIntent = PendingIntent.getActivity(
        context,
        TicketEntryService.START_ACTIVITY_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val entryAlertNotificationBuilder =
        NotificationCompat.Builder(context, context.getString(R.string.entry_alert_channel_name))
            .setSmallIcon(R.mipmap.ic_festago_logo_round)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

    fun sendEntryAlertNotification(title: String, body: String) {
        entryAlertNotificationBuilder
            .setContentTitle(title)
            .setContentText(body)

        notificationManager.notify(ENTRY_ALERT.id, entryAlertNotificationBuilder.build())
    }
}
