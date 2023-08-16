package com.festago.festago.repository

import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation

interface FestivalRepository {
    suspend fun loadFestivals(): Result<List<Festival>>
    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation>
}
