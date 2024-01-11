package com.festago.festago.common.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    @ApplicationContext context: Context
) : AnalyticsHelper {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context.applicationContext)
    }

    override fun logEvent(event: AnalyticsEvent) {
        val params = Bundle().apply {
            event.extras.forEach {
                putString(it.key, it.value)
            }
        }
        firebaseAnalytics.logEvent(LOG_NAME, params)
    }

    companion object {
        private const val LOG_NAME = "festago_log"
    }
}
