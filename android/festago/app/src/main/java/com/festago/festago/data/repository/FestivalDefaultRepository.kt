package com.festago.festago.data.repository

import com.festago.festago.data.datasource.FestivalLocalDateSource
import com.festago.festago.data.datasource.FestivalRemoteDateSource
import com.festago.festago.model.Festival
import com.festago.festago.model.Reservation
import com.festago.festago.repository.FestivalRepository
import javax.inject.Inject

class FestivalDefaultRepository @Inject constructor(
    private val festivalLocalDateSource: FestivalLocalDateSource,
    private val festivalRemoteDateSource: FestivalRemoteDateSource,
) : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> {
        if (festivalLocalDateSource.loadFestivals().isSuccess) {
            return festivalLocalDateSource.loadFestivals()
        }

        return festivalRemoteDateSource.loadFestivals()
    }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        festivalRemoteDateSource.loadFestivalDetail(festivalId)
}
