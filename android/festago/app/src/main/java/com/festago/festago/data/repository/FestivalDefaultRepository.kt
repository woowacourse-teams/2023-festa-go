package com.festago.festago.data.repository

import com.festago.festago.data.dao.FestivalDao
import com.festago.festago.data.dao.mapper.toDomain
import com.festago.festago.data.dao.mapper.toEntity
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import com.festago.festago.repository.FestivalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalDao: FestivalDao,
    private val festivalRetrofitService: FestivalRetrofitService,
) : FestivalRepository {
    override val festivals: Flow<List<Festival>>
        get() = festivalDao.getFestivals().transform { emit(it.toDomain()) }

    override suspend fun loadFestivals(): Result<List<Festival>> =
        runCatchingResponse { festivalRetrofitService.getFestivals() }
            .onSuccessOrCatch { festivals ->
                festivals.toDomain().also {
                    CoroutineScope(Dispatchers.IO).launch {
                        println(festivalDao.insertFestivals(it.toEntity()))
                    }
                }
            }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        runCatchingResponse { festivalRetrofitService.getFestivalDetail(festivalId) }
            .onSuccessOrCatch { it.toDomain() }
}
