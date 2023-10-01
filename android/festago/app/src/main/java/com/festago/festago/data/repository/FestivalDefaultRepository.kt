package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import com.festago.festago.repository.FestivalRepository
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> {
        festivalRetrofitService.getFestivals().runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> {
        festivalRetrofitService.getFestivalDetail(festivalId).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }
}
