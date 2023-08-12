package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.datasource.AuthLocalDataSource
import com.festago.festago.data.retrofit.AuthRetrofitClient
import com.festago.festago.data.retrofit.NormalRetrofitClient
import com.kakao.sdk.common.KakaoSdk

class FestagoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseAnalyticsHelper.init(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        initRetrofit()
    }

    private fun initRetrofit() {
        val authLocalDataSource = AuthLocalDataSource.getInstance(applicationContext)
        NormalRetrofitClient.init(BuildConfig.BASE_URL)
        AuthRetrofitClient.init(BuildConfig.BASE_URL) {
            authLocalDataSource.token ?: NULL_TOKEN
        }
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
