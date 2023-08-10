package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.AuthRetrofitClient
import com.festago.festago.data.NormalRetrofitClient
import com.festago.festago.data.datasource.AuthLocalDataSource
import com.kakao.sdk.common.KakaoSdk

class FestagoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseAnalyticsHelper.init(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        initRetrofit()
    }

    private fun initRetrofit() {
        val token = AuthLocalDataSource.getInstance(applicationContext).token ?: NULL_TOKEN
        NormalRetrofitClient.init(BuildConfig.BASE_URL)
        AuthRetrofitClient.create(baseUrl = BuildConfig.BASE_URL, token = token)
    }

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
