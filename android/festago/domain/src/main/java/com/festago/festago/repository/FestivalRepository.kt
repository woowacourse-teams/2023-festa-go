package com.festago.festago.repository

import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import kotlinx.coroutines.flow.Flow

interface FestivalRepository {
    fun loadFestivals(): Flow<Result<List<Festival>>>
    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation>
}
