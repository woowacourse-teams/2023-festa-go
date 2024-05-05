package com.festago.festago.domain.repository

import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.festival.FestivalsPage
import java.time.LocalDate

interface ArtistRepository {
    suspend fun loadArtistDetail(id: Long, delayTimeMillis: Long = 0L): Result<ArtistDetail>

    suspend fun loadArtistFestivals(
        id: Long,
        size: Int? = null,
        lastFestivalId: Long? = null,
        lastStartDate: LocalDate? = null,
        isPast: Boolean? = null,
    ): Result<FestivalsPage>
}
