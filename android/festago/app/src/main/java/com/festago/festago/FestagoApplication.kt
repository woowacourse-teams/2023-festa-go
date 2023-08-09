package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.datasource.SharedPrefAuthDataSource
import com.kakao.sdk.common.KakaoSdk

class FestagoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseAnalyticsHelper.create(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        RetrofitClient.create(
            authDataSource = SharedPrefAuthDataSource.getInstance(applicationContext),
            baseUrl = BuildConfig.BASE_URL,
        )
    }
}
