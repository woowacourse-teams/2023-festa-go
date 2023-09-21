package com.festago.festago.presentation.service

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class TicketEntryService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {}
}
