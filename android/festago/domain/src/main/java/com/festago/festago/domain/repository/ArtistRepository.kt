package com.festago.festago.domain.repository

import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.Stages

interface ArtistRepository {
    suspend fun loadArtistDetail(id: Long): Result<ArtistDetail>

    suspend fun loadArtistStages(id: Long, size: Int): Result<Stages>
}
