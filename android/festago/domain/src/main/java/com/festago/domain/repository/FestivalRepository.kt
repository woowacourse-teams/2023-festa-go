package com.festago.domain.repository

import com.festago.domain.model.Festival
import com.festago.domain.model.Reservation

interface FestivalRepository {
    suspend fun loadFestivals(): Result<List<Festival>>
    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation>
}
