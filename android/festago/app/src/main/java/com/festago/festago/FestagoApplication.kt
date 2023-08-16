package com.festago.festago

import android.app.Application
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.repository.TokenDefaultRepository
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
        FirebaseAnalyticsHelper.init(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        initRetrofit()
    }

    private fun initRetrofit() {
        localDataSourceContainer = LocalDataSourceContainer(applicationContext)
        tokenContainer = TokenContainer(
            tokenRepository = TokenDefaultRepository(
                authRetrofitService = NormalRetrofitClient(BuildConfig.BASE_URL).authRetrofitService,
                authLocalDataSource = localDataSourceContainer.authDataSource,
            ),
        )

        serviceContainer = ServiceContainer(
            NormalRetrofitClient(BuildConfig.BASE_URL),
            AuthRetrofitClient(BuildConfig.BASE_URL, tokenContainer.tokenManager),
        )

        repositoryContainer = RepositoryContainer(
            localDataSourceContainer,
            serviceContainer,
        )

        FirebaseAnalyticsHelper.init(applicationContext)
        analysisContainer = AnalysisContainer()
    }

    companion object DependencyContainer {
        lateinit var serviceContainer: ServiceContainer
        lateinit var localDataSourceContainer: LocalDataSourceContainer
        lateinit var repositoryContainer: RepositoryContainer
        lateinit var analysisContainer: AnalysisContainer
        lateinit var tokenContainer: TokenContainer
    }
}
