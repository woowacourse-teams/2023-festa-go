package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalLocation
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.FestivalRepository
import java.time.LocalDate
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {

    override suspend fun loadPopularFestivals(): Result<List<Festival>> {
        return runCatchingResponse {
            festivalRetrofitService.getPopularFestivals()
        }.onSuccessOrCatch { it.map { festival -> festival.toDomain() } }
    }

    override suspend fun loadFestivals(
        festivalLocation: FestivalLocation?,
        festivalFilter: FestivalFilter?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        size: Int?,
    ): Result<FestivalsPage> {
        return runCatchingResponse {
            festivalRetrofitService.getFestivals(
                region = getFestivalLocationName(festivalLocation),
                filter = festivalFilter?.name,
                lastFestivalId = lastFestivalId,
                lastStartDate = lastStartDate,
                size = size,
            )
        }.onSuccessOrCatch { it.toDomain() }
    }

    private fun getFestivalLocationName(festivalLocation: FestivalLocation?): String? {
        return when (festivalLocation) {
            FestivalLocation.BUSAN -> "부산"
            FestivalLocation.SEOUL -> "서울"
            FestivalLocation.GYEONGGI -> "경기"
            else -> null
        }
    }
}
