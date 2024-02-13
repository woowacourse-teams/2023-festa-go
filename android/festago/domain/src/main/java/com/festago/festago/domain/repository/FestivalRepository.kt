package com.festago.festago.domain.repository

import com.festago.festago.domain.model.festival.FestivalFilter
import com.festago.festago.domain.model.festival.FestivalLocation
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.festival.PopularFestivals
import java.time.LocalDate

interface FestivalRepository {
    suspend fun loadPopularFestivals(): Result<PopularFestivals>
    suspend fun loadFestivals(
        festivalLocation: FestivalLocation? = null,
        festivalFilter: FestivalFilter? = null,
        lastFestivalId: Long? = null,
        lastStartDate: LocalDate? = null,
        size: Int? = null,
    ): Result<FestivalsPage>
}
