package com.festago.festago.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsHelper private constructor(context: Context) : AnalyticsHelper {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

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

        private var instance: FirebaseAnalyticsHelper? = null

        fun create(context: Context) {
            if (instance == null) {
                instance = FirebaseAnalyticsHelper(context)
            }
        }

        fun getInstance(): FirebaseAnalyticsHelper {
            return instance ?: throw NullPointerException()
        }
    }
}
