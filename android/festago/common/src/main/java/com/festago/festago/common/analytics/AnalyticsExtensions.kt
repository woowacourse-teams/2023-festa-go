package com.festago.festago.common.analytics

fun AnalyticsHelper.logNetworkFailure(key: String, value: String) {
    logEvent(
        AnalyticsEvent(
            type = "Network Failure Type",
            extras = listOf(AnalyticsEvent.Param(key, value)),
        ),
    )
}
