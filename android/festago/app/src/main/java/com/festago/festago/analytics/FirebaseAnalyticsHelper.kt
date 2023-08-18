package com.festago.festago.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsHelper : AnalyticsHelper {

    private const val LOG_NAME = "festago_log"
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        if (::firebaseAnalytics.isInitialized) return
        firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext)
    }

    override fun logEvent(event: AnalyticsEvent) {
        val params = Bundle().apply {
            event.extras.forEach {
                putString(it.key, it.value)
            }
        }
        firebaseAnalytics.logEvent(LOG_NAME, params)
    }
}
