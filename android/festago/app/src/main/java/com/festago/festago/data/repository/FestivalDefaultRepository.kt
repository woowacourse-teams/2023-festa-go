package com.festago.festago.data.repository

import com.festago.festago.data.dao.FestivalDao
import com.festago.festago.data.dao.FestivalEntity
import com.festago.festago.data.datasource.FestivalRemoteDateSource
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
    private val festivalRemoteDateSource: FestivalRemoteDateSource,
) : FestivalRepository {

    override fun loadFestivals(): Flow<Result<List<Festival>>> {
        CoroutineScope(Dispatchers.IO).launch {
            festivalRemoteDateSource.loadFestivals().collect {
                it.onSuccess { festivals -> festivalDao.insertFestivals(festivals.map(FestivalEntity::from)) }
            }
        }
        return festivalDao.getFestivals().transform { festivalEntities ->
            emit(Result.success(festivalEntities.map(FestivalEntity::toDomain)))
        }
    }

    override suspend fun loadFestivalDetail(festivalId: Long): Result<Reservation> =
        festivalRemoteDateSource.loadFestivalDetail(festivalId)
}
