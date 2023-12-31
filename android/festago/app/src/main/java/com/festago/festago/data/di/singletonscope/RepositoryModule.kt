package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.repository.AuthDefaultRepository
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.data.repository.SocialAuthKakaoRepository
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.FestivalRepository
import com.festago.festago.repository.SocialAuthRepository
import com.festago.festago.repository.TicketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindsAuthDefaultRepository(authRepository: AuthDefaultRepository): AuthRepository

    @Binds
    fun bindsSocialAuthDefaultRepository(socialAuthRepository: SocialAuthKakaoRepository): SocialAuthRepository

    @Binds
    @Singleton
    fun bindsFestivalDefaultRepository(festivalRepository: FestivalDefaultRepository): FestivalRepository

    @Binds
    @Singleton
    fun bindsTicketDefaultRepository(ticketRepository: TicketDefaultRepository): TicketRepository
}
