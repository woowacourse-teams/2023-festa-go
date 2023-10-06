package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runRetrofitWithErrorHandler
import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import com.festago.festago.repository.FestivalRepository
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> =
        runRetrofitWithErrorHandler { festivalRetrofitService.getFestivals() }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        runRetrofitWithErrorHandler { festivalRetrofitService.getFestivalDetail(festivalId) }
            .onSuccessOrCatch { it.toDomain() }
}
