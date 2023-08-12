package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Festival
import com.festago.festago.domain.model.Reservation

interface FestivalRepository {
    suspend fun loadFestivals(): Result<List<Festival>>
    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation>
}
