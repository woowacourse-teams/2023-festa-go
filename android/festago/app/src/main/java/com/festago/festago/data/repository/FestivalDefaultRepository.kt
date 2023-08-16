package com.festago.festago.data.repository

import com.festago.domain.model.Festival
import com.festago.domain.model.Reservation
import com.festago.domain.repository.FestivalRepository
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler

class FestivalDefaultRepository(
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
