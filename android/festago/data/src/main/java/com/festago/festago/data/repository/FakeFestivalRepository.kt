package com.festago.festago.data.repository

import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.SchoolRegion
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.festival.PopularFestivals
import com.festago.festago.domain.repository.FestivalRepository
import java.time.LocalDate
import javax.inject.Inject

class FakeFestivalRepository @Inject constructor() : FestivalRepository {

    override suspend fun loadPopularFestivals(): Result<PopularFestivals> {
        return Result.success(PopularFestivals("인기 축제 목록", FakeFestivals.popularFestivals))
    }

    override suspend fun loadFestivals(
        schoolRegion: SchoolRegion?,
        festivalFilter: FestivalFilter?,
        lastFestivalId: Long?,
        lastStartDate: LocalDate?,
        size: Int?,
    ): Result<FestivalsPage> {
        if (festivalFilter == FestivalFilter.PROGRESS) {
            return Result.success(FestivalsPage(false, FakeFestivals.progressFestivals))
        }
        return Result.success(FestivalsPage(false, FakeFestivals.plannedFestivals))
    }
}
