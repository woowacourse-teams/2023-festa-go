package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.repository.FakeArtistRepository
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.domain.repository.FestivalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsFestivalDefaultRepository(festivalRepository: FestivalDefaultRepository): FestivalRepository

    @Binds
    @Singleton
    fun bindsArtistRepository(artistRepository: FakeArtistRepository): ArtistRepository
}
