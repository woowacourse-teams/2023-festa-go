package com.festago.festago.domain.repository

import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.festival.FestivalsPage

interface ArtistRepository {
    suspend fun loadArtistDetail(id: Long): Result<ArtistDetail>

    suspend fun loadArtistFestivals(id: Long, size: Int): Result<FestivalsPage>
}
