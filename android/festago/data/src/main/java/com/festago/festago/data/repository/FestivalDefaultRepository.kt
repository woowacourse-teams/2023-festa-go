package com.festago.festago.data.repository

import com.festago.festago.data.repository.FakeFestivals.festivalList
import com.festago.festago.data.repository.FakeFestivals.popularFestivals
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalLocation
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.repository.FestivalRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {

    override suspend fun loadPopularFestivals(): Result<List<Festival>> {
        // TODO: 서버 배포 후 API 연동 필요
        // runCatchingResponse {
        //     festivalRetrofitService.getPopularFestivals()
        // }.onSuccessOrCatch { it.map { festival -> festival.toDomain() } }
        delay(5000)
        return Result.success(popularFestivals)
    }

    override suspend fun loadFestivals(
        festivalLocation: FestivalLocation?,
        festivalFilter: FestivalFilter?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        limit: Int?,
    ): Result<FestivalsPage> {
        // TODO: 서버 배포 후 API 연동 필요
        // runCatchingResponse {
        //     festivalRetrofitService.getFestivals(
        //         location = getFestivalLocationName(festivalLocation),
        //         filter = festivalFilter.name,
        //         lastFestivalId = lastFestivalId,
        //         lastStartDate = lastStartDate.toString(),
        //         limit = limit,
        //     )
        // }.onSuccessOrCatch { it.toDomain() }

        delay(3000)
        return Result.success(
            FestivalsPage(
                isLastPage = true,
                festivals = festivalList + festivalList + festivalList,
            ),
        )
    }

    private fun getFestivalLocationName(festivalLocation: FestivalLocation): String {
        return when (festivalLocation) {
            FestivalLocation.BUSAN -> "부산"
            FestivalLocation.SEOUL -> "서울"
            FestivalLocation.GYEONGGI -> "경기"
            FestivalLocation.ALL -> ""
        }
    }
}
