package com.festago.festago.data.datasource

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import javax.inject.Inject

class FestivalRemoteDateSource @Inject constructor(
    private val festivalRetrofitService: FestivalRetrofitService,
) {
    suspend fun loadFestivals(): Result<List<Festival>> =
        runCatchingResponse { festivalRetrofitService.getFestivals() }
            .onSuccessOrCatch { it.toDomain() }

    suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        runCatchingResponse { festivalRetrofitService.getFestivalDetail(festivalId) }
            .onSuccessOrCatch { it.toDomain() }
}
