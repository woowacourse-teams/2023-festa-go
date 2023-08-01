package com.festago.festago.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class FirebaseAnalyticsHelper private constructor() : AnalyticsHelper {

    private val firebaseAnalytics = Firebase.analytics

    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            event.extras.forEach {
                param(it.key, it.value)
            }
        }
    }

    companion object {

        private var instance: FirebaseAnalyticsHelper? = null

        private fun createInstance(): FirebaseAnalyticsHelper {
            instance = FirebaseAnalyticsHelper()
            return instance!!
        }

        @Synchronized
        fun getInstance(): FirebaseAnalyticsHelper {
            return instance ?: createInstance()
        }
    }
}
