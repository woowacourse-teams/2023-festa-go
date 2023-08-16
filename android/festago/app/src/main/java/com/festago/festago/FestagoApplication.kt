package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.di.AnalysisContainer
import com.festago.festago.di.AuthServiceContainer
import com.festago.festago.di.LocalDataSourceContainer
import com.festago.festago.di.NormalServiceContainer
import com.festago.festago.di.RepositoryContainer
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
        normalServiceContainer = NormalServiceContainer(BuildConfig.BASE_URL)

        tokenContainer = TokenContainer(
            normalServiceContainer = normalServiceContainer,
            localDataSourceContainer = LocalDataSourceContainer(applicationContext),
        )

        authServiceContainer = AuthServiceContainer(
            baseUrl = BuildConfig.BASE_URL,
            tokenContainer = tokenContainer,
        )

        repositoryContainer = RepositoryContainer(
            authServiceContainer = authServiceContainer,
            normalServiceContainer = normalServiceContainer,
            tokenContainer = tokenContainer,
        )
    }

    private fun initFirebaseContainer() {
        FirebaseAnalyticsHelper.init(applicationContext)
        analysisContainer = AnalysisContainer()
    }

    companion object DependencyContainer {
        lateinit var normalServiceContainer: NormalServiceContainer
        lateinit var authServiceContainer: AuthServiceContainer
        lateinit var repositoryContainer: RepositoryContainer
        lateinit var analysisContainer: AnalysisContainer
        lateinit var tokenContainer: TokenContainer
    }
}
