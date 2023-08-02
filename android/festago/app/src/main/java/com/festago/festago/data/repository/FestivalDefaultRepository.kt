package com.festago.festago.data.repository

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.domain.model.Festival
import com.festago.festago.domain.repository.FestivalRepository
import kotlinx.coroutines.delay

class FestivalDefaultRepository(
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> {
        delay(1000)
        val response = festivalRetrofitService.getFestivals()
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
    }
}
