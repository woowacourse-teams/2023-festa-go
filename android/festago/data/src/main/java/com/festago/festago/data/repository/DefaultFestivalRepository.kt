package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.festival.FestivalDetail
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.festival.PopularFestivals
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.domain.repository.FestivalRepository
import java.time.LocalDate
import javax.inject.Inject

class DefaultFestivalRepository @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {

    override suspend fun loadPopularFestivals(): Result<PopularFestivals> {
        return runCatchingResponse {
            festivalRetrofitService.getPopularFestivals()
        }.onSuccessOrCatch { it.toDomain() }
    }

    override suspend fun loadFestivals(
        schoolRegion: SchoolRegion?,
        festivalFilter: FestivalFilter?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        size: Int?,
    ): Result<FestivalsPage> {
        return runCatchingResponse {
            festivalRetrofitService.getFestivals(
                region = schoolRegion?.name,
                filter = festivalFilter?.name,
                lastFestivalId = lastFestivalId,
                lastStartDate = lastStartDate,
                size = size,
            )
        }.onSuccessOrCatch { it.toDomain() }
    }

    override suspend fun loadFestivalDetail(id: Long): Result<FestivalDetail> {
        return runCatchingResponse {
            festivalRetrofitService.getFestivalDetail(id)
        }.onSuccessOrCatch { it.toDomain() }
    }
}
