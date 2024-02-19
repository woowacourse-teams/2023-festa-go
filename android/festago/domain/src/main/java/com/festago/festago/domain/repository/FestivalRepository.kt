package com.festago.festago.domain.repository

import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.festival.PopularFestivals
import com.festago.festago.domain.model.festival.SchoolRegion
import java.time.LocalDate

interface FestivalRepository {
    suspend fun loadPopularFestivals(): Result<PopularFestivals>
    suspend fun loadFestivals(
        schoolRegion: SchoolRegion? = null,
        festivalFilter: FestivalFilter? = null,
        lastFestivalId: Long? = null,
        lastStartDate: LocalDate? = null,
        size: Int? = null,
    ): Result<FestivalsPage>
}
