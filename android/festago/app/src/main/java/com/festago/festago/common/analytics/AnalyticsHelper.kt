package com.festago.festago.common.analytics

import com.festago.festago.common.analytics.AnalyticsEvent

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}
