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
        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!.toDomain())
        } else {
            if (response.code() >= 400) {
                return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
            }
            return Result.failure(Throwable("Error - body: ${response.errorBody()} message: ${response.message()}"))
        }
    }
}
