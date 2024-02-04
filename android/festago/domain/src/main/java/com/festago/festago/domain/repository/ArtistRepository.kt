package com.festago.festago.domain.repository

import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.Stages
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalLocation
import com.festago.festago.domain.model.festival.FestivalsPage
import java.time.LocalDate

interface ArtistRepository {
    suspend fun loadArtistDetail(): Result<ArtistDetail>

    suspend fun loadArtistStages(size: Int): Result<Stages>
}
