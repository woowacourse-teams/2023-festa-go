package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.retrofit.AuthRetrofitClient
import com.festago.festago.data.retrofit.NormalRetrofitClient
import com.festago.festago.di.AnalysisContainer
import com.festago.festago.di.LocalDataSourceContainer
import com.festago.festago.di.RepositoryContainer
import com.festago.festago.di.ServiceContainer
import com.festago.festago.di.TokenContainer
import com.kakao.sdk.common.KakaoSdk

class FestagoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKakaoSdk()
        initRepositoryContainer()
        initFirebaseContainer()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun initRepositoryContainer() {
        val normalRetrofitClient = NormalRetrofitClient(BuildConfig.BASE_URL)

        tokenContainer = TokenContainer(
            normalRetrofitClient = normalRetrofitClient,
            localDataSourceContainer = LocalDataSourceContainer(applicationContext),
        )

        val authRetrofitClient = AuthRetrofitClient(
            baseUrl = BuildConfig.BASE_URL,
            tokenManager = tokenContainer.tokenManager,
        )

        serviceContainer = ServiceContainer(
            normalRetrofitClient = normalRetrofitClient,
            authRetrofitClient = authRetrofitClient,
        )

        repositoryContainer = RepositoryContainer(
            serviceContainer = serviceContainer,
            tokenContainer = tokenContainer,
        )
    }

    private fun initFirebaseContainer() {
        FirebaseAnalyticsHelper.init(applicationContext)
        analysisContainer = AnalysisContainer()
    }

    companion object DependencyContainer {
        lateinit var serviceContainer: ServiceContainer
        lateinit var repositoryContainer: RepositoryContainer
        lateinit var analysisContainer: AnalysisContainer
        lateinit var tokenContainer: TokenContainer
    }
}
