package com.festago.festago

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.festago.festago.presentation.R
import com.festago.festago.presentation.fcm.FcmMessageType
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FestagoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(
            FcmMessageType.ENTRY_ALERT.channelId,
            getString(R.string.entry_alert_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
