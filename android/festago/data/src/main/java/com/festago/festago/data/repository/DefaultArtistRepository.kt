package com.festago.festago.data.repository

import com.festago.festago.data.service.ArtistRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.ArtistRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

class DefaultArtistRepository @Inject constructor(
    private val artistRetrofitService: ArtistRetrofitService,
) : ArtistRepository {

    override suspend fun loadArtistDetail(id: Long, delayTimeMillis: Long): Result<ArtistDetail> {
        delay(delayTimeMillis)
        return runCatchingResponse { artistRetrofitService.getArtistDetail(id) }
            .onSuccessOrCatch { it.toDomain() }
    }

    override suspend fun loadArtistFestivals(
        id: Long,
        size: Int?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        isPast: Boolean?,
    ): Result<FestivalsPage> {
        return runCatchingResponse {
            artistRetrofitService.getArtistFestivals(
                artistId = id,
                size = size,
                lastFestivalId = lastFestivalId,
                lastStartDate = lastStartDate,
                isPast = isPast,
            )
        }.onSuccessOrCatch { it.toDomain() }
    }
}
