package com.festago.festago.repository

import com.festago.festago.model.Festival
import com.festago.festago.model.FestivalFilter
import com.festago.festago.model.Reservation

interface FestivalRepository {
    suspend fun loadFestivals(festivalFilter: FestivalFilter): Result<List<Festival>>
    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation>
}
