package com.festago.festago.presentation.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.festago.festago.R
import com.festago.festago.presentation.service.FcmMessageType.ENTRY_ALERT
import com.festago.festago.presentation.ui.home.HomeActivity

class NotificationManager(private val context: Context) {

    private val intent = HomeActivity.getIntent(context).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private val pendingIntent = PendingIntent.getActivity(
        context,
        HOME_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val entryAlertNotificationBuilder =
        NotificationCompat.Builder(context, ENTRY_ALERT.channelId)
            .setSmallIcon(R.mipmap.ic_festago_logo_round)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

    fun sendEntryAlertNotification(title: String, body: String) {
        entryAlertNotificationBuilder
            .setContentTitle(title)
            .setContentText(body)

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(0, entryAlertNotificationBuilder.build())
        }
    }

    companion object {
        private const val HOME_REQUEST_CODE = 0
    }
}
