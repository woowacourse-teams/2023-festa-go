package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.domain.model.Festival
import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.repository.FestivalRepository

class FestivalDefaultRepository(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> {
        try {
            val response = festivalRetrofitService.getFestivals()
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> {
        try {
            val response = festivalRetrofitService.getFestivalDetail(festivalId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
