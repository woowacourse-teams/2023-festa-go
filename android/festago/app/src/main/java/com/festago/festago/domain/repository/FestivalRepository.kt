package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Festival

interface FestivalRepository {
    suspend fun loadFestivals(): Result<List<Festival>>
}
