package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.repository.DefaultRecentSearchRepository
import com.festago.festago.data.repository.FakeArtistRepository
import com.festago.festago.data.repository.FakeFestivalRepository
import com.festago.festago.data.repository.FakeSchoolRepository
import com.festago.festago.domain.repository.ArtistRepository
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.domain.repository.RecentSearchRepository
import com.festago.festago.domain.repository.SchoolRepository
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
    fun bindsFestivalDefaultRepository(festivalRepository: FakeFestivalRepository): FestivalRepository

    @Binds
    @Singleton
    fun bindsArtistRepository(artistRepository: FakeArtistRepository): ArtistRepository

    @Binds
    @Singleton
    fun bindsSchoolRepository(schoolRepository: FakeSchoolRepository): SchoolRepository

    @Binds
    @Singleton
    fun bindsRecentSearchRepository(recentSearchRepository: DefaultRecentSearchRepository): RecentSearchRepository
}
