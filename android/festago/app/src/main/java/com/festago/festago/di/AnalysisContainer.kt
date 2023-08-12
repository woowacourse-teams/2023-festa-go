package com.festago.festago.di

import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.FirebaseAnalyticsHelper

class AnalysisContainer {
    val analyticsHelper: AnalyticsHelper =
        FirebaseAnalyticsHelper
}
